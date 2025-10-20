package sc.snicky.springbootjwtauth.api.v1.exceptions.business.users;

import sc.snicky.springbootjwtauth.api.v1.exceptions.NotFoundException;

public class UserNotFoundException extends NotFoundException {
    /**
     * Constructs a new UserNotFoundException with the specified detail message.
     *
     * @param message the detail message
     */
    public UserNotFoundException(String message) {
        super(message);
    }
}
