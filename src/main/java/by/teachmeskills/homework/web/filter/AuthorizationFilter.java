package by.teachmeskills.homework.web.filter;

import by.teachmeskills.homework.entity.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.NonNull;
import lombok.SneakyThrows;

import java.io.IOException;

@WebFilter(filterName = "AuthorizationFilter", servletNames = {
        "PostServlet", "PostHistoryServlet", "CommentServlet", "UserServlet", "AdminServlet", "LogoutServlet"
})
public class AuthorizationFilter extends HttpFilter {
    private static final String USER_SESSION_ATTRIBUTE = "user";

    @Override
    @SneakyThrows({IOException.class, ServletException.class})
    public void doFilter(HttpServletRequest req, HttpServletResponse resp, FilterChain chain) {
        @NonNull HttpSession session = req.getSession();
        User currentUser = (User) session.getAttribute(USER_SESSION_ATTRIBUTE);
        if (currentUser != null)
            chain.doFilter(req, resp);
        else
            resp.sendError(HttpServletResponse.SC_FORBIDDEN);
    }
}
