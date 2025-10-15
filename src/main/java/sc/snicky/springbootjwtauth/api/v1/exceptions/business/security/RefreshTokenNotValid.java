package sc.snicky.springbootjwtauth.api.v1.exceptions.business.security;

import sc.snicky.springbootjwtauth.api.v1.exceptions.ConflictException;

public class RefreshTokenNotValid extends ConflictException {
    public RefreshTokenNotValid(String message) {
        super(message);
    }
}
