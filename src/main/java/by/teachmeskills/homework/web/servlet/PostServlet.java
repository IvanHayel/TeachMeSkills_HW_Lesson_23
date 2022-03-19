package by.teachmeskills.homework.web.servlet;

import by.teachmeskills.homework.entity.Post;
import by.teachmeskills.homework.entity.User;
import by.teachmeskills.homework.service.PostService;
import by.teachmeskills.homework.service.UserService;
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

@WebServlet(name = "PostServlet", value = "/post")
public class PostServlet extends HttpServlet {
    private static final PostService postService = PostService.getInstance();
    private static final UserService userService = UserService.getInstance();
    private static final String USER_SESSION_ATTRIBUTE = "user";
    private static final String POST_ID_PARAMETER = "id";
    private static final String POST_TEXT_PARAMETER = "text";
    private static final String POST_LIKE_PARAMETER = "like";
    private static final String POST_NOT_EXIST_MESSAGE = "Post %d not exist!";
    private static final String POST_DELETE_SUCCESS_MESSAGE = "%s deleted successfully!";
    private static final String POST_DELETE_DENY_MESSAGE = "Deleting %s denied!";

    private transient User currentUser;
    private transient PrintWriter writer;
    private transient HttpSession session;

    @Override
    @SneakyThrows(IOException.class)
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        writer = resp.getWriter();
        String postId = req.getParameter(POST_ID_PARAMETER);
        if (postId == null) getAllPosts();
        else getPostById(postId);
    }

    private void getAllPosts() {
        List<Post> posts = postService.getAll();
        writer.println(posts);
    }

    private void getPostById(String postId) {
        Integer id = Integer.valueOf(postId);
        Post post = postService.getById(id);
        if (post == null) writer.println(String.format(POST_NOT_EXIST_MESSAGE, id));
        else writer.println(post);
    }

    @Override
    @SneakyThrows(IOException.class)
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        session = req.getSession();
        writer = resp.getWriter();
        currentUser = (User) session.getAttribute(USER_SESSION_ATTRIBUTE);
        Post post = createPostFromRequestParameters(req);
        if (postService.save(post)) writer.println(post);
        else resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
    }

    private Post createPostFromRequestParameters(HttpServletRequest req) {
        @NonNull User owner = currentUser;
        @NonNull Integer id = Integer.valueOf(req.getParameter(POST_ID_PARAMETER));
        @NonNull String text = req.getParameter(POST_TEXT_PARAMETER);
        return new Post(id, owner, text);
    }

    @Override
    @SneakyThrows(IOException.class)
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) {
        session = req.getSession();
        writer = resp.getWriter();
        currentUser = (User) session.getAttribute(USER_SESSION_ATTRIBUTE);
        @NonNull Integer postId = Integer.valueOf(req.getParameter(POST_ID_PARAMETER));
        Post postToDelete = postService.getById(postId);
        if (postToDelete != null) {
            if (userService.isAdmin(currentUser) || postService.isOwner(postToDelete, currentUser)) {
                if (postService.removeById(postId) != null) {
                    writer.println(String.format(POST_DELETE_SUCCESS_MESSAGE, postToDelete));
                } else writer.println(String.format(POST_DELETE_DENY_MESSAGE, postToDelete));
            } else resp.sendError(HttpServletResponse.SC_FORBIDDEN);
        } else writer.println(String.format(POST_NOT_EXIST_MESSAGE, postId));
    }

    @Override
    @SneakyThrows(IOException.class)
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) {
        session = req.getSession();
        writer = resp.getWriter();
        currentUser = (User) session.getAttribute(USER_SESSION_ATTRIBUTE);
        @NonNull Integer postId = Integer.valueOf(req.getParameter(POST_ID_PARAMETER));
        Post postToUpdate = postService.getById(postId);
        String textToUpdate = req.getParameter(POST_TEXT_PARAMETER);
        String like = req.getParameter(POST_LIKE_PARAMETER);

        if (postToUpdate == null) {
            writer.println(String.format(POST_NOT_EXIST_MESSAGE, postId));
            return;
        }
        if (textToUpdate != null) {
            if (userService.isAdmin(currentUser) || postService.isOwner(postToUpdate, currentUser)) {
                postToUpdate.setText(textToUpdate);
                writer.println(postToUpdate);
            } else {
                resp.sendError(HttpServletResponse.SC_FORBIDDEN);
            }
        }
        if (like != null) like(postToUpdate);
    }

    private void like(Post postToUpdate) {
        String userFullName = currentUser.getFullName();
        List<String> likes = postToUpdate.getLikes();
        if (!likes.remove(userFullName)) likes.add(userFullName);
        writer.println(postToUpdate);
    }
}