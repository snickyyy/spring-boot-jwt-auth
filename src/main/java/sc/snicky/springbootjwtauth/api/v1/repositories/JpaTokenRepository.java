package sc.snicky.springbootjwtauth.api.v1.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sc.snicky.springbootjwtauth.api.v1.domain.models.Token;

import java.util.UUID;

@Repository
public interface JpaTokenRepository extends JpaRepository<Token, UUID> {
}
