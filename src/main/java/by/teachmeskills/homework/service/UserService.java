package by.teachmeskills.homework.service;

import by.teachmeskills.homework.entity.User;
import by.teachmeskills.homework.role.Role;
import by.teachmeskills.homework.storage.EntityStorage;
import lombok.NonNull;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class UserService {
    private static final EntityStorage<User> userStorage = new EntityStorage<>();

    public static UserService getInstance() {
        if (instance == null) instance = new UserService();
        return instance;
    }

    private static UserService instance;

    private UserService() {
        rootRegistration();
    }

    private void rootRegistration() {
        User root = new User(-1, "root", "root3301", "Ivan", "Hayel");
        root.addRole(Role.ROOT);
        userStorage.save(root);
    }

    public List<User> getAll() {
        return userStorage.getAll();
    }

    public User getById(@NonNull Integer id) {
        return userStorage.getById(id);
    }

    public User getByLogin(@NonNull String login) {
        List<User> users = getAll();
        return users.stream()
                .filter(user -> user.getLogin().equals(login))
                .findFirst()
                .orElse(null);
    }

    public boolean save(@NonNull User user) {
        return userStorage.save(user);
    }

    public User removeById(@NonNull Integer id) {
        return userStorage.removeById(id);
    }

    public boolean removeAll() {
        return userStorage.removeAll();
    }

    public boolean removeAll(@NonNull List<User> entities) {
        return userStorage.removeAll(entities);
    }

    public boolean removeAllCommonUsers() {
        List<User> users = getAll();
        List<User> usersToDelete = users.stream()
                .filter(user -> !isAdmin(user))
                .collect(Collectors.toList());
        return removeAll(usersToDelete);
    }

    public boolean isUserExist(@NonNull User user) {
        return userStorage.isContains(user);
    }

    public boolean isLoginExist(@NonNull String login) {
        List<User> users = getAll();
        return users.stream()
                .map(User::getLogin)
                .anyMatch(login::equals);
    }

    public boolean isIdExist(@NonNull Integer id) {
        List<User> users = getAll();
        return users.stream()
                .map(User::getId)
                .anyMatch(id::equals);
    }

    public boolean isAdmin(@NonNull User user) {
        Set<Role> roles = user.getRoles();
        for (Role role : roles)
            if (role.getAccessLevel() > 0) return true;
        return false;
    }

    public User createUser(@NonNull Integer id, @NonNull String login, @NonNull String password,
                           @NonNull String name, @NonNull String surname) {
        User user = new User(id, login, password, name, surname);
        user.addRole(Role.COMMON_USER);
        return user;
    }

    public User updateUser(@NonNull User user, String login, String password,
                           String name, String surname) {
        if (login != null) user.setLogin(login);
        if (password != null) user.setPassword(password);
        if (name != null) user.setName(name);
        if (surname != null) user.setSurname(surname);
        return user;
    }

    public void grantAdministratorRights(@NonNull User user) {
        user.addRole(Role.ADMIN);
    }

    public void removeAdministratorRights(@NonNull User user) {
        user.removeRole(Role.ADMIN);
    }
}