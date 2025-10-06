package sc.snicky.springbootjwtauth.api.v1.configs;

import org.springframework.context.annotation.Configuration;
import sc.snicky.springbootjwtauth.api.v1.repositories.BasicTokenRepository;
import sc.snicky.springbootjwtauth.api.v1.repositories.JpaTokenRepository;
import sc.snicky.springbootjwtauth.api.v1.repositories.PostgresTokenRepositoryImpl;

@Configuration
public class TokenRepositoryConfig {
    /**
     * Creates a Postgres-based token repository bean.
     * Activated when the property `app.auth.tokens.refresh.db` is set to `postgres`.
     *
     * @param jpaTokenRepository the JPA token repository dependency
     * @return a PostgresTokenRepositoryImpl instance
     */
    public BasicTokenRepository postgresTokenRepository(JpaTokenRepository jpaTokenRepository) {
        return new PostgresTokenRepositoryImpl(jpaTokenRepository);
    }

    /**
     * Creates a Redis-based token repository bean.
     * Activated when the property `app.auth.tokens.refresh.db` is set to `redis`.
     * Currently not implemented.
     *
     * @param jpaTokenRepository the JPA token repository dependency
     * @return nothing, always throws UnsupportedOperationException
     * @throws UnsupportedOperationException if called
     */
    public BasicTokenRepository redisTokenRepository(JpaTokenRepository jpaTokenRepository) {
        throw new UnsupportedOperationException("Redis token repository is not implemented yet."); // todo: change
    }
}
