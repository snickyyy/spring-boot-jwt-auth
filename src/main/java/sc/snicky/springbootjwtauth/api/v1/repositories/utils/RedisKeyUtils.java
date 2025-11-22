package sc.snicky.springbootjwtauth.api.v1.repositories.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class RedisKeyUtils {
    @Value("${app.redis.tags.divider::}")
    private String redisKeyDivider;

    /**
     * Builds a Redis key by joining the provided parts with the configured divider.
     *
     * @param parts the parts to be joined to form the key
     * @return the constructed Redis key
     */
    public String buildKey(String... parts) {
        return String.join(redisKeyDivider, parts);
    }
}
