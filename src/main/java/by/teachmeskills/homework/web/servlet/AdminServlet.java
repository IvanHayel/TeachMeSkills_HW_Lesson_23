package by.teachmeskills.homework.web.servlet;

import by.teachmeskills.homework.entity.User;
import by.teachmeskills.homework.service.UserService;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.SneakyThrows;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(name = "AdminServlet", value = "/admin")
public class AdminServlet extends HttpServlet {
    private static final UserService userService = UserService.getInstance();
    private static final String USER_ID_PARAMETER = "id";
    private static final String USER_LOGIN_PARAMETER = "login";
    private static final String USER_NAME_PARAMETER = "name";
    private static final String USER_SURNAME_PARAMETER = "surname";
    private static final String USER_PASSWORD_PARAMETER = "password";
    private static final String INCORRECT_LOGIN_MESSAGE = "%s - not exist!";
    private static final String REMOVE_ALL_COMMON_USERS_MESSAGE = "All common users removed";

    private transient PrintWriter writer;

    @Override
    @SneakyThrows(IOException.class)
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        writer = resp.getWriter();
        getAllUsers(req);
    }

    private void getAllUsers(HttpServletRequest req) {
        String requestedUserLogin = req.getParameter(USER_LOGIN_PARAMETER);
        if (requestedUserLogin == null) {
            List<User> users = userService.getAll();
            writer.println(users);
        } else {
            User requestedUser = userService.getByLogin(requestedUserLogin);
            if (requestedUser != null) writer.println(requestedUser);
            else writer.println(INCORRECT_LOGIN_MESSAGE);
        }
    }

    @Override
    @SneakyThrows(IOException.class)
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        writer = resp.getWriter();
        User user = createUserFromRequestParameters(req);
        if (userService.save(user)) writer.println(user);
        else resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
    }

    private User createUserFromRequestParameters(HttpServletRequest req) {
        Integer id = Integer.valueOf(req.getParameter(USER_ID_PARAMETER));
        String login = req.getParameter(req.getParameter(USER_LOGIN_PARAMETER));
        String name = req.getParameter(USER_NAME_PARAMETER);
        String surname = req.getParameter(USER_SURNAME_PARAMETER);
        String password = req.getParameter(USER_PASSWORD_PARAMETER);
        return userService.createUser(id, login, password, name, surname);
    }

    @Override
    @SneakyThrows(IOException.class)
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) {
        writer = resp.getWriter();
        String requestedUserLogin = req.getParameter(USER_LOGIN_PARAMETER);
        if (requestedUserLogin == null) {
            if (userService.removeAllCommonUsers())
                writer.println(REMOVE_ALL_COMMON_USERS_MESSAGE);
            else
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
        } else {
            User userToDelete = userService.getByLogin(requestedUserLogin);
            if (userToDelete != null) {
                if (userService.removeById(userToDelete.getId()) != null) {
                    writer.println(userToDelete);
                } else resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            } else resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    @Override
    @SneakyThrows(IOException.class)
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) {
        writer = resp.getWriter();
        @NonNull String requestedUserLogin = req.getParameter(USER_LOGIN_PARAMETER);
        User userToUpdate = userService.getByLogin(requestedUserLogin);
        if (userToUpdate != null) {
            String login = req.getParameter(USER_LOGIN_PARAMETER);
            String name = req.getParameter(USER_NAME_PARAMETER);
            String surname = req.getParameter(USER_SURNAME_PARAMETER);
            String password = req.getParameter(USER_PASSWORD_PARAMETER);
            writer.println(userService.updateUser(userToUpdate, login, name, surname, password));
        } else resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
    }
}