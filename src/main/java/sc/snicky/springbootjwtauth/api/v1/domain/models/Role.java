package sc.snicky.springbootjwtauth.api.v1.domain.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import sc.snicky.springbootjwtauth.api.v1.domain.enums.ERole;

import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@Entity(name = "roles")
public class Role extends BaseEntity<Integer> implements Serializable {
    /**
     * Maximum allowed length for the role name.
     */
    private static final int ROLE_NAME_MAX_LENGTH = 50;

    /**
     * The name of the role.
     * Must be unique and not null. Maximum length is 50 characters.
     **/
    @Enumerated(value = EnumType.STRING)
    @Column(name = "name", nullable = false, length = ROLE_NAME_MAX_LENGTH, unique = true)
    private ERole name;
}
