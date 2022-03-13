package by.teachmeskills.homework.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
public class Post extends Entity {
    @NonNull private final Integer id;
    @NonNull private final User user;
    @NonNull private String text;
    @NonNull private List<Comment> comments;
    @NonNull private List<String> likes;
}