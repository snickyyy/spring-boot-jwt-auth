package sc.snicky.springbootjwtauth.api.v1.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import sc.snicky.springbootjwtauth.api.v1.domain.models.BasicRefreshToken;
import sc.snicky.springbootjwtauth.api.v1.domain.models.JpaRefreshToken;

@Mapper(componentModel = "spring")
public interface JpaRefreshTokenMapper {
    /**
     * Maps a {@link JpaRefreshToken} entity to a {@link BasicRefreshToken} domain model.
     *
     * @param jpaRefreshToken the JPA entity to map
     * @return the mapped domain model
     */
    @Mapping(target = "token", source = "id")
    @Mapping(target = "expiresAt", source = "exp")
    BasicRefreshToken toBasicRefreshToken(JpaRefreshToken jpaRefreshToken);

    /**
     * Maps a {@link BasicRefreshToken} domain model to a {@link JpaRefreshToken} entity.
     *
     * @param basicRefreshToken the domain model to map
     * @return the mapped JPA entity
     */
    @Mapping(target = "id", source = "token")
    @Mapping(target = "exp", source = "expiresAt")
    JpaRefreshToken toJpaRefreshToken(BasicRefreshToken basicRefreshToken);
}
