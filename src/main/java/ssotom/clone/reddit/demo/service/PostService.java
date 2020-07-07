package ssotom.clone.reddit.demo.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ssotom.clone.reddit.demo.exception.NotFoundException;
import ssotom.clone.reddit.demo.model.Post;
import ssotom.clone.reddit.demo.model.Subreddit;
import ssotom.clone.reddit.demo.model.User;
import ssotom.clone.reddit.demo.repository.PostRepository;
import ssotom.clone.reddit.demo.repository.SubredditRepository;
import ssotom.clone.reddit.demo.repository.UserRepository;
import ssotom.clone.reddit.demo.dto.request.PostRequest;
import ssotom.clone.reddit.demo.dto.response.PostResponse;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class PostService {

    private final PostRepository postRepository;
    private final SubredditRepository subredditRepository;
    private final UserRepository userRepository;
    private final AuthService authService;


    public List<PostResponse> getAll() {
        return postRepository.findAll()
                .stream()
                .map(Post::mapToDto)
                .collect(Collectors.toList());
    }

    public PostResponse getById(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Post not found with id: " + id));
        return post.mapToDto();
    }

    @Transactional
    public PostResponse save(PostRequest postRequest) {
        Subreddit subreddit = subredditRepository.findByName(postRequest.getSubredditName())
                .orElseThrow(() -> new NotFoundException("Subreddit not found with name: " + postRequest.getSubredditName()));
        Post post = postRequest.mapToEntity(subreddit, authService.getCurrentUser());

        return postRepository.save(post).mapToDto();
    }

    public List<PostResponse> getBySubreddit(Long id) {
        Subreddit subreddit = subredditRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Subreddit not found with id: " + id));

        return postRepository.findAllBySubreddit(subreddit)
                .stream()
                .map(Post::mapToDto)
                .collect(Collectors.toList());
    }

    public List<PostResponse> getByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("User no found with username: " + username));

        return postRepository.findByUser(user)
                .stream()
                .map(Post::mapToDto)
                .collect(Collectors.toList());
    }

}
