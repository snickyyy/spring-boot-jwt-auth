package sc.snicky.springbootjwtauth.api.v1.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sc.snicky.springbootjwtauth.api.v1.domain.models.Role;

@Repository
public interface JpaRoleRepository extends JpaRepository<Role, Integer> {
}
