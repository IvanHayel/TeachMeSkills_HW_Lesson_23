package by.teachmeskills.homework.web.servlet;

import by.teachmeskills.homework.entity.User;
import by.teachmeskills.homework.service.UserService;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.NonNull;
import lombok.SneakyThrows;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "AuthorizationServlet", value = "/authorization")
public class AuthorizationServlet extends HttpServlet {
    private static final UserService userService = UserService.getInstance();
    private static final String USER_SESSION_ATTRIBUTE = "user";
    private static final String USER_LOGIN_PARAMETER = "login";
    private static final String USER_PASSWORD_PARAMETER = "password";
    private static final String ALREADY_LOGGED_IN_MESSAGE = "You're already logged in!";
    private static final String LOGIN_NOT_EXIST_MESSAGE = "%s - not exist!";
    private static final String AUTHORIZATION_SUCCESS_MESSAGE = "Welcome, %s!";
    private static final String PASSWORD_MISMATCH_MESSAGE = "Incorrect password for %s. Try again!";

    private transient HttpSession session;
    private transient PrintWriter writer;

    @Override
    @SneakyThrows(IOException.class)
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        session = req.getSession();
        writer = resp.getWriter();
        if (session.getAttribute(USER_SESSION_ATTRIBUTE) != null) {
            writer.println(ALREADY_LOGGED_IN_MESSAGE);
        } else {
            String login = req.getParameter(USER_LOGIN_PARAMETER);
            String password = req.getParameter(USER_PASSWORD_PARAMETER);
            if(isAnyParameterMissing(login, password))
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            else
                logIn(login, password);
        }
    }

    private boolean isAnyParameterMissing(String... parameters) {
        for (String parameter : parameters)
            if (parameter == null) return true;
        return false;
    }

    private void logIn(String login, String password) {
        if (!userService.isLoginExist(login)) {
            writer.println(String.format(LOGIN_NOT_EXIST_MESSAGE, login));
        } else {
            @NonNull User user = userService.getByLogin(login);
            if (user.getPassword().equals(password)) {
                session.setAttribute(USER_SESSION_ATTRIBUTE, user);
                writer.println(String.format(AUTHORIZATION_SUCCESS_MESSAGE, user.getFullName()));
            } else {
                writer.println(String.format(PASSWORD_MISMATCH_MESSAGE, login));
            }
        }
    }
}