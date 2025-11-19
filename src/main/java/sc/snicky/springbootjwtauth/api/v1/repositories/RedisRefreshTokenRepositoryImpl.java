package sc.snicky.springbootjwtauth.api.v1.repositories;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import sc.snicky.springbootjwtauth.api.v1.domain.models.BasicRefreshToken;
import sc.snicky.springbootjwtauth.api.v1.domain.types.ProtectedToken;
import sc.snicky.springbootjwtauth.api.v1.exceptions.internal.InvalidTokenTtlException;
import sc.snicky.springbootjwtauth.api.v1.repositories.utils.RedisKeyUtils;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;

/**
 * Repository implementation for storing tokens in Redis.
 * <p>
 * The actual implementation is not available yet and will be added in the future.
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class RedisRefreshTokenRepositoryImpl implements BasicRefreshTokenRepository {
    private final RedisTemplate<String, BasicRefreshToken> redisTemplate;
    private final RedisKeyUtils redisKeyUtils;
    private final UsersTokenManager usersTokenManager;
    @Value("${app.redis.tags.refresh-token:token}")
    private String redisTokenKeyPrefix;

    /**
     * Stores the given refresh token in Redis.
     *
     * @param token the refresh token to be stored
     */
    @Override
    public void save(BasicRefreshToken token) {
        redisTemplate.opsForValue().set(
                redisKeyUtils.buildKey(redisTokenKeyPrefix, token.getToken().getToken()),
                token,
                getTtl(token.getExpiresAt())
        );
        usersTokenManager.assignTokenToUser(
                token.getUser().getId(),
                token.getToken().getToken()
        );
        log.info("Saved refresh token for user: {}", token.getUser().getId());
    }

    /**
     * Retrieves a refresh token from Redis using its token string.
     *
     * @param token the token string to look up
     * @return an Optional containing the refresh token if found, otherwise empty
     */
    @Override
    public Optional<BasicRefreshToken> findByToken(ProtectedToken token) {
        return Optional.ofNullable(
                redisTemplate.opsForValue().get(
                        redisKeyUtils.buildKey(redisTokenKeyPrefix, token.getToken())
                )
        );
    }

    /**
     * Removes a refresh token from Redis using its token string.
     *
     * @param token the token string of the refresh token to be removed
     */
    @Override
    public void delete(ProtectedToken token) {
        redisTemplate.opsForValue().getAndDelete(
                redisKeyUtils.buildKey(redisTokenKeyPrefix, token.getToken())
        );
    }

    /**
     * Removes all refresh tokens associated with the specified user ID from Redis.
     *
     * @param userId the ID of the user whose refresh tokens should be removed
     */
    @Override
    public void deleteAllByUserId(Integer userId) {
        usersTokenManager.removeAllTokensFromUser(userId);
    }

    private Duration getTtl(Instant expiresAt) {
        Duration ttl = Duration.between(Instant.now(), expiresAt);

        if (ttl.isNegative() || ttl.isZero()) {
            throw new InvalidTokenTtlException(
                    "Refresh token expiresAt is in the past: " + expiresAt
            );
        }
        return ttl;
    }
}
