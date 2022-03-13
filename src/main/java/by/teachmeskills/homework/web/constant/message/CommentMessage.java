package by.teachmeskills.homework.web.constant.message;

import by.teachmeskills.homework.entity.Post;
import by.teachmeskills.homework.entity.User;

public enum CommentMessage {
    SUCCESS("%s leaved comment to %s."),
    DENIED("Comment to %s denied!"),
    DELETE_SUCCESS("%s delete comment to %s."),
    UPDATE_SUCCESS("%s update comment to %s."),
    DELETE_DENY("Delete comment to %s denied!"),
    UPDATE_DENY("Update comment to %s denied!"),
    ACCESS_DENY("Not enough rights.");

    private final String format;

    CommentMessage(String format) {
        this.format = format;
    }

    public String get() {
        return format;
    }

    public String get(Post post, User user) {
        switch (this) {
            case SUCCESS:
            case DELETE_SUCCESS:
            case UPDATE_SUCCESS:
                return String.format(format, user.getNameWithSurname(), post);
            default:
                return get();
        }
    }

    public String get(Post post) {
        switch (this) {
            case DENIED:
            case DELETE_DENY:
            case UPDATE_DENY:
                return String.format(format, post);
            default:
                return get();
        }
    }
}