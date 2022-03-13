package by.teachmeskills.homework.web.constant.message;

import by.teachmeskills.homework.entity.User;
import lombok.NonNull;

public enum AuthorizationMessage {
    SUCCESS("Welcome, %s %s!"),
    PASSWORD_MISMATCH("Wrong password for %s."),
    USER_NOT_EXIST("User with login '%s' doesn't exist!"),
    ALREADY_AUTHENTICATED("Already authenticated!");

    private final String format;

    AuthorizationMessage(String format) {
        this.format = format;
    }

    public String get() {
        return format;
    }

    public String get(@NonNull User user) {
        switch (this) {
            case SUCCESS:
                return String.format(format, user.getName(), user.getSurname());
            case PASSWORD_MISMATCH:
                return String.format(format, user.getLogin());
            default:
                return get();
        }
    }

    public String get(@NonNull String login) {
        switch (this) {
            case USER_NOT_EXIST:
                return String.format(format, login);
            default:
                return get();
        }
    }
}