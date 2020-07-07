package ssotom.clone.reddit.demo.controller;

import lombok.AllArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ssotom.clone.reddit.demo.dto.request.VoteRequest;
import ssotom.clone.reddit.demo.dto.response.ErrorResponse;
import ssotom.clone.reddit.demo.exception.NotFoundException;
import ssotom.clone.reddit.demo.exception.SpringRedditException;
import ssotom.clone.reddit.demo.service.VoteService;

import javax.validation.Valid;

@AllArgsConstructor
@RestController
@RequestMapping("/api/vote")
public class VoteController {

    private final VoteService voteService;

    @PostMapping
    public ResponseEntity<?> vote(@Valid @RequestBody VoteRequest voteRequest, BindingResult result) {
        if(result.hasErrors()) {
            return ErrorResponse.returnError(result);
        }
        try {
            voteService.vote(voteRequest);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (NotFoundException e) {
            return ErrorResponse.returnError(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (SpringRedditException e) {
            return ErrorResponse.returnError(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (DataAccessException e) {
            return ErrorResponse.returnError(e.getMostSpecificCause().getLocalizedMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
