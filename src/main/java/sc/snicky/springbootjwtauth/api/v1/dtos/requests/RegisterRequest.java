package sc.snicky.springbootjwtauth.api.v1.dtos.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@SuppressWarnings("checkstyle:MagicNumber")
public record RegisterRequest(
        @Email
        @Size(max = 100, message = "Email must be at most 100 characters long")
        String email,

        @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).*$")
        @Size(min = 6, message = "Password must be at least 6 characters long")
        String password
) {
}
