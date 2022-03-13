package by.teachmeskills.homework.web.servlet;

import by.teachmeskills.homework.entity.User;
import by.teachmeskills.homework.web.constant.attribute.SessionAttribute;
import by.teachmeskills.homework.web.constant.message.LogoutMessage;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;

import java.io.IOException;

@WebServlet(name = "LogoutServlet", value = "/logout")
public class LogoutServlet extends HttpServlet {
    @Override
    @SneakyThrows(IOException.class)
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        User currentUser = (User) req.getSession().getAttribute(SessionAttribute.USER.get());
        req.getSession().invalidate();
        resp.getWriter().println(LogoutMessage.LOGOUT.get(currentUser));
    }
}