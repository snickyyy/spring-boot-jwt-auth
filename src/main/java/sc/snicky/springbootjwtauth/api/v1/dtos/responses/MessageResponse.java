package sc.snicky.springbootjwtauth.api.v1.dtos.responses;

import java.time.Instant;

public record MessageResponse<T>(T message, Instant timestamp) {
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
