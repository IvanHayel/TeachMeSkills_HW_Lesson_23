package by.teachmeskills.homework.web.constant.message;

import by.teachmeskills.homework.entity.Post;

import java.util.List;
import java.util.StringJoiner;

public enum PostHistoryMessage {
    PROVIDE_USER_POSTS("My posts:%n%s"),
    DELETE_USER_POSTS("All posts have been deleted:%n%s");

    private final String format;

    PostHistoryMessage(String format) {
        this.format = format;
    }

    public String get() {
        return format;
    }

    public String get(List<Post> posts) {
        switch (this) {
            case PROVIDE_USER_POSTS:
            case DELETE_USER_POSTS:
                StringJoiner joiner = new StringJoiner("\n");
                for (Post post : posts) joiner.add(post.toString());
                return String.format(format, joiner);
            default:
                return get();
        }
    }
}