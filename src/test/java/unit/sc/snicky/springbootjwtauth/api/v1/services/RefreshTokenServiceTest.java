package unit.sc.snicky.springbootjwtauth.api.v1.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import sc.snicky.springbootjwtauth.api.v1.domain.enums.ERole;
import sc.snicky.springbootjwtauth.api.v1.domain.models.PostgresTokenAdaptor;
import sc.snicky.springbootjwtauth.api.v1.domain.models.RefreshTokenDetails;
import sc.snicky.springbootjwtauth.api.v1.domain.models.Role;
import sc.snicky.springbootjwtauth.api.v1.domain.models.Token;
import sc.snicky.springbootjwtauth.api.v1.domain.models.User;
import sc.snicky.springbootjwtauth.api.v1.exceptions.business.security.InvalidRefreshTokenException;
import sc.snicky.springbootjwtauth.api.v1.exceptions.business.users.UserNotFoundException;
import sc.snicky.springbootjwtauth.api.v1.repositories.BasicRefreshTokenRepository;
import sc.snicky.springbootjwtauth.api.v1.repositories.JpaUserRepository;
import sc.snicky.springbootjwtauth.api.v1.services.RefreshTokenServiceImpl;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Tag("unit")
@ExtendWith(MockitoExtension.class)
public class RefreshTokenServiceTest {
    private static final String TEST_EMAIL = "testuser@test.te";
    private static final String TEST_PASSWORD = "testpassword";
    private static final Long TEST_REFRESH_TOKEN_DURATION = 9000000L;

    @Mock
    private BasicRefreshTokenRepository basicRefreshTokenRepository;

    @Mock
    private JpaUserRepository jpaUserRepository;

    @InjectMocks
    private RefreshTokenServiceImpl refreshTokenServiceTest;

    /**
     * Executed before each test.
     * <p>
     * Sets the refresh token duration in milliseconds
     * for the tested service.
     */
    @BeforeEach
    void setup() {
        refreshTokenServiceTest.setRefreshTokenDurationMs(TEST_REFRESH_TOKEN_DURATION);
    }

    @Test
    void testGenerateRefreshTokenWithSuccess() {
        var userId = 1;
        var testUser = buildUser();
        testUser.setId(userId);
        when(jpaUserRepository.findById(userId)).thenReturn(Optional.of(testUser));
        doNothing().when(basicRefreshTokenRepository).save(any());

        RefreshTokenDetails result = refreshTokenServiceTest.generate(userId);

        assertNotNull(result);
        assertEquals(testUser.getId(), result.getUser().getId());

        verify(basicRefreshTokenRepository).save(any());
    }

    @Test
    void testGenerateRefreshTokenWithUserNotFoundException() {
        var userId = 1;
        var testUser = buildUser();
        testUser.setId(userId);
        when(jpaUserRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> refreshTokenServiceTest.generate(userId));
    }

    @Test
    void testIsValidWithSuccess() {
        var tokenId = UUID.randomUUID();
        var token = PostgresTokenAdaptor.ofToken(buildToken(
                User.builder()
                        .email(TEST_EMAIL)
                        .password(TEST_PASSWORD)
                        .role(Role.builder().name(ERole.USER).build())
                        .build()
        ));
        when(basicRefreshTokenRepository.findByToken(tokenId)).thenReturn(Optional.of(token));

        var result = refreshTokenServiceTest.isValid(tokenId);

        assertTrue(result);
    }

    @Test
    void testIsValidWithTokenNotFound() {
        var tokenId = UUID.randomUUID();
        when(basicRefreshTokenRepository.findByToken(tokenId)).thenReturn(Optional.empty());

        var result = refreshTokenServiceTest.isValid(tokenId);

        assertFalse(result);
    }

    @Test
    void testIsValidWithTokenIsExpired() {
        var tokenId = UUID.randomUUID();
        var token = PostgresTokenAdaptor.ofToken(buildToken(
                User.builder()
                        .email(TEST_EMAIL)
                        .password(TEST_PASSWORD)
                        .role(Role.builder().name(ERole.USER).build())
                        .build()
        ));
        token.setExpiry(Instant.now().minusSeconds(1));
        when(basicRefreshTokenRepository.findByToken(tokenId)).thenReturn(Optional.of(token));

        var result = refreshTokenServiceTest.isValid(tokenId);

        assertFalse(result);
    }

    @Test
    void testRotateWithSuccess() {
        var oldTokenId = UUID.randomUUID();
        var testUser = buildUser();
        var oldToken = PostgresTokenAdaptor.ofToken(buildToken(testUser));
        when(basicRefreshTokenRepository.findByToken(oldTokenId)).thenReturn(Optional.of(oldToken));
        doNothing().when(basicRefreshTokenRepository).delete(oldTokenId);
        doNothing().when(basicRefreshTokenRepository).save(any());

        RefreshTokenDetails result = refreshTokenServiceTest.rotate(oldTokenId);

        assertNotNull(result);
        assertNotEquals(oldToken.getToken(), result.getToken());
        assertEquals(testUser.getEmail(), result.getUser().getEmail());
        assertEquals(oldToken.getExpiry(), result.getExpiry());

        verify(basicRefreshTokenRepository).delete(oldTokenId);
        verify(basicRefreshTokenRepository).save(result);
    }

    @Test
    void testRotateWithRefreshTokenNotValidException() {
        var oldTokenId = UUID.randomUUID();
        when(basicRefreshTokenRepository.findByToken(oldTokenId)).thenReturn(Optional.empty());

        assertThrows(
                InvalidRefreshTokenException.class,
                () -> refreshTokenServiceTest.rotate(oldTokenId)
        );
    }

    @Test
    void testRevokeTokenWithSuccess() {
        var tokenId = UUID.randomUUID();
        doNothing().when(basicRefreshTokenRepository).delete(tokenId);

        refreshTokenServiceTest.revoke(tokenId);

        verify(basicRefreshTokenRepository).delete(tokenId);
    }

    @Test
    void testFindByTokenWithSuccess() {
        var tokenId = UUID.randomUUID();
        var testToken = PostgresTokenAdaptor.ofToken(buildToken(
                User.builder()
                        .email(TEST_EMAIL)
                        .password(TEST_PASSWORD)
                        .role(Role.builder().name(ERole.USER).build())
                        .build()
        ));
        when(basicRefreshTokenRepository.findByToken(tokenId)).thenReturn(Optional.of(testToken));

        Optional<RefreshTokenDetails> result = refreshTokenServiceTest.findByToken(tokenId);

        assertTrue(result.isPresent());
        assertEquals(testToken.getToken(), result.get().getToken());
        assertEquals(testToken.getUser().getEmail(), result.get().getUser().getEmail());

        verify(basicRefreshTokenRepository).findByToken(tokenId);
    }

    private Token buildToken(User user) {
        return Token.builder()
                .id(UUID.randomUUID())
                .user(user)
                .exp(Instant.now().plusMillis(TEST_REFRESH_TOKEN_DURATION))
                .build();
    }

    private User buildUser() {
        return User.builder()
                .email(TEST_EMAIL)
                .password(TEST_PASSWORD)
                .role(Role.builder().name(ERole.USER).build())
                .build();
    }
}
