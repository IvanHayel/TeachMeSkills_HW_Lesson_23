package by.teachmeskills.homework.web.servlet;

import by.teachmeskills.homework.entity.User;
import by.teachmeskills.homework.service.UserService;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.SneakyThrows;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "UserServlet", value = "/user")
public class UserServlet extends HttpServlet {
    private static final UserService userService = UserService.getInstance();
    private static final String USER_SESSION_ATTRIBUTE = "user";
    private static final String USER_LOGIN_PARAMETER = "login";
    private static final String USER_NAME_PARAMETER = "name";
    private static final String USER_SURNAME_PARAMETER = "surname";
    private static final String USER_PASSWORD_PARAMETER = "password";

    private transient User currentUser;
    private transient PrintWriter writer;
    private transient HttpSession session;

    @Override
    @SneakyThrows(IOException.class)
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) {
        currentUser = (User) req.getSession().getAttribute(USER_SESSION_ATTRIBUTE);
        writer = resp.getWriter();
        User userToUpdate = userService.getById(currentUser.getId());
        String login = req.getParameter(USER_LOGIN_PARAMETER);
        String name = req.getParameter(USER_NAME_PARAMETER);
        String surname = req.getParameter(USER_SURNAME_PARAMETER);
        String password = req.getParameter(USER_PASSWORD_PARAMETER);
        currentUser = userService.updateUser(userToUpdate, login, password, name, surname);
        writer.println(currentUser);
    }

    @Override
    @SneakyThrows(IOException.class)
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) {
        session = req.getSession();
        writer = resp.getWriter();
        currentUser = (User) session.getAttribute(USER_SESSION_ATTRIBUTE);
        if (userService.removeById(currentUser.getId()) != null)
            resp.sendRedirect("/logout");
        else
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
    }
}