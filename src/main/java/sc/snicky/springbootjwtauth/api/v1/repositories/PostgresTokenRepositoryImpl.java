package sc.snicky.springbootjwtauth.api.v1.repositories;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import sc.snicky.springbootjwtauth.api.v1.domain.models.Token;

import java.util.Optional;
import java.util.UUID;

/**
 * The implementation of the BasicTokenRepository interface for managing tokens in a PostgreSQL database.
 */
@Repository
@RequiredArgsConstructor
public class PostgresTokenRepositoryImpl implements BasicTokenRepository {
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
    public void save(Token token) {
        jpaTokenRepository.save(token);
    }

    /**
     * Finds a token by its UUID.
     *
     * @param token the UUID of the token
     * @return an Optional containing the found token, or empty if not found
     */
    @Override
    public Optional<Token> findByToken(UUID token) {
        return jpaTokenRepository.findById(token);
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
}
