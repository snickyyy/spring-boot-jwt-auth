package sc.snicky.springbootjwtauth.api.v1.domain.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "tokens")
public class Token extends BaseEntity<UUID> implements Serializable {
    /**
     * The unique identifier of the token.
     */
    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    /**
     * The user associated with the token.
     * Many tokens can belong to one user.
     * On user deletion, all associated tokens are also deleted (cascade).
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "user_id")
    private User user;

    /**
     * The expiration time of the token.
     * Cannot be null.
     */
    @Column(name = "exp", nullable = false)
    private Instant exp;

    /**
     * Pre-persist lifecycle callback to ensure the UUID is set before saving.
     * If the ID is null, a new UUID is generated.
     */
    @PrePersist
    public void prePersist() {
        if (id == null) {
            id = UUID.randomUUID();
        }
    }
}