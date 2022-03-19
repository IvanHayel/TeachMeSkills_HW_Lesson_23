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
    private static final String POST_CONTENT_PARAMETER = "content";
    private static final String POST_LIKE_PARAMETER = "like";
    private static final String POST_NOT_EXIST_MESSAGE = "Post %d doesn't exist!";
    private static final String POST_ALREADY_EXIST_MESSAGE = "Post %d already exists!";
    private static final String POST_DELETE_SUCCESS_MESSAGE = "%s deleted successfully!";
    private static final String POST_DELETE_DENY_MESSAGE = "Deleting %s denied!";

    private transient User currentUser;
    private transient PrintWriter writer;
    private transient HttpSession session;

    @Override
    @SneakyThrows(IOException.class)
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        writer = resp.getWriter();
        String id = req.getParameter(POST_ID_PARAMETER);
        if (id == null) getAllPosts();
        else getPostByRequestedId(id);
    }

    private void getAllPosts() {
        List<Post> posts = postService.getAll();
        writer.println(posts);
    }

    private void getPostByRequestedId(String id) {
        Integer parsedId = Integer.valueOf(id);
        Post post = postService.getById(parsedId);
        if (post == null) writer.println(String.format(POST_NOT_EXIST_MESSAGE, parsedId));
        else writer.println(post);
    }

    @Override
    @SneakyThrows(IOException.class)
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        session = req.getSession();
        writer = resp.getWriter();
        currentUser = (User) session.getAttribute(USER_SESSION_ATTRIBUTE);
        String id = req.getParameter(POST_ID_PARAMETER);
        String content = req.getParameter(POST_CONTENT_PARAMETER);
        if (isAnyParameterMissing(id, content)) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
        } else {
            getCreationResult(resp, id, content);
        }
    }

    private void getCreationResult(HttpServletResponse resp, String id, String content) throws IOException {
        Integer parsedId = Integer.valueOf(id);
        Post post = new Post(parsedId, currentUser, content);
        if (postService.isContains(parsedId)) {
            writer.println(String.format(POST_ALREADY_EXIST_MESSAGE, parsedId));
        } else {
            if (postService.save(post)) writer.println(post);
            else resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    @Override
    @SneakyThrows(IOException.class)
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) {
        session = req.getSession();
        writer = resp.getWriter();
        currentUser = (User) session.getAttribute(USER_SESSION_ATTRIBUTE);
        String postId = req.getParameter(POST_ID_PARAMETER);
        if (isAnyParameterMissing(postId)) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
        } else {
            getDeletingResult(resp, postId);
        }
    }

    private void getDeletingResult(HttpServletResponse resp, String postId) throws IOException {
        Integer parsedPostId = Integer.valueOf(postId);
        Post postToDelete = postService.getById(parsedPostId);
        if (postToDelete != null) {
            if (userService.isAdmin(currentUser) || postService.isOwner(postToDelete, currentUser)) {
                if (postService.removeById(parsedPostId) != null) {
                    writer.println(String.format(POST_DELETE_SUCCESS_MESSAGE, postToDelete));
                } else writer.println(String.format(POST_DELETE_DENY_MESSAGE, postToDelete));
            } else resp.sendError(HttpServletResponse.SC_FORBIDDEN);
        } else writer.println(String.format(POST_NOT_EXIST_MESSAGE, parsedPostId));
    }

    @Override
    @SneakyThrows(IOException.class)
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) {
        session = req.getSession();
        writer = resp.getWriter();
        currentUser = (User) session.getAttribute(USER_SESSION_ATTRIBUTE);
        String postId = req.getParameter(POST_ID_PARAMETER);
        String contentToUpdate = req.getParameter(POST_CONTENT_PARAMETER);
        String like = req.getParameter(POST_LIKE_PARAMETER);
        if (isAnyParameterMissing(postId)) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
        } else {
            getUpdatingResult(resp, postId, contentToUpdate, like);
        }
    }

    private void getUpdatingResult(HttpServletResponse resp, String postId, String contentToUpdate, String like)
            throws IOException {
        Integer parsedPostId = Integer.valueOf(postId);
        Post postToUpdate = postService.getById(parsedPostId);
        if (postToUpdate == null) {
            writer.println(String.format(POST_NOT_EXIST_MESSAGE, parsedPostId));
            return;
        }
        if (contentToUpdate != null) {
            if (userService.isAdmin(currentUser) || postService.isOwner(postToUpdate, currentUser)) {
                postService.updatePostContent(postToUpdate, contentToUpdate);
                writer.println(postToUpdate);
            } else {
                resp.sendError(HttpServletResponse.SC_FORBIDDEN);
            }
        }
        if (like != null) {
            postService.likePost(postToUpdate, currentUser);
            writer.println(postToUpdate);
        }
    }

    private boolean isAnyParameterMissing(String... parameters) {
        for (String parameter : parameters)
            if (parameter == null) return true;
        return false;
    }
}