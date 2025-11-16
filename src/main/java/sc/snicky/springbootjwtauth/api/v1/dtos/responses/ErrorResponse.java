package sc.snicky.springbootjwtauth.api.v1.dtos.responses;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;

public record ErrorResponse(
        @Schema(description = "Error message", example = "Invalid request")
        String message,
        @Schema(description = "Details about the error", example = "Missing required field")
        String details,
        @Schema(description = "HTTP status code", example = "400")
        Integer code,
        @Schema(description = "Timestamp of the error", example = "2023-10-01T12:00:00Z")
        Instant timestamp
) {
}
