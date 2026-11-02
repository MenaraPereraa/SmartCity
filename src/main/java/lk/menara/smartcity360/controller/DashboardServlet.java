package lk.menara.smartcity360.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lk.menara.smartcity360.dto.ComplaintView;
import lk.menara.smartcity360.dto.DashStats;
import lk.menara.smartcity360.dto.UserDTO;
import lk.menara.smartcity360.model.enums.ComplaintStatus;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;


@WebServlet("/app/dashboard")
public class DashboardServlet extends HttpServlet {
    private static final String JNDI_NAME = "jdbc/SmartCity";
    private static final String SESSION_USER = "user";
    private static final String VIEW_DIR = "/WEB-INF/views/";
    private static final int RESIDENT_RECENT_LIMIT = 5;
    private static final int STAFF_QUEUE_LIMIT = 8;
    private static final int TREND_DAYS = 7;

    private record Scope(String clause, Integer id) {
        static Scope resident(int userId) { return new Scope("c.app_user_id = ?", userId); }
        static Scope role(int roleId)     { return new Scope("c.current_role_id = ?", roleId); }
        static Scope city()               { return new Scope("1 = 1", null); }

        int bind(PreparedStatement ps) throws SQLException {
            if (id == null) return 1;
            ps.setInt(1, id);
            return 2;
        }
    }

    private static final String COUNT_BY_STATUS =
            "SELECT c.status, COUNT(*) AS cnt FROM complaint c WHERE {SCOPE} GROUP BY c.status";

    private static final String SUBMITTED_PER_DAY =
            "SELECT DATE(c.created_at) AS d, COUNT(*) AS cnt FROM complaint c "
                    + "WHERE {SCOPE} AND c.created_at >= ? GROUP BY DATE(c.created_at)";

    // Assumes complaint.resolved_at is filled when a staff member presses Resolve
    private static final String RESOLVED_PER_DAY =
            "SELECT DATE(c.resolved_at) AS d, COUNT(*) AS cnt FROM complaint c "
                    + "WHERE {SCOPE} AND c.status = 'RESOLVED' AND c.resolved_at >= ? "
                    + "GROUP BY DATE(c.resolved_at)";

    // LEFT JOINs so a complaint with a missing zone/category still shows up in the list
    private static final String RESIDENT_RECENT =
            "SELECT c.id, c.description, c.severity, c.status, c.current_role_id, c.created_at, "
                    + "cc.name AS category_name, z.name AS zone_name "
                    + "FROM complaint c "
                    + "LEFT JOIN complaint_category cc ON cc.id = c.category_id "
                    + "LEFT JOIN zone z ON z.id = c.zone_id "
                    + "WHERE c.app_user_id = ? "
                    + "ORDER BY c.created_at DESC LIMIT " + RESIDENT_RECENT_LIMIT;

    private static final String STAFF_QUEUE =
            "SELECT c.id, c.description, c.severity, c.status, c.current_role_id, c.created_at, "
                    + "cc.name AS category_name, z.name AS zone_name, "
                    + "CONCAT(au.first_name, ' ', au.last_name) AS reporter_name "
                    + "FROM complaint c "
                    + "LEFT JOIN complaint_category cc ON cc.id = c.category_id "
                    + "LEFT JOIN zone z ON z.id = c.zone_id "
                    + "LEFT JOIN app_user au ON au.id = c.app_user_id "
                    + "WHERE c.current_role_id = ? AND c.status IN ('OPEN','ESCALATED') "
                    + "ORDER BY c.created_at ASC LIMIT " + STAFF_QUEUE_LIMIT;

    private DataSource dataSource;

