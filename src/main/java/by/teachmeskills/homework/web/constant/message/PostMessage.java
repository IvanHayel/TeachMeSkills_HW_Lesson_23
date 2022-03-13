package by.teachmeskills.homework.web.constant.message;

import by.teachmeskills.homework.entity.Post;
import lombok.NonNull;

import java.util.List;
import java.util.StringJoiner;

public enum PostMessage {
    POST_NOT_EXIST("Post with id = %s doesn't exist!"),
    PROVIDE_POST("Requested Post:%n%s"),
    NEW_POST_SUCCESS("%s successfully posted."),
    DELETE_SUCCESS("%s successfully deleted."),
    UPDATE_SUCCESS("%s successfully updated"),
    DELETE_DENY("%s delete denied."),
    ACCESS_DENY("Not enough rights."),
    PROVIDE_ALL_POSTS("All posts:%n%s"),
    POST_LIKE("%s like post %s"),
    POST_REMOVE_LIKE("%s don't like post %s anymore");

    private final String format;

    PostMessage(String format) {
        this.format = format;
    }

    public String get() {
        return format;
    }

    public String get(@NonNull Post post) {
        switch (this) {
            case PROVIDE_POST:
            case NEW_POST_SUCCESS:
            case DELETE_SUCCESS:
            case UPDATE_SUCCESS:
            case DELETE_DENY:
                return String.format(format, post);
            default:
                return get();
        }
    }

    public String get(@NonNull Integer id) {
        switch (this) {
            case POST_NOT_EXIST:
                return String.format(format, id);
            default:
                return get();
        }
    }

    public String get(@NonNull List<Post> posts) {
        switch (this) {
            case PROVIDE_ALL_POSTS:
                StringJoiner joiner = new StringJoiner("\n");
                for (Post post : posts) joiner.add(post.toString());
                return String.format(format, joiner);
            default:
                return get();
        }
    }

    public String get(@NonNull String userNameWithSurname, @NonNull Post post) {
        switch (this) {
            case POST_LIKE:
            case POST_REMOVE_LIKE:
                return String.format(format, userNameWithSurname, post);
            default:
                return get();
        }
    }
}