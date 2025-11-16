package sc.snicky.springbootjwtauth.api.v1.controllers;

import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sc.snicky.springbootjwtauth.api.v1.dtos.responses.MessageResponse;

@Profile("dev")
@RestController
@RequestMapping("/api/v1")
public class HelloController {
    /**
     * Returns the roles of the current user.
     *
     * @param authentication the authentication object containing user details
     * @return ResponseEntity with a MessageResponse object containing the user's roles
     */
    @GetMapping("/get-my-roles")
    public ResponseEntity<MessageResponse<?>> hello(Authentication authentication) {
        return ResponseEntity.ok(MessageResponse.of(authentication.getAuthorities()));
    }

    /**
     * Returns a greeting message for users with the USER role.
     *
     * @return ResponseEntity with a MessageResponse object containing the message "Hello user"
     */
    @GetMapping("/hello-user")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<MessageResponse<String>> helloUser() {
        return ResponseEntity.ok(MessageResponse.of("Hello user"));
    }

    /**
     * Returns a greeting message for users with the ADMIN role.
     *
     * @return ResponseEntity with a MessageResponse object containing the message "Hello admin"
     */
    @GetMapping("/hello-admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MessageResponse<String>> helloAdmin() {
        return ResponseEntity.ok(MessageResponse.of("Hello admin"));
    }
}
