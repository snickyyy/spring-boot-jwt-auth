package sc.snicky.springbootjwtauth.api.v1.exceptions.business.security;

import sc.snicky.springbootjwtauth.api.v1.exceptions.UnauthorizedException;

public class InvalidAccessTokenException extends UnauthorizedException {
    /**
     * Constructs a new InvalidAccessTokenException with the specified detail message.
     *
     * @param message the detail message explaining why the access token is invalid
     */
    public InvalidAccessTokenException(String message) {
        super(message);
    }
}
