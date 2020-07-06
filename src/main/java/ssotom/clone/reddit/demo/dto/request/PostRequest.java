package ssotom.clone.reddit.demo.dto.request;

import lombok.Data;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import ssotom.clone.reddit.demo.model.Post;
import ssotom.clone.reddit.demo.model.Subreddit;
import ssotom.clone.reddit.demo.model.User;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostRequest {

    @NotBlank
    private String subredditName;

    @NotBlank
    private String postName;

    @NotNull
    private String url;

    @NotNull
    private String description;

    public Post mapToEntity(Subreddit subreddit, User user) {
        return Post.builder()
                .name(postName)
                .description(description)
                .url(url)
                .subreddit(subreddit)
                .user(user)
                .build();
    }

}
