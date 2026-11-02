package lk.menara.smartcity360.service;

import lk.menara.smartcity360.dto.EnergyMeterView;
import lk.menara.smartcity360.dto.StreamLogView;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * All energy database work (MySQL, JNDI data source "jdbc/SmartCity").
 *
 * Tables used: energy_meter, building, meter_reading,
 *              security_layer, energy_stream_layer, energy_stream_log.
 *
 * How a stream run is saved: energy_stream_log has one row per layer that was applied.
 * All rows of one run share the same meter and the same created_at time.
 * Each row points to an energy_stream_layer row (security layer + its order).
 */
public class EnergyService {

    private static final String JNDI_NAME = "jdbc/SmartCity";
    private static final DateTimeFormatter TIME = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    /** Fixed order of the layers. It matches StreamPipelineFactory (compress, encrypt, authenticate). */
    private static final Map<String, Integer> LAYER_ORDER = new LinkedHashMap<>();
    static {
        LAYER_ORDER.put("Compression", 1);
        LAYER_ORDER.put("Encryption", 2);
        LAYER_ORDER.put("Authentication", 3);
    }

    /** Meter + building + its newest reading. */
    private static final String METER_SELECT =
            "SELECT m.id, m.meter_code, m.is_active, b.name AS building_name, "
                    + "       r.kwh, r.voltage, r.read_at "
                    + "FROM energy_meter m "
                    + "JOIN building b ON b.id = m.building_id "
                    + "LEFT JOIN meter_reading r ON r.id = ( "
                    + "    SELECT r2.id FROM meter_reading r2 "
                    + "    WHERE r2.energy_meter_id = m.id "
                    + "    ORDER BY r2.read_at DESC, r2.id DESC LIMIT 1) ";

    /* ───────────────────────── reads ───────────────────────── */

