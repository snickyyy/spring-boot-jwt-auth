package sc.snicky.springbootjwtauth.api.v1.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sc.snicky.springbootjwtauth.api.v1.domain.models.JpaRefreshToken;
import sc.snicky.springbootjwtauth.api.v1.domain.types.ProtectedToken;

import java.util.Optional;

@Repository
public interface JpaRefreshTokenRepository extends JpaRepository<JpaRefreshToken, ProtectedToken> {
    /**
     * Finds a refresh token by its value.
     *
     * @param refreshToken the refresh token value
     * @return an Optional containing the refresh token if found
     */
    Optional<JpaRefreshToken> findByToken(ProtectedToken refreshToken);

    /**
     * Deletes all refresh tokens associated with the given user ID.
     *
     * @param userId the user ID
     */
    void deleteAllByUserId(Integer userId);

    /**
     * Deletes a refresh token by its value.
     *
     * @param refreshToken the refresh token value
     */
    void deleteByToken(ProtectedToken refreshToken);
}
