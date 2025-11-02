package sc.snicky.springbootjwtauth.api.v1.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import sc.snicky.springbootjwtauth.api.v1.domain.models.UserDetailsAdaptor;
import sc.snicky.springbootjwtauth.api.v1.repositories.JpaUserRepository;

/**
 * Service implementation for loading user details by username.
 */
@Service
@RequiredArgsConstructor
public class DefaultUserDetailsService implements UserDetailsService {
    private final JpaUserRepository jpaUserRepository;

    /**
     * Loads user details by the given username (email).
     *
     * @param username the username (email) to search for
     * @return the UserDetails object for the user
     * @throws UsernameNotFoundException if the user is not found
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return UserDetailsAdaptor.ofUser(jpaUserRepository.findByEmailAndIsActiveTrue(username)
                .orElseThrow(() -> new UsernameNotFoundException("User with email " + username + " not found")));
    }
}
