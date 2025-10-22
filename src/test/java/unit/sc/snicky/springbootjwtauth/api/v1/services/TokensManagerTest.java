package unit.sc.snicky.springbootjwtauth.api.v1.services;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import sc.snicky.springbootjwtauth.api.v1.domain.enums.ERole;
import sc.snicky.springbootjwtauth.api.v1.domain.models.PostgresTokenAdaptor;
import sc.snicky.springbootjwtauth.api.v1.domain.models.Role;
import sc.snicky.springbootjwtauth.api.v1.domain.models.Token;
import sc.snicky.springbootjwtauth.api.v1.domain.models.User;
import sc.snicky.springbootjwtauth.api.v1.services.AccessTokenServiceImpl;
import sc.snicky.springbootjwtauth.api.v1.services.RefreshTokenServiceImpl;
import sc.snicky.springbootjwtauth.api.v1.services.TokensManagerImpl;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Tag("unit")
@ExtendWith(MockitoExtension.class)
public class TokensManagerTest {
    private static final String TEST_EMAIL = "testuser@test.te";
    private static final String TEST_PASSWORD = "testpassword";
    private static final Long TEST_REFRESH_TOKEN_DURATION = 9000000L;

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
        var token = PostgresTokenAdaptor.ofToken(buildToken(user));
        when(refreshTokenService.generate(1)).thenReturn(token);

        var result = tokensManager.generateTokens(1);

        assertNotNull(result);
        assertEquals(token.getToken().toString(), result.refreshToken());

        verify(refreshTokenService).generate(1);
    }

    @Test
    void refreshTokensWithSuccess() {
        var user = buildUser();
        user.setId(1);
        var oldToken = buildToken(user);
        var newToken = PostgresTokenAdaptor.ofToken(buildToken(user));
        when(refreshTokenService.rotate(oldToken.getId())).thenReturn(newToken);

        var result = tokensManager.refreshTokens(oldToken.getId().toString());

        assertNotNull(result);
        assertEquals(newToken.getToken().toString(), result.refreshToken());

        verify(refreshTokenService).rotate(oldToken.getId());
    }

    @Test
    void revoke

    private User buildUser() {
        var user = User.builder()
                .email(TEST_EMAIL)
                .password(TEST_PASSWORD)
                .build();
        user.assignRole(Role.builder().name(ERole.USER).build());
        return user;
    }

    private Token buildToken(User user) {
        return Token.builder()
                .id(UUID.randomUUID())
                .user(user)
                .exp(Instant.now().plusMillis(TEST_REFRESH_TOKEN_DURATION))
                .build();
    }
}
