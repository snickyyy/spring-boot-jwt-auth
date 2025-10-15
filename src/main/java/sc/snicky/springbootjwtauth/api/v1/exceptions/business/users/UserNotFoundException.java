package sc.snicky.springbootjwtauth.api.v1.exceptions.business.users;

import sc.snicky.springbootjwtauth.api.v1.exceptions.NotFoundException;

public class UserNotFoundException extends NotFoundException {
    public UserNotFoundException(String message) {
        super(message);
    }
}
