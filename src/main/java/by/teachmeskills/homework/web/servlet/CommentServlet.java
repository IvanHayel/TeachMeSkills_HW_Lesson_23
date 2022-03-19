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
import lombok.NonNull;
import lombok.SneakyThrows;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "CommentServlet", value = "/post/comment")
public class CommentServlet extends HttpServlet {
    private static final PostService postService = PostService.getInstance();
    private static final UserService userService = UserService.getInstance();
    private static final String USER_SESSION_ATTRIBUTE = "user";
    private static final String POST_ID_PARAMETER = "post-id";
    private static final String POST_NOT_EXIST_MESSAGE = "Post %d not exist!";
    private static final String COMMENT_ID_PARAMETER = "id";
    private static final String COMMENT_CONTENT_PARAMETER = "comment";
    private static final String COMMENT_DENY_MESSAGE = "Commenting denied!";
    private static final String DELETE_COMMENT_DENY_MESSAGE = "Deleting comment denied!";
    private static final String UPDATE_COMMENT_DENY_MESSAGE = "Updating comment denied!";

    private transient User currentUser;
    private transient PrintWriter writer;
    private transient HttpSession session;

    @Override
    @SneakyThrows(IOException.class)
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        session = req.getSession();
        writer = resp.getWriter();
        currentUser = (User) session.getAttribute(USER_SESSION_ATTRIBUTE);
        @NonNull Integer postId = Integer.valueOf(req.getParameter(POST_ID_PARAMETER));
        Post commentedPost = postService.getById(postId);
        if (commentedPost == null) {
            writer.println(String.format(POST_NOT_EXIST_MESSAGE, postId));
        } else {
            Comment comment = createComment(req, commentedPost);
            if (comment != null) {
                commentedPost.getComments().add(comment);
                writer.println(commentedPost);
            } else {
                writer.println(COMMENT_DENY_MESSAGE);
            }
        }
    }

    private Comment createComment(HttpServletRequest req, Post commentedPost) {
        String text = req.getParameter(COMMENT_CONTENT_PARAMETER);
        if (text == null) return null;
        Integer commentId = commentedPost.getCommentId();
        return new Comment(commentId, currentUser, text);
    }

    @Override
    @SneakyThrows(IOException.class)
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) {
        session = req.getSession();
        writer = resp.getWriter();
        currentUser = (User) session.getAttribute(USER_SESSION_ATTRIBUTE);
        @NonNull Integer postId = Integer.valueOf(req.getParameter(POST_ID_PARAMETER));
        Post commentedPost = postService.getById(postId);
        if (commentedPost == null) {
            writer.println(String.format(POST_NOT_EXIST_MESSAGE, postId));
        } else {
            @NonNull Integer commentToRemoveId = Integer.valueOf(req.getParameter(COMMENT_ID_PARAMETER));
            Comment commentToRemove = postService.getCommentById(commentedPost, commentToRemoveId);
            if (userService.isAdmin(currentUser) ||
                    postService.isOwner(commentedPost, currentUser) ||
                    postService.isOwner(commentToRemove, currentUser)
            ) {
                if (commentedPost.getComments().remove(commentToRemove))
                    writer.println(commentedPost);
                else writer.println(DELETE_COMMENT_DENY_MESSAGE);
            } else resp.sendError(HttpServletResponse.SC_FORBIDDEN);
        }
    }

    @Override
    @SneakyThrows(IOException.class)
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) {
        session = req.getSession();
        writer = resp.getWriter();
        currentUser = (User) session.getAttribute(USER_SESSION_ATTRIBUTE);

        @NonNull Integer postId = Integer.valueOf(req.getParameter(POST_ID_PARAMETER));
        Post commentedPost = postService.getById(postId);
        String textToUpdate = req.getParameter(COMMENT_CONTENT_PARAMETER);

        if (commentedPost == null) {
            writer.println(POST_NOT_EXIST_MESSAGE);
        } else {
            Integer commentToUpdateId = Integer.valueOf(req.getParameter(COMMENT_ID_PARAMETER));
            Comment commentToUpdate = postService.getCommentById(commentedPost, commentToUpdateId);
            if (userService.isAdmin(currentUser) || postService.isOwner(commentToUpdate, currentUser)) {
                if (commentToUpdate != null && textToUpdate != null) {
                    commentToUpdate.setText(textToUpdate);
                    writer.println(commentedPost);
                } else writer.println(UPDATE_COMMENT_DENY_MESSAGE);
            } else resp.sendError(HttpServletResponse.SC_FORBIDDEN);
        }
    }
}