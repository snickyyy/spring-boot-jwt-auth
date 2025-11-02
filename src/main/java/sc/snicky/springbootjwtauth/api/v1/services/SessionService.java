package sc.snicky.springbootjwtauth.api.v1.services;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;
import sc.snicky.springbootjwtauth.api.v1.exceptions.business.security.DefaultUnauthorizedException;

import java.time.Duration;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

@Service
public class SessionService {
    @Value("${app.auth.naming.refresh-token-cookie:session}")
    private  String nameTokenCookie;

    @Value("${app.auth.tokens.expiration.refresh:604800000}")
    private Long sessionTokenDurationMs;

    /**
     * Reads the session token from the cookies in the provided HTTP request.
     * This method searches for a cookie with the name specified by the
     * `nameTokenCookie` property and retrieves its value.
     *
     * @param request the HTTP request containing cookies
     * @return the session token as a {@link String}
     * @throws DefaultUnauthorizedException if the session token is not found
     */
    public String getSessionToken(HttpServletRequest request) {
        return Optional.ofNullable(request.getCookies())
                .stream()
                .flatMap(Arrays::stream)
                .filter(cookie -> Objects.equals(cookie.getName(), nameTokenCookie))
                .map(Cookie::getValue)
                .findFirst()
                .orElseThrow(() -> new DefaultUnauthorizedException("User is not authorized"));
    }

    /**
     * Sets the session token in the HTTP response as a secure, HTTP-only cookie.
     *
     * @param response the HTTP response to which the cookie will be added
     * @param token    the session token to be set in the cookie
     */
    public void setSessionToken(HttpServletResponse response, String token) {
        var cookie = ResponseCookie.from(nameTokenCookie, token)
                .maxAge(Duration.ofMillis(sessionTokenDurationMs))
                .httpOnly(true)
                .secure(true)
                .path("/")
                .build();
        response.addHeader("Set-Cookie", cookie.toString());
    }

    /**
     * Clears the session token by setting an expired cookie in the HTTP response.
     *
     * @param response the HTTP response to which the expired cookie will be added
     */
    public void clearSessionToken(HttpServletResponse response) {
        var cookie = ResponseCookie.from(nameTokenCookie, "")
                .maxAge(0)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .build();
        response.addHeader("Set-Cookie", cookie.toString());
    }
}
