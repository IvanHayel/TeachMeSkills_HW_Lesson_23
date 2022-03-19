package by.teachmeskills.homework.entity;

import lombok.*;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
public class Comment extends Entity {
    private User owner;
    private String content;

    public Comment(@NonNull Integer id, @NonNull User owner, @NonNull String content) {
        this.id = id;
        this.owner = owner;
        this.content = content;
    }
}