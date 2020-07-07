package ssotom.clone.reddit.demo.model;


import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import ssotom.clone.reddit.demo.dto.CommentDto;
import ssotom.clone.reddit.demo.dto.SubredditDto;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String text;

    @ManyToOne(fetch = FetchType.LAZY)
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    private Instant createdAt = Instant.now();

    public CommentDto mapToDto() {
        return CommentDto.builder()
                .id(id)
                .postId(post.getId())
                .text(text)
                .username(user.getUsername())
                .createdAt(createdAt)
                .build();
    }

}
