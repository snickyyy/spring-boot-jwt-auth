package sc.snicky.springbootjwtauth.api.v1.exceptions.handlers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import sc.snicky.springbootjwtauth.api.v1.dtos.responses.ErrorResponse;

@Slf4j
@Order
@ControllerAdvice
public class GlobalExceptionHandler {
    /**
     * Handles all uncaught exceptions in the application.
     *
     * @param ex the exception that was thrown
     * @return a ResponseEntity containing an ErrorResponse with details about the error
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception ex) {
        log.error(ex.getMessage(), ex);
        return  ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse(
                "Internal Server Error",
                "",
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                java.time.Instant.now()
        ));
    }
}
