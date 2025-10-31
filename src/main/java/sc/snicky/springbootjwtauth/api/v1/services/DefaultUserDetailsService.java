package sc.snicky.springbootjwtauth.api.v1.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import sc.snicky.springbootjwtauth.api.v1.domain.models.UserDetailsAdaptor;
import sc.snicky.springbootjwtauth.api.v1.repositories.JpaUserRepository;

@Service
@RequiredArgsConstructor
public class DefaultUserDetailsService implements UserDetailsService {
    private final JpaUserRepository jpaUserRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return UserDetailsAdaptor.ofUser(jpaUserRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User with email " + username + " not found")));
    }
}
