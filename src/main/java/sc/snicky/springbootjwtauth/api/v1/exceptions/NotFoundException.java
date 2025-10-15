package sc.snicky.springbootjwtauth.api.v1.exceptions;

public abstract class NotFoundException extends RuntimeException {
    /**
     * Constructor for NotFoundException.
     *
     * @param message error message
     */
    public NotFoundException(String message) {
        super(message);
    }
}
