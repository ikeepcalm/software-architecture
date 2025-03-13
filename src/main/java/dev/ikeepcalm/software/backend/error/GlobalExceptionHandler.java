package dev.ikeepcalm.software.backend.error;

import dev.ikeepcalm.software.backend.error.exceptions.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(ResourceNotFoundException ex) {
        ErrorResponse.ErrorDetails errorDetails = ErrorResponse.ErrorDetails.builder()
                .code("RESOURCE_NOT_FOUND")
                .message(ex.getMessage())
                .build();
        
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(errorDetails));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException ex) {
        List<ErrorResponse.FieldError> fieldErrors = ex.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> new ErrorResponse.FieldError(
                        fieldError.getField(),
                        fieldError.getDefaultMessage()
                ))
                .collect(Collectors.toList());

        ErrorResponse.ErrorDetails errorDetails = ErrorResponse.ErrorDetails.builder()
                .code("VALIDATION_ERROR")
                .message("Validation failed for request")
                .details(fieldErrors)
                .build();
        
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(errorDetails));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        ErrorResponse.ErrorDetails errorDetails = ErrorResponse.ErrorDetails.builder()
                .code("INTERNAL_SERVER_ERROR")
                .message("An unexpected error occurred")
                .build();
        
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse(errorDetails));
    }
}