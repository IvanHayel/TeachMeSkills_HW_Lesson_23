package by.teachmeskills.homework.web.filter;

import by.teachmeskills.homework.entity.User;
import by.teachmeskills.homework.web.constant.attribute.SessionAttribute;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.NonNull;
import lombok.SneakyThrows;

import java.io.IOException;

@WebFilter(filterName = "AuthorizationFilter", servletNames = {
        "PostServlet", "PostHistoryServlet", "CommentServlet", "UserServlet", "AdminServlet", "LogoutServlet"
})
// TODO: extends HttpFilter
public class AuthorizationFilter implements Filter {
    @Override
    @SneakyThrows({IOException.class, ServletException.class})
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        @NonNull HttpSession session = req.getSession();
        User currentUser = (User) session.getAttribute(SessionAttribute.USER.get());
        if (currentUser != null)
            chain.doFilter(req, resp);
        else
            resp.sendError(HttpServletResponse.SC_FORBIDDEN);
    }
}
