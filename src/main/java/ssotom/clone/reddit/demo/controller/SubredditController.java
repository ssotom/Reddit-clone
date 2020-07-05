package ssotom.clone.reddit.demo.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ssotom.clone.reddit.demo.dto.SubredditDTO;
import ssotom.clone.reddit.demo.service.SubredditService;

import javax.validation.Valid;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/subreddit")
public class SubredditController {

    private final SubredditService subredditService;

    @GetMapping
    public List<SubredditDTO> getAllSubreddits() {
        return subredditService.getAll();
    }

    @GetMapping("/{id}")
    public SubredditDTO getSubreddit(@PathVariable Long id) {
        return subredditService.getByd(id);
    }

    @PostMapping
    public SubredditDTO create(@Valid @RequestBody SubredditDTO subredditDto) {
        return subredditService.save(subredditDto);
    }

}
