package sc.snicky.springbootjwtauth.api.v1.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sc.snicky.springbootjwtauth.api.v1.domain.models.PostgresTokenAdaptor;
import sc.snicky.springbootjwtauth.api.v1.domain.models.RefreshTokenDetails;
import sc.snicky.springbootjwtauth.api.v1.domain.models.Token;
import sc.snicky.springbootjwtauth.api.v1.domain.models.User;
import sc.snicky.springbootjwtauth.api.v1.exceptions.business.security.RefreshTokenNotValid;
import sc.snicky.springbootjwtauth.api.v1.exceptions.business.users.UserNotFoundException;
import sc.snicky.springbootjwtauth.api.v1.repositories.BasicRefreshTokenRepository;
import sc.snicky.springbootjwtauth.api.v1.repositories.JpaUserRepository;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {
    private final BasicRefreshTokenRepository basicRefreshTokenRepository;
    @Value("${app.auth.tokens.expiration.refresh:604800000}")
    private Long refreshTokenDurationMs;

    private final JpaUserRepository jpaUserRepository;

    @Override
    public RefreshTokenDetails generate(Integer userId) {
        var user = jpaUserRepository.findById(userId).orElseThrow(
                () -> {
                    log.error("User with id {} not found", userId);
                    return new UserNotFoundException("User not found");
                }
        );
        var result = PostgresTokenAdaptor.ofToken(buildToken(user));
        basicRefreshTokenRepository.save(result);
        return result;
    }

    public RefreshTokenDetails generate(User user) {
        var result = PostgresTokenAdaptor.ofToken(buildToken(user));
        basicRefreshTokenRepository.save(result);
        return result;
    }

    public RefreshTokenDetails generate(User user, Instant expiration) {
        var result = PostgresTokenAdaptor.ofToken(buildToken(user));
        result.setExpiry(expiration);
        basicRefreshTokenRepository.save(result);
        return result;
    }

    @Override
    @Transactional
    public RefreshTokenDetails rotate(UUID oldToken) {
        return basicRefreshTokenRepository.findByToken(oldToken)
                .map(t -> {
                    revoke(oldToken);
                    return generate(t.getUser(), t.getExpiry());
                })
                .orElseThrow(() -> {
                    log.error("Refresh token {} not found for rotation", oldToken);
                    return new RefreshTokenNotValid("Refresh token is not valid");
                });
    }

    @Override
    public Optional<RefreshTokenDetails> findByToken(UUID token) {
        return basicRefreshTokenRepository.findByToken(token);
    }

    @Override
    public boolean isValid(UUID token) {
        return findByToken(token)
                .map(t -> t.getExpiry().isAfter(Instant.now()))
                .orElse(false);
    }

    @Override
    public void revoke(UUID token) {
        basicRefreshTokenRepository.delete(token);
    }

    private Token buildToken(User user) {
        return Token.builder()
                .id(UUID.randomUUID())
                .user(user)
                .exp(Instant.now().plusMillis(refreshTokenDurationMs))
                .build();
    }
}
