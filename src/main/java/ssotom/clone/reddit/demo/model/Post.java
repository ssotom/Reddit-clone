package ssotom.clone.reddit.demo.model;

import com.github.marlonlom.utilities.timeago.TimeAgo;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import ssotom.clone.reddit.demo.dto.response.PostResponse;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.Instant;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String name;

    @NotBlank
    private String url;

    @Lob
    private String description;

    private Integer voteCount;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "post")
    private List<Comment> comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="subreddit_id")
    private Subreddit subreddit;

    private Instant createdAt = Instant.now();

    public PostResponse mapToDto() {
        PostResponse postResponse = new PostResponse();
        postResponse.setId(id);
        postResponse.setPostName(name);
        postResponse.setUrl(url);
        postResponse.setDescription(description);
        postResponse.setUsername(user.getUsername());
        postResponse.setSubredditName(subreddit.getName());
        postResponse.setVoteCount(voteCount);
        postResponse.setCommentCount(comment.size());
        postResponse.setDuration(TimeAgo.using(createdAt.toEpochMilli()));
        return postResponse;
    }
    
}
