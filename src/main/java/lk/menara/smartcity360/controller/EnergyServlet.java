package lk.menara.smartcity360.controller;

import com.google.crypto.tink.Aead;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lk.menara.smartcity360.dto.EnergyMeterView;
import lk.menara.smartcity360.exception.AuthenticationFailedException;
import lk.menara.smartcity360.exception.StreamProcessingException;
import lk.menara.smartcity360.patterns.decorator.BasicMeterStream;
import lk.menara.smartcity360.patterns.decorator.CompressionDecorator;
import lk.menara.smartcity360.patterns.decorator.EncryptionDecorator;
import lk.menara.smartcity360.patterns.decorator.MeterStream;
import lk.menara.smartcity360.patterns.decorator.StreamPipelineFactory;
import lk.menara.smartcity360.patterns.decorator.TinkKeyProvider;
import lk.menara.smartcity360.service.EnergyService;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** Energy page. All data comes from MySQL through EnergyService. */
@WebServlet("/app/energy")
public class EnergyServlet extends HttpServlet {

    private static final String VIEW = "/WEB-INF/views/energy.jsp";

    /** Token the Authentication layer checks. Change it here or load it from your config. */
    private static final String EXPECTED_TOKEN = "SC360-STREAM";

    /** One key for the whole app run, so encrypt and decrypt use the same key. */
    private static final Aead AEAD = TinkKeyProvider.newAes256GcmAead();

    // Limits of the database columns: kwh decimal(10,3), voltage decimal(6,2)
    private static final BigDecimal MAX_KWH = new BigDecimal("9999999.999");
    private static final BigDecimal MAX_VOLTAGE = new BigDecimal("9999.99");

    private final EnergyService service = new EnergyService();

    /* ───────────────────────── GET ───────────────────────── */

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        moveToRequest(session, req, "energyFlash", "flash");
        moveToRequest(session, req, "energyResult", "streamResult");

