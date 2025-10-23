package sc.snicky.springbootjwtauth.api.v1.repositories;

import lombok.RequiredArgsConstructor;
import sc.snicky.springbootjwtauth.api.v1.domain.models.BasicRefreshToken;

import java.util.Optional;

/**
 * Repository implementation for storing tokens in Redis.
 * <p>
 * The actual implementation is not available yet and will be added in the future.
 */
@RequiredArgsConstructor
public class RedisRefreshTokenRepositoryImpl implements BasicRefreshTokenRepository { //todo add implementation
    /**
     * Saves a refresh token to Redis.
     *
     * @param token the refresh token to save
     */
    @Override
    public void save(BasicRefreshToken token) {

    }

    /**
     * Finds a refresh token in Redis by its token string.
     *
     * @param token the token string to search for
     * @return an Optional containing the found refresh token, or empty if not found
     */
    @Override
    public Optional<BasicRefreshToken> findByToken(String token) {
        return Optional.empty();
    }

    /**
     * Deletes a refresh token from Redis by its token string.
     *
     * @param token the token string to delete
     */
    @Override
    public void delete(String token) {

    }

    /**
     * Deletes all refresh tokens in Redis associated with a specific user ID.
     *
     * @param userId the user ID whose tokens should be deleted
     */
    @Override
    public void deleteAllByUserId(Integer userId) {

    }
}