    @Override
    public void init() throws ServletException {
        try {
            dataSource = (DataSource) new InitialContext().lookup(JNDI_NAME);
        } catch (NamingException e) {
            throw new ServletException("JDBC resource '" + JNDI_NAME + "' was not found. Check the GlassFish admin console.", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        UserDTO user = session == null ? null : (UserDTO) session.getAttribute(SESSION_USER);
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        DashStats stats = new DashStats();
        String view;

        try (Connection conn = dataSource.getConnection()) {
            if (user.isResident()) {
                loadCounts(conn, Scope.resident(user.getId()), stats);
                stats.setRecent(loadComplaints(conn, RESIDENT_RECENT, user.getId(), false));
                view = "resident/dashboard.jsp";
            } else {
                // NOTE: getRoleCode() must return LOCAL_STAFF / SUPERVISOR / CITY_MANAGER / ADMIN
                switch (user.getRoleCode()) {
                    case "SUPERVISOR" -> {
                        loadCounts(conn, Scope.role(user.getRoleId()), stats);
                        loadTrend(conn, Scope.role(user.getRoleId()), stats);
                        view = "supervisor/dashboard.jsp";
                    }
                    case "CITY_MANAGER", "ADMIN" -> {
                        loadCounts(conn, Scope.city(), stats);
                        loadTrend(conn, Scope.city(), stats);
                        view = "manager/dashboard.jsp";
                    }
                    default -> { // LOCAL_STAFF
                        loadCounts(conn, Scope.role(user.getRoleId()), stats);
                        view = "staff/dashboard.jsp";
                    }
                }
                stats.setRecent(loadComplaints(conn, STAFF_QUEUE, user.getRoleId(), true));
            }
        } catch (SQLException e) {
            throw new ServletException("Dashboard data error", e);
        }

        req.setAttribute("stats", stats);
        req.setAttribute("pageTitle", "Dashboard");
        req.getRequestDispatcher(VIEW_DIR + view).forward(req, resp);
    }

    private void loadCounts(Connection conn, Scope scope, DashStats stats) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(COUNT_BY_STATUS.replace("{SCOPE}", scope.clause()))) {
            scope.bind(ps);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int count = rs.getInt("cnt");
                    switch (ComplaintStatus.valueOf(rs.getString("status"))) {
                        case OPEN -> stats.setOpen(count);
                        case ESCALATED -> stats.setEscalated(count);
                        case RESOLVED -> stats.setResolved(count);
                        case UNRESOLVED -> stats.setUnresolved(count);
                        default -> throw new SQLException("Unexpected complaint status in the database");
                    }
                }
            }
        }
    }

    /** Last 7 days (including today): submitted vs resolved, zeros filled in. */
    private void loadTrend(Connection conn, Scope scope, DashStats stats) throws SQLException {
        LocalDate start = LocalDate.now().minusDays(TREND_DAYS - 1L);

        Map<LocalDate, Integer> submitted = new LinkedHashMap<>();
        Map<LocalDate, Integer> resolved = new LinkedHashMap<>();
        for (int i = 0; i < TREND_DAYS; i++) {
            submitted.put(start.plusDays(i), 0);
            resolved.put(start.plusDays(i), 0);
        }

        fillPerDay(conn, SUBMITTED_PER_DAY, scope, start, submitted);
        fillPerDay(conn, RESOLVED_PER_DAY, scope, start, resolved);

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("EEE d", Locale.ENGLISH);
        stats.setTrendLabels(toJsonStrings(submitted.keySet().stream().map(fmt::format).collect(Collectors.toList())));
        stats.setSubmittedData(toJsonNumbers(new ArrayList<>(submitted.values())));
        stats.setResolvedData(toJsonNumbers(new ArrayList<>(resolved.values())));
    }

    private void fillPerDay(Connection conn, String sql, Scope scope, LocalDate start, Map<LocalDate, Integer> target)
            throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(sql.replace("{SCOPE}", scope.clause()))) {
            int next = scope.bind(ps);
            ps.setDate(next, Date.valueOf(start));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Date d = rs.getDate("d");
                    if (d != null && target.containsKey(d.toLocalDate())) {
                        target.put(d.toLocalDate(), rs.getInt("cnt"));
                    }
                }
            }
        }
    }

    private List<ComplaintView> loadComplaints(Connection conn, String sql, int param, boolean withReporter)
            throws SQLException {
        List<ComplaintView> list = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, param);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ComplaintView cv = new ComplaintView();
                    cv.setId(rs.getInt("id"));
                    cv.setDescription(rs.getString("description"));
                    cv.setSeverity(rs.getInt("severity"));
                    cv.setStatus(rs.getString("status"));
                    int currentRoleId = rs.getInt("current_role_id");
                    cv.setCurrentRoleId(rs.wasNull() ? null : currentRoleId);
                    cv.setCreatedAt(rs.getTimestamp("created_at"));
                    cv.setCategoryName(rs.getString("category_name"));
                    cv.setZoneName(rs.getString("zone_name"));
                    if (withReporter) {
                        cv.setReporterName(rs.getString("reporter_name"));
                    }
                    list.add(cv);
                }
            }
        }
        return list;
    }

    private static String toJsonStrings(List<String> values) {
        return values.stream()
                .map(v -> "\"" + v.replace("\\", "\\\\").replace("\"", "\\\"") + "\"")
                .collect(Collectors.joining(",", "[", "]"));
    }

    private static String toJsonNumbers(List<Integer> values) {
        return values.stream().map(String::valueOf).collect(Collectors.joining(",", "[", "]"));
    }
}