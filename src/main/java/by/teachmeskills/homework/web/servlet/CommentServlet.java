package by.teachmeskills.homework.web.servlet;

import by.teachmeskills.homework.entity.Comment;
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

@WebServlet(name = "CommentServlet", value = "/post/comment")
public class CommentServlet extends HttpServlet {
    private static final PostService postService = PostService.getInstance();
    private static final UserService userService = UserService.getInstance();
    private static final String USER_SESSION_ATTRIBUTE = "user";
    private static final String POST_ID_PARAMETER = "post-id";
    private static final String COMMENT_ID_PARAMETER = "id";
    private static final String COMMENT_CONTENT_PARAMETER = "comment";
    private static final String POST_NOT_EXIST_MESSAGE = "Post %d not exist!";
    private static final String COMMENT_DENY_MESSAGE = "Commenting denied!";
    private static final String DELETE_COMMENT_DENY_MESSAGE = "Deleting comment denied!";

    private transient User currentUser;
    private transient PrintWriter writer;
    private transient HttpSession session;

    @Override
    @SneakyThrows(IOException.class)
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        session = req.getSession();
        writer = resp.getWriter();
        currentUser = (User) session.getAttribute(USER_SESSION_ATTRIBUTE);
        String postId = req.getParameter(POST_ID_PARAMETER);
        if (isAnyParameterMissing(postId))
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
        else
            getCreationResult(req, postId);
    }

    private void getCreationResult(HttpServletRequest req, String postId) {
        Integer parsedPostId = Integer.valueOf(postId);
        Post commentedPost = postService.getById(parsedPostId);
        if (commentedPost == null) {
            writer.println(String.format(POST_NOT_EXIST_MESSAGE, parsedPostId));
        } else {
            String content = req.getParameter(COMMENT_CONTENT_PARAMETER);
            Comment comment = postService.createComment(commentedPost, currentUser, content);
            if (comment != null) {
                postService.addComment(commentedPost, comment);
                writer.println(commentedPost);
            } else {
                writer.println(COMMENT_DENY_MESSAGE);
            }
        }
    }

    @Override
    @SneakyThrows(IOException.class)
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) {
        session = req.getSession();
        writer = resp.getWriter();
        currentUser = (User) session.getAttribute(USER_SESSION_ATTRIBUTE);
        String postId = req.getParameter(POST_ID_PARAMETER);
        String commentId = req.getParameter(COMMENT_ID_PARAMETER);
        if (isAnyParameterMissing(postId, commentId)) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
        } else {
            getDeletingResult(resp, postId, commentId);
        }
    }

    private void getDeletingResult(HttpServletResponse resp, String postId, String commentId) throws IOException {
        Integer parsedPostId = Integer.valueOf(postId);
        Post commentedPost = postService.getById(parsedPostId);
        if (commentedPost != null) {
            Integer parsedCommentId = Integer.valueOf(commentId);
            Comment commentToRemove = postService.getCommentById(commentedPost, parsedCommentId);
            if (userService.isAdmin(currentUser) || postService.isOwner(commentedPost, currentUser) ||
                postService.isOwner(commentToRemove, currentUser)
            ) {
                if (commentedPost.getComments().remove(commentToRemove)) {
                    writer.println(commentedPost);
                } else writer.println(DELETE_COMMENT_DENY_MESSAGE);
            } else resp.sendError(HttpServletResponse.SC_FORBIDDEN);
        } else {
            writer.println(String.format(POST_NOT_EXIST_MESSAGE, parsedPostId));
        }
    }

    @Override
    @SneakyThrows(IOException.class)
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) {
        session = req.getSession();
        writer = resp.getWriter();
        currentUser = (User) session.getAttribute(USER_SESSION_ATTRIBUTE);
        String postId = req.getParameter(POST_ID_PARAMETER);
        String commentId = req.getParameter(COMMENT_ID_PARAMETER);
        String contentToUpdate = req.getParameter(COMMENT_CONTENT_PARAMETER);
        if (isAnyParameterMissing(postId, commentId, contentToUpdate)) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
        } else {
            getUpdatingResult(resp, postId, commentId, contentToUpdate);
        }
    }

    private void getUpdatingResult(HttpServletResponse resp, String postId, String commentId, String contentToUpdate)
            throws IOException {
        Integer parsedPostId = Integer.valueOf(postId);
        Post commentedPost = postService.getById(parsedPostId);
        if (commentedPost == null) {
            writer.println(POST_NOT_EXIST_MESSAGE);
        } else {
            Integer parsedCommentId = Integer.valueOf(commentId);
            Comment commentToUpdate = postService.getCommentById(commentedPost, parsedCommentId);
            if (userService.isAdmin(currentUser) || postService.isOwner(commentToUpdate, currentUser)) {
                postService.updateComment(commentToUpdate, contentToUpdate);
                writer.println(commentedPost);
            } else resp.sendError(HttpServletResponse.SC_FORBIDDEN);
        }
    }

    private boolean isAnyParameterMissing(String... parameters) {
        for (String parameter : parameters)
            if (parameter == null) return true;
        return false;
    }
}