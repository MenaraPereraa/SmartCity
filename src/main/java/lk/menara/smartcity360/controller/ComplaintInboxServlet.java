package lk.menara.smartcity360.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import lk.menara.smartcity360.dto.ComplaintView;
import lk.menara.smartcity360.dto.UserDTO;
import lk.menara.smartcity360.logger.Log;
import lk.menara.smartcity360.model.enums.ComplaintStatus;

@WebServlet("/app/inbox")
public class ComplaintInboxServlet extends HttpServlet {
    private static final String JNDI_NAME = "jdbc/SmartCity";
    private static final String VIEW = "/WEB-INF/views/complaint-list.jsp";
    private static final String VIEW_PARAMETER = "view";
    private static final String CLOSED_VIEW = "closed";
    private static final String PAGE_TITLE = "Complaints Inbox";

    private static final String SELECT_COMPLAINTS =
            "SELECT c.id, c.description, c.severity, c.status, c.current_role_id, c.created_at, c.resolved_at, "
                    + "r.name AS current_role_name, cc.name AS category_name, z.name AS zone_name, "
                    + "CONCAT(au.first_name, ' ', au.last_name) AS reporter_name "
                    + "FROM complaint c "
                    + "JOIN complaint_category cc ON cc.id = c.category_id "
                    + "JOIN zone z ON z.id = c.zone_id "
                    + "JOIN app_user au ON au.id = c.app_user_id "
                    + "LEFT JOIN role r ON r.id = c.current_role_id "
                    + "WHERE c.current_role_id = ? AND c.status IN ";

    private static final String OPEN_FILTER =
            "('" + ComplaintStatus.OPEN + "','" + ComplaintStatus.ESCALATED + "') ORDER BY c.created_at ASC";

    private static final String CLOSED_FILTER =
            "('" + ComplaintStatus.RESOLVED + "','" + ComplaintStatus.UNRESOLVED + "') ORDER BY c.created_at DESC";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        UserDTO user = session == null ? null : (UserDTO) session.getAttribute("user");
        if (user == null || user.isResident()) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Staff only");
            return;
        }

        boolean openTab = !CLOSED_VIEW.equalsIgnoreCase(req.getParameter(VIEW_PARAMETER));

        req.setAttribute("complaints", loadComplaints(user.getRoleId(), openTab));
        req.setAttribute("inboxTabs", true);
        req.setAttribute("openTab", openTab);
        req.setAttribute("listTitle", PAGE_TITLE);
        req.setAttribute("pageTitle", PAGE_TITLE);
        req.getRequestDispatcher(VIEW).forward(req, resp);
    }

    private List<ComplaintView> loadComplaints(int roleId, boolean openTab) throws ServletException {
        String sql = SELECT_COMPLAINTS + (openTab ? OPEN_FILTER : CLOSED_FILTER);
        List<ComplaintView> complaints = new ArrayList<>();
        try {
            DataSource dataSource = (DataSource) new InitialContext().lookup(JNDI_NAME);
            try (Connection connection = dataSource.getConnection();
                 PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, roleId);
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        complaints.add(mapComplaint(resultSet));
                    }
                }
            }
        } catch (NamingException | SQLException e) {
            Log.logger.log(Level.SEVERE, "Error loading inbox complaints", e);
            throw new ServletException("Error loading inbox complaints", e);
        }
        return complaints;
    }

    private ComplaintView mapComplaint(ResultSet resultSet) throws SQLException {
        ComplaintView complaint = new ComplaintView();
        complaint.setId(resultSet.getInt("id"));
        complaint.setDescription(resultSet.getString("description"));
        complaint.setSeverity(resultSet.getInt("severity"));
        complaint.setStatus(resultSet.getString("status"));
        complaint.setCurrentRoleId(resultSet.getObject("current_role_id", Integer.class));
        complaint.setCurrentRoleName(resultSet.getString("current_role_name"));
        complaint.setCreatedAt(resultSet.getTimestamp("created_at"));
        complaint.setResolvedAt(resultSet.getTimestamp("resolved_at"));
        complaint.setCategoryName(resultSet.getString("category_name"));
        complaint.setZoneName(resultSet.getString("zone_name"));
        complaint.setReporterName(resultSet.getString("reporter_name"));
        return complaint;
    }
}