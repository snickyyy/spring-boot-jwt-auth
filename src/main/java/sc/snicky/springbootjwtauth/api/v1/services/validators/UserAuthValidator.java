package sc.snicky.springbootjwtauth.api.v1.services.validators;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import sc.snicky.springbootjwtauth.api.v1.domain.models.User;
import sc.snicky.springbootjwtauth.api.v1.exceptions.business.security.PasswordOrEmailIsInvalidException;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserAuthValidator {
    private static final String ERROR_MSG = "Password or email is invalid";
    private final PasswordEncoder passwordEncoder;

    /**
     * Validates the credentials of a user by checking the provided raw password
     * against the stored encoded password and verifying if the user is active.
     *
     * @param user        the user whose credentials are being validated
     * @param rawPassword the raw password provided for validation
     * @throws PasswordOrEmailIsInvalidException if the password does not match
     *                                           or the user is not active
     */
    public void validateCredentials(User user, String rawPassword) {
        if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
            log.debug("Invalid password for user with id {}", user.getId());
            throw new PasswordOrEmailIsInvalidException(ERROR_MSG);
        }

        if (!user.getIsActive()) {
            log.debug("Attempt to login with inactive account, user id {}", user.getId());
            throw new PasswordOrEmailIsInvalidException(ERROR_MSG);
        }
    }
}
