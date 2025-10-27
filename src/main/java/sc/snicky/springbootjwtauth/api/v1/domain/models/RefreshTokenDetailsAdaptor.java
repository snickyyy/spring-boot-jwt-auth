package sc.snicky.springbootjwtauth.api.v1.domain.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
public class RefreshTokenDetailsAdaptor implements RefreshTokenDetails {
    private UUID token;
    private User user;
    private Instant expiry;
    private Instant createdAt;

    /**
     * Creates a new {@link RefreshTokenDetailsAdaptor} instance from the given token and basic refresh token.
     *
     * @param token the refresh token UUID
     * @param basicRefreshToken the basic refresh token containing user and expiry details
     * @return a new {@code RefreshTokenDetailsAdaptor} instance
     */
    public static RefreshTokenDetailsAdaptor ofToken(UUID token, BasicRefreshToken basicRefreshToken) {
        return RefreshTokenDetailsAdaptor.builder()
                .token(token)
                .user(basicRefreshToken.getUser())
                .expiry(basicRefreshToken.getExpiresAt())
                .createdAt(basicRefreshToken.getCreatedAt())
                .build();
    }
}
