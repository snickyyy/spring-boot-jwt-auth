package sc.snicky.springbootjwtauth.api.v1.services;

import org.springframework.security.core.userdetails.UserDetails;
import sc.snicky.springbootjwtauth.api.v1.domain.enums.ERole;

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
}
