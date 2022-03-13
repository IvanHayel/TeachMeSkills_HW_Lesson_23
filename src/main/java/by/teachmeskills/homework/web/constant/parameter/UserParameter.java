package by.teachmeskills.homework.web.constant.parameter;

public enum UserParameter {
    ROLE("role"),
    LOGIN("login"),
    NAME("name"),
    SURNAME("surname"),
    PASSWORD("password");

    private final String value;

    UserParameter(String value) {
        this.value = value;
    }

    public String get() {
        return value;
    }
}