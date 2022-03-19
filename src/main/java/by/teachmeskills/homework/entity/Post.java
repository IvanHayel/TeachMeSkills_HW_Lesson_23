package by.teachmeskills.homework.entity;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
public class Post extends Entity {
    private User owner;
    private String content;
    private List<Comment> comments = new ArrayList<>();
    private List<String> likes = new ArrayList<>();

    public Post(@NonNull Integer id, @NonNull User owner, @NonNull String content) {
        this.id = id;
        this.owner = owner;
        this.content = content;
    }

    public Integer getCommentId() {
        return comments.size() + 1;
    }
}