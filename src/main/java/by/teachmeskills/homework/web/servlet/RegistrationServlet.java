package by.teachmeskills.homework.web.servlet;

import by.teachmeskills.homework.entity.User;
import by.teachmeskills.homework.service.UserService;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "RegistrationServlet", value = "/registration")
public class RegistrationServlet extends HttpServlet {
    private static final UserService userService = UserService.getInstance();
    private static final String USER_ID_PARAMETER = "id";
    private static final String USER_LOGIN_PARAMETER = "login";
    private static final String USER_NAME_PARAMETER = "name";
    private static final String USER_SURNAME_PARAMETER = "surname";
    private static final String USER_PASSWORD_PARAMETER = "password";
    private static final String USER_EXIST_MESSAGE = "User already exist!";
    private static final String REGISTRATION_SUCCESS_MESSAGE = "%s registered successfully!";

    private transient PrintWriter writer;

    @Override
    @SneakyThrows(IOException.class)
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        writer = resp.getWriter();
        User user = createUserFromRequestParameters(req);
        if (userService.isUserAlreadyExist(user)) {
            writer.println(USER_EXIST_MESSAGE);
        } else {
            userService.save(user);
            writer.println(String.format(REGISTRATION_SUCCESS_MESSAGE, user));
        }
    }

    private User createUserFromRequestParameters(HttpServletRequest req) {
        Integer id = Integer.valueOf(req.getParameter(USER_ID_PARAMETER));
        String login = req.getParameter(req.getParameter(USER_LOGIN_PARAMETER));
        String name = req.getParameter(USER_NAME_PARAMETER);
        String surname = req.getParameter(USER_SURNAME_PARAMETER);
        String password = req.getParameter(USER_PASSWORD_PARAMETER);
        return userService.createUser(id, login, password, name, surname);
    }
}