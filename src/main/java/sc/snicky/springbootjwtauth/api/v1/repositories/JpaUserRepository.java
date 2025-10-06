package sc.snicky.springbootjwtauth.api.v1.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import sc.snicky.springbootjwtauth.api.v1.domain.models.User;

public interface JpaUserRepository extends JpaRepository<User, Integer> {
    /**
     * Find a user by their username.
     *
     * @param username the username to search for
     * @return the User entity if found, otherwise null
     */
    User findByUsername(String username);
}
