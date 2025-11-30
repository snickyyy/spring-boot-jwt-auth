package sc.snicky.springbootjwtauth.api.v1.exceptions.business.security;

import sc.snicky.springbootjwtauth.api.v1.exceptions.ConflictException;

public class VerificationCodeIsInvalidException extends ConflictException {
    /**
     * Constructs a new VerificationCodeIsInvalidException with the specified detail message.
     *
     * @param message the detail message explaining the reason for the exception
     */
    public VerificationCodeIsInvalidException(String message) {
        super(message);
    }
}
