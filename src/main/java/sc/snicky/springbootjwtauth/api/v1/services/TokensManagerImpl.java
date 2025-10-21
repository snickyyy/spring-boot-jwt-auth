package sc.snicky.springbootjwtauth.api.v1.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import sc.snicky.springbootjwtauth.api.v1.dtos.TokenPair;

@Slf4j
@Service
public class TokensManagerImpl implements TokensManager{
    @Override
    public TokenPair generateTokens(Integer userId) {
        return null;
    }

    @Override
    public TokenPair refreshTokens(String refreshToken) {
        return null;
    }

    @Override
    public void revokeRefreshToken(String refreshToken) {

    }

    @Override
    public void revokeAllTokensForUser(Integer userId) {

    }

    @Override
    public UserDetails extractUserDetails(String accessToken) {
        return null;
    }
}
