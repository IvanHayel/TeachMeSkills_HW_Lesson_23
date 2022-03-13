package by.teachmeskills.homework.web.constant.attribute;

public enum SessionAttribute {
    USER("user");
    private final String attribute;

    SessionAttribute(String attribute) {
        this.attribute = attribute;
    }

    public String get() {
        return attribute;
    }
}