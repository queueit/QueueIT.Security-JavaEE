package queueit.security;

public class SessionValidationException extends SecurityException {
    private IQueue queue;
    
    public IQueue getQueue() {
        return this.queue;
    }
    
    SessionValidationException(String message, IQueue queue)
    {
        super(message);
        this.queue = queue;
    }

    SessionValidationException(String message, Throwable cause, IQueue queue)
    {
        super(message, cause);
        this.queue = queue;
    }
}
