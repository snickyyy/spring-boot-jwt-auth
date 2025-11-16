package sc.snicky.springbootjwtauth.api.v1.dtos.responses;


import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;

public record AuthResponse(
        @Schema(description = "Response message", example = "Authentication successful")
        String message,
        @Schema(description = "JWT access token", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
        String accessToken,
        @Schema(description = "Timestamp of the response", example = "2023-10-05T12:34:56Z")
        Instant timestamp
) {
}
