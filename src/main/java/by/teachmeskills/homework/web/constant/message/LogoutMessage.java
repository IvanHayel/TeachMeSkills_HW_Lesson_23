package by.teachmeskills.homework.web.constant.message;

import by.teachmeskills.homework.entity.User;

public enum LogoutMessage {
    LOGOUT("%s logout.");

    private final String format;

    LogoutMessage(String format) {
        this.format = format;
    }

    public String get() {
        return format;
    }

    public String get(User user) {
        switch (this) {
            case LOGOUT:
                return String.format(format, user);
            default:
                return get();
        }
    }
}