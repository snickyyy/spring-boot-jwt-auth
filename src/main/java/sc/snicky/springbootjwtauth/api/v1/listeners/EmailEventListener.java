package sc.snicky.springbootjwtauth.api.v1.listeners;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import sc.snicky.springbootjwtauth.api.v1.events.UserRegisteredEvent;
import sc.snicky.springbootjwtauth.api.v1.services.EmailService;

@Slf4j
@Component
@RequiredArgsConstructor
public class EmailEventListener {
    private final EmailService emailService;

    /**
     * Handles the {@link UserRegisteredEvent} by sending a welcome email to the registered user.
     *
     * @param event the event containing user registration details
     */
    @EventListener
    public void handleUserRegisteredEvent(UserRegisteredEvent event) {
        var user = event.user();
        String subject = "Welcome to Our Service!";
        String body = String.format("Hello %s,\n\nThank you for registering with us!\n\nConfirm code: %d,\n",
                user.getEmail(), event.confirmCode());

        emailService.sendEmail(user.getEmail(), subject, body);
        log.info("Received UserRegisteredEvent for user with id {}", user.getId());
    }
}
