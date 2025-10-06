package sc.snicky.springbootjwtauth.api.v1.domain.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity(name = "users")
public class User extends BaseEntity<Integer> implements Serializable {
    /**
     * Max length for the email field.
     */
    private static final int EMAIL_MAX_LENGTH = 100;

    /**
     * User email address.
     */
    @Column(name = "email", nullable = false, unique = true, length = EMAIL_MAX_LENGTH)
    private String email;

    /**
     * User password hash.
     */
    @Column(name = "password", nullable = false, length = Integer.MAX_VALUE)
    private String password;

    /**
     * User role.
     */
    @ManyToOne
    @OnDelete(action = OnDeleteAction.SET_NULL)
    @JoinColumn(name = "role_id")
    private Role role;

    /**
     * Tokens associated with the user.
     */
    @OneToMany(mappedBy = "user")
    private Set<Token> tokens = new LinkedHashSet<>();
}
