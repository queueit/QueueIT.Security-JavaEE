package queueit.security;

public class KnownUserValidationException extends SessionValidationException {
    KnownUserValidationException(KnownUserException cause, IQueue queue)
    {
        super(cause.getMessage(), cause, queue);
    }
}
