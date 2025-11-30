package sc.snicky.springbootjwtauth.api.v1.listeners;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import sc.snicky.springbootjwtauth.api.v1.events.UserRegisteredEvent;
import sc.snicky.springbootjwtauth.api.v1.services.EmailService;

@Slf4j
@Component
@RequiredArgsConstructor
public class EmailEventListener {
    @Value("${server.address}")
    private String serverAddress;

    @Value("${server.port}")
    private String serverPort;
    private final EmailService emailService;

    /**
     * Handles the {@link UserRegisteredEvent} by sending a welcome email to the registered user.
     *
     * @param event the event containing user registration details
     */
    @EventListener
    public void handleUserRegisteredEvent(UserRegisteredEvent event) {
        String subject = "Welcome to Our Service!";
        var body = String.format(
                "Hello %s,%n%nThank you for registering with us!%n%nConfirm link: %s,%n",
                event.userEmail(),
                event.confirmCode()
        );

        emailService.sendEmail(event.userEmail(), subject, body);
    }

    private String buildConfirmLink(String confirmCode) {
        return "http://" + serverAddress + ":" + serverPort + "/api/v1/auth/confirm?code=" + confirmCode;
    }
}
