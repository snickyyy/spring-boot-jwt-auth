package sc.snicky.springbootjwtauth.api.v1.exceptions;

public abstract class ConflictException extends RuntimeException {
    /**
     * Constructs a new ConflictException with the specified detail message.
     *
     * @param message the detail message
     */
    public ConflictException(String message) {
        super(message);
    }
}
