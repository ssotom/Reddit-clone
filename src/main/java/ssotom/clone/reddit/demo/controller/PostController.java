package ssotom.clone.reddit.demo.controller;

import lombok.AllArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ssotom.clone.reddit.demo.exception.NotFoundException;
import ssotom.clone.reddit.demo.dto.request.PostRequest;
import ssotom.clone.reddit.demo.dto.response.ErrorResponse;
import ssotom.clone.reddit.demo.dto.response.PostResponse;
import ssotom.clone.reddit.demo.service.PostService;

import javax.validation.Valid;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/post")
public class PostController {

    private final PostService postService;

    @GetMapping
    public ResponseEntity<List<PostResponse>> getAll() {
        return ResponseEntity.status(HttpStatus.OK).body(postService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        try {
            return new ResponseEntity<>(postService.getById(id), HttpStatus.OK);
        } catch (NotFoundException e) {
            return ErrorResponse.returnError(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody PostRequest postRequest, BindingResult result) {
        if(result.hasErrors()) {
            return ErrorResponse.returnError(result);
        }
        try {
            PostResponse post = postService.save(postRequest);
            return new ResponseEntity<>(post, HttpStatus.CREATED);
        } catch (NotFoundException e) {
            return ErrorResponse.returnError(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (DataAccessException e) {
            return ErrorResponse.returnError(e.getMostSpecificCause().getLocalizedMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/by-subreddit/{id}")
    public ResponseEntity<?> getBySubreddit(Long id) {
        try {
            return new ResponseEntity<>(postService.getBySubreddit(id), HttpStatus.OK);
        } catch (NotFoundException e) {
            return ErrorResponse.returnError(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/by-user/{username}")
    public ResponseEntity<?> getPostsByUsername(String username) {
        try {
            return new ResponseEntity<>(postService.getByUsername(username), HttpStatus.OK);
        } catch (NotFoundException e) {
            return ErrorResponse.returnError(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

}
