package sc.snicky.springbootjwtauth.api.v1.exceptions.business.security;

import sc.snicky.springbootjwtauth.api.v1.exceptions.UnauthorizedException;

public class InvalidRefreshTokenException extends UnauthorizedException {
    /**
     * Constructs a new RefreshTokenNotValid with the specified detail message.
     *
     * @param message the detail message
     */
    public InvalidRefreshTokenException(String message) {
        super(message);
    }
}
