package by.teachmeskills.homework.web.servlet;

import by.teachmeskills.homework.entity.Comment;
import by.teachmeskills.homework.entity.Post;
import by.teachmeskills.homework.entity.User;
import by.teachmeskills.homework.service.PostService;
import by.teachmeskills.homework.service.Service;
import by.teachmeskills.homework.web.constant.attribute.SessionAttribute;
import by.teachmeskills.homework.web.constant.message.PostMessage;
import by.teachmeskills.homework.web.constant.parameter.PostParameter;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.SneakyThrows;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "PostServlet", value = "/post")
public class PostServlet extends HttpServlet {
    private static final Service<Integer, Post> postService = new PostService();

    private transient User currentUser;
    private transient PrintWriter writer;
    private transient HttpSession session;

    @Override
    @SneakyThrows(IOException.class)
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        writer = resp.getWriter();
        String postId = req.getParameter(PostParameter.ID.get());
        if (postId == null) provideAllPosts();
        else providePostById(postId);
    }

    private void provideAllPosts() {
        List<Post> posts = postService.getAll();
        writer.println(PostMessage.PROVIDE_ALL_POSTS.get(posts));
    }

    private void providePostById(String postId) {
        Integer id = Integer.valueOf(postId);
        Post post = postService.getByKey(id);
        if (post == null) writer.println(PostMessage.POST_NOT_EXIST.get(id));
        else writer.println(PostMessage.PROVIDE_POST.get(post));
    }

    @Override
    @SneakyThrows(IOException.class)
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        writer = resp.getWriter();
        Post post = createPostFromRequestParameters(req);
        boolean successfullyPosted = postService.save(post);
        if (successfullyPosted) writer.println(PostMessage.NEW_POST_SUCCESS.get(post));
        else resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
    }

    private Post createPostFromRequestParameters(HttpServletRequest req) {
        session = req.getSession();
        User owner = (User) session.getAttribute(SessionAttribute.USER.get());
        Integer id = Integer.valueOf(req.getParameter(PostParameter.ID.get()));
        String text = req.getParameter(PostParameter.TEXT.get());
        List<Comment> comments = new ArrayList<>();
        List<String> likes = new ArrayList<>();
        return new Post(id, owner, text, comments, likes);
    }

    @Override
    @SneakyThrows(IOException.class)
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) {
        session = req.getSession();
        writer = resp.getWriter();
        currentUser = (User) session.getAttribute(SessionAttribute.USER.get());
        Integer postId = Integer.valueOf(req.getParameter(PostParameter.ID.get()));
        Post postToDelete = postService.getByKey(postId);
        delete(postId, postToDelete);
    }

    private void delete(Integer postId, Post postToDelete) {
        if (postToDelete != null) {
            if (isAdminOrOwner(postToDelete)) {
                if (postService.removeByKey(postId) != null) {
                    writer.println(PostMessage.DELETE_SUCCESS.get(postToDelete));
                } else writer.println(PostMessage.DELETE_DENY.get(postToDelete));
            } else writer.println(PostMessage.ACCESS_DENY.get());
        } else writer.println(PostMessage.POST_NOT_EXIST.get(postId));
    }

    private boolean isAdminOrOwner(Post post) {
        return currentUser.isAdmin() || post.getUser().equals(currentUser);
    }

    @Override
    @SneakyThrows(IOException.class)
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) {
        session = req.getSession();
        writer = resp.getWriter();
        currentUser = (User) session.getAttribute(SessionAttribute.USER.get());

        Integer postId = Integer.valueOf(req.getParameter(PostParameter.ID.get()));
        Post postToUpdate = postService.getByKey(postId);
        String textToUpdate = req.getParameter(PostParameter.TEXT.get());
        String like = req.getParameter(PostParameter.LIKE.get());

        if (postToUpdate == null) {
            writer.println(PostMessage.POST_NOT_EXIST.get(postId));
            return;
        }
        if (textToUpdate != null) update(postToUpdate, textToUpdate);
        if (like != null) like(postToUpdate);
    }

    private void update(Post postToUpdate, String textToUpdate) {
        if (isAdminOrOwner(postToUpdate)) {
            postToUpdate.setText(textToUpdate);
            writer.println(PostMessage.UPDATE_SUCCESS.get(postToUpdate));
        } else writer.println(PostMessage.ACCESS_DENY.get(postToUpdate));
    }

    private void like(Post postToUpdate) {
        String userNameWithSurname = currentUser.getNameWithSurname();
        List<String> likes = postToUpdate.getLikes();
        if (likes.remove(userNameWithSurname))
            writer.println(PostMessage.POST_REMOVE_LIKE.get(userNameWithSurname, postToUpdate));
        else {
            likes.add(userNameWithSurname);
            writer.println(PostMessage.POST_LIKE.get(userNameWithSurname, postToUpdate));
        }
    }
}