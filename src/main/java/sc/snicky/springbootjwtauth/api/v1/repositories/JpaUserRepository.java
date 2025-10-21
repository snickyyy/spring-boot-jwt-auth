package sc.snicky.springbootjwtauth.api.v1.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sc.snicky.springbootjwtauth.api.v1.domain.models.User;

import java.util.Optional;

@Repository
public interface JpaUserRepository extends JpaRepository<User, Integer> {
    /**
     * Find a user by their username.
     *
     * @param email the username to search for
     * @return an {@link Optional} containing the found user, or empty if not found
     */
    Optional<User> findByEmail(String email);
}
