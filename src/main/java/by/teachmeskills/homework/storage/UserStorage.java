package by.teachmeskills.homework.storage;

import by.teachmeskills.homework.entity.User;
import lombok.NonNull;
import lombok.Value;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Value
public class UserStorage implements Storable<String, User> {
    // TODO: private
    List<User> users = new ArrayList<>();

    @Override
    public List<User> getAll() {
        return new ArrayList<>(users);
    }

    @Override
    public User getByKey(@NonNull String login) {
        return users.stream()
                .filter(user -> user.getLogin().equals(login))
                .findFirst()
                .orElse(null);
    }

    @Override
    public boolean save(@NonNull User user) {
        if (users.contains(user)) return false;
        return users.add(user);
    }

    @Override
    public boolean contains(@NonNull String login) {
        return users.stream()
                .map(User::getLogin)
                .anyMatch(login::equals);
    }

    @Override
    public User removeByKey(String login) {
        for (User user : users)
            if (user.getLogin().equals(login))
                return users.remove(user) ? user : null;
        return null;
    }

    @Override
    public List<User> removeAll() {
        List<User> temp = new ArrayList<>(users);
        return users.removeAll(temp) ? temp : Collections.emptyList();
    }

    @Override
    public List<User> removeAll(List<User> entities) {
        users.removeAll(entities);
        return entities;
    }
}