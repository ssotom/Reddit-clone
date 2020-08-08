package ssotom.clone.reddit.demo.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ssotom.clone.reddit.demo.dto.CommentDto;
import ssotom.clone.reddit.demo.exception.NotFoundException;
import ssotom.clone.reddit.demo.model.Comment;
import ssotom.clone.reddit.demo.model.Post;
import ssotom.clone.reddit.demo.model.User;
import ssotom.clone.reddit.demo.repository.CommentRepository;
import ssotom.clone.reddit.demo.repository.PostRepository;
import ssotom.clone.reddit.demo.repository.UserRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;


@AllArgsConstructor
@Transactional
@Service
public class CommentService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final AuthService authService;

    public List<CommentDto> getAllByPost(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Post not found with id: " + id));
        return commentRepository.findByPost(post)
                .stream()
                .map(Comment::mapToDto)
                .collect(Collectors.toList());
    }

    public List<CommentDto> getAllByUser(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("Post not found with username: " + username));
        return commentRepository.findAllByUser(user)
                .stream()
                .map(Comment::mapToDto)
                .collect(Collectors.toList());
    }

    public CommentDto save(CommentDto commentDto) {
        Post post = postRepository.findById(commentDto.getPostId())
                .orElseThrow(() -> new NotFoundException("Post not found with id: " + commentDto.getPostId()));
        Comment comment = commentDto.mapToEntity(post, authService.getCurrentUser());

        return commentRepository.save(comment).mapToDto();
    }

}
