package sc.snicky.springbootjwtauth.api.v1.exceptions;

public abstract class UnauthorizedException extends RuntimeException {
    /**
     * Constructs a new UnauthorizedException with the specified detail message.
     *
     * @param message the detail message
     */
    public UnauthorizedException(String message) {
        super(message);
    }
}
