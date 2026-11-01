package lk.menara.smartcity360.controller;

import jakarta.servlet.http.HttpSession;
import lk.menara.smartcity360.entity.AppUser;
import lk.menara.smartcity360.util.JPAUtil;

import javax.persistence.EntityManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "DashboardServlet", value = "/dashboard")
public class DashboardServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        AppUser loggedUser = (session != null) ? (AppUser) session.getAttribute("loggedUser") : null;

        if (loggedUser == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        EntityManager em = JPAUtil.getEntityManager();
        try {
            long totalUsers = em.createQuery("SELECT COUNT(u) FROM AppUser u", Long.class).getSingleResult();

            request.setAttribute("totalUsers", totalUsers);
            // request.setAttribute("totalComplaints", totalComplaints);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            em.close();
        }

        request.getRequestDispatcher("/dashboard.jsp").forward(request, response);
    }
}