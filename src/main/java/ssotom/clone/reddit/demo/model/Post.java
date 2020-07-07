package ssotom.clone.reddit.demo.model;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import ssotom.clone.reddit.demo.dto.response.PostResponse;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.Instant;

@Data
@Builder
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

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    private Subreddit subreddit;

    private Instant createdAt = Instant.now();

    public PostResponse mapToDto() {
        return PostResponse.builder()
                .id(id)
                .postName(name)
                .url(url)
                .description(description)
                .username(user.getUsername())
                .subredditName(subreddit.getName())
                .voteCount(voteCount)
                .build();
    }
    
}
