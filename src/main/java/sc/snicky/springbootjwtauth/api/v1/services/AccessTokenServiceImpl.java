package sc.snicky.springbootjwtauth.api.v1.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import sc.snicky.springbootjwtauth.api.v1.domain.enums.ERole;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Implementation of AccessTokenService interface responsible for JWT token operations:
 * generating tokens, validating tokens, and extracting claims from tokens.
 */
@Setter
@Service
public class AccessTokenServiceImpl implements AccessTokenService {
    /**
     * Duration of access token in milliseconds, loaded from application properties.
     */
    @Value("${app.auth.tokens.expiration.access:3600000}")
    private Long accessTokenDurationMs;

    /**
     * Secret key used for signing JWT tokens, loaded from application properties.
     */
    @Value("${app.auth.tokens.secret.access:default_jwt_signing_key_which_should_be_replaced}")
    private String jwtSigningKey;

    /**
     * Extracts user roles from a JWT token.
     *
     * @param token The JWT token string
     * @return A set of ERole enums representing the user's roles
     */
    @Override
    public Set<ERole> extractRoles(String token) {
        var claims = extractAllClaims(token);
        var roles = (List<String>) claims.get("roles");
        return roles.stream()
                .map(x -> x.replace("ROLE_", "")) // Remove "ROLE_" prefix if present
                .map(ERole::valueOf)
                .collect(Collectors.toSet());
    }

    /**
     * Generates a JWT token for a user.
     *
     * @param userDetails The UserDetails object containing user information
     * @return A JWT token string
     */
    @Override
    public String generate(UserDetails userDetails) {
        var roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
        return generateToken(Map.of("roles", roles), userDetails);
    }

    /**
     * Validates if a token is still valid (not expired).
     *
     * @param token The JWT token string to validate
     * @return true if the token is valid, false otherwise
     */
    @Override
    public boolean isValid(String token) {
        return !isTokenExpired(token);
    }

    /**
     * Extracts the username from a JWT token.
     *
     * @param token The JWT token string
     * @return The username (subject) from the token
     */
    @Override
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Extracts the expiration date from a JWT token.
     *
     * @param token The JWT token string
     * @return The expiration instant
     */
    @Override
    public Instant extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration).toInstant();
    }

    /**
     * Generates a JWT token with additional claims.
     *
     * @param extraClaims Additional claims to include in the token
     * @param userDetails The UserDetails object containing user information
     * @return A JWT token string
     */
    private String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return Jwts.builder()
                .signWith(getSigningKey())
                .claims(extraClaims)
                .subject(userDetails.getUsername())
                .issuedAt(Date.from(Instant.now()))
                .expiration(Date.from(Instant.now().plusMillis(accessTokenDurationMs)))
                .compact();
    }

    /**
     * Checks if a token has expired.
     *
     * @param token The JWT token string to check
     * @return true if the token is expired, false otherwise
     */
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).isBefore(Instant.now());
    }

    /**
     * Extracts a specific claim from a JWT token using a claims resolver function.
     *
     * @param token The JWT token string
     * @param claimsResolver Function to extract a specific claim
     * @param <T> The type of the claim to extract
     * @return The extracted claim value
     */
    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Extracts all claims from a JWT token.
     *
     * @param token The JWT token string
     * @return All claims contained in the token
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * Gets the signing key used to verify JWT tokens.
     *
     * @return The SecretKey object used for signing
     */
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(jwtSigningKey.getBytes(StandardCharsets.UTF_8));
    }
}
