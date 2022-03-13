package by.teachmeskills.homework.web.constant.message;

import by.teachmeskills.homework.entity.User;
import lombok.NonNull;

public enum RegistrationMessage {
    SUCCESS("%s successfully registered!");

    private final String format;

    RegistrationMessage(@NonNull String format) {
        this.format = format;
    }

    public String get() {
        return format;
    }

    public String get(@NonNull User user) {
        switch (this) {
            case SUCCESS:
                return String.format(format, user);
            default:
                return get();
        }
    }
}