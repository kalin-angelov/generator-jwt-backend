package app.web;

import app.exceptions.EmailExistInDatabaseException;
import app.exceptions.InvalidUsernameOrPasswordException;
import app.exceptions.UsernameExistInDatabaseException;
import app.web.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionAdvice {

    @ExceptionHandler(EmailExistInDatabaseException.class)
    public ResponseEntity<ErrorResponse> handleEmailIsTaken() {

        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "Invalid Email");

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(errorResponse);
    }

    @ExceptionHandler(UsernameExistInDatabaseException.class)
    public ResponseEntity<ErrorResponse> handleUsernameIsTaken() {

        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "Invalid Username");

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(errorResponse);
    }

    @ExceptionHandler(InvalidUsernameOrPasswordException.class)
    public ResponseEntity<ErrorResponse> handleInvalidUsernameOrPassword() {

        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "Invalid Username or Password");

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(errorResponse);
    }
}
