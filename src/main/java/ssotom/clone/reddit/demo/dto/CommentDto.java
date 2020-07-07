package ssotom.clone.reddit.demo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import ssotom.clone.reddit.demo.model.Comment;
import ssotom.clone.reddit.demo.model.Post;
import ssotom.clone.reddit.demo.model.User;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @NotNull
    private Long postId;

    @NotNull
    private String text;

    @NotBlank
    private String username;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Instant createdAt;

    public Comment mapToEntity(Post post, User user) {
        Comment comment = new Comment();
        comment.setId(id);
        comment.setText(text);
        comment.setPost(post);
        comment.setUser(user);
        return comment;
    }

}
