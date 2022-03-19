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
    private static final String ALREADY_AUTHENTICATED_MESSAGE = "You've already authenticated!";
    private static final String INCORRECT_LOGIN_MESSAGE = "%s - not exist!";
    private static final String AUTHORIZATION_SUCCESS_MESSAGE = "Welcome, %s!";
    private static final String PASSWORD_MISMATCH_MESSAGE = "Incorrect password for %s!";

    private transient HttpSession session;
    private transient PrintWriter writer;

    @Override
    @SneakyThrows(IOException.class)
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        session = req.getSession();
        writer = resp.getWriter();
        if (session.getAttribute(USER_SESSION_ATTRIBUTE) != null) {
            writer.println(ALREADY_AUTHENTICATED_MESSAGE);
        } else {
            @NonNull String login = req.getParameter(USER_LOGIN_PARAMETER);
            @NonNull String password = req.getParameter(USER_PASSWORD_PARAMETER);
            authenticate(login, password);
        }
    }

    private void authenticate(String login, String password) {
        if (!userService.isLoginExist(login)) {
            writer.println(String.format(INCORRECT_LOGIN_MESSAGE, login));
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