package sc.snicky.springbootjwtauth.api.v1.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sc.snicky.springbootjwtauth.api.v1.domain.models.User;

import java.util.Optional;

@Repository
public interface JpaUserRepository extends JpaRepository<User, Integer> {
    /**
     * Find an active user by their ID.
     *
     * @param id the ID of the user
     * @return an {@link Optional} containing the found user, or empty if not found
     */
    Optional<User> findByIdAndIsActiveTrue(Integer id);

    /**
     * Find an active user by their email.
     *
     * @param email the email to search for
     * @return an {@link Optional} containing the found user, or empty if not found
     */
    Optional<User> findByEmailAndIsActiveTrue(String email);
}
