package by.teachmeskills.homework.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Post extends Entity {
    private User owner;
    private String text;
    private List<Comment> comments = new ArrayList<>();
    private List<String> likes = new ArrayList<>();

    public Post(@NonNull Integer id, @NonNull User owner, @NonNull String text) {
        this.id = id;
        this.owner = owner;
        this.text = text;
    }

    public Integer getCommentId() {
        return comments.size() + 1;
    }
}