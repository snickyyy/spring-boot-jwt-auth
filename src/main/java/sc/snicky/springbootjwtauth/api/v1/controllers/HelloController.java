package sc.snicky.springbootjwtauth.api.v1.controllers;

import org.springframework.http.ResponseEntity;
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
    @GetMapping("/hello")
    public ResponseEntity<MessageResponse<String>> hello() {
        return ResponseEntity.ok(MessageResponse.of("Hello World"));
    }
}
