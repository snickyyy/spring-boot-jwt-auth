package sc.snicky.springbootjwtauth.api.v1.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import sc.snicky.springbootjwtauth.api.v1.domain.enums.ERole;
import sc.snicky.springbootjwtauth.api.v1.domain.models.BasicRefreshToken;
import sc.snicky.springbootjwtauth.api.v1.domain.models.RefreshTokenDetails;
import sc.snicky.springbootjwtauth.api.v1.domain.models.RefreshTokenDetailsAdaptor;
import sc.snicky.springbootjwtauth.api.v1.domain.models.Role;
import sc.snicky.springbootjwtauth.api.v1.domain.models.User;
import sc.snicky.springbootjwtauth.api.v1.domain.types.NonProtectedToken;
import sc.snicky.springbootjwtauth.api.v1.domain.types.ProtectedToken;
import sc.snicky.springbootjwtauth.api.v1.events.UserRegisteredEvent;
import sc.snicky.springbootjwtauth.api.v1.exceptions.business.security.PasswordOrEmailIsInvalidException;
import sc.snicky.springbootjwtauth.api.v1.exceptions.business.users.UserAlreadyExistException;
import sc.snicky.springbootjwtauth.api.v1.exceptions.business.users.UserNotFoundException;
import sc.snicky.springbootjwtauth.api.v1.repositories.utils.RedisKeyUtils;
import sc.snicky.springbootjwtauth.api.v1.services.utils.TokenUtils;
import sc.snicky.springbootjwtauth.api.v1.services.validators.UserAuthValidator;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Tag("unit")
@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {
    private static final String TEST_EMAIL = "testuser@test.te";
    private static final String TEST_PASSWORD = "testpassword";
    private static final Long TEST_ACCESS_TOKEN_DURATION = 3600000L;
    private final Long TEST_REFRESH_TOKEN_DURATION = 9000000L;
    private final String TEST_NON_PROTECTED_TOKEN = TokenUtils.generateToken();
    private final ProtectedToken TEST_PROTECTED_TOKEN = new ProtectedToken(TokenUtils.hashToken(TEST_NON_PROTECTED_TOKEN));

    @Spy
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    @Spy
    private final RedisKeyUtils redisKeyUtils = new RedisKeyUtils();
    @Mock
    private UserService userService;
    @Mock
    private RedisTemplate<String, Object> redisTemplate;
    @Mock
    private ValueOperations<String, Object> valueOperations;
    @Mock
    private TokensManagerImpl tokensManager;
    @Mock
    private ApplicationEventPublisher publisher;
    @Mock
    private RefreshTokenService refreshTokenService;
    @Mock
    private UserAuthValidator userAuthValidator;
    @Spy
    private AccessTokenServiceImpl accessTokenService = new AccessTokenServiceImpl();
    @InjectMocks
    private AuthServiceImpl authService;

    /**
     * Sets up the test environment before each test.
     * Sets the JWT signing key and token duration.
     */
    @BeforeEach
    void setUp() {
        accessTokenService.setJwtSigningKey("test_jwt_signing_key_which_should_be_replaced");
        accessTokenService.setAccessTokenDurationMs(TEST_ACCESS_TOKEN_DURATION); // 1 hour

        authService.setEmailVerificationDurationMs(900000L);
        authService.setRedisEmailConfirmCodeKeyPrefix("email_verification");

        redisKeyUtils.setRedisKeyDivider(":");
    }

    @Test
    void testRegisterWithSuccess() {
        doNothing().when(userService).saveUser(any(), any(ERole.class));
        doNothing().when(publisher).publishEvent(any(UserRegisteredEvent.class));

        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        doNothing().when(valueOperations).set(any(), any(), any());

        authService.register(TEST_EMAIL, TEST_PASSWORD);

        verify(userService).saveUser(any(), any(ERole.class));
        verify(publisher).publishEvent(any(UserRegisteredEvent.class));
        verify(valueOperations).set(any(), any(), any());
    }

    @Test
    void testRegisterUserWithAlreadyExistingEmail() {
        doThrow(UserAlreadyExistException.class)
                .when(userService).saveUser(any(), any(ERole.class));

        assertThrows(UserAlreadyExistException.class,
                () -> authService.register(TEST_EMAIL, TEST_PASSWORD));
        verify(userService).saveUser(any(), any(ERole.class));
    }

    @Test
    void testLoginWithSuccess() {
        var user = buildUser();
        when(userService.getActiveUserByEmail(TEST_EMAIL)).thenReturn(user);
        var token = buildToken(user);
        when(refreshTokenService.generate(user.getId())).thenReturn(token);

        var tokenPair = authService.login(TEST_EMAIL, TEST_PASSWORD);

        assertNotNull(tokenPair);
        assertNotNull(tokenPair.accessToken());
        assertNotNull(tokenPair.refreshToken());
        assertDoesNotThrow(() -> accessTokenService.extractUserDetails(tokenPair.accessToken()));
        assertEquals(user.getEmail(), accessTokenService.extractUserDetails(tokenPair.accessToken()).getUsername());

        verify(userService).getActiveUserByEmail(TEST_EMAIL);
        verify(refreshTokenService).generate(user.getId());
    }

    @Test
    void testLoginWithInvalidPassword() {
        var user = buildUser();
        when(userService.getActiveUserByEmail(TEST_EMAIL)).thenReturn(user);
        doThrow(PasswordOrEmailIsInvalidException.class).when(userAuthValidator)
                .validateCredentials(user, "wrongpassword");

        assertThrows(PasswordOrEmailIsInvalidException.class,
                () -> authService.login(TEST_EMAIL, "wrongpassword"));

        verify(userService).getActiveUserByEmail(TEST_EMAIL);
        verify(userAuthValidator).validateCredentials(user, "wrongpassword");
    }

    @Test
    void testLoginWithInvalidEmail() {
        when(userService.getActiveUserByEmail(TEST_EMAIL))
                .thenThrow(UserNotFoundException.class);

        assertThrows(PasswordOrEmailIsInvalidException.class,
                () -> authService.login(TEST_EMAIL, TEST_PASSWORD));

        verify(userService).getActiveUserByEmail(TEST_EMAIL);
    }

    private RefreshTokenDetails buildToken(User user) {
        return RefreshTokenDetailsAdaptor.ofToken(
                new NonProtectedToken(TEST_NON_PROTECTED_TOKEN),
                BasicRefreshToken.builder()
                        .token(TEST_PROTECTED_TOKEN)
                        .user(user)
                        .isActive(true)
                        .expiresAt(Instant.now().plusMillis(TEST_REFRESH_TOKEN_DURATION))
                        .build());
    }

    private User buildUser() {
        var user = User.builder()
                .email(TEST_EMAIL)
                .password(passwordEncoder.encode(TEST_PASSWORD))
                .build();
        user.assignRole(Role.builder().name(ERole.USER).build());
        user.setId(1);
        return user;
    }
}
