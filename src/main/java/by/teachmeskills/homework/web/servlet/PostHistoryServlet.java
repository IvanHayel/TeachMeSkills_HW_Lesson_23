package by.teachmeskills.homework.web.servlet;

import by.teachmeskills.homework.entity.Post;
import by.teachmeskills.homework.entity.User;
import by.teachmeskills.homework.service.PostService;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(name = "PostHistoryServlet", value = "/my-posts")
public class PostHistoryServlet extends HttpServlet {
    private static final PostService postService = PostService.getInstance();
    private static final String USER_SESSION_ATTRIBUTE = "user";
    private static final String DELETE_POSTS_SUCCESS_MESSAGE = "All your posts deleted!";
    private static final String DELETE_POSTS_DENY_MESSAGE = "Deleting posts denied!";

    private transient User currentUser;
    private transient PrintWriter writer;

    @Override
    @SneakyThrows(IOException.class)
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        currentUser = (User) req.getSession().getAttribute(USER_SESSION_ATTRIBUTE);
        writer = resp.getWriter();
        List<Post> userPosts = postService.getUserPosts(currentUser);
        writer.println(userPosts);
    }

    @Override
    @SneakyThrows(IOException.class)
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) {
        currentUser = (User) req.getSession().getAttribute(USER_SESSION_ATTRIBUTE);
        writer = resp.getWriter();
        List<Post> userPosts = postService.getUserPosts(currentUser);
        if(postService.removeAll(userPosts)) writer.println(DELETE_POSTS_SUCCESS_MESSAGE);
        else writer.println(DELETE_POSTS_DENY_MESSAGE);
    }
}