package by.teachmeskills.homework.service;

import by.teachmeskills.homework.entity.Post;
import by.teachmeskills.homework.storage.PostStorage;
import by.teachmeskills.homework.storage.Storable;
import lombok.NonNull;
import lombok.Value;

import java.util.List;

@Value
public class PostService implements Service<Integer, Post> {
    private static final Storable<Integer, Post> postStorage = new PostStorage();

    @Override
    public List<Post> getAll() {
        return postStorage.getAll();
    }

    @Override
    public Post getByKey(@NonNull Integer id) {
        return postStorage.getByKey(id);
    }

    @Override
    public boolean save(@NonNull Post post) {
        return postStorage.save(post);
    }

    @Override
    public boolean contains(@NonNull Integer id) {
        return postStorage.contains(id);
    }

    @Override
    public Post removeByKey(@NonNull Integer id) {
        return postStorage.removeByKey(id);
    }

    @Override
    public List<Post> removeAll() {
        return postStorage.removeAll();
    }

    @Override
    public List<Post> removeAll(List<Post> entities) {
        return postStorage.removeAll(entities);
    }
}