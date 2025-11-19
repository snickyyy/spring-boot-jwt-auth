package sc.snicky.springbootjwtauth.api.v1.exceptions.internal;

import sc.snicky.springbootjwtauth.api.v1.exceptions.InternalException;

public class InvalidTokenTtlException extends InternalException {
    /**
     * Exception thrown when the token's time-to-live is invalid.
     *
     * @param message the error message
     */
    public InvalidTokenTtlException(String message) {
        super(message);
    }
}
