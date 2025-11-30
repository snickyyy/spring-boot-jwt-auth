package sc.snicky.springbootjwtauth.api.v1.events;

public record UserRegisteredEvent(String userEmail, String confirmCode) { }