        try {
            req.setAttribute("meters", service.findMeters());
            req.setAttribute("streamLogs", service.findRecentStreamLogs(30));
            req.setAttribute("buildings", service.findBuildings());
        } catch (SQLException e) {
            throw new ServletException("Could not load energy data", e);
        }
        req.setAttribute("streamTokenHint", EXPECTED_TOKEN);
        req.setAttribute("pageTitle", "Energy Management");
        req.getRequestDispatcher(VIEW).forward(req, resp);
    }

    /* ───────────────────────── POST ───────────────────────── */

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession();
        String action = req.getParameter("action") == null ? "" : req.getParameter("action");

        try {
            switch (action) {
                case "addMeter":       addMeter(req, session);       break;
                case "toggleActive":   toggleActive(req, session);   break;
                case "addReading":     addReading(req, session);     break;
                case "simulateStream": simulateStream(req, session); break;
                default:               flash(session, "error", "Unknown action.");
            }
        } catch (SQLException e) {
            getServletContext().log("Energy database error (" + action + ")", e);
            flash(session, "error", "Database error. The change was not saved.");
        }
        resp.sendRedirect(req.getContextPath() + "/app/energy");   // Post / Redirect / Get
    }

    private void addMeter(HttpServletRequest req, HttpSession session) throws SQLException {
        Integer buildingId = parseInt(req.getParameter("buildingId"));
        if (buildingId == null) {
            flash(session, "error", "Choose a building.");
            return;
        }
        String code = service.addMeter(buildingId);   // the code is created automatically
        if (code == null) {
            flash(session, "error", "That building does not exist.");
        } else {
            flash(session, "success", "Meter " + code + " added.");
        }
    }

    private void toggleActive(HttpServletRequest req, HttpSession session) throws SQLException {
        Integer id = parseInt(req.getParameter("meterId"));
        Boolean now = id == null ? null : service.toggleActive(id);
        if (now == null) {
            flash(session, "error", "Meter not found.");
        } else {
            flash(session, "success", "The meter is now " + (now ? "active." : "inactive."));
        }
    }

    private void addReading(HttpServletRequest req, HttpSession session) throws SQLException {
        Integer id = parseInt(req.getParameter("meterId"));
        BigDecimal kwh = parseDecimal(req.getParameter("kwh"), 3);
        BigDecimal voltage = parseDecimal(req.getParameter("voltage"), 2);

        if (id == null || service.findMeter(id) == null) {
            flash(session, "error", "Meter not found.");
        } else if (kwh == null || kwh.signum() < 0 || kwh.compareTo(MAX_KWH) > 0) {
            flash(session, "error", "Enter a valid kWh value.");
        } else if (voltage == null || voltage.signum() < 0 || voltage.compareTo(MAX_VOLTAGE) > 0) {
            flash(session, "error", "Enter a valid voltage value.");
        } else {
            service.addReading(id, kwh, voltage);
            flash(session, "success", "Reading saved.");
        }
    }

    /** Runs the decorator pipeline on the data and saves the run in the database. */
    private void simulateStream(HttpServletRequest req, HttpSession session) throws SQLException {
        boolean compress = req.getParameter("compress") != null;
        boolean encrypt = req.getParameter("encrypt") != null;
        boolean authenticate = req.getParameter("authenticate") != null;
        String token = req.getParameter("token");
        String payload = trim(req.getParameter("payload"));

        Integer id = parseInt(req.getParameter("meterId"));
        EnergyMeterView meter = id == null ? null : service.findMeter(id);
        if (meter == null) {
            flash(session, "error", "Choose a meter first.");
            return;
        }
        if (!meter.isActive()) {
            flash(session, "error", meter.getMeterCode() + " is inactive. Activate it to stream data.");
            return;
        }

        // Empty box: use the newest reading from the database
        if (payload.isEmpty()) {
            if (meter.getLatestKwh() == null) {
                flash(session, "error", meter.getMeterCode() + " has no readings yet. Add a reading or paste some data.");
                return;
            }
            payload = "meter=" + meter.getMeterCode()
                    + ";kwh=" + meter.getLatestKwh()
                    + ";voltage=" + meter.getLatestVoltage()
                    + ";read_at=" + meter.getLastReadAt();
        }

        List<String> layers = new ArrayList<>();
        if (compress) layers.add("Compression");
        if (encrypt) layers.add("Encryption");
        if (authenticate) layers.add("Authentication");

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("meterCode", meter.getMeterCode());
        result.put("layers", layers);
        result.put("original", payload);

        try {
            MeterStream stream = StreamPipelineFactory.build(
                    new BasicMeterStream(payload), compress, encrypt, AEAD,
                    authenticate, token, EXPECTED_TOKEN);

            String output = stream.read();

            // Decode again to prove nothing was lost (decrypt first, then decompress)
            String decoded = output;
            if (encrypt) decoded = EncryptionDecorator.decrypt(decoded, AEAD);
            if (compress) decoded = CompressionDecorator.decompress(decoded);

            result.put("ok", true);
            result.put("pipeline", stream.describe());
            result.put("output", output);
            result.put("decoded", decoded);
            result.put("roundTrip", payload.equals(decoded));
            result.put("originalSize", payload.length());
            result.put("outputSize", output.length());

            service.logStream(meter.getId(), layers);   // save the run in energy_stream_log
        } catch (AuthenticationFailedException e) {
            result.put("ok", false);
            result.put("error", "Authentication failed. The token is not correct.");
        } catch (StreamProcessingException e) {
            result.put("ok", false);
            result.put("error", "Stream error: " + e.getMessage());
        }
        session.setAttribute("energyResult", result);
    }

    /* ───────────────────────── helpers ───────────────────────── */

    private static void flash(HttpSession session, String type, String text) {
        Map<String, String> f = new LinkedHashMap<>();
        f.put("type", type);
        f.put("text", text);
        session.setAttribute("energyFlash", f);
    }

    private static void moveToRequest(HttpSession session, HttpServletRequest req, String from, String to) {
        Object value = session.getAttribute(from);
        if (value != null) {
            req.setAttribute(to, value);
            session.removeAttribute(from);
        }
    }

    private static String trim(String s) { return s == null ? "" : s.trim(); }

    private static Integer parseInt(String s) {
        try { return Integer.valueOf(s); } catch (Exception e) { return null; }
    }

    private static BigDecimal parseDecimal(String s, int scale) {
        if (s == null || s.trim().isEmpty()) return null;
        try { return new BigDecimal(s.trim()).setScale(scale, RoundingMode.HALF_UP); }
        catch (NumberFormatException e) { return null; }
    }
}