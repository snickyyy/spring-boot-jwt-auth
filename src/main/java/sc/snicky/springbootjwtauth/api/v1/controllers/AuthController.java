package sc.snicky.springbootjwtauth.api.v1.controllers;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sc.snicky.springbootjwtauth.api.v1.dtos.requests.RegisterRequest;
import sc.snicky.springbootjwtauth.api.v1.dtos.responses.RegisterResponse;
import sc.snicky.springbootjwtauth.api.v1.services.AuthService;
import sc.snicky.springbootjwtauth.api.v1.services.SessionService;

import java.time.Instant;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final SessionService sessionService;

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(
            HttpServletResponse response, @Valid @RequestBody RegisterRequest registerRequest) {

        var tokens = authService.register(registerRequest.email(), registerRequest.password());
        sessionService.setSessionToken(response, tokens.refreshToken());

        return ResponseEntity.ok(new RegisterResponse(
                "User registered successfully",
                tokens.accessToken(),
                Instant.now()
        ));
    }
}
