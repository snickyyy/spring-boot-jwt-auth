package sc.snicky.springbootjwtauth.api.v1.repositories;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import sc.snicky.springbootjwtauth.api.v1.repositories.utils.RedisKeyUtils;

@Repository
@RequiredArgsConstructor
public class UsersTokenManager {
    private final RedisTemplate<String, Object> redisTemplate;
    private final RedisKeyUtils redisKeyUtils;

    @Value("${app.redis.tags.users-tokens-store:users-tokens-store}")
    private String redisUsersTokensStorePrefix;

    /**
     * Checks if the given token is valid for the specified user.
     *
     * @param userId the ID of the user
     * @param token the token to validate
     * @return {@code true} if the token is valid, {@code false} otherwise
     */
    public Boolean isTokenValidForUser(Integer userId, String token) {
        return redisTemplate.opsForSet().isMember(
                redisKeyUtils.buildKey(redisUsersTokensStorePrefix, String.valueOf(userId)),
                token
        );
    }

    /**
     * Assigns a token to the specified user.
     *
     * @param userId the ID of the user
     * @param token the token to assign
     */
    public void assignTokenToUser(Integer userId, String token) {
        redisTemplate.opsForSet().add(
                redisKeyUtils.buildKey(redisUsersTokensStorePrefix, String.valueOf(userId)),
                token
        );
    }

    /**
     * Removes a specific token from the specified user.
     *
     * @param userId the ID of the user
     * @param token the token to remove
     */
    public void removeTokenFromUser(Integer userId, String token) {
        redisTemplate.opsForSet().remove(
                redisKeyUtils.buildKey(redisUsersTokensStorePrefix, String.valueOf(userId)),
                token
        );
    }

    /**
     * Removes all tokens associated with the specified user.
     *
     * @param userId the ID of the user
     */
    public void removeAllTokensFromUser(Integer userId) {
        redisTemplate.delete(
                redisKeyUtils.buildKey(redisUsersTokensStorePrefix, String.valueOf(userId))
        );
    }
}
