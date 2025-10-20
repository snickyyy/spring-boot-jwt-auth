package sc.snicky.springbootjwtauth.api.v1.exceptions;

public abstract class UnauthorizedException extends RuntimeException {
    public UnauthorizedException(String message) {
        super(message);
    }
}
