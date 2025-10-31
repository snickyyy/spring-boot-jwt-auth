package sc.snicky.springbootjwtauth.api.v1.domain.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import sc.snicky.springbootjwtauth.api.v1.domain.types.NonProtectedToken;

import java.time.Instant;

@Data
@Builder
@AllArgsConstructor
public class RefreshTokenDetailsAdaptor implements RefreshTokenDetails {
    private NonProtectedToken token;
    private User user;
    private Boolean isActive;
    private Instant expiry;
    private Instant createdAt;

    /**
     * Creates a new {@link RefreshTokenDetailsAdaptor} instance from the given token and basic refresh token.
     *
     * @param token the refresh token NonProtectedToken
     * @param basicRefreshToken the basic refresh token containing user and expiry details
     * @return a new {@code RefreshTokenDetailsAdaptor} instance
     */
    public static RefreshTokenDetailsAdaptor ofToken(NonProtectedToken token, BasicRefreshToken basicRefreshToken) {
        return RefreshTokenDetailsAdaptor.builder()
                .token(token)
                .user(basicRefreshToken.getUser())
                .isActive(basicRefreshToken.getIsActive())
                .expiry(basicRefreshToken.getExpiresAt())
                .createdAt(basicRefreshToken.getCreatedAt())
                .build();
    }
}
