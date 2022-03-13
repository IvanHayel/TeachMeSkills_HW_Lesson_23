package by.teachmeskills.homework.web.servlet;

import by.teachmeskills.homework.entity.Role;
import by.teachmeskills.homework.entity.User;
import by.teachmeskills.homework.service.Service;
import by.teachmeskills.homework.service.UserService;
import by.teachmeskills.homework.web.constant.attribute.SessionAttribute;
import by.teachmeskills.homework.web.constant.message.UserMessage;
import by.teachmeskills.homework.web.constant.parameter.UserParameter;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "UserServlet", value = "/user")
public class UserServlet extends HttpServlet {
    private static final Service<String, User> userService = new UserService();

    private transient User currentUser;

    @Override
    @SneakyThrows(IOException.class)
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) {
        currentUser = (User) req.getSession().getAttribute(SessionAttribute.USER.get());
        User userToUpdate = userService.getByKey(currentUser.getLogin());
        update(req, userToUpdate);
        currentUser = userToUpdate;
        resp.getWriter().println(UserMessage.UPDATE_SUCCESS.get(currentUser));
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
        PrintWriter writer = resp.getWriter();
        currentUser = (User) req.getSession().getAttribute(SessionAttribute.USER.get());
        if (userService.removeByKey(currentUser.getLogin()) != null) {
            writer.println(UserMessage.DELETE_SUCCESS.get(currentUser));
            req.getSession().invalidate();
        } else {
            writer.println(UserMessage.DELETE_DENY.get(currentUser));
        }
    }
}