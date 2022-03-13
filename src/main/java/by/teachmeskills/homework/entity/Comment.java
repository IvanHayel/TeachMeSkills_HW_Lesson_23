package by.teachmeskills.homework.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

@Data
@EqualsAndHashCode(callSuper = false)
public class Comment extends Entity {
    @NonNull private final Integer id;
    @NonNull private final User user;
    @NonNull private String text;
}