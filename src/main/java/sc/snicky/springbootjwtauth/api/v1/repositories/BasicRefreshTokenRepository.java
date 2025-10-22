package sc.snicky.springbootjwtauth.api.v1.repositories;

import org.springframework.stereotype.Repository;
import sc.snicky.springbootjwtauth.api.v1.domain.models.RefreshTokenDetails;

import java.util.Optional;
import java.util.UUID;

/**
 * This repository serves as a basic repository for maintaining token and should have implementation.
 */
@Repository
public interface BasicRefreshTokenRepository {
    /**
     * Save token.
     *
     * @param token the token
     */
    void save(RefreshTokenDetails token);

    /**
     * Find token by uuid of token.
     *
     * @param token the token
     * @return the optional
     */
    Optional<RefreshTokenDetails> findByToken(UUID token);

    /**
     * Delete token.
     *
     * @param token the token
     */
    void delete(UUID token);

    /**
     * Deletes all refresh tokens associated with the specified user ID from storage.
     *
     * @param userId the ID of the user whose tokens should be deleted
     */
    void deleteAllByUserId(Integer userId);
}
