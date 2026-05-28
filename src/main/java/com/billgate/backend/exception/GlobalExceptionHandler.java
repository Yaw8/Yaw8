package com.billgate.backend.exception;

// Handles validation errors from @Valid.
import org.springframework.web.bind.MethodArgumentNotValidException;

// Handles database constraint errors.
import org.springframework.dao.DataIntegrityViolationException;

// HTTP status codes.
import org.springframework.http.HttpStatus;

// HTTP response wrapper.
import org.springframework.http.ResponseEntity;

// Allows this class to handle exceptions globally.
import org.springframework.web.bind.annotation.ControllerAdvice;

// Marks methods as exception handlers.
import org.springframework.web.bind.annotation.ExceptionHandler;

// Global exception handler.
//
// This catches backend errors in one central place
// instead of letting raw stack traces leak to the client.
@ControllerAdvice
public class GlobalExceptionHandler {

    // Handles validation errors.
    //
    // Example:
    // - invalid email
    // - short password
    // - empty name
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleValidationErrors(
            MethodArgumentNotValidException ex
    ) {
        String message = ex.getBindingResult()
                .getFieldErrors()
                .get(0)
                .getDefaultMessage();

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(message);
    }

    // Handles database relationship/constraint errors.
    //
    // Example:
    // deleting category that bills/repairs still use.
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<String> handleDataIntegrityErrors(
            DataIntegrityViolationException ex
    ) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(
                        "This record cannot be deleted because it is still being used."
                );
    }

    // Handles general runtime errors.
    //
    // Example:
    // user not found
    // bill not found
    // invalid login
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeErrors(
            RuntimeException ex
    ) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ex.getMessage());
    }
}