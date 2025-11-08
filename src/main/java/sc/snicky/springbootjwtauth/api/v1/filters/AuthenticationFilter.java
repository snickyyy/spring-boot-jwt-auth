package sc.snicky.springbootjwtauth.api.v1.filters;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import sc.snicky.springbootjwtauth.api.v1.services.AccessTokenService;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthenticationFilter extends OncePerRequestFilter {
    public static final String BEARER_PREFIX = "Bearer ";
    public static final String HEADER_NAME = "Authorization";
    private final AccessTokenService accessTokenService;


    /**
     * Filters incoming requests to authenticate users based on JWT tokens.
     *
     * @param request     the HTTP request
     * @param response    the HTTP response
     * @param filterChain the filter chain
     * @throws ServletException if a servlet error occurs
     * @throws IOException      if an I/O error occurs
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        var authHeader = request.getHeader(HEADER_NAME);
        if (authHeader == null || !authHeader.startsWith(BEARER_PREFIX)) {
            filterChain.doFilter(request, response);
            return;
        }

        var jwt = authHeader.substring(BEARER_PREFIX.length());
        UserDetails userDetails;
        try {
            userDetails = accessTokenService.extractUserDetails(jwt);
        } catch (JwtException e) {
            log.error("Cannot extract user details from JWT", e);
            filterChain.doFilter(request, response);
            return;
        }

        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            var context = SecurityContextHolder.createEmptyContext();
            var auth = UsernamePasswordAuthenticationToken.authenticated(userDetails.getUsername(),
                    null,
                    userDetails.getAuthorities());
            context.setAuthentication(auth);
            SecurityContextHolder.setContext(context);
            log.info("Authentication Successfully for user {}", userDetails.getUsername());
        }
        filterChain.doFilter(request, response);
    }
}
