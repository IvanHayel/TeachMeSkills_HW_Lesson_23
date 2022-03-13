package by.teachmeskills.homework.storage;

import by.teachmeskills.homework.entity.Post;
import lombok.NonNull;
import lombok.Value;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Value
public class PostStorage implements Storable<Integer, Post> {
    List<Post> posts = new ArrayList<>();

    @Override
    public List<Post> getAll() {
        return new ArrayList<>(posts);
    }

    @Override
    public Post getByKey(@NonNull Integer id) {
        return posts.stream()
                .filter(post -> post.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public boolean save(@NonNull Post post) {
        return !posts.contains(post) && posts.add(post);
    }

    @Override
    public boolean contains(@NonNull Integer id) {
        return posts.stream()
                .mapToInt(Post::getId)
                .anyMatch(id::equals);
    }

    @Override
    public Post removeByKey(Integer id) {
        for (Post post : posts)
            if (post.getId().equals(id))
                return posts.remove(post) ? post : null;
        return null;
    }

    @Override
    public List<Post> removeAll() {
        List<Post> temp = new ArrayList<>(posts);
        return posts.removeAll(temp) ? temp : Collections.emptyList();
    }

    @Override
    public List<Post> removeAll(List<Post> entities) {
        posts.removeAll(entities);
        return entities;
    }
}