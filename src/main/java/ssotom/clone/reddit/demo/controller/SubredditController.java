package ssotom.clone.reddit.demo.controller;

import lombok.AllArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ssotom.clone.reddit.demo.dto.SubredditDTO;
import ssotom.clone.reddit.demo.exception.NotFoundException;
import ssotom.clone.reddit.demo.response.ErrorResponse;
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
    public ResponseEntity<?>  getSubreddit(@PathVariable Long id) {
        try {
            SubredditDTO subreddit = subredditService.getByd(id);
            return new ResponseEntity<>(subreddit, HttpStatus.OK);
        } catch (NotFoundException e) {
            return ErrorResponse.returnError(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody SubredditDTO subredditDTO, BindingResult result) {
        if(result.hasErrors()) {
            return ErrorResponse.returnError(result);
        }
        try {
            SubredditDTO subreddit = subredditService.save(subredditDTO);
            return new ResponseEntity<>(subreddit, HttpStatus.CREATED);
        } catch (DataAccessException e) {
            return ErrorResponse.returnError(e.getMostSpecificCause().getLocalizedMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
