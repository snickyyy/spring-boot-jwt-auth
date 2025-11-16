package sc.snicky.springbootjwtauth.api.v1.exceptions.business.security;

import sc.snicky.springbootjwtauth.api.v1.exceptions.UnauthorizedException;

public class DefaultUnauthorizedException extends UnauthorizedException {
    /**
     * Constructs a new DefaultUnauthorizedException with the specified detail message.
     *
     * @param message the detail message explaining the reason for the exception
     */
    public DefaultUnauthorizedException(String message) {
        super(message);
    }
}
