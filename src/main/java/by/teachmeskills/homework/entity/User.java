package by.teachmeskills.homework.entity;

import by.teachmeskills.homework.role.Role;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class User extends Entity {
    private String login;
    private String password;
    private String name;
    private String surname;
    @EqualsAndHashCode.Exclude
    private final List<Role> roles = new ArrayList<>();

    public User(@NonNull Integer id, @NonNull String login, @NonNull String password,
                @NonNull String name, @NonNull String surname) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.name = name;
        this.surname = surname;
    }

    public String getFullName() {
        return String.format("%s %s", name, surname);
    }

    public boolean addRole(Role role) {
        return roles.add(role);
    }
}