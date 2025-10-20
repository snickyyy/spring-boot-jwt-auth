package sc.snicky.springbootjwtauth.api.v1.exceptions.business.security;

import sc.snicky.springbootjwtauth.api.v1.exceptions.UnauthorizedException;

public class InvalidAccessTokenException extends UnauthorizedException {
    public InvalidAccessTokenException(String message) {
        super(message);
    }
}
