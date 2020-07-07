package ssotom.clone.reddit.demo.dto.response;


import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostResponse {
    private Long id;
    private String subredditName;
    private String postName;
    private String url;
    private String description;
    private String username;
    private Integer voteCount;
    private Integer commentCount;
    private String duration;
}
