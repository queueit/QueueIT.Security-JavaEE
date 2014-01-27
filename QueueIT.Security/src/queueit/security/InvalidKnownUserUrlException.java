package queueit.security;
import java.net.URI;

/**
 *
 * @author Queue-it
 * last update: 2012-10-25
 */
public class InvalidKnownUserUrlException extends KnownUserException {
    public InvalidKnownUserUrlException() {
        super("The hash of the request is invalid", null);
    }
}