    public List<EnergyMeterView> findMeters() throws SQLException {
        List<EnergyMeterView> list = new ArrayList<>();
        try (Connection c = open();
             PreparedStatement ps = c.prepareStatement(METER_SELECT + "ORDER BY m.meter_code");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapMeter(rs));
        }
        return list;
    }

    /** Returns null when the meter does not exist. */
    public EnergyMeterView findMeter(int meterId) throws SQLException {
        try (Connection c = open();
             PreparedStatement ps = c.prepareStatement(METER_SELECT + "WHERE m.id = ?")) {
            ps.setInt(1, meterId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapMeter(rs) : null;
            }
        }
    }

    /** Buildings for the "Add meter" drop-down (id -> name). */
    public Map<Integer, String> findBuildings() throws SQLException {
        Map<Integer, String> map = new LinkedHashMap<>();
        try (Connection c = open();
             PreparedStatement ps = c.prepareStatement("SELECT id, name FROM building ORDER BY name");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) map.put(rs.getInt("id"), rs.getString("name"));
        }
        return map;
    }

    /** Newest stream runs first. One entry per run (meter + time). */
    public List<StreamLogView> findRecentStreamLogs(int limit) throws SQLException {
        String sql =
                "SELECT em.meter_code, l.created_at, "
                        + "       GROUP_CONCAT(DISTINCT sl.name ORDER BY esl.layer_order SEPARATOR ',') AS layers "
                        + "FROM energy_stream_log l "
                        + "JOIN energy_meter em ON em.id = l.energy_meter_id "
                        + "JOIN energy_stream_layer esl ON esl.id = l.energy_stream_layer_id "
                        + "JOIN security_layer sl ON sl.id = esl.security_layer_id "
                        + "GROUP BY l.energy_meter_id, em.meter_code, l.created_at "
                        + "ORDER BY l.created_at DESC, em.meter_code "
                        + "LIMIT ?";
        List<StreamLogView> list = new ArrayList<>();
        try (Connection c = open(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, limit);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String joined = rs.getString("layers");
                    List<String> layers = joined == null || joined.isEmpty()
                            ? Collections.<String>emptyList()
                            : Arrays.asList(joined.split(","));
                    list.add(new StreamLogView(
                            rs.getTimestamp("created_at").toLocalDateTime().format(TIME),
                            rs.getString("meter_code"),
                            layers));
                }
            }
        }
        return list;
    }

    /* ───────────────────────── writes ───────────────────────── */

    /**
     * Adds a meter to a building. The code is created automatically: EM-{CITY}-{NNN}
     * CITY = first 3 letters of the city the building is in, NNN = next free number.
     * Example: EM-COL-003
     *
     * @return the new meter code, or null when the building does not exist.
     */
    public String addMeter(int buildingId) throws SQLException {
        try (Connection c = open()) {
            if (!buildingExists(c, buildingId)) return null;
            String prefix = "EM-" + cityCode(c, buildingId) + "-";

            for (int attempt = 0; attempt < 5; attempt++) {
                String code = prefix + String.format("%03d", nextNumber(c, prefix));
                try (PreparedStatement ps = c.prepareStatement(
                        "INSERT INTO energy_meter (meter_code, installed_at, is_active, building_id) VALUES (?, NOW(), 1, ?)")) {
                    ps.setString(1, code);
                    ps.setInt(2, buildingId);
                    ps.executeUpdate();
                    return code;
                } catch (SQLIntegrityConstraintViolationException e) {
                    // 1062 = another meter took this code a moment ago. Try the next number.
                    if (e.getErrorCode() != 1062) throw e;
                }
            }
            throw new SQLException("Could not create a unique meter code");
        }
    }

    /** @return the new state (true = active), or null when the meter was not found. */
    public Boolean toggleActive(int meterId) throws SQLException {
        try (Connection c = open()) {
            try (PreparedStatement ps = c.prepareStatement("UPDATE energy_meter SET is_active = NOT is_active WHERE id = ?")) {
                ps.setInt(1, meterId);
                if (ps.executeUpdate() == 0) return null;
            }
            try (PreparedStatement ps = c.prepareStatement("SELECT is_active FROM energy_meter WHERE id = ?")) {
                ps.setInt(1, meterId);
                try (ResultSet rs = ps.executeQuery()) {
                    return rs.next() ? rs.getBoolean(1) : null;
                }
            }
        }
    }

    /** read_at is filled by the database (its default is the current time). */
    public void addReading(int meterId, BigDecimal kwh, BigDecimal voltage) throws SQLException {
        try (Connection c = open();
             PreparedStatement ps = c.prepareStatement(
                     "INSERT INTO meter_reading (kwh, voltage, energy_meter_id) VALUES (?, ?, ?)")) {
            ps.setBigDecimal(1, kwh);
            ps.setBigDecimal(2, voltage);
            ps.setInt(3, meterId);
            ps.executeUpdate();
        }
    }

    /**
     * Saves one stream run: one energy_stream_log row for each layer.
     * Everything is saved in one transaction.
     */
    public void logStream(int meterId, List<String> layerNames) throws SQLException {
        if (layerNames.isEmpty()) return;   // nothing to link a log row to

        try (Connection c = open()) {
            c.setAutoCommit(false);
            try {
                Timestamp when = Timestamp.valueOf(LocalDateTime.now().withNano(0));
                for (String name : layerNames) {
                    Integer order = LAYER_ORDER.get(name);
                    if (order == null) throw new SQLException("Unknown layer: " + name);

                    int securityLayerId = findOrCreateSecurityLayer(c, name);
                    long streamLayerId = findOrCreateStreamLayer(c, securityLayerId, order);

                    try (PreparedStatement ps = c.prepareStatement(
                            "INSERT INTO energy_stream_log (created_at, energy_stream_layer_id, energy_meter_id) VALUES (?, ?, ?)")) {
                        ps.setTimestamp(1, when);
                        ps.setLong(2, streamLayerId);
                        ps.setInt(3, meterId);
                        ps.executeUpdate();
                    }
                }
                c.commit();
            } catch (SQLException | RuntimeException e) {
                c.rollback();
                throw e;
            }
        }
    }

    /* ───────────────────────── helpers ───────────────────────── */

    private boolean buildingExists(Connection c, int buildingId) throws SQLException {
        try (PreparedStatement ps = c.prepareStatement("SELECT 1 FROM building WHERE id = ?")) {
            ps.setInt(1, buildingId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    /** Walks up the zone tree (street -> district -> city) and returns 3 letters of the city name. */
    private String cityCode(Connection c, int buildingId) throws SQLException {
        String sql =
                "WITH RECURSIVE up AS ( "
                        + "    SELECT z.id, z.name, z.`level`, z.parent_id "
                        + "    FROM building b JOIN zone z ON z.id = b.zone_id WHERE b.id = ? "
                        + "    UNION ALL "
                        + "    SELECT p.id, p.name, p.`level`, p.parent_id "
                        + "    FROM zone p JOIN up ON p.id = up.parent_id ) "
                        + "SELECT name FROM up WHERE `level` = 'CITY' LIMIT 1";
        String name = null;
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, buildingId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) name = rs.getString(1);
            }
        }
        String letters = name == null ? "" : name.replaceAll("[^A-Za-z]", "").toUpperCase();
        if (letters.isEmpty()) return "GEN";                       // building has no city above it
        return letters.length() >= 3 ? letters.substring(0, 3) : letters;
    }

    /** Next free number for this prefix, for example EM-COL-: 1 + the highest number used. */
    private int nextNumber(Connection c, String prefix) throws SQLException {
        String sql = "SELECT COALESCE(MAX(CAST(SUBSTRING_INDEX(meter_code, '-', -1) AS UNSIGNED)), 0) + 1 "
                + "FROM energy_meter WHERE meter_code LIKE ?";
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, prefix + "%");
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getInt(1);
            }
        }
    }

    private int findOrCreateSecurityLayer(Connection c, String name) throws SQLException {
        try (PreparedStatement ps = c.prepareStatement("SELECT id FROM security_layer WHERE name = ?")) {
            ps.setString(1, name);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        try (PreparedStatement ps = c.prepareStatement("INSERT INTO security_layer (name) VALUES (?)", Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, name);
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                keys.next();
                return keys.getInt(1);
            }
        }
    }

    private long findOrCreateStreamLayer(Connection c, int securityLayerId, int order) throws SQLException {
        try (PreparedStatement ps = c.prepareStatement(
                "SELECT id FROM energy_stream_layer WHERE security_layer_id = ? AND layer_order = ? LIMIT 1")) {
            ps.setInt(1, securityLayerId);
            ps.setInt(2, order);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getLong(1);
            }
        }
        try (PreparedStatement ps = c.prepareStatement(
                "INSERT INTO energy_stream_layer (security_layer_id, layer_order) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, securityLayerId);
            ps.setInt(2, order);
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                keys.next();
                return keys.getLong(1);
            }
        }
    }

    private static EnergyMeterView mapMeter(ResultSet rs) throws SQLException {
        Timestamp readAt = rs.getTimestamp("read_at");
        return new EnergyMeterView(
                rs.getInt("id"),
                rs.getString("meter_code"),
                rs.getString("building_name"),
                rs.getBoolean("is_active"),
                plain(rs.getBigDecimal("kwh")),
                plain(rs.getBigDecimal("voltage")),
                readAt == null ? null : readAt.toLocalDateTime().format(TIME));
    }

    private static String plain(BigDecimal v) {
        return v == null ? null : v.stripTrailingZeros().toPlainString();
    }

    private Connection open() throws SQLException {
        try {
            DataSource ds = (DataSource) new InitialContext().lookup(JNDI_NAME);
            return ds.getConnection();
        } catch (NamingException e) {
            throw new SQLException("Data source not found: " + JNDI_NAME, e);
        }
    }
}
