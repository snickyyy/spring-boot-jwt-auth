package unit.sc.snicky.springbootjwtauth.api.v1.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import sc.snicky.springbootjwtauth.api.v1.domain.enums.ERole;
import sc.snicky.springbootjwtauth.api.v1.domain.models.BasicRefreshToken;
import sc.snicky.springbootjwtauth.api.v1.domain.models.RefreshTokenDetails;
import sc.snicky.springbootjwtauth.api.v1.domain.models.Role;
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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Tag("unit")
@ExtendWith(MockitoExtension.class)
public class RefreshTokenServiceTest {
    private static final String TEST_EMAIL = "testuser@test.te";
    private static final String TEST_PASSWORD = "testpassword";
    private final Long TEST_REFRESH_TOKEN_DURATION = 9000000L;
    private final UUID TEST_TOKEN = UUID.randomUUID();


    @Spy
    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private final String TEST_HASHED_TOKEN = passwordEncoder.encode(TEST_TOKEN.toString());

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
        var token = buildToken(buildUser());
        when(basicRefreshTokenRepository.findByToken(any())).thenReturn(Optional.of(token));

        var result = refreshTokenServiceTest.isValid(TEST_TOKEN);

        assertTrue(result);
    }

    @Test
    void testIsValidWithTokenNotFound() {
        when(basicRefreshTokenRepository.findByToken(any())).thenReturn(Optional.empty());

        var result = refreshTokenServiceTest.isValid(TEST_TOKEN);

        assertFalse(result);
    }

    @Test
    void testIsValidWithTokenIsExpired() {
        var token = buildToken(buildUser());
        token.setExpiresAt(Instant.now().minusSeconds(1));
        when(basicRefreshTokenRepository.findByToken(any())).thenReturn(Optional.of(token));

        var result = refreshTokenServiceTest.isValid(TEST_TOKEN);

        assertFalse(result);
    }

    @Test
    void testRotateWithSuccess() {
        var testUser = buildUser();
        var oldToken = buildToken(testUser);
        when(basicRefreshTokenRepository.findByToken(any())).thenReturn(Optional.of(oldToken));
        doNothing().when(basicRefreshTokenRepository).delete(any());
        doNothing().when(basicRefreshTokenRepository).save(any());

        RefreshTokenDetails result = refreshTokenServiceTest.rotate(TEST_TOKEN);

        assertNotNull(result);
        assertFalse(passwordEncoder.matches(result.getToken().toString(), TEST_HASHED_TOKEN));
        assertEquals(testUser.getEmail(), result.getUser().getEmail());
        assertEquals(oldToken.getExpiresAt(), result.getExpiry());

        verify(basicRefreshTokenRepository).delete(any());
    }

    @Test
    void testRotateWithRefreshTokenNotValidException() {
        when(basicRefreshTokenRepository.findByToken(any())).thenReturn(Optional.empty());

        assertThrows(
                InvalidRefreshTokenException.class,
                () -> refreshTokenServiceTest.rotate(TEST_TOKEN)
        );
    }

    @Test
    void testRotateWithRefreshTokenIsExpired() {
        var token = buildToken(buildUser());
        token.setExpiresAt(Instant.now().minusSeconds(1));
        when(basicRefreshTokenRepository.findByToken(any())).thenReturn(Optional.of(token));

        assertThrows(
                InvalidRefreshTokenException.class,
                () -> refreshTokenServiceTest.rotate(TEST_TOKEN)
        );
    }

    @Test
    void testRevokeTokenWithSuccess() {
        doNothing().when(basicRefreshTokenRepository).delete(any());

        refreshTokenServiceTest.revoke(TEST_TOKEN);

        verify(basicRefreshTokenRepository).delete(any());
    }

    @Test
    void testFindByTokenWithSuccess() {

        var testToken = buildToken(buildUser());
        when(basicRefreshTokenRepository.findByToken(anyString()))
                .thenReturn(Optional.of(testToken));

        Optional<RefreshTokenDetails> result = refreshTokenServiceTest.findByToken(TEST_TOKEN);

        assertTrue(result.isPresent());
        assertEquals(testToken.getUser().getEmail(), result.get().getUser().getEmail());

        verify(basicRefreshTokenRepository).findByToken(anyString());
    }

    private BasicRefreshToken buildToken(User user) {
        return BasicRefreshToken.builder()
                .token(TEST_HASHED_TOKEN)
                .user(user)
                .expiresAt(Instant.now().plusMillis(TEST_REFRESH_TOKEN_DURATION))
                .build();
    }

    private User buildUser() {
        var user = User.builder()
                .email(TEST_EMAIL)
                .password(TEST_PASSWORD)
                .build();
        user.assignRole(Role.builder().name(ERole.USER).build());
        return user;
    }
}
