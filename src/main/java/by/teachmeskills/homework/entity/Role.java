package by.teachmeskills.homework.entity;

import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = false)
// TODO: whole this class just a mass. Rewrite it
public class Role extends Entity {
    public static final String USER_ROLE = "user";
    public static final int COMMON_USER_ROLE_ID = 1;
    public static final int COMMON_USER_ACCESS_LEVEL = 0;
    public static final String ADMIN_ROLE = "admin";
    public static final int ADMIN_ROLE_ID = 0;
    public static final int ADMIN_ACCESS_LEVEL = 1;

    @NonNull Integer id;
    @NonNull Integer accessLevel;
}