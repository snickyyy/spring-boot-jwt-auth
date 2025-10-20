package sc.snicky.springbootjwtauth.api.v1.dtos;

import jakarta.validation.constraints.NotBlank;

public record TokenPair(
        @NotBlank(message = "Access token must not be blank")
        String accessToken,
        @NotBlank(message = "Refresh token must not be blank")
        String refreshToken
) { }
