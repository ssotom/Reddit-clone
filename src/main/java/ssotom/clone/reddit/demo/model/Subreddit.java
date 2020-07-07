package ssotom.clone.reddit.demo.model;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import ssotom.clone.reddit.demo.dto.SubredditDto;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.time.Instant;


@Data
@Builder
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

    @OneToMany(fetch = FetchType.LAZY)
    private List<Post> posts;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    private Instant createdAt = Instant.now();

    public SubredditDto mapToDto() {
        return SubredditDto.builder()
                .id(id)
                .name(name)
                .description(description)
                .postCount(posts.size())
                .build();
    }

}
