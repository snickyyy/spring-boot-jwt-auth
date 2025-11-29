package sc.snicky.springbootjwtauth.api.v1.events;

import sc.snicky.springbootjwtauth.api.v1.domain.models.User;

public record UserRegisteredEvent(User user, String confirmCode) { }
