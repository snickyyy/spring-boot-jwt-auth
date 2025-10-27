package sc.snicky.springbootjwtauth.api.v1.services;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sc.snicky.springbootjwtauth.api.v1.domain.models.BasicRefreshToken;
import sc.snicky.springbootjwtauth.api.v1.domain.models.RefreshTokenDetails;
import sc.snicky.springbootjwtauth.api.v1.domain.models.RefreshTokenDetailsAdaptor;
import sc.snicky.springbootjwtauth.api.v1.domain.models.User;
import sc.snicky.springbootjwtauth.api.v1.exceptions.business.security.InvalidRefreshTokenException;
import sc.snicky.springbootjwtauth.api.v1.exceptions.business.users.UserNotFoundException;
import sc.snicky.springbootjwtauth.api.v1.repositories.BasicRefreshTokenRepository;
import sc.snicky.springbootjwtauth.api.v1.repositories.JpaUserRepository;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@Setter
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {
    private final PasswordEncoder passwordEncoder;
    private final BasicRefreshTokenRepository basicRefreshTokenRepository;
    @Value("${app.auth.tokens.expiration.refresh:604800000}")
    private Long refreshTokenDurationMs;

    private final JpaUserRepository jpaUserRepository;

    /**
     * Generates a new refresh token for the user with the specified ID.
     *
     * @param userId the ID of the user
     * @return the generated refresh token details
     * @throws UserNotFoundException if the user is not found
     */
    @Override
    public RefreshTokenDetails generate(Integer userId) {
        return generate(getUser(userId));
    }

    /**
     * Generates a new refresh token for the specified user.
     *
     * @param user the user entity
     * @return the generated refresh token details
     */
    public RefreshTokenDetails generate(User user) {
        var token = UUID.randomUUID();
        var refreshToken = buildToken(token, user);
        basicRefreshTokenRepository.save(refreshToken);
        return RefreshTokenDetailsAdaptor.builder() // todo поменять и поставить везде интерфейс базовый
                .token(token)
                .user(user)
                .expiry(refreshToken.getExpiresAt())
                .build();
    }

    /**
     * Generates a new refresh token for the specified user with a custom expiration.
     *
     * @param user the user entity
     * @param expiration the expiration instant
     * @return the generated refresh token details
     */
    public RefreshTokenDetails generate(User user, Instant expiration) {
        var token = UUID.randomUUID();
        var refreshToken = buildToken(token, user);
        refreshToken.setExpiresAt(expiration);
        basicRefreshTokenRepository.save(refreshToken);
        return RefreshTokenDetailsAdaptor.builder()
                .token(token)
                .user(user)
                .expiry(refreshToken.getExpiresAt())
                .build();
    }

    /**
     * Rotates (replaces) the specified refresh token with a new one.
     *
     * @param oldToken the UUID of the old refresh token
     * @return the new refresh token details
     * @throws InvalidRefreshTokenException if the old token is not found or invalid
     */
    @Override
    @Transactional
    public RefreshTokenDetails rotate(UUID oldToken) {
        return basicRefreshTokenRepository.findByToken(passwordEncoder.encode(oldToken.toString()))
                .map(t -> {
                    if (t.getExpiresAt().isBefore(Instant.now())) {
                        log.error("Refresh token {} has expired and cannot be rotated", oldToken);
                        throw new InvalidRefreshTokenException("Refresh token has expired");
                    }
                    revoke(oldToken);
                    return generate(t.getUser(), t.getExpiresAt());
                })
                .orElseThrow(() -> {
                    log.error("Refresh token {} not found for rotation", oldToken);
                    return new InvalidRefreshTokenException("Refresh token is not valid");
                });
    }

    /**
     * Finds a refresh token by its UUID.
     *
     * @param token the UUID of the refresh token
     * @return an Optional containing the refresh token details if found, or empty otherwise
     */
    @Override
    public Optional<RefreshTokenDetails> findByToken(UUID token) {
        return basicRefreshTokenRepository.findByToken(passwordEncoder.encode(token.toString()))
                .map(result -> RefreshTokenDetailsAdaptor.builder()
                        .token(token)
                        .user(result.getUser())
                        .expiry(result.getExpiresAt())
                        .createdAt(result.getCreatedAt())
                        .build());
    }

    /**
     * Checks if the specified refresh token is valid (exists and not expired).
     *
     * @param token the UUID of the refresh token
     * @return true if the token is valid, false otherwise
     */
    @Override
    public boolean isValid(UUID token) {
        return findByToken(token)
                .map(t -> t.getExpiry().isAfter(Instant.now()))
                .orElse(false);
    }

    /**
     * Revokes (deletes) all refresh tokens for the user with the specified ID.
     *
     * @param userId the ID of the user whose tokens should be revoked
     */
    @Transactional(readOnly = true)
    public void revokeAllTokensForUser(Integer userId) {
        var user = getUser(userId);
        basicRefreshTokenRepository.deleteAllByUserId(user.getId());
    }


    /**
     * Revokes (deletes) the specified refresh token.
     *
     * @param token the UUID of the refresh token to revoke
     */
    @Override
    public void revoke(UUID token) {
        basicRefreshTokenRepository.delete(passwordEncoder.encode(token.toString()));
    }

    /**
     * Builds a new Token entity for the specified user with the configured expiration.
     *
     * @param token the UUID of the token
     * @param user the user entity
     * @return the built Token entity
     */
    private BasicRefreshToken buildToken(UUID token, User user) {
        return BasicRefreshToken.builder()
                .token(passwordEncoder.encode(token.toString()))
                .user(user)
                .expiresAt(Instant.now().plusMillis(refreshTokenDurationMs))
                .build();
    }

    private User getUser(Integer userId) {
        return jpaUserRepository.findById(userId).orElseThrow(
                () -> {
                    log.error("User with id {} not found", userId);
                    return new UserNotFoundException("User not found");
                }
        );
    }
}
