package sc.snicky.springbootjwtauth.api.v1.domain.models;

import lombok.Builder;
import lombok.Data;
import sc.snicky.springbootjwtauth.api.v1.domain.types.ProtectedToken;

import java.time.Instant;


@Data
@Builder
public class BasicRefreshToken {
    private ProtectedToken token;
    private User user;
    private Boolean isActive;
    private Instant expiresAt;
    private Instant createdAt;
}
