package sc.snicky.springbootjwtauth.api.v1.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sc.snicky.springbootjwtauth.api.v1.domain.models.Token;

import java.util.UUID;

@Repository
public interface JpaTokenRepository extends JpaRepository<Token, UUID> {
    /**
     * Deletes all refresh tokens associated with the specified user ID from Database.
     *
     * @param userId the ID of the user whose tokens should be deleted
     */
    void deleteAllByUserId(Integer userId);
}
