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
import java.util.List;

@WebServlet(name = "AdminServlet", value = "/admin")
public class AdminServlet extends HttpServlet {
    private static final UserService userService = UserService.getInstance();
    private static final String USER_ID_PARAMETER = "id";
    private static final String USER_LOGIN_PARAMETER = "login";
    private static final String USER_NAME_PARAMETER = "name";
    private static final String USER_SURNAME_PARAMETER = "surname";
    private static final String USER_PASSWORD_PARAMETER = "password";
    private static final String ADMIN_RIGHTS_PARAMETER = "admin-rights";
    private static final String USER_NOT_EXIST_MESSAGE = "%s - not exist!";
    private static final String REMOVE_ALL_COMMON_USERS_MESSAGE = "All common users removed";
    private static final String USER_EXIST_MESSAGE = "You are already registered!";
    private static final String LOGIN_EXIST_MESSAGE = "Login already taken by another user. Try another!";
    private static final String ID_EXIST_MESSAGE = "Identifier already taken by another user. Try another!";
    private static final String USER_CREATION_SUCCESS_MESSAGE = "%s successfully created";
    private static final String MAKE_ADMIN_MESSAGE = "You've granted administrator rights to %s.";
    private static final String REMOVE_ADMIN_MESSAGE = "You've removed administrator rights from %s.";

    private transient PrintWriter writer;

    @Override
    @SneakyThrows(IOException.class)
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        writer = resp.getWriter();
        getAllUsers(req);
    }

    private void getAllUsers(HttpServletRequest req) {
        String requestedUserId = req.getParameter(USER_ID_PARAMETER);
        if (requestedUserId == null)
            getAllUsers();
        else
            getUserByRequestedId(requestedUserId);
    }

    private void getAllUsers() {
        List<User> users = userService.getAll();
        writer.println(users);
    }

    private void getUserByRequestedId(String requestedUserId) {
        Integer parsedUserId = Integer.parseInt(requestedUserId);
        User requestedUser = userService.getById(parsedUserId);
        if (requestedUser != null) writer.println(requestedUser);
        else writer.println(String.format(USER_NOT_EXIST_MESSAGE, parsedUserId));
    }

    @Override
    @SneakyThrows(IOException.class)
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        writer = resp.getWriter();
        String id = req.getParameter(USER_ID_PARAMETER);
        String login = req.getParameter(USER_LOGIN_PARAMETER);
        String name = req.getParameter(USER_NAME_PARAMETER);
        String surname = req.getParameter(USER_SURNAME_PARAMETER);
        String password = req.getParameter(USER_PASSWORD_PARAMETER);
        if (isAnyParameterMissing(id, login, name, surname, password))
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
        else
            getCreationResult(id, login, name, surname, password);

    }

    private void getCreationResult(String id, String login, String name, String surname, String password) {
        Integer parsedId = Integer.valueOf(id);
        User user = userService.createUser(parsedId, login, password, name, surname);
        if (userService.isUserExist(user)) {
            writer.println(USER_EXIST_MESSAGE);
        } else if (userService.isLoginExist(login)) {
            writer.println(LOGIN_EXIST_MESSAGE);
        } else if (userService.isIdExist(parsedId)) {
            writer.println(ID_EXIST_MESSAGE);
        } else {
            userService.save(user);
            writer.println(String.format(USER_CREATION_SUCCESS_MESSAGE, user));
        }
    }

    @Override
    @SneakyThrows(IOException.class)
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) {
        writer = resp.getWriter();
        String requestedUserId = req.getParameter(USER_ID_PARAMETER);
        if (requestedUserId == null)
            getResultOfDeletingAllCommonUsers(resp);
        else
            getResultOfDeletingByRequestedId(resp, requestedUserId);

    }

    private void getResultOfDeletingAllCommonUsers(HttpServletResponse resp) throws IOException {
        if (userService.removeAllCommonUsers())
            writer.println(REMOVE_ALL_COMMON_USERS_MESSAGE);
        else
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
    }

    private void getResultOfDeletingByRequestedId(HttpServletResponse resp, String requestedUserId) throws IOException {
        Integer parsedUserId = Integer.valueOf(requestedUserId);
        User userToDelete = userService.getById(parsedUserId);
        if (userToDelete != null) {
            if (userService.removeById(parsedUserId) != null) {
                writer.println(userToDelete);
            } else resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
        } else resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
    }

    @Override
    @SneakyThrows(IOException.class)
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) {
        writer = resp.getWriter();
        String userId = req.getParameter(USER_ID_PARAMETER);
        if (isAnyParameterMissing(userId))
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
        else
            getUpdatingResult(req, resp, userId);

    }

    private void getUpdatingResult(HttpServletRequest req, HttpServletResponse resp, String userId) throws IOException {
        Integer parsedUserId = Integer.valueOf(userId);
        User userToUpdate = userService.getById(parsedUserId);
        if (userToUpdate != null) {
            String login = req.getParameter(USER_LOGIN_PARAMETER);
            String name = req.getParameter(USER_NAME_PARAMETER);
            String surname = req.getParameter(USER_SURNAME_PARAMETER);
            String password = req.getParameter(USER_PASSWORD_PARAMETER);
            String adminRights = req.getParameter(ADMIN_RIGHTS_PARAMETER);
            writer.println(userService.updateUser(userToUpdate, login, name, surname, password));
            if (adminRights != null) changeAdministratorRights(userToUpdate);
        } else resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
    }

    private void changeAdministratorRights(User userToUpdate) {
        if (userService.isAdmin(userToUpdate)) {
            userService.removeAdministratorRights(userToUpdate);
            writer.println(String.format(REMOVE_ADMIN_MESSAGE, userToUpdate));
        } else {
            userService.grantAdministratorRights(userToUpdate);
            writer.println(String.format(MAKE_ADMIN_MESSAGE, userToUpdate));
        }
    }

    private boolean isAnyParameterMissing(String... parameters) {
        for (String parameter : parameters)
            if (parameter == null) return true;
        return false;
    }
}