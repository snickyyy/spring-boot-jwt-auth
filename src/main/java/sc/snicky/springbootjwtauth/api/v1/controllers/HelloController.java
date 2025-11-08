package sc.snicky.springbootjwtauth.api.v1.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sc.snicky.springbootjwtauth.api.v1.dtos.responses.MessageResponse;

@RestController
@RequestMapping("/api/v1")
public class HelloController {

    /**
     * Handles GET requests and returns a greeting message.
     *
     * @return ResponseEntity containing a MessageResponse with "Hello World"
     */
    @GetMapping("/hello-all")
    public ResponseEntity<MessageResponse<?>> hello(Authentication authentication) {
        return ResponseEntity.ok(MessageResponse.of(authentication.getAuthorities()));
    }

    @GetMapping("/hello-user")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<MessageResponse<String>> helloUser() {
        return ResponseEntity.ok(MessageResponse.of("Hello user"));
    }

    @GetMapping("/hello-admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MessageResponse<String>> helloAdmin() {
        return ResponseEntity.ok(MessageResponse.of("Hello admin"));
    }
}
