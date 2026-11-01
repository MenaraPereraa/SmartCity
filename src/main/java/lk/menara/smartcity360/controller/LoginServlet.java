package lk.menara.smartcity360.controller;

import jakarta.servlet.http.HttpSession;
import lk.menara.smartcity360.auth.AuthService;
import lk.menara.smartcity360.entity.AppUser;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "LoginServlet", value = "/login")
public class LoginServlet extends HttpServlet {

    private AuthService authService = new AuthService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String email = request.getParameter("email");
        String password = request.getParameter("password");

        AppUser user = authService.authenticate(email, password);

        if (user != null) {
            HttpSession session = request.getSession();
            session.setAttribute("loggedUser", user);

            if (user.getRole() != null && "Admin".equalsIgnoreCase(user.getRole().getName())) {
                response.sendRedirect(request.getContextPath() + "/admin/dashboard.jsp");
            } else {
                response.sendRedirect(request.getContextPath() + "/dashboard.jsp");
            }
        } else {
            request.setAttribute("errorMessage", "Invalid Email or Password!");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
        }
    }
}