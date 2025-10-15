package sc.snicky.springbootjwtauth.api.v1.domain.models;

import java.time.Instant;
import java.util.UUID;

/**
 * Represents the details of a refresh token used for authentication.
 * This interface provides access to token metadata and ownership information.
 */
public interface RefreshTokenDetails {
    /**
     * Returns the unique identifier of the refresh token.
     * This is the actual token value that clients use for refresh requests.
     *
     * @return the UUID representing the token value, never {@code null}
     */
    UUID getToken();

    /**
     * Returns the unique identifier of the user who owns this refresh token.
     * This links the token to a specific user in the system.
     *
     * @return the user ID as an Integer, never {@code null}
     */
    Integer getUserId();

    /**
     * Returns the expiration timestamp of the refresh token.
     * After this time, the token becomes invalid and cannot be used.
     *
     * @return the expiration Instant, never {@code null}
     */
    Instant getExpiry();

    /**
     * Returns the creation timestamp of the refresh token.
     * This indicates when the token was originally issued.
     *
     * @return the creation Instant, never {@code null}
     */
    Instant getCreatedAt();
}
