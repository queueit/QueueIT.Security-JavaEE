package queueit.security;

public class ExpiredValidationException extends SessionValidationException {
    private IKnownUser knownUser;
    
    public IKnownUser getKnownUser() {
        return this.knownUser;
    }
    
    ExpiredValidationException(IQueue queue, IKnownUser knownUser)
    {
        super("Known User token is expired", queue);
        this.knownUser = knownUser;
    }
}
