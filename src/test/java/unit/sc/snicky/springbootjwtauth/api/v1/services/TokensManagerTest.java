package unit.sc.snicky.springbootjwtauth.api.v1.services;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import sc.snicky.springbootjwtauth.api.v1.domain.enums.ERole;
import sc.snicky.springbootjwtauth.api.v1.domain.models.BasicRefreshToken;
import sc.snicky.springbootjwtauth.api.v1.domain.models.JpaRefreshTokenDetailsAdaptor;
import sc.snicky.springbootjwtauth.api.v1.domain.models.Role;
import sc.snicky.springbootjwtauth.api.v1.domain.models.User;
import sc.snicky.springbootjwtauth.api.v1.services.AccessTokenServiceImpl;
import sc.snicky.springbootjwtauth.api.v1.services.RefreshTokenServiceImpl;
import sc.snicky.springbootjwtauth.api.v1.services.TokensManagerImpl;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Tag("unit")
@ExtendWith(MockitoExtension.class)
public class TokensManagerTest {
    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private static final String TEST_EMAIL = "testuser@test.te";
    private static final String TEST_PASSWORD = "testpassword";
    private static final Long TEST_REFRESH_TOKEN_DURATION = 9000000L;
    private static final UUID TEST_TOKEN = UUID.randomUUID();
    private static final String TEST_HASHED_TOKEN = passwordEncoder.encode(TEST_TOKEN.toString());

    @Mock
    private RefreshTokenServiceImpl refreshTokenService;

    @Mock
    private AccessTokenServiceImpl accessTokenService;

    @InjectMocks
    private TokensManagerImpl tokensManager;


    @Test
    void generateTokensWithSuccess() {
        var user = buildUser();
        user.setId(1);
        var token = buildToken(user);
        when(refreshTokenService.generate(1)).thenReturn(JpaRefreshTokenDetailsAdaptor.ofToken(TEST_TOKEN, token));

        var result = tokensManager.generateTokens(1);

        assertNotNull(result);
        assertTrue(passwordEncoder.matches(result.refreshToken(), token.getToken()));

        verify(refreshTokenService).generate(1);
    }

    @Test
    void refreshTokensWithSuccess() {
        var user = buildUser();
        user.setId(1);
        var newToken = buildToken(user);
        when(refreshTokenService.rotate(TEST_TOKEN)).thenReturn(JpaRefreshTokenDetailsAdaptor.ofToken(TEST_TOKEN, newToken));

        var result = tokensManager.refreshTokens(TEST_TOKEN.toString());

        assertNotNull(result);
        assertTrue(passwordEncoder.matches(result.refreshToken(), newToken.getToken()));

        verify(refreshTokenService).rotate(TEST_TOKEN);
    }

    private User buildUser() {
        var user = User.builder()
                .email(TEST_EMAIL)
                .password(TEST_PASSWORD)
                .build();
        user.assignRole(Role.builder().name(ERole.USER).build());
        return user;
    }

    private BasicRefreshToken buildToken(User user) {
        return BasicRefreshToken.builder()
                .token(TEST_HASHED_TOKEN)
                .user(user)
                .expiresAt(Instant.now().plusMillis(TEST_REFRESH_TOKEN_DURATION))
                .build();
    }
}
