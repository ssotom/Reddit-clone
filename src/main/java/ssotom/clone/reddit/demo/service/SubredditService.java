package ssotom.clone.reddit.demo.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ssotom.clone.reddit.demo.dto.SubredditDto;
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

    public SubredditDto getByd(Long id) {
        Subreddit subreddit = subredditRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Subreddit not found with id: " + id));
        return subreddit.mapToDto();
    }

    public List<SubredditDto> getAll() {
        return subredditRepository.findAll()
                .stream()
                .map(Subreddit::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public SubredditDto save(SubredditDto subredditDto) {
        Subreddit subreddit = subredditDto.mapToEntity(authService.getCurrentUser());
        subreddit.setName("/r/" + subreddit.getName());

       return subredditRepository.save(subreddit).mapToDto();
    }

}
