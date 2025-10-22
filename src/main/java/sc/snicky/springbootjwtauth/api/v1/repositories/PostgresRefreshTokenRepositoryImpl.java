package sc.snicky.springbootjwtauth.api.v1.repositories;

import lombok.RequiredArgsConstructor;
import sc.snicky.springbootjwtauth.api.v1.domain.models.PostgresTokenAdaptor;
import sc.snicky.springbootjwtauth.api.v1.domain.models.RefreshTokenDetails;
import sc.snicky.springbootjwtauth.api.v1.domain.models.Token;

import java.util.Optional;
import java.util.UUID;

/**
 * The implementation of the BasicRefreshTokenRepository interface for managing tokens in a PostgreSQL database.
 */
@RequiredArgsConstructor
public class PostgresRefreshTokenRepositoryImpl implements BasicRefreshTokenRepository {
    /**
     * JPA token repository for database operations.
     */
    private final JpaTokenRepository jpaTokenRepository;

    /**
     * Saves the provided token to the database.
     *
     * @param token the token to save
     */
    @Override
    public void save(RefreshTokenDetails token) {
        var entityToken = Token.builder()
                .id(token.getToken())
                .user(token.getUser())
                .exp(token.getExpiry())
                .build();
        entityToken.setCreatedAt(token.getCreatedAt());
        jpaTokenRepository.save(entityToken);
    }

    /**
     * Finds a token by its UUID.
     *
     * @param token the UUID of the token
     * @return an Optional containing the found token, or empty if not found
     */
    @Override
    public Optional<RefreshTokenDetails> findByToken(UUID token) {
        return jpaTokenRepository.findById(token).map(PostgresTokenAdaptor::ofToken);
    }

    /**
     * Deletes a token by its UUID.
     *
     * @param token the UUID of the token to delete
     */
    @Override
    public void delete(UUID token) {
        jpaTokenRepository.deleteById(token);
    }

    /**
     * Deletes all tokens associated with a specific user ID.
     *
     * @param userId the ID of the user whose tokens are to be deleted
     */
    @Override
    public void deleteAllByUserId(Integer userId) {
        jpaTokenRepository.deleteAllByUserId(userId);
    }
}
