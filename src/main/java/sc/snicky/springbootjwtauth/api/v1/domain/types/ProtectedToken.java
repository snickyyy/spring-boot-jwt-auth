package sc.snicky.springbootjwtauth.api.v1.domain.types;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class ProtectedToken implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private final String token;
}
