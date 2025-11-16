package sc.snicky.springbootjwtauth.api.v1.domain.models;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.io.Serializable;
import java.time.Instant;

@Getter
@Setter
@MappedSuperclass
public abstract class BaseEntity<ID> implements Serializable, BaseModel<ID> {
    /**
     * The unique identifier of the entity.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private ID id;


    /**
     * The timestamp when the entity was created.
     */
    @ColumnDefault("now()")
    @Column(name = "created_at", nullable = false, updatable = false, insertable = false)
    private Instant createdAt;
}
