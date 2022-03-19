package by.teachmeskills.homework.service;

import by.teachmeskills.homework.entity.Comment;
import by.teachmeskills.homework.entity.Post;
import by.teachmeskills.homework.entity.User;
import by.teachmeskills.homework.storage.EntityStorage;
import lombok.NonNull;

import java.util.List;
import java.util.stream.Collectors;

public class PostService {
    private static final EntityStorage<Post> postStorage = new EntityStorage<>();

    private static PostService instance;

    private PostService() {
    }

    public static PostService getInstance() {
        if (instance == null) instance = new PostService();
        return instance;
    }

    public List<Post> getAll() {
        return postStorage.getAll();
    }

    public Post getById(@NonNull Integer id) {
        return postStorage.getById(id);
    }

    public List<Post> getUserPosts(@NonNull User user) {
        List<Post> posts = getAll();
        return posts.stream()
                .filter(post -> post.getOwner().equals(user))
                .collect(Collectors.toList());
    }

    public Comment getCommentById(@NonNull Post post, @NonNull Integer commentId) {
        return post.getComments().stream()
                .filter(item -> item.getId().equals(commentId))
                .findFirst()
                .orElse(null);
    }

    public boolean save(@NonNull Post post) {
        return postStorage.save(post);
    }

    public boolean isContains(@NonNull Integer id) {
        return postStorage.isContains(id);
    }

    public Post removeById(@NonNull Integer id) {
        return postStorage.removeById(id);
    }

    public boolean removeAll() {
        return postStorage.removeAll();
    }

    public boolean removeAll(@NonNull List<Post> posts) {
        return postStorage.removeAll(posts);
    }

    public boolean isOwner(@NonNull Post post, @NonNull User user) {
        return post.getOwner().equals(user);
    }

    public boolean isOwner(@NonNull Comment comment, @NonNull User user) {
        return comment.getOwner().equals(user);
    }
}