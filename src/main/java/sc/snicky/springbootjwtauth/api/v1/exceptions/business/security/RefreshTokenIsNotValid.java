package sc.snicky.springbootjwtauth.api.v1.exceptions.business.security;

import sc.snicky.springbootjwtauth.api.v1.exceptions.ConflictException;

public class RefreshTokenIsNotValid extends ConflictException {
    /**
     * Constructs a new RefreshTokenNotValid with the specified detail message.
     *
     * @param message the detail message
     */
    public RefreshTokenIsNotValid(String message) {
        super(message);
    }
}
