package sc.snicky.springbootjwtauth.api.v1.services;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sc.snicky.springbootjwtauth.api.v1.domain.models.PostgresTokenAdaptor;
import sc.snicky.springbootjwtauth.api.v1.domain.models.RefreshTokenDetails;
import sc.snicky.springbootjwtauth.api.v1.domain.models.Token;
import sc.snicky.springbootjwtauth.api.v1.domain.models.User;
import sc.snicky.springbootjwtauth.api.v1.exceptions.business.security.RefreshTokenIsNotValid;
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
        var user = jpaUserRepository.findById(userId).orElseThrow(
                () -> {
                    log.error("User with id {} not found", userId);
                    return new UserNotFoundException("User not found");
                }
        );
        var result = PostgresTokenAdaptor.ofToken(buildToken(user));
        basicRefreshTokenRepository.save(result);
        return result;
    }

    /**
     * Generates a new refresh token for the specified user.
     *
     * @param user the user entity
     * @return the generated refresh token details
     */
    public RefreshTokenDetails generate(User user) {
        var result = PostgresTokenAdaptor.ofToken(buildToken(user));
        basicRefreshTokenRepository.save(result);
        return result;
    }

    /**
     * Generates a new refresh token for the specified user with a custom expiration.
     *
     * @param user the user entity
     * @param expiration the expiration instant
     * @return the generated refresh token details
     */
    public RefreshTokenDetails generate(User user, Instant expiration) {
        var result = PostgresTokenAdaptor.ofToken(buildToken(user));
        result.setExpiry(expiration);
        basicRefreshTokenRepository.save(result);
        return result;
    }

    /**
     * Rotates (replaces) the specified refresh token with a new one.
     *
     * @param oldToken the UUID of the old refresh token
     * @return the new refresh token details
     * @throws RefreshTokenIsNotValid if the old token is not found or invalid
     */
    @Override
    @Transactional
    public RefreshTokenDetails rotate(UUID oldToken) {
        return basicRefreshTokenRepository.findByToken(oldToken)
                .map(t -> {
                    revoke(oldToken);
                    return generate(t.getUser(), t.getExpiry());
                })
                .orElseThrow(() -> {
                    log.error("Refresh token {} not found for rotation", oldToken);
                    return new RefreshTokenIsNotValid("Refresh token is not valid");
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
        return basicRefreshTokenRepository.findByToken(token);
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
     * Revokes (deletes) the specified refresh token.
     *
     * @param token the UUID of the refresh token to revoke
     */
    @Override
    public void revoke(UUID token) {
        basicRefreshTokenRepository.delete(token);
    }

    /**
     * Builds a new Token entity for the specified user with the configured expiration.
     *
     * @param user the user entity
     * @return the built Token entity
     */
    private Token buildToken(User user) {
        return Token.builder()
                .id(UUID.randomUUID())
                .user(user)
                .exp(Instant.now().plusMillis(refreshTokenDurationMs))
                .build();
    }
}
