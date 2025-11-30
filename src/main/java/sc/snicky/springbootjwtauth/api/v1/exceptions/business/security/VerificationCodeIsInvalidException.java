package sc.snicky.springbootjwtauth.api.v1.exceptions.business.security;

import sc.snicky.springbootjwtauth.api.v1.exceptions.ConflictException;

public class VerificationCodeIsInvalidException extends ConflictException {
    public VerificationCodeIsInvalidException(String message) {
        super(message);
    }
}
