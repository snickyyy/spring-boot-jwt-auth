package sc.snicky.springbootjwtauth.api.v1.exceptions.business.users;

import sc.snicky.springbootjwtauth.api.v1.exceptions.ConflictException;

public class UserAlreadyExistException extends ConflictException {
    /**
     * Exception thrown when attempting to create a user that already exists.
     * <p>
     *
     * @param message the detail message
     */
    public UserAlreadyExistException(String message) {
        super(message);
    }
}
