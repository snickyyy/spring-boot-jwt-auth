package sc.snicky.springbootjwtauth.api.v1.configs;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import sc.snicky.springbootjwtauth.api.v1.mappers.JpaRefreshTokenMapper;
import sc.snicky.springbootjwtauth.api.v1.repositories.BasicRefreshTokenRepository;
import sc.snicky.springbootjwtauth.api.v1.repositories.JpaRefreshTokenRepository;
import sc.snicky.springbootjwtauth.api.v1.repositories.PostgresRefreshTokenRepositoryImpl;

@Deprecated
@Configuration
@ConditionalOnProperty(prefix = "app.old-config", name = "enabled", havingValue = "true")
public class TokenRepositoryConfig {
    /**
     * Creates a Postgres-based token repository bean.
     * Activated when the property `app.auth.tokens.refresh.db` is set to `postgres`.
     *
     * @param mapper                    the JPA token mapper dependency
     * @param repo the JPA token repository dependency
     * @return a PostgresTokenRepositoryImpl instance
     */
    @Bean
    @ConditionalOnProperty(prefix = "app.auth.tokens.refresh", name = "db", havingValue = "postgres")
    public BasicRefreshTokenRepository postgresTokenRepository(JpaRefreshTokenMapper mapper, JpaRefreshTokenRepository repo) {
        return new PostgresRefreshTokenRepositoryImpl(repo, mapper);
    }

    /**
     * Creates a Redis-based token repository bean.
     * Activated when the property `app.auth.tokens.refresh.db` is set to `redis`.
     * Currently not implemented.
     *
     * @return nothing, always throws UnsupportedOperationException
     * @throws UnsupportedOperationException if called
     */
    @Bean
    @ConditionalOnProperty(prefix = "app.auth.tokens.refresh", name = "db", havingValue = "redis")
    public BasicRefreshTokenRepository redisTokenRepository() {
        throw new UnsupportedOperationException("Redis token repository is not implemented yet."); // todo: change
    }
}
