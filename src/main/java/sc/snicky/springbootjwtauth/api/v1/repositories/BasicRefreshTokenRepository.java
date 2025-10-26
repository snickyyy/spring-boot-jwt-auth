package sc.snicky.springbootjwtauth.api.v1.repositories;

import org.springframework.stereotype.Repository;
import sc.snicky.springbootjwtauth.api.v1.domain.models.BasicRefreshToken;

import java.util.Optional;

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
    void save(BasicRefreshToken token);

    /**
     * Find token by uuid of token.
     *
     * @param token the hashed value of token
     * @return the optional
     */
    Optional<BasicRefreshToken> findByToken(String token);

    /**
     * Delete token.
     *
     * @param token the hashed value of token
     */
    void delete(String token);

    /**
     * Deletes all refresh tokens associated with the specified user ID from storage.
     *
     * @param userId the ID of the user whose tokens should be deleted
     */
    void deleteAllByUserId(Integer userId);
}
