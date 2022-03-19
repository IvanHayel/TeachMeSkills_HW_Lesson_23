package by.teachmeskills.homework.entity;

import by.teachmeskills.homework.role.Role;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
public class User extends Entity {
    private String login;
    @ToString.Exclude
    private String password;
    private String name;
    private String surname;
    @EqualsAndHashCode.Exclude
    private final Set<Role> roles = new HashSet<>();

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

    public boolean removeRole(Role role) {
        return roles.remove(role);
    }
}