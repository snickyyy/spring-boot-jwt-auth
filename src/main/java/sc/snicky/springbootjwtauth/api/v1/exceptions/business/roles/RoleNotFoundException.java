package sc.snicky.springbootjwtauth.api.v1.exceptions.business.roles;

import sc.snicky.springbootjwtauth.api.v1.exceptions.NotFoundException;

public class RoleNotFoundException extends NotFoundException {
    /**
     * Constructs a new RoleNotFoundException with the specified detail message.
     *
     * @param message the detail message
     */
    public RoleNotFoundException(String message) {
        super(message);
    }
}
