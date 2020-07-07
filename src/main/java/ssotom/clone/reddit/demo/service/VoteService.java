package ssotom.clone.reddit.demo.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ssotom.clone.reddit.demo.dto.request.VoteRequest;
import ssotom.clone.reddit.demo.exception.NotFoundException;
import ssotom.clone.reddit.demo.exception.SpringRedditException;
import ssotom.clone.reddit.demo.model.Post;
import ssotom.clone.reddit.demo.model.User;
import ssotom.clone.reddit.demo.model.Vote;
import ssotom.clone.reddit.demo.model.VoteType;
import ssotom.clone.reddit.demo.repository.PostRepository;
import ssotom.clone.reddit.demo.repository.VoteRepository;

import javax.transaction.Transactional;
import java.util.Optional;

@AllArgsConstructor
@Service
public class VoteService {

    private final PostRepository postRepository;
    private final VoteRepository voteRepository;
    private final AuthService authService;

    @Transactional
    public void vote(VoteRequest voteRequest) {
        Post post = postRepository.findById(voteRequest.getPostId())
                .orElseThrow(() -> new NotFoundException("Post not found with id: " + voteRequest.getPostId()));

        User user = authService.getCurrentUser();
        Optional<Vote> voteByPostAndUser = voteRepository
                .findTopByPostAndUserOrderByIdDesc(post, user);

        if (voteByPostAndUser.isPresent()
                && voteByPostAndUser.get().getVoteType().equals(voteRequest.getVoteType())) {
            throw new SpringRedditException("You have already " + voteRequest.getVoteType() + "'d for this post");
        }
        if (VoteType.UPVOTE.equals(voteRequest.getVoteType())) {
            post.setVoteCount(post.getVoteCount() + 1);
        } else {
            post.setVoteCount(post.getVoteCount() - 1);
        }
        voteRepository.save(voteRequest.mapToEntity(post, user));
        postRepository.save(post);
    }

}
