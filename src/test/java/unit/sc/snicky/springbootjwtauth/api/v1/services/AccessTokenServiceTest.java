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

@Tag("unit")
@ExtendWith(MockitoExtension.class)
public class AccessTokenServiceTest {
    private final static String TEST_USERNAME = "testuser";
    private final static String TEST_PASSWORD = "testpassword";

    @InjectMocks
    private AccessTokenServiceImpl accessTokenServiceImpl;

    @BeforeEach
    void setup() {
        accessTokenServiceImpl.setJwtSigningKey("test_jwt_signing_key_which_should_be_replaced");
        accessTokenServiceImpl.setAccessTokenDurationMs(3600000L); // 1 hour
    }

    private String buildDefaultToken() {
        var user = User.builder()
                .username(TEST_USERNAME)
                .password(TEST_PASSWORD)
                .roles(ERole.USER.name(), ERole.ADMIN.name())
                .build();
        return accessTokenServiceImpl.generate(user);
    }

    @Test
    void testExtractRoles() {
        var token = buildDefaultToken();

        var roles = accessTokenServiceImpl.extractRoles(token);

        Assertions.assertNotNull(roles);
        Assertions.assertTrue(roles.contains(ERole.USER));
        Assertions.assertTrue(roles.contains(ERole.ADMIN));
        Assertions.assertEquals(2, roles.size());
    }

    @Test
    void testExtractUsername() {
        var token = buildDefaultToken();

        var username = accessTokenServiceImpl.extractUsername(token);

        Assertions.assertNotNull(username);
        Assertions.assertEquals(TEST_USERNAME, username);
    }

    @Test
    void testIsValid_ValidToken() {
        var token = buildDefaultToken();

        var isValid = accessTokenServiceImpl.isValid(token);

        Assertions.assertTrue(isValid);
    }

    @Test
    void testIsValid_ExpOfTokenIsInvalid() {
        accessTokenServiceImpl.setAccessTokenDurationMs(0L); // Set token duration to 0 to force expiration
        var token = buildDefaultToken();

        Assertions.assertThrows(ExpiredJwtException.class, () -> accessTokenServiceImpl.isValid(token));

    }

}
