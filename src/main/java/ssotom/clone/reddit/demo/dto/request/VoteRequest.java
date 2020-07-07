package ssotom.clone.reddit.demo.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import ssotom.clone.reddit.demo.model.Post;
import ssotom.clone.reddit.demo.model.User;
import ssotom.clone.reddit.demo.model.Vote;
import ssotom.clone.reddit.demo.model.VoteType;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class VoteRequest {

    @NotNull
    private VoteType voteType;

    @NotNull
    private Long postId;

    public Vote mapToEntity(Post post, User user) {
        Vote vote = new Vote();
        vote.setVoteType(voteType);
        vote.setPost(post);
        vote.setUser(user);
        return vote;
    }

}
