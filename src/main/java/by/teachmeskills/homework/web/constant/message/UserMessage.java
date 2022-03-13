package by.teachmeskills.homework.web.constant.message;

import by.teachmeskills.homework.entity.User;

public enum UserMessage {
    UPDATE_SUCCESS("%s successfully updated."),
    DELETE_SUCCESS("%s successfully deleted."),
    DELETE_DENY("%s delete denied.");

    private final String format;

    UserMessage(String format) {
        this.format = format;
    }

    public String get() {
        return format;
    }

    public String get(User user) {
        switch (this) {
            case UPDATE_SUCCESS:
            case DELETE_SUCCESS:
            case DELETE_DENY:
                return String.format(format, user);
            default:
                return get();
        }
    }
}