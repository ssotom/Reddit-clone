package ssotom.clone.reddit.demo.model;

import com.github.marlonlom.utilities.timeago.TimeAgo;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import ssotom.clone.reddit.demo.dto.CommentDto;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String text;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="post_id")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User user;

    private Instant createdAt = Instant.now();

    public CommentDto mapToDto() {
        CommentDto commentDto = new CommentDto();
        commentDto.setId(id);
        commentDto.setPostId(post.getId());
        commentDto.setText(text);
        commentDto.setUsername(user.getUsername());
        commentDto.setDuration(TimeAgo.using(createdAt.toEpochMilli()));
        return commentDto;
    }

}
