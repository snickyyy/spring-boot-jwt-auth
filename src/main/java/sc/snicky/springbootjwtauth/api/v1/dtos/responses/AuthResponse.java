package sc.snicky.springbootjwtauth.api.v1.dtos.responses;


import java.time.Instant;

public record AuthResponse(
        String message,
        String accessToken,
        Instant timestamp
) {
}
