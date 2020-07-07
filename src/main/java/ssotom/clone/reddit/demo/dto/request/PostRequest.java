package ssotom.clone.reddit.demo.dto.request;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import ssotom.clone.reddit.demo.model.Post;
import ssotom.clone.reddit.demo.model.Subreddit;
import ssotom.clone.reddit.demo.model.User;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Collections;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostRequest {

    @NotBlank
    private String subredditName;

    @NotBlank
    private String postName;

    @NotBlank
    private String url;

    @NotNull
    private String description;

    public Post mapToEntity(Subreddit subreddit, User user) {
        Post post = new Post();
        post.setName(postName);
        post.setDescription(description);
        post.setUrl(url);
        post.setVoteCount(0);
        post.setComment(Collections.emptyList());
        post.setSubreddit(subreddit);
        post.setUser(user);
        return post;
    }

}
