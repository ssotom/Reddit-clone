package ssotom.clone.reddit.demo.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import ssotom.clone.reddit.demo.dto.SubredditDto;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Subreddit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String name;

    @NotNull
    private String description;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "subreddit")
    private List<Post> posts;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User user;

    private Instant createdAt = Instant.now();

    public SubredditDto mapToDto() {
        SubredditDto subredditDto = new SubredditDto();
        subredditDto.setId(id);
        subredditDto.setName(name);
        subredditDto.setDescription(description);
        subredditDto.setPostCount(posts.size());
        return subredditDto;
    }

}
