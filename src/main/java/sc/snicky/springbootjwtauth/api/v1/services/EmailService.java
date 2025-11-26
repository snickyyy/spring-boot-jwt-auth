package sc.snicky.springbootjwtauth.api.v1.services;

public interface EmailService {
    /**
     * Sends an email to the specified recipient.
     *
     * @param to      the recipient's email address
     * @param subject the subject of the email
     * @param body    the body content of the email
     */
    void sendEmail(String to, String subject, String body);
}
