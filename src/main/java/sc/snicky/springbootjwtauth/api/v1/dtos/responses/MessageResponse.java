package sc.snicky.springbootjwtauth.api.v1.dtos.responses;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;

public record MessageResponse<T>(
        @Schema(description = "The message content", example = "Operation successful") T message,
        @Schema(description = "The timestamp of the response", example = "2023-01-01T12:00:00Z") Instant timestamp
) {
    /**
     * Creates a new MessageResponse with the given message and the current timestamp.
     *
     * @param message the message to include in the response
     * @return a MessageResponse containing the message and current timestamp
     */
    public static <T> MessageResponse<T> of(T message) {
        return new MessageResponse<>(message, Instant.now());
    }
}
