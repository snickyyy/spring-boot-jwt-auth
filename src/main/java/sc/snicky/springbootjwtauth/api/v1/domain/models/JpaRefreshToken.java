package sc.snicky.springbootjwtauth.api.v1.domain.models;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import sc.snicky.springbootjwtauth.api.v1.domain.converters.ProtectedTokenConvertor;
import sc.snicky.springbootjwtauth.api.v1.domain.types.ProtectedToken;

import java.io.Serializable;
import java.time.Instant;

@Getter
@Setter
@Entity
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Table(name = "tokens")
public class JpaRefreshToken extends BaseEntity<String> implements Serializable { // todo add device and last active fields
    /**
     * The unique identifier of the token.
     */
    @Id
    @Column(name = "id", nullable = false, updatable = false)
    @Convert(converter = ProtectedTokenConvertor.class)
    private ProtectedToken token;

    /**
     * Indicates whether the token is active.
     * Defaults to true and cannot be null.
     */
    @Column(name = "is_active", nullable = false, columnDefinition = "BOOLEAN DEFAULT TRUE")
    private Boolean isActive;

    /**
     * The user associated with the token.
     * Many tokens can belong to one user.
     * On user deletion, all associated tokens are also deleted (cascade).
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "user_id")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private User user;

    /**
     * The expiration time of the token.
     * Cannot be null.
     */
    @Column(name = "exp", nullable = false)
    private Instant exp;
}
