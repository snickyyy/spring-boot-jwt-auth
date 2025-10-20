package sc.snicky.springbootjwtauth.api.v1.services;

import org.springframework.security.core.userdetails.UserDetails;
import sc.snicky.springbootjwtauth.api.v1.dtos.TokenPair;
import sc.snicky.springbootjwtauth.api.v1.exceptions.business.security.InvalidAccessTokenException;
import sc.snicky.springbootjwtauth.api.v1.exceptions.business.security.InvalidRefreshTokenException;
import sc.snicky.springbootjwtauth.api.v1.exceptions.business.users.UserNotFoundException;

public interface TokensManager {
    /**
     * Generates a new pair of tokens for a given user ID.
     * The refresh token is persisted.
     *
     * @param userId The ID of the user.
     * @return A new TokenPair.
     * @throws UserNotFoundException if the user with the given ID does not exist.
     */
    TokenPair generateTokens(Integer userId);
    /**
     * Generates a new TokenPair using a valid refresh token.
     * This method implements token rotation: the provided refresh token is immediately
     * invalidated and replaced by the new one in the returned TokenPair.
     *
     * @param refreshToken The UUID of the refresh token to be used.
     * @return A new TokenPair.
     * @throws InvalidRefreshTokenException if the token is not found, expired, or has already been used.
     */
    TokenPair refreshTokens(String refreshToken);
    /**
     * Revokes a specific refresh token, effectively logging out a single session.
     *
     * @param refreshToken The token to revoke.
     */
    void revokeRefreshToken(String refreshToken);
    /**
     * Revokes all active refresh tokens for a user, logging them out from all devices.
     *
     * @param userId The ID of the user.
     */
    void revokeAllTokensForUser(Integer userId);

    /**
     * Extracts UserDetails from a JWT access token after validating its signature and expiration.
     * This is the primary method for authenticating a user from a token.
     *
     * @param accessToken The JWT access token.
     * @return UserDetails object populated with claims from the token.
     * @throws InvalidAccessTokenException if the token is malformed, expired, or has an invalid signature.
     */
    UserDetails extractUserDetails(String accessToken);
}
