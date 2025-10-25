package sc.snicky.springbootjwtauth.api.v1.domain.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
public class JpaRefreshTokenDetailsAdaptor implements RefreshTokenDetails {
    private UUID token;
    private User user;
    private Instant expiry;
    private Instant createdAt;

    /**
     * Creates a new {@link JpaRefreshTokenDetailsAdaptor} instance from the given token and basic refresh token.
     *
     * @param token the refresh token UUID
     * @param basicRefreshToken the basic refresh token containing user and expiry details
     * @return a new {@code JpaRefreshTokenDetailsAdaptor} instance
     */
    public static JpaRefreshTokenDetailsAdaptor ofToken(UUID token, BasicRefreshToken basicRefreshToken) {
        return JpaRefreshTokenDetailsAdaptor.builder()
                .token(token)
                .user(basicRefreshToken.getUser())
                .expiry(basicRefreshToken.getExpiresAt())
                .createdAt(basicRefreshToken.getCreatedAt())
                .build();
    }
}
