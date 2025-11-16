package sc.snicky.springbootjwtauth.api.v1.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sc.snicky.springbootjwtauth.api.v1.domain.enums.ERole;
import sc.snicky.springbootjwtauth.api.v1.domain.models.User;
import sc.snicky.springbootjwtauth.api.v1.domain.models.UserDetailsAdaptor;
import sc.snicky.springbootjwtauth.api.v1.dtos.TokenPair;
import sc.snicky.springbootjwtauth.api.v1.exceptions.business.security.PasswordOrEmailIsInvalidException;
import sc.snicky.springbootjwtauth.api.v1.exceptions.business.users.UserAlreadyExistException;
import sc.snicky.springbootjwtauth.api.v1.exceptions.business.users.UserNotFoundException;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;
    private final TokensManager tokensManager;
    private final RefreshTokenService refreshTokenService;
    private final AccessTokenService accessTokenService;

    /**
     * Registers a new user with the provided email and password.
     * If the user already exists, a {@link UserAlreadyExistException} is thrown.
     *
     * @param email    the email of the user to register
     * @param password the password of the user to register
     * @return a {@link TokenPair} containing the access and refresh tokens for the registered user
     * @throws UserAlreadyExistException if a user with the given email already exists
     */
    @Override
    @Transactional
    public TokenPair register(String email, String password) {
        var user = User.builder()
                .email(email)
                .isActive(true) // todo add email verification later
                .password(passwordEncoder.encode(password))
                .build();
        userService.saveUser(user, ERole.USER);
        log.debug("User with email {} registered successfully, user id: {}", email, user.getId());
        return buildTokenPairForUser(user); // todo change on getReferenceById
    }

    /**
     * Authenticates a user with the provided email and password.
     * If the credentials are valid, a {@link TokenPair} is returned.
     * If the credentials are invalid, a {@link PasswordOrEmailIsInvalidException} is thrown.
     *
     * @param email    the email of the user attempting to log in
     * @param password the password of the user attempting to log in
     * @return a {@link TokenPair} containing the access and refresh tokens for the authenticated user
     * @throws PasswordOrEmailIsInvalidException if the email or password is invalid
     */
    @Override
    @Transactional
    public TokenPair login(String email, String password) {
        try {
            var user = userService.getUserByEmail(email);
            if (!passwordEncoder.matches(password, user.getPassword())) {
                log.debug("Invalid password for user with email {}", email);
                throw new PasswordOrEmailIsInvalidException("Password or email is invalid");
            }
            log.debug("User with email {} logged in successfully", email);
            return buildTokenPairForUser(user);
        } catch (UserNotFoundException e) {
            log.debug("Attempt to login with non-existent email {}", email);
            throw new PasswordOrEmailIsInvalidException("Password or email is invalid");
        }
    }

    /**
     * Refreshes the access and refresh tokens using the provided refresh token.
     *
     * @param refreshToken the refresh token used to generate new tokens
     * @return a {@link TokenPair} containing the new access and refresh tokens
     */
    @Override
    public TokenPair refreshTokens(String refreshToken) {
        return tokensManager.refreshTokens(refreshToken);
    }

    /**
     * Logs out the user by revoking the provided refresh token.
     *
     * @param refreshToken the refresh token to be revoked
     */
    @Override
    public void logout(String refreshToken) {
        refreshTokenService.revoke(refreshToken);
    }

    private TokenPair buildTokenPairForUser(User user) {
        var refreshToken = refreshTokenService.generate(user.getId());
        var accessToken = accessTokenService.generate(UserDetailsAdaptor.ofUser(refreshToken.getUser()));
        return TokensManagerImpl.buildTokenPair(accessToken, refreshToken);
    }
}
