package sc.snicky.springbootjwtauth.api.v1.dtos.responses;

import java.time.Instant;

public record ErrorResponse(
        String message,
        String details,
        Integer code,
        Instant timestamp
) {
}
