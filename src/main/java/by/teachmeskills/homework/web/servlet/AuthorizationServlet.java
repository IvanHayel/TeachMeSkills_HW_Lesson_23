package by.teachmeskills.homework.web.servlet;

import by.teachmeskills.homework.entity.User;
import by.teachmeskills.homework.service.Service;
import by.teachmeskills.homework.service.UserService;
import by.teachmeskills.homework.web.constant.attribute.SessionAttribute;
import by.teachmeskills.homework.web.constant.message.AuthorizationMessage;
import by.teachmeskills.homework.web.constant.parameter.UserParameter;
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
    private static final Service<String, User> userService = new UserService();

    private transient HttpSession session;
    private transient PrintWriter writer;

    @Override
    @SneakyThrows(IOException.class)
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        session = req.getSession();
        writer = resp.getWriter();

        if (session.getAttribute(SessionAttribute.USER.get()) != null) {
            writer.println(AuthorizationMessage.ALREADY_AUTHENTICATED.get());
        } else {
            @NonNull String login = req.getParameter(UserParameter.LOGIN.get());
            @NonNull String password = req.getParameter(UserParameter.PASSWORD.get());
            authenticate(login, password);
        }
    }

    private void authenticate(String login, String password) {
        if (session == null || writer == null) return;
        if (userService.contains(login)) { // TODO: revert result. There will be fewer steps of 'if's
            User user = userService.getByKey(login);
            if (user.getPassword().equals(password)) {
                session.setAttribute(SessionAttribute.USER.get(), user);
                writer.println(AuthorizationMessage.SUCCESS.get(user));
            } else {
                writer.println(AuthorizationMessage.PASSWORD_MISMATCH.get(user));
            }
        } else {
            writer.println(AuthorizationMessage.USER_NOT_EXIST.get(login));
        }
    }
}