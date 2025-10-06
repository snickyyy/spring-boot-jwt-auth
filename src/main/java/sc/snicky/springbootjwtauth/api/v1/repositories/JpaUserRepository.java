package sc.snicky.springbootjwtauth.api.v1.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sc.snicky.springbootjwtauth.api.v1.domain.models.User;

@Repository
public interface JpaUserRepository extends JpaRepository<User, Integer> {
    /**
     * Find a user by their username.
     *
     * @param email the username to search for
     * @return the User entity if found, otherwise null
     */
    User findByEmail(String email);
}
