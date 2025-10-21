package sc.snicky.springbootjwtauth.api.v1.domain.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Data
@Builder
@AllArgsConstructor
public class UserDetailsAdaptor implements UserDetails {
    private Collection<? extends GrantedAuthority> grantedAuthorities;
    private String username;
    private String password;

    public static UserDetailsAdaptor ofUser(User user) {
        return UserDetailsAdaptor.builder()
                .grantedAuthorities(user.getRoles().stream()
                        .map(Role::getName)
                        .map(erole -> (GrantedAuthority) erole::name)
                        .toList())
                .password(user.getPassword())
                .username(user.getEmail())
                .build();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return grantedAuthorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }
}
