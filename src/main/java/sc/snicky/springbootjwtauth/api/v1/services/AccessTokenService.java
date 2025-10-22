package sc.snicky.springbootjwtauth.api.v1.services;

import org.springframework.security.core.userdetails.UserDetails;
import sc.snicky.springbootjwtauth.api.v1.domain.enums.ERole;
import sc.snicky.springbootjwtauth.api.v1.exceptions.business.security.InvalidAccessTokenException;

import java.time.Instant;
import java.util.Set;

public interface AccessTokenService {
    /**
     * Generates a JWT access token for the given user details.
     *
     * @param userDetails the user details for which to generate the token
     * @return the generated JWT token as a String
     */
    String generate(UserDetails userDetails);

    /**
     * Validates the provided JWT access token.
     *
     * @param token the JWT token to validate
     * @return true if the token is valid, false otherwise
     */
    boolean isValid(String token);

    /**
     * Extracts the username from the provided JWT access token.
     *
     * @param token the JWT token from which to extract the username
     * @return the extracted username
     */
    String extractUsername(String token);

    /**
     * Extracts the expiration date from the provided JWT access token.
     *
     * @param token the JWT token from which to extract the expiration date
     * @return the expiration date as an Instant
     */
    Instant extractExpiration(String token);

    /**
     * Extracts the user roles from the provided JWT access token.
     *
     * @param token the JWT token from which to extract roles
     * @return a set of extracted user roles
     */
    Set<ERole> extractRoles(String token);

    /**
     * Extracts UserDetails from a JWT access token after validating its signature and expiration.
     * This is the primary method for authenticating a user from a token.
     *
     * @param token The JWT access token.
     * @return UserDetails object populated with claims from the token.
     * @throws InvalidAccessTokenException if the token is malformed, expired, or has an invalid signature.
     */
    UserDetails extractUserDetails(String token);
}
