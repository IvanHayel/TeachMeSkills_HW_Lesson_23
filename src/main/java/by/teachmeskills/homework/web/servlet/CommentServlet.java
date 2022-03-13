package by.teachmeskills.homework.web.servlet;

import by.teachmeskills.homework.entity.Comment;
import by.teachmeskills.homework.entity.Post;
import by.teachmeskills.homework.entity.User;
import by.teachmeskills.homework.service.PostService;
import by.teachmeskills.homework.service.Service;
import by.teachmeskills.homework.web.constant.attribute.SessionAttribute;
import by.teachmeskills.homework.web.constant.message.CommentMessage;
import by.teachmeskills.homework.web.constant.message.PostMessage;
import by.teachmeskills.homework.web.constant.parameter.CommentParameter;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.SneakyThrows;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(name = "CommentServlet", value = "/post/comment")
public class CommentServlet extends HttpServlet {
    private static final Service<Integer, Post> postService = new PostService();

    private transient User currentUser;
    private transient PrintWriter writer;
    private transient HttpSession session;

    @Override
    @SneakyThrows(IOException.class)
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        session = req.getSession();
        writer = resp.getWriter();
        currentUser = (User) session.getAttribute(SessionAttribute.USER.get());
        comment(req);
    }

    private void comment(HttpServletRequest req) {
        Integer postId = Integer.valueOf(req.getParameter(CommentParameter.POST_ID.get()));
        Post commentedPost = postService.getByKey(postId);
        if (commentedPost == null) {
            writer.println(PostMessage.POST_NOT_EXIST.get(postId));
        } else {
            Comment comment = createComment(req, commentedPost);
            if (comment != null) {
                commentedPost.getComments().add(comment);
                writer.println(CommentMessage.SUCCESS.get(commentedPost, currentUser));
            } else {
                writer.println(CommentMessage.DENIED.get(commentedPost, currentUser));
            }
        }
    }

    private Comment createComment(HttpServletRequest req, Post commentedPost) {
        String text = req.getParameter(CommentParameter.COMMENT.get());
        if (text == null) return null;
        List<Comment> comments = commentedPost.getComments();
        Integer commentId = comments.size() + 1;
        return new Comment(commentId, currentUser, text);
    }

    @Override
    @SneakyThrows(IOException.class)
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) {
        session = req.getSession();
        writer = resp.getWriter();
        currentUser = (User) session.getAttribute(SessionAttribute.USER.get());

        Integer postId = Integer.valueOf(req.getParameter(CommentParameter.POST_ID.get()));
        Post commentedPost = postService.getByKey(postId);

        if (commentedPost == null) {
            writer.println(PostMessage.POST_NOT_EXIST.get(postId));
        } else {
            Integer commentToRemoveId = Integer.valueOf(req.getParameter(CommentParameter.ID.get()));
            List<Comment> comments = commentedPost.getComments();
            Comment commentToRemove = comments.stream().filter(item -> item.getId().equals(commentToRemoveId)).findFirst().orElse(null);
            delete(commentedPost, commentToRemove);
        }
    }

    private void delete(Post commentedPost, Comment commentToRemove) {
        if (isAdminOrPostOwnerOrCommentOwner(commentedPost, commentToRemove)) {
            if (commentedPost.getComments().remove(commentToRemove)) {
                writer.println(CommentMessage.DELETE_SUCCESS.get(commentedPost, currentUser));
            } else writer.println(CommentMessage.DELETE_DENY.get(commentedPost));
        } else writer.println(CommentMessage.ACCESS_DENY.get());

    }

    private boolean isAdminOrPostOwnerOrCommentOwner(Post post, Comment comment) {
        return currentUser.isAdmin() || comment.getUser().equals(currentUser) || post.getUser().equals(currentUser);
    }

    @Override
    @SneakyThrows(IOException.class)
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) {
        session = req.getSession();
        writer = resp.getWriter();
        currentUser = (User) session.getAttribute(SessionAttribute.USER.get());

        Integer postId = Integer.valueOf(req.getParameter(CommentParameter.POST_ID.get()));
        Post commentedPost = postService.getByKey(postId);
        String textToUpdate = req.getParameter(CommentParameter.COMMENT.get());

        if (commentedPost == null) {
            writer.println(PostMessage.POST_NOT_EXIST.get(postId));
        } else {
            Integer commentToUpdateId = Integer.valueOf(req.getParameter(CommentParameter.ID.get()));
            List<Comment> comments = commentedPost.getComments();
            Comment commentToUpdate = comments.stream().filter(item -> item.getId().equals(commentToUpdateId)).findFirst().orElse(null);
            update(textToUpdate, commentToUpdate, commentedPost);
        }
    }

    private void update(String textToUpdate, Comment commentToUpdate, Post commentedPost) {
        if (isAdminOrCommentOwner(commentToUpdate)) {
            if (commentToUpdate != null && textToUpdate != null) {
                commentToUpdate.setText(textToUpdate);
                writer.println(CommentMessage.UPDATE_SUCCESS.get(commentedPost, currentUser));
            } else writer.println(CommentMessage.UPDATE_DENY.get(commentedPost));
        } else writer.println(CommentMessage.ACCESS_DENY);
    }

    private boolean isAdminOrCommentOwner(Comment comment) {
        return currentUser.isAdmin() || comment.getUser().equals(currentUser);
    }
}