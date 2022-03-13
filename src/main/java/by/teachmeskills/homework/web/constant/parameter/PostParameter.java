package by.teachmeskills.homework.web.constant.parameter;

public enum PostParameter {
    ID("id"),
    OWNER("owner"),
    TEXT("text"),
    COMMENT("comment"),
    LIKE("like");

    private final String value;

    PostParameter(String value) {
        this.value = value;
    }

    public String get(){
        return value;
    }
}