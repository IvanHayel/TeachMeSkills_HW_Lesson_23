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
    // TODO: It's OK to see the whole info about User in toString() method. because it's a inner entity.
    //  You can create ReadOnlyUserEntity with not private personal info and use it everywhere. In comments in post e.t.c
    @NonNull private final Integer id = ++nextId;
    @ToString.Exclude
    @NonNull private final String login;

    @EqualsAndHashCode.Exclude @ToString.Exclude
    // TODO: There can be more than one role for user
    @NonNull private Role role;
    @NonNull private String name;
    @NonNull private String surname;
    @EqualsAndHashCode.Exclude @ToString.Exclude

    // TODO: so what you are planing to do after deserialization?
    @NonNull private transient String password;

    // TODO: It's shouldn't be a User method.
    public boolean isAdmin() {
        return role.getAccessLevel() > 0;
    }

    // TODO: getFullName
    public String getNameWithSurname() {
        return String.format("%s %s", name, surname);
    }
}