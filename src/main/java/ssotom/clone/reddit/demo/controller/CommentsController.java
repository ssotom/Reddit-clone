package ssotom.clone.reddit.demo.controller;

import lombok.AllArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ssotom.clone.reddit.demo.dto.CommentDto;
import ssotom.clone.reddit.demo.dto.response.ErrorResponse;
import ssotom.clone.reddit.demo.exception.NotFoundException;
import ssotom.clone.reddit.demo.service.CommentService;

import javax.validation.Valid;

@AllArgsConstructor
@RestController
@RequestMapping("/api/comment")
public class CommentsController {

    private final CommentService commentService;

    @GetMapping("/by-post/{id}")
    public ResponseEntity<?> getAllByPost(@PathVariable Long id) {
        try {
            return new ResponseEntity<>(commentService.getAllByPost(id), HttpStatus.OK);
        } catch (NotFoundException e) {
            return ErrorResponse.returnError(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/by-user/{username}")
    public ResponseEntity<?> getAllByUser(@PathVariable String username) {
        try {
            return new ResponseEntity<>(commentService.getAllByUser(username), HttpStatus.OK);
        } catch (NotFoundException e) {
            return ErrorResponse.returnError(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody CommentDto commentDto, BindingResult result) {
        if(result.hasErrors()) {
            return ErrorResponse.returnError(result);
        }
        try {
            CommentDto comment = commentService.save(commentDto);
            return new ResponseEntity<>(comment, HttpStatus.CREATED);
        } catch (NotFoundException e) {
            return ErrorResponse.returnError(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (DataAccessException e) {
            return ErrorResponse.returnError(e.getMostSpecificCause().getLocalizedMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

}
