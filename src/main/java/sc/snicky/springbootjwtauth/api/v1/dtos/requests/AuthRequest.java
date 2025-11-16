package sc.snicky.springbootjwtauth.api.v1.dtos.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record AuthRequest(
        // CHECKSTYLE:OFF
        @Email
        @NotBlank
        @Size(max = 100, message = "Email must be at most 100 characters long")
        @Schema(description = "User email address", example = "user@example.com")
        String email,

        @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).*$")
        @NotBlank
        @Size(min = 6, message = "Password must be at least 6 characters long")
        @Schema(description = "User password with at least one digit, one lowercase, and one uppercase letter", example = "Password_123")
        String password
        // CHECKSTYLE:ON
) {
}
