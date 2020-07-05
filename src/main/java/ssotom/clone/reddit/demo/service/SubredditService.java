package ssotom.clone.reddit.demo.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ssotom.clone.reddit.demo.dto.SubredditDTO;
import ssotom.clone.reddit.demo.exception.NotFoundException;
import ssotom.clone.reddit.demo.model.Subreddit;
import ssotom.clone.reddit.demo.repository.SubredditRepository;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class SubredditService {

    private final SubredditRepository subredditRepository;
    private final AuthService authService;

    public SubredditDTO getByd(Long id) {
        Subreddit subreddit = subredditRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Subreddit not found with id -" + id));
        return mapToDto(subreddit);
    }

    public List<SubredditDTO> getAll() {
        return subredditRepository.findAll()
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public SubredditDTO save(SubredditDTO subredditDto) {
        Subreddit subreddit = subredditRepository.save(mapToSubreddit(subredditDto));
        subredditDto.setId(subreddit.getId());
        return subredditDto;
    }

    private SubredditDTO mapToDto(Subreddit subreddit) {
        return SubredditDTO.builder()
                .id(subreddit.getId())
                .name(subreddit.getName())
                .postCount(subreddit.getPosts().size())
                .build();
    }

    private Subreddit mapToSubreddit(SubredditDTO subredditDTO) {
        return Subreddit.builder()
                .name("/r/" + subredditDTO.getName())
                .description(subredditDTO.getDescription())
                .user(authService.getCurrentUser())
                .createdAt(Instant.now())
                .build();
    }

}
