package sc.snicky.springbootjwtauth.api.v1.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import sc.snicky.springbootjwtauth.api.v1.domain.models.Role;

public interface JpaRoleRepository extends JpaRepository<Role, Integer> {
}
