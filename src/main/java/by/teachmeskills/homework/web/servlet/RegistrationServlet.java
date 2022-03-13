package by.teachmeskills.homework.web.servlet;

import by.teachmeskills.homework.entity.Role;
import by.teachmeskills.homework.entity.User;
import by.teachmeskills.homework.service.Service;
import by.teachmeskills.homework.service.UserService;
import by.teachmeskills.homework.web.constant.message.RegistrationMessage;
import by.teachmeskills.homework.web.constant.parameter.UserParameter;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;

import java.io.IOException;

@WebServlet(name = "RegistrationServlet", value = "/registration")
public class RegistrationServlet extends HttpServlet {
    private static final Service<String, User> userService = new UserService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        register(req, resp);
    }

    @SneakyThrows(IOException.class)
    private void register(HttpServletRequest req, HttpServletResponse resp) {
        User user = createUserFromRequestParameters(req);
        if (userService.save(user))
            resp.getWriter().println(RegistrationMessage.SUCCESS.get(user));
        else
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
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
}