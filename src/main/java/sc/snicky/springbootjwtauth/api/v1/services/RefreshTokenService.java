package sc.snicky.springbootjwtauth.api.v1.services;

import sc.snicky.springbootjwtauth.api.v1.domain.models.RefreshTokenDetails;
import sc.snicky.springbootjwtauth.api.v1.domain.models.User;

import java.util.Optional;

public interface RefreshTokenService {
    /**
     * Generates a new refresh token for the specified user.
     *
     * @param userId the ID of the user
     * @return the generated {@link RefreshTokenDetails}
     */
    RefreshTokenDetails generate(Integer userId);

    /**
     * Generates a new refresh token for the specified user.
     *
     * @param user the {@link User} entity
     * @return the generated {@link RefreshTokenDetails}
     */
    RefreshTokenDetails generate(User user);

    /**
     * Rotates (replaces) the old refresh token with a new one.
     *
     * @param oldToken the String of the old refresh token
     * @return the new {@link RefreshTokenDetails}
     */
    RefreshTokenDetails rotate(String oldToken);

    /**
     * Finds refresh token details by token.
     *
     * @param token the String of the refresh token
     * @return an {@link Optional} containing {@link RefreshTokenDetails} if found, otherwise empty
     */
    Optional<RefreshTokenDetails> findByToken(String token);

    /**
     * Checks if the given refresh token is valid.
     *
     * @param token the String of the refresh token
     * @return true if the token is valid, false otherwise
     */
    boolean isValid(String token);

    /**
     * Revokes the specified refresh token.
     *
     * @param token the String of the refresh token to revoke
     */
    void revoke(String token);

    /**
     * Revokes all refresh tokens for the user with the specified ID.
     *
     * @param userId the ID of the user whose tokens should be revoked
     */
    void revokeAllTokensForUser(Integer userId);
}
