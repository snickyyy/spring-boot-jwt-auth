package sc.snicky.springbootjwtauth.api.v1.domain.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
public class PostgresTokenAdaptor implements RefreshTokenDetails {
    private UUID token;
    private Integer userId;
    private Instant expiry;
    private Instant createdAt;

    /**
     * Creates a new instance of {@link PostgresTokenAdaptor} from the given {@link Token}.
     *
     * @param token the source token object
     * @return a new {@link PostgresTokenAdaptor} with fields populated from the token
     */
    public static PostgresTokenAdaptor ofToken(Token token) {
        return PostgresTokenAdaptor.builder()
                .token(token.getId())
                .userId(token.getUser().getId())
                .createdAt(token.getCreatedAt())
                .expiry(token.getExp())
                .build();
    }
}
