package sc.snicky.springbootjwtauth.api.v1.domain.models;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;


@Data
@Builder
public class BasicRefreshToken {
    private String token;
    private User user;
    private Boolean isActive;
    private Instant expiresAt;
    private Instant createdAt;
}
