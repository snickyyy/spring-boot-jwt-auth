package sc.snicky.springbootjwtauth.api.v1.domain.enums;

public enum ERole {
    /**
     * Represents an anonymous user with limited access.
     */
    ANONYMOUS,

    /**
     * Represents a regular authenticated user.
     */
    USER,

    /**
     * Represents an administrator with full access.
     */
    ADMIN
}
