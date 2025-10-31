package sc.snicky.springbootjwtauth.api.v1.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sc.snicky.springbootjwtauth.api.v1.domain.models.Role;

import java.util.Optional;

@Repository
public interface JpaRoleRepository extends JpaRepository<Role, Integer> {
    /**
     * Finds a Role entity by its name.
     *
     * @param name the name of the role to search for
     * @return an Optional containing the Role if found, or empty if not found
     */
    Optional<Role> findByName(String name);
}
