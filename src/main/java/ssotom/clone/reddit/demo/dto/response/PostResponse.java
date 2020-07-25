package ssotom.clone.reddit.demo.dto.response;


import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import ssotom.clone.reddit.demo.model.Vote;
import ssotom.clone.reddit.demo.model.VoteType;

import java.util.Optional;

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
    private boolean upVote;
    private boolean downVote;

    public void setUpVote(Vote vote) {
        this.upVote = checkVoteType(vote, VoteType.UPVOTE);
    }

    public void setDownVote(Vote vote) {
        this.downVote = checkVoteType(vote, VoteType.DOWNVOTE);
    }

    private boolean checkVoteType(Vote vote, VoteType voteType) {
        return vote.getVoteType().equals(voteType);
    }

}
