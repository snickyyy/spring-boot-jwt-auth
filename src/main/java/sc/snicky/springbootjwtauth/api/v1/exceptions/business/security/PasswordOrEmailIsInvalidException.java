package sc.snicky.springbootjwtauth.api.v1.exceptions.business.security;

import sc.snicky.springbootjwtauth.api.v1.exceptions.UnauthorizedException;

public class PasswordOrEmailIsInvalidException extends UnauthorizedException {
    /**
     * Constructs a new PasswordOrEmailIsInvalidException exception with the specified detail message.
     *
     * @param message the detail message
     */
    public PasswordOrEmailIsInvalidException(String message) {
        super(message);
    }
}
