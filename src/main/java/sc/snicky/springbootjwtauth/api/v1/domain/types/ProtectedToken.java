package sc.snicky.springbootjwtauth.api.v1.domain.types;

import lombok.Data;

@Data
public class ProtectedToken {
    private final String token;
}
