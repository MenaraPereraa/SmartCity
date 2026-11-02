package lk.menara.smartcity360.config;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.UUID;

/**
 * Runs on every request to:
 * 1. Set the "ctx" attribute to the servlet context path.
 * 2. Ensure a CSRF token exists in the session and as a request attribute.
 */
@WebFilter("/*")
public class RequestSetupFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpReq = (HttpServletRequest) request;

        // Set context path attribute used in JSPs
        request.setAttribute("ctx", httpReq.getContextPath());

        // CSRF token management
        HttpSession session = httpReq.getSession(false);
        if (session == null) {
            session = httpReq.getSession(true);
        }

        String csrfToken = (String) session.getAttribute("csrfToken");
        if (csrfToken == null || csrfToken.isBlank()) {
            csrfToken = UUID.randomUUID().toString();
            session.setAttribute("csrfToken", csrfToken);
        }
        request.setAttribute("csrfToken", csrfToken);

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {}
}
