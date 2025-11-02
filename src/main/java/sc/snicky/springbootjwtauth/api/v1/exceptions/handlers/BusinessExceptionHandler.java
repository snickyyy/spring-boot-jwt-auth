package sc.snicky.springbootjwtauth.api.v1.exceptions.handlers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import sc.snicky.springbootjwtauth.api.v1.dtos.responses.ErrorResponse;
import sc.snicky.springbootjwtauth.api.v1.exceptions.ConflictException;
import sc.snicky.springbootjwtauth.api.v1.exceptions.NotFoundException;
import sc.snicky.springbootjwtauth.api.v1.exceptions.UnauthorizedException;

import java.time.Instant;

@Slf4j
@Order(0)
@RestControllerAdvice
public class BusinessExceptionHandler {
    /**
     * Handles exceptions of type {@link ConflictException}.
     *
     * @param ex the exception to handle
     * @return a {@link ResponseEntity} containing the error response with HTTP status 409 (Conflict)
     */
    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ErrorResponse> handleConflictException(ConflictException ex) {
        var errorResponse = new ErrorResponse(
                "Conflict",
                ex.getMessage(),
                HttpStatus.CONFLICT.value(),
                Instant.now()
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

    /**
     * Handles exceptions of type {@link NotFoundException}.
     *
     * @param ex the exception to handle
     * @return a {@link ResponseEntity} containing the error response with HTTP status 404 (Not Found)
     */
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFoundException(NotFoundException ex) {
        var errorResponse = new ErrorResponse(
                "Not found",
                ex.getMessage(),
                HttpStatus.NOT_FOUND.value(),
                Instant.now()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    /**
     * Handles exceptions of type {@link UnauthorizedException}.
     *
     * @param ex the exception to handle
     * @return a {@link ResponseEntity} containing the error response with HTTP status 401 (Unauthorized)
     */
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorResponse> handleUnauthorizedException(UnauthorizedException ex) {
        var errorResponse = new ErrorResponse(
                "Unauthorized",
                ex.getMessage(),
                HttpStatus.UNAUTHORIZED.value(),
                Instant.now()
        );
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }
}
