package by.teachmeskills.homework.web.constant.parameter;

public enum CommentParameter {
    ID("id"),
    COMMENT("comment"),
    POST_ID("post-id");

    private String value;

    CommentParameter(String value) {
        this.value = value;
    }

    public String get() {
        return value;
    }
}