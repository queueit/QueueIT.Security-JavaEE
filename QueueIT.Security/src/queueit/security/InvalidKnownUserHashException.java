package queueit.security;

public class InvalidKnownUserHashException extends KnownUserException {
    public InvalidKnownUserHashException() {
        super("The hash of the request is invalid", null);
    }
}
