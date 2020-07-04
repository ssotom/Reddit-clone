package ssotom.clone.reddit.demo.response;

import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@Data
public class ErrorResponse {
    private HttpStatus status;
    private String message;
    private List<String> errors;

    public static ResponseEntity<?> returnError(BindingResult result) {
        List<String> errors = new LinkedList<>();
        for (FieldError error : result.getFieldErrors()) {
            errors.add(error.getField() + ": " + error.getDefaultMessage());
        }
        return new ResponseEntity<>(new ErrorResponse(HttpStatus.BAD_REQUEST, "The request has some errors",
                errors), HttpStatus.BAD_REQUEST);
    }

    public static ResponseEntity<?> returnError(HttpStatus httpStatus, String message) {
        return new ResponseEntity<>(new ErrorResponse(httpStatus, "Fatal error", message), httpStatus);
    }

    public static ResponseEntity<?> returnInternalServerError(String message) {
        return new ResponseEntity<>(new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Fatal error", message), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public ErrorResponse(HttpStatus status, String message, List<String> errors) {
        this.status = status;
        this.message = message;
        this.errors = errors;
    }

    public ErrorResponse(HttpStatus status, String message, String error) {
        this.status = status;
        this.message = message;
        errors = Arrays.asList(error);
    }

}
