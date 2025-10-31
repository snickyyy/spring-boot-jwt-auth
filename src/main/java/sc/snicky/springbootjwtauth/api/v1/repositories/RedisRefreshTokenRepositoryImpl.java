package sc.snicky.springbootjwtauth.api.v1.repositories;

import lombok.RequiredArgsConstructor;
import sc.snicky.springbootjwtauth.api.v1.domain.models.BasicRefreshToken;
import sc.snicky.springbootjwtauth.api.v1.domain.types.ProtectedToken;

import java.util.Optional;

/**
 * Repository implementation for storing tokens in Redis.
 * <p>
 * The actual implementation is not available yet and will be added in the future.
 */
@RequiredArgsConstructor
public class RedisRefreshTokenRepositoryImpl implements BasicRefreshTokenRepository { //todo add implementation
    /**
     * Stores the given refresh token in Redis.
     *
     * @param token the refresh token to be stored
     */
    @Override
    public void save(BasicRefreshToken token) {

    }

    /**
     * Retrieves a refresh token from Redis using its token string.
     *
     * @param token the token string to look up
     * @return an Optional containing the refresh token if found, otherwise empty
     */
    @Override
    public Optional<BasicRefreshToken> findByToken(ProtectedToken token) {
        return Optional.empty();
    }

    /**
     * Removes a refresh token from Redis using its token string.
     *
     * @param token the token string of the refresh token to be removed
     */
    @Override
    public void delete(ProtectedToken token) {

    }

    /**
     * Removes all refresh tokens associated with the specified user ID from Redis.
     *
     * @param userId the ID of the user whose refresh tokens should be removed
     */
    @Override
    public void deleteAllByUserId(Integer userId) {

    }
}
