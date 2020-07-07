package ssotom.clone.reddit.demo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import ssotom.clone.reddit.demo.model.Comment;
import ssotom.clone.reddit.demo.model.Post;
import ssotom.clone.reddit.demo.model.User;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.Instant;

@Data
@Builder
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
        return Comment.builder()
                .id(id)
                .text(text)
                .post(post)
                .user(user)
                .build();
    }

}
