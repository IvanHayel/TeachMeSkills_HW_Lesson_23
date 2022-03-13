package by.teachmeskills.homework.service;

import by.teachmeskills.homework.entity.User;
import by.teachmeskills.homework.storage.Storable;
import by.teachmeskills.homework.storage.UserStorage;
import lombok.NonNull;
import lombok.Value;

import java.util.List;

@Value
public class UserService implements Service<String, User> {
    private static final Storable<String, User> userStorage = new UserStorage();

    @Override
    public List<User> getAll() {
        return userStorage.getAll();
    }

    @Override
    public User getByKey(@NonNull String login) {
        return userStorage.getByKey(login);
    }

    @Override
    public boolean save(@NonNull User user) {
        return userStorage.save(user);
    }

    @Override
    public boolean contains(@NonNull String login) {
        return userStorage.contains(login);
    }

    @Override
    public User removeByKey(@NonNull String login) {
        return userStorage.removeByKey(login);
    }

    @Override
    public List<User> removeAll() {
        return userStorage.removeAll();
    }

    @Override
    public List<User> removeAll(List<User> entities) {
        return userStorage.removeAll(entities);
    }
}