package sc.snicky.springbootjwtauth.api.v1.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sc.snicky.springbootjwtauth.api.v1.domain.models.JpaRefreshToken;

@Repository
public interface JpaRefreshTokenRepository extends JpaRepository<JpaRefreshToken, String> {
    /**
     * Deletes all refresh tokens associated with the specified user ID from Database.
     *
     * @param userId the ID of the user whose tokens should be deleted
     */
    void deleteAllByUserId(Integer userId);
}
