package by.teachmeskills.homework.role;

import lombok.Getter;

public enum Role {
    ADMIN(1, "admin", 1),
    COMMON_USER(0, "common-user", 0);

    @Getter private final Integer id;
    @Getter private final String name;
    @Getter private final Integer accessLevel;

    Role(Integer id, String name, Integer accessLevel) {
        this.id = id;
        this.name = name;
        this.accessLevel = accessLevel;
    }
}