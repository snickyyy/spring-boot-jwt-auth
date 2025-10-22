package sc.snicky.springbootjwtauth.api.v1.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import sc.snicky.springbootjwtauth.api.v1.domain.models.RefreshTokenDetails;
import sc.snicky.springbootjwtauth.api.v1.domain.models.UserDetailsAdaptor;
import sc.snicky.springbootjwtauth.api.v1.dtos.TokenPair;
import sc.snicky.springbootjwtauth.api.v1.exceptions.business.security.InvalidRefreshTokenException;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class TokensManagerImpl implements TokensManager {
    private final RefreshTokenService refreshTokenService;
    private final AccessTokenService accessTokenService;

    @Override
    public TokenPair generateTokens(Integer userId) {
        var refreshToken = refreshTokenService.generate(userId);
        var accessToken = accessTokenService.generate(UserDetailsAdaptor.ofUser(refreshToken.getUser()));
        return buildTokenPair(accessToken, refreshToken);
    }

    @Override
    public TokenPair refreshTokens(String refreshToken) {
        var uuidRefreshToken = convertToUUID(refreshToken);
        var newRefreshToken = refreshTokenService.rotate(uuidRefreshToken);
        var accessToken = accessTokenService.generate(
                UserDetailsAdaptor.ofUser(newRefreshToken.getUser()));
        return buildTokenPair(accessToken, newRefreshToken);
    }

    @Override
    public void revokeRefreshToken(String refreshToken) {
        refreshTokenService.revoke(convertToUUID(refreshToken));
    }

    @Override
    public void revokeAllTokensForUser(Integer userId) {
        refreshTokenService.revokeAllTokensForUser(userId);
    }

    private UUID convertToUUID(String token) {
        try {
            return UUID.fromString(token);
        } catch (IllegalArgumentException ex) {
            log.error("Invalid token format: {}", token);
            throw new InvalidRefreshTokenException("Invalid token format");
        }
    }

    private TokenPair buildTokenPair(String accessToken, RefreshTokenDetails refreshToken) {
        return TokenPair.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken.getToken().toString())
                .build();
    }
}
