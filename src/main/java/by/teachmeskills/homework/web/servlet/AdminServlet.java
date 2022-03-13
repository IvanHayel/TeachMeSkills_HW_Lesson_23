package by.teachmeskills.homework.web.servlet;

import by.teachmeskills.homework.entity.Role;
import by.teachmeskills.homework.entity.User;
import by.teachmeskills.homework.service.Service;
import by.teachmeskills.homework.service.UserService;
import by.teachmeskills.homework.web.constant.attribute.SessionAttribute;
import by.teachmeskills.homework.web.constant.message.AdminMessage;
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
import java.util.List;
import java.util.stream.Collectors;

@WebServlet(name = "AdminServlet", value = "/admin")
public class AdminServlet extends HttpServlet {
    private static final Service<String, User> userService = new UserService();

    private transient User admin;
    private transient PrintWriter writer;
    private transient HttpSession session;

    @Override
    @SneakyThrows(IOException.class)
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        writer = resp.getWriter();
        session = req.getSession();
        admin = (User) session.getAttribute(SessionAttribute.USER.get());
        provideUsers(req);
    }

    private void provideUsers(HttpServletRequest req) {
        String requestedUserLogin = req.getParameter(UserParameter.LOGIN.get());
        if (requestedUserLogin == null) {
            List<User> users = userService.getAll();
            writer.println(AdminMessage.PROVIDE_ALL_USERS.get(users));
        } else {
            User requestedUser = userService.getByKey(requestedUserLogin);
            if (requestedUser != null) writer.println(AdminMessage.PROVIDE_USER.get(requestedUser));
            else writer.println(AdminMessage.NOT_EXIST.get(requestedUserLogin));
        }
    }

    @Override
    @SneakyThrows(IOException.class)
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        writer = resp.getWriter();
        session = req.getSession();
        admin = (User) session.getAttribute(SessionAttribute.USER.get());
        User user = createUserFromRequestParameters(req);
        if (userService.save(user)) resp.getWriter().println(AdminMessage.CREATE_USER.get(user, admin));
        else resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
    }

    private User createUserFromRequestParameters(HttpServletRequest req) {
        Role role = getRole(req.getParameter(UserParameter.ROLE.get()));
        String login = req.getParameter(UserParameter.LOGIN.get());
        String name = req.getParameter(UserParameter.NAME.get());
        String surname = req.getParameter(UserParameter.SURNAME.get());
        String password = req.getParameter(UserParameter.PASSWORD.get());
        return new User(login, role, name, surname, password);
    }

    private Role getRole(String roleParameter) {
        roleParameter = roleParameter == null ? Role.USER_ROLE : roleParameter;
        switch (roleParameter) {
            case Role.ADMIN_ROLE:
                return new Role(Role.ADMIN_ROLE_ID, Role.ADMIN_ACCESS_LEVEL);
            default:
                return new Role(Role.COMMON_USER_ROLE_ID, Role.COMMON_USER_ACCESS_LEVEL);
        }
    }

    @Override
    @SneakyThrows(IOException.class)
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) {
        writer = resp.getWriter();
        session = req.getSession();
        admin = (User) session.getAttribute(SessionAttribute.USER.get());
        deleteUsers(req);
    }

    private void deleteUsers(HttpServletRequest req) {
        String requestedUserLogin = req.getParameter(UserParameter.LOGIN.get());
        if (requestedUserLogin == null) {
            List<User> users = userService.getAll();
            List<User> usersToDelete = users.stream().filter(user -> !user.equals(admin)).collect(Collectors.toList());
            List<User> deletedUsers = userService.removeAll(usersToDelete);
            writer.println(AdminMessage.DELETE_ALL_USERS.get(deletedUsers));
        } else {
            User userToDelete = userService.getByKey(requestedUserLogin);
            if (userToDelete != null) {
                if (userService.removeByKey(requestedUserLogin) != null) {
                    writer.println(AdminMessage.DELETE_USER_SUCCESS.get(userToDelete, admin));
                } else writer.println(AdminMessage.DELETE_USER_DENY.get(userToDelete));
            } else writer.println(AdminMessage.NOT_EXIST.get(requestedUserLogin));
        }
    }

    @Override
    @SneakyThrows(IOException.class)
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) {
        writer = resp.getWriter();
        session = req.getSession();
        admin = (User) session.getAttribute(SessionAttribute.USER.get());
        @NonNull String requestedUserLogin = req.getParameter(UserParameter.LOGIN.get());
        User userToUpdate = userService.getByKey(requestedUserLogin);
        if (userToUpdate != null) update(req, userToUpdate);
        else writer.println(AdminMessage.NOT_EXIST.get(requestedUserLogin));

    }

    private void update(HttpServletRequest req, User userToUpdate) {
        Role role = getRole(req.getParameter(UserParameter.ROLE.get()));
        String name = req.getParameter(UserParameter.NAME.get());
        String surname = req.getParameter(UserParameter.SURNAME.get());
        String password = req.getParameter(UserParameter.PASSWORD.get());
        userToUpdate.setRole(role);
        if (name != null) userToUpdate.setName(name);
        if (surname != null) userToUpdate.setSurname(surname);
        if (password != null) userToUpdate.setPassword(password);
        writer.println(AdminMessage.UPDATE_USER.get(userToUpdate, admin));
    }
}