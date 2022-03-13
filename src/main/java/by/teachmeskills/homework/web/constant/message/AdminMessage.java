package by.teachmeskills.homework.web.constant.message;

import by.teachmeskills.homework.entity.User;

import java.util.List;
import java.util.StringJoiner;

public enum AdminMessage {
    PROVIDE_ALL_USERS("All users:%n%s"),
    DELETE_ALL_USERS("All users deleted:%n%s"),
    PROVIDE_USER("Requested user: %s"),
    DELETE_USER_DENY("%s delete denied!"),
    CREATE_USER("%s created by %s"),
    UPDATE_USER("%s updated by %s"),
    DELETE_USER_SUCCESS("%s deleted by %s"),
    NOT_EXIST("%s not exist.");

    private final String format;

    AdminMessage(String format) {
        this.format = format;
    }

    public String get() {
        return format;
    }

    public String get(String value) {
         switch (this) {
            case NOT_EXIST:
                return String.format(format, value);
            default:
                return get();
        }
    }

    public String get(User user) {
        switch (this) {
            case PROVIDE_USER:
            case DELETE_USER_DENY:
                return String.format(format, user);
            default:
                return get();
        }
    }

    public String get(User user, User admin) {
        switch (this) {
            case CREATE_USER:
            case UPDATE_USER:
            case DELETE_USER_SUCCESS:
                return String.format(format, user, admin);
            default:
                return get();
        }
    }

    public String get(List<User> users) {
        switch (this) {
            case PROVIDE_ALL_USERS:
            case DELETE_ALL_USERS:
                StringJoiner joiner = new StringJoiner("\n");
                for (User user : users) joiner.add(user.toString());
                return String.format(format, joiner);
            default:
                return get();
        }
    }
}