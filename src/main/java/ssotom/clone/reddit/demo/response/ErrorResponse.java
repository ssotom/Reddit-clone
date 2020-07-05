package ssotom.clone.reddit.demo.response;

import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

@Data
public class ErrorResponse {
    private int status;
    private String error;
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

    public static ResponseEntity<?> returnError(String message, HttpStatus httpStatus) {
        return new ResponseEntity<>(new ErrorResponse(httpStatus, "Fatal error", message), httpStatus);
    }

    public ErrorResponse(HttpStatus status, String message, List<String> errors) {
        this.status = status.value();
        this.message = message;
        this.error = status.name();
        this.errors = errors;
    }

    public ErrorResponse(HttpStatus status, String message, String error) {
        this.status = status.value();
        this.message = message;
        this.error = status.name();
        errors = Arrays.asList(error);
    }

}
