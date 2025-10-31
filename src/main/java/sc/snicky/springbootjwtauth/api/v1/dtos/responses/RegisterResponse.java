package sc.snicky.springbootjwtauth.api.v1.dtos.responses;


import java.time.Instant;

public record RegisterResponse(
        String message,
        String accessToken,
        Instant timestamp
) {
}
