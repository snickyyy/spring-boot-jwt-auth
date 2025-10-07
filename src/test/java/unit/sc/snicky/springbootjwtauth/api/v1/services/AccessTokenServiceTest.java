package unit.sc.snicky.springbootjwtauth.api.v1.services;

import io.jsonwebtoken.ExpiredJwtException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.User;
import sc.snicky.springbootjwtauth.api.v1.domain.enums.ERole;
import sc.snicky.springbootjwtauth.api.v1.services.AccessTokenServiceImpl;

/**
 * Unit tests for {@link AccessTokenServiceImpl}.
 * Tests token generation, extraction of roles and username, and token validation.
 */
@Tag("unit")
@ExtendWith(MockitoExtension.class)
public class AccessTokenServiceTest {
    private static final String TEST_USERNAME = "testuser";
    private static final String TEST_PASSWORD = "testpassword";

    private static final Long TEST_ACCESS_TOKEN_DURATION = 3600000L;

    @InjectMocks
    private AccessTokenServiceImpl accessTokenServiceImpl;

    /**
     * Sets up the test environment before each test.
     * Sets the JWT signing key and token duration.
     */
    @BeforeEach
    void setup() {
        accessTokenServiceImpl.setJwtSigningKey("test_jwt_signing_key_which_should_be_replaced");
        accessTokenServiceImpl.setAccessTokenDurationMs(TEST_ACCESS_TOKEN_DURATION); // 1 hour
    }

    /**
     * Builds a default JWT token for testing purposes.
     *
     * @return a valid JWT token string
     */
    private String buildDefaultToken() {
        var user = User.builder()
                .username(TEST_USERNAME)
                .password(TEST_PASSWORD)
                .roles(ERole.USER.name(), ERole.ADMIN.name())
                .build();
        return accessTokenServiceImpl.generate(user);
    }

    /**
     * Tests extraction of roles from a valid JWT token.
     */
    @Test
    void testExtractRoles() {
        var token = buildDefaultToken();

        var roles = accessTokenServiceImpl.extractRoles(token);

        Assertions.assertNotNull(roles);
        Assertions.assertTrue(roles.contains(ERole.USER));
        Assertions.assertTrue(roles.contains(ERole.ADMIN));
        Assertions.assertEquals(2, roles.size());
    }

    /**
     * Tests extraction of username from a valid JWT token.
     */
    @Test
    void testExtractUsername() {
        var token = buildDefaultToken();

        var username = accessTokenServiceImpl.extractUsername(token);

        Assertions.assertNotNull(username);
        Assertions.assertEquals(TEST_USERNAME, username);
    }

    /**
     * Tests validation of a valid JWT token.
     */
    @Test
    void testIsValidValidToken() {
        var token = buildDefaultToken();

        var isValid = accessTokenServiceImpl.isValid(token);

        Assertions.assertTrue(isValid);
    }

    /**
     * Tests validation of an expired JWT token.
     * Expects {@link ExpiredJwtException} to be thrown.
     */
    @Test
    void testIsValidExpiredTokenIsInvalid() {
        accessTokenServiceImpl.setAccessTokenDurationMs(0L); // Set token duration to 0 to force expiration
        var token = buildDefaultToken();

        Assertions.assertThrows(ExpiredJwtException.class, () -> accessTokenServiceImpl.isValid(token));
    }
}
