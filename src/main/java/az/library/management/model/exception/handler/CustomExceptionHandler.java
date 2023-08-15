package az.library.management.model.exception.handler;

import az.library.management.model.exception.*;
import az.library.management.util.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import java.util.List;

@ControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(NoUserFoundException.class)
    public ResponseEntity<ErrorResponse> handleNoUserFoundException(NoUserFoundException noUserFoundException) {
        ErrorResponse errorResponse = new ErrorResponse("Resource Not Found Error", noUserFoundException.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(BooksNotReturnedException.class)
    public ResponseEntity<ErrorResponse> handleBooksNotReturnedException(BooksNotReturnedException booksNotReturnedException) {
        ErrorResponse errorResponse = new ErrorResponse("Books Not Returned on Time", booksNotReturnedException.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

    @ExceptionHandler(NoBookFoundException.class)
    public ResponseEntity<ErrorResponse> handleNoBookFoundException(NoBookFoundException noBookFoundException) {
        ErrorResponse errorResponse = new ErrorResponse("Resource Not Found Error",noBookFoundException.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleBasicValidationException(MethodArgumentNotValidException ex) {
        List<String> fieldErrors = ex.getBindingResult().getFieldErrors().stream().map(error -> error.getField() + " : " + error.getDefaultMessage()).toList();
        ErrorResponse errorResponse = new ErrorResponse("Validation Errors", fieldErrors);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(BookUnavailableException.class)
    public ResponseEntity<ErrorResponse> handleBookUnavailableException(BookUnavailableException bookUnavailableException) {
        ErrorResponse errorResponse = new ErrorResponse("Book Availability to Borrow Error",bookUnavailableException.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

    @ExceptionHandler(NoTransactionException.class)
    public ResponseEntity<ErrorResponse> handleNoTransactionFoundException(NoTransactionException noTransactionException) {
        ErrorResponse errorResponse = new ErrorResponse("Resource Not Found Error",noTransactionException.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }
}

