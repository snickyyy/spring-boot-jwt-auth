package sc.snicky.springbootjwtauth.api.v1.domain.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
public class JpaRefreshTokenDetailsAdaptor implements RefreshTokenDetails {
    private UUID token;
    private User user;
    private Instant expiry;
    private Instant createdAt;
}
