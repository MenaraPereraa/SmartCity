package lk.menara.smartcity360.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Locale;
import java.util.logging.Level;
import java.util.regex.Pattern;
import lk.menara.smartcity360.auth.AuthService;
import lk.menara.smartcity360.logger.Log;

@WebServlet("/signup")
public class SignupServlet extends HttpServlet {
    private static final String VIEW = "/WEB-INF/views/signup.jsp";
    private static final int MAX_NAME_LENGTH = 45;
    private static final int MAX_EMAIL_LENGTH = 150;
    private static final int MIN_PASSWORD_LENGTH = 8;
    private static final int MAX_PASSWORD_LENGTH = 100;
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$");

    private final AuthService authService = new AuthService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        showForm(req, resp, null);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String firstName = trim(req.getParameter("firstName"));
        String lastName = trim(req.getParameter("lastName"));
        String email = trim(req.getParameter("email")).toLowerCase(Locale.ROOT);
        String password = req.getParameter("password");
        String confirmPassword = req.getParameter("confirmPassword");
        String buildingIdText = trim(req.getParameter("buildingId"));

        String error = validate(firstName, lastName, email, password, confirmPassword, buildingIdText);
        if (error != null) {
            showForm(req, resp, error);
            return;
        }

        try {
            authService.registerResident(firstName, lastName, email, password, Integer.parseInt(buildingIdText));
            req.getSession().setAttribute("flash", "Account created successfully. Please sign in.");
            resp.sendRedirect(req.getContextPath() + "/login");
        } catch (IllegalArgumentException e) {
            showForm(req, resp, "That email address is already registered.");
        } catch (Exception e) {
            Log.logger.log(Level.SEVERE, "Resident registration failed", e);
            showForm(req, resp, "Registration failed. Please try again.");
        }
    }

    private void showForm(HttpServletRequest req, HttpServletResponse resp, String error)
            throws ServletException, IOException {
        req.setAttribute("error", error);
        req.setAttribute("pageTitle", "Create Account");
        req.setAttribute("buildings", authService.getBuildings());
        req.getRequestDispatcher(VIEW).forward(req, resp);
    }

    private String validate(String firstName, String lastName, String email, String password,
                            String confirmPassword, String buildingIdText) {
        if (firstName.isEmpty() || firstName.length() > MAX_NAME_LENGTH) {
            return "First name is required (maximum " + MAX_NAME_LENGTH + " characters).";
        }
        if (lastName.isEmpty() || lastName.length() > MAX_NAME_LENGTH) {
            return "Last name is required (maximum " + MAX_NAME_LENGTH + " characters).";
        }
        if (email.length() > MAX_EMAIL_LENGTH || !EMAIL_PATTERN.matcher(email).matches()) {
            return "Enter a valid email address.";
        }
        if (password == null || password.length() < MIN_PASSWORD_LENGTH || password.length() > MAX_PASSWORD_LENGTH) {
            return "Password must be " + MIN_PASSWORD_LENGTH + " to " + MAX_PASSWORD_LENGTH + " characters.";
        }
        if (!password.equals(confirmPassword)) {
            return "Passwords do not match.";
        }
        if (buildingIdText.isEmpty()) {
            return "Please select a building.";
        }
        try {
            Integer.parseInt(buildingIdText);
        } catch (NumberFormatException e) {
            return "Invalid building selection.";
        }
        return null;
    }

    private String trim(String value) {
        return value == null ? "" : value.trim();
    }
}