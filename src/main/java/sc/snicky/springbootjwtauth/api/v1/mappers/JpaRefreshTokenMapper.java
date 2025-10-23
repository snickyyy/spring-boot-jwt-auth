package sc.snicky.springbootjwtauth.api.v1.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import sc.snicky.springbootjwtauth.api.v1.domain.models.BasicRefreshToken;
import sc.snicky.springbootjwtauth.api.v1.domain.models.JpaRefreshToken;

@Mapper
public interface JpaRefreshTokenMapper {
    @Mapping(target = "token", source = "id")
    @Mapping(target = "expiresAt", source = "exp")
    BasicRefreshToken toBasicRefreshToken(JpaRefreshToken jpaRefreshToken);
}
