package sc.snicky.springbootjwtauth.api.v1.domain.types;

import lombok.Data;

@Data
public class NonProtectedToken {
    private final String token;
}
