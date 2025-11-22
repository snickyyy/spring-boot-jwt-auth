package sc.snicky.springbootjwtauth.api.v1.exceptions;

public abstract class InternalException extends RuntimeException {
    /**
     * Constructs a new InternalException with the specified detail message.
     *
     * @param message the detail message
     */
    public InternalException(String message) {
        super(message);
    }
}
