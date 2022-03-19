package by.teachmeskills.homework.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Comment extends Entity {
    private User owner;
    private String text;

    public Comment(@NonNull Integer id, @NonNull User owner, @NonNull String text) {
        this.id = id;
        this.owner = owner;
        this.text = text;
    }
}