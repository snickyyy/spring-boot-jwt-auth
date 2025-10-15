package sc.snicky.springbootjwtauth.api.v1.exceptions.business.security;

import sc.snicky.springbootjwtauth.api.v1.exceptions.ConflictException;

public class RefreshTokenNotValid extends ConflictException {
    /**
     * Constructs a new RefreshTokenNotValid with the specified detail message.
     *
     * @param message the detail message
     */
    public RefreshTokenNotValid(String message) {
        super(message);
    }
}
