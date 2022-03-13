package by.teachmeskills.homework.web.servlet;

import by.teachmeskills.homework.entity.Post;
import by.teachmeskills.homework.entity.User;
import by.teachmeskills.homework.service.PostService;
import by.teachmeskills.homework.service.Service;
import by.teachmeskills.homework.web.constant.attribute.SessionAttribute;
import by.teachmeskills.homework.web.constant.message.PostHistoryMessage;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet(name = "PostHistoryServlet", value = "/my-posts")
public class PostHistoryServlet extends HttpServlet {
    private static final Service<Integer, Post> postService = new PostService();

    @Override
    @SneakyThrows(IOException.class)
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        User currentUser = (User) req.getSession().getAttribute(SessionAttribute.USER.get());
        List<Post> allPosts = postService.getAll();
        List<Post> userPosts = allPosts.stream()
                .filter(post -> post.getUser().equals(currentUser))
                .collect(Collectors.toList());
        resp.getWriter().println(PostHistoryMessage.PROVIDE_USER_POSTS.get(userPosts));
    }

    @Override
    @SneakyThrows(IOException.class)
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) {
        User currentUser = (User) req.getSession().getAttribute(SessionAttribute.USER.get());
        List<Post> allPosts = postService.getAll();
        List<Post> userPosts = allPosts.stream()
                .filter(post -> post.getUser().equals(currentUser))
                .collect(Collectors.toList());
        List<Post> removedUsers = postService.removeAll(userPosts);
        resp.getWriter().println(PostHistoryMessage.DELETE_USER_POSTS.get(removedUsers));
    }
}