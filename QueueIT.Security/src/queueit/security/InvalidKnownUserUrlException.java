package queueit.security;

public class InvalidKnownUserUrlException extends KnownUserException {
    public InvalidKnownUserUrlException() {
        super("The hash of the request is invalid", null);
    }
}
