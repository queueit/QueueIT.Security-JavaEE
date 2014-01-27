package queueit.security;
import java.net.URI;
/**
 *
 * @author Queue-it
 * last update: 2012-10-25
 */
public class InvalidKnownUserHashException extends KnownUserException {
    public InvalidKnownUserHashException() {
        super("The hash of the request is invalid", null);
    }
}
