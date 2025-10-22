package sc.snicky.springbootjwtauth.api.v1.repositories;

import lombok.RequiredArgsConstructor;
import sc.snicky.springbootjwtauth.api.v1.domain.models.RefreshTokenDetails;

import java.util.Optional;
import java.util.UUID;

/**
 * Repository implementation for storing tokens in Redis.
 * <p>
 * The actual implementation is not available yet and will be added in the future.
 */
@RequiredArgsConstructor
public class RedisRefreshTokenRepositoryImpl implements BasicRefreshTokenRepository { //todo add implementation
    /**
     * Saves the given token to Redis storage.
     *
     * @param token the token to save
     */
    @Override
    public void save(RefreshTokenDetails token) {

    }

    /**
     * Finds a token in Redis by its UUID.
     *
     * @param token the UUID of the token to find
     * @return an Optional containing the found token, or empty if not found
     */
    @Override
    public Optional<RefreshTokenDetails> findByToken(UUID token) {
        return Optional.empty();
    }

    /**
     * Deletes all refresh tokens associated with the specified user ID from Redis.
     *
     * @param userId the ID of the user whose tokens should be deleted
     */
    @Override
    public void deleteAllByUserId(Integer userId) {

    }

    /**
     * Deletes the token with the specified UUID from Redis.
     *
     * @param token the UUID of the token to delete
     */
    @Override
    public void delete(UUID token) {

    }
}
