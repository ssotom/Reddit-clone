package ssotom.clone.reddit.demo.controller;

import lombok.AllArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import ssotom.clone.reddit.demo.exception.SpringRedditException;
import ssotom.clone.reddit.demo.request.SingUpRequest;
import ssotom.clone.reddit.demo.response.ErrorResponse;
import ssotom.clone.reddit.demo.response.MessageResponse;
import ssotom.clone.reddit.demo.service.AuthService;

import javax.validation.Valid;

@AllArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody SingUpRequest singUpRequest, BindingResult result) {
        validateRegisterRequest(singUpRequest, result);
        if(result.hasErrors()) {
            return ErrorResponse.returnError(result);
        }
        try {
            authService.signup(singUpRequest);
            return new ResponseEntity<>(new MessageResponse("Account created successfully"), HttpStatus.CREATED);
        } catch (DataAccessException e) {
            return ErrorResponse.returnInternalServerError(e.getMostSpecificCause().getLocalizedMessage());
        }
    }

    @GetMapping("account_verification/{token}")
    public ResponseEntity<?> verifyAccount(@PathVariable String token) {
        try {
            authService.verifyAccount(token);
            return new ResponseEntity<>(new MessageResponse("Account activated successfully"), HttpStatus.OK);
        } catch (SpringRedditException e) {
            return ErrorResponse.returnError(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    private void validateRegisterRequest(SingUpRequest singUpRequest, BindingResult result) {
        if(authService.existsByEmail(singUpRequest.getEmail())) {
            FieldError error = new FieldError("user", "email", singUpRequest.getEmail() + " in use");
            result.addError(error);
        }
        if(authService.existsByUsername(singUpRequest.getUsername())) {
            FieldError error = new FieldError("user", "username", singUpRequest.getUsername() + " in use");
            result.addError(error);
        }
    }

}
