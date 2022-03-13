package by.teachmeskills.homework.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = false)
public class User extends Entity {
    private static Integer nextId = 0;

    @EqualsAndHashCode.Exclude
    @NonNull private final Integer id = ++nextId;
    @ToString.Exclude
    @NonNull private final String login;

    @EqualsAndHashCode.Exclude @ToString.Exclude
    @NonNull private Role role;
    @NonNull private String name;
    @NonNull private String surname;
    @EqualsAndHashCode.Exclude @ToString.Exclude
    @NonNull private transient String password;

    public boolean isAdmin() {
        return role.getAccessLevel() > 0;
    }

    public String getNameWithSurname() {
        return String.format("%s %s", name, surname);
    }
}