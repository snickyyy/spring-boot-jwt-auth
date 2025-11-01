package sc.snicky.springbootjwtauth.api.v1.exceptions.handlers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import sc.snicky.springbootjwtauth.api.v1.dtos.responses.ErrorResponse;

import java.time.Instant;
import java.util.Optional;

@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
public class ValidationExceptionHandler { // todo добавить AOP для логирования ошибок
    /**
     * Handles exceptions when a method argument type does not match the expected type.
     *
     * @param ex the exception thrown when a method argument type mismatch occurs
     * @return a ResponseEntity containing an ErrorResponse with details about the error
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        var errorResponse = new ErrorResponse(
                "Type Mismatch",
                String.format("Parameter '%s' should be of type '%s'",
                        ex.getName(),
                        Optional.ofNullable(ex.getRequiredType())
                                .map(Class::getSimpleName)
                                .orElse("unknown")),
                HttpStatus.BAD_REQUEST.value(),
                Instant.now()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    /**
     * Handles exceptions when method arguments fail validation.
     *
     * @param ex the exception thrown when validation errors occur
     * @return a ResponseEntity containing an ErrorResponse with details about the validation errors
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationErrors(MethodArgumentNotValidException ex) {
        var errorResponse = new ErrorResponse(
                "Validation Error",
                ex.getBindingResult().getFieldErrors().stream()
                        .map(error -> error.getField() + ": " + error.getDefaultMessage())
                        .reduce((msg1, msg2) -> msg1 + "; " + msg2)
                        .orElse(""),
                HttpStatus.BAD_REQUEST.value(),
                Instant.now()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    /**
     * Handles exceptions when a required request parameter is missing.
     *
     * @param ex the exception thrown when a required request parameter is missing
     * @return a ResponseEntity containing an ErrorResponse with details about the missing parameter
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse> handleMissingParams(MissingServletRequestParameterException ex) {
        var errorResponse = new ErrorResponse(
                "Missing Request Parameter",
                ex.getParameterName() + " parameter is missing",
                HttpStatus.BAD_REQUEST.value(),
                Instant.now()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }
}
