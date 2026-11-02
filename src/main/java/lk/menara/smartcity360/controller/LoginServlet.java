package lk.menara.smartcity360.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import lk.menara.smartcity360.auth.AuthService;
import lk.menara.smartcity360.dto.UserDTO;

import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private final AuthService authService = new AuthService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setAttribute("pageTitle", "Sign In");
        req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String email = req.getParameter("email");
        String password = req.getParameter("password");

        UserDTO user = null;
        try {
            user = authService.authenticate(email, password);
        } catch (Exception e) {
            req.setAttribute("error", "An unexpected error occurred. Please try again.");
            req.setAttribute("pageTitle", "Sign In");
            req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req, resp);
            return;
        }

        if (user != null) {
            HttpSession oldSession = req.getSession(false);
            if (oldSession != null) {
                oldSession.invalidate();
            }
            HttpSession session = req.getSession(true);
            session.setAttribute("user", user);

            String next = req.getParameter("next");
            if (next != null && next.startsWith("/") && !next.startsWith("//")) {
                resp.sendRedirect(req.getContextPath() + next);
            } else {
                resp.sendRedirect(req.getContextPath() + "/app/dashboard");
            }
        } else {
            req.setAttribute("error", "Invalid email or password.");
            req.setAttribute("pageTitle", "Sign In");
            req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req, resp);
        }
    }
}