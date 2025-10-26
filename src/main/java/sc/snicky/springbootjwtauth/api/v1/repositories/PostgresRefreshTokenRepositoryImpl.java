package sc.snicky.springbootjwtauth.api.v1.repositories;

import lombok.RequiredArgsConstructor;
import sc.snicky.springbootjwtauth.api.v1.domain.models.BasicRefreshToken;
import sc.snicky.springbootjwtauth.api.v1.mappers.JpaRefreshTokenMapper;

import java.util.Optional;

/**
 * The implementation of the BasicRefreshTokenRepository interface for managing tokens in a PostgreSQL database.
 */
@RequiredArgsConstructor
public class PostgresRefreshTokenRepositoryImpl implements BasicRefreshTokenRepository {
    /**
     * JPA token repository for database operations.
     */
    private final JpaRefreshTokenRepository jpaRefreshTokenRepository;
    private final JpaRefreshTokenMapper jpaRefreshTokenMapper;

    /**
     * Saves the provided token to the database.
     *
     * @param token the token to save
     */
    @Override
    public void save(BasicRefreshToken token) {
        var entityToken = jpaRefreshTokenMapper.toJpaRefreshToken(token);
        jpaRefreshTokenRepository.save(entityToken);
    }

    /**
     * Finds a token by its UUID.
     *
     * @param token the UUID of the token
     * @return an Optional containing the found token, or empty if not found
     */
    @Override
    public Optional<BasicRefreshToken> findByToken(String token) {
        return jpaRefreshTokenRepository.findById(token).map(jpaRefreshTokenMapper::toBasicRefreshToken);
    }

    /**
     * Deletes a token by its UUID.
     *
     * @param token the UUID of the token to delete
     */
    @Override
    public void delete(String token) {
        jpaRefreshTokenRepository.deleteById(token);
    }

    /**
     * Deletes all tokens associated with a specific user ID.
     *
     * @param userId the ID of the user whose tokens are to be deleted
     */
    @Override
    public void deleteAllByUserId(Integer userId) {
        jpaRefreshTokenRepository.deleteAllByUserId(userId);
    }
}
