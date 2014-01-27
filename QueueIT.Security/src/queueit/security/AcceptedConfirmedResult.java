package queueit.security;

public class AcceptedConfirmedResult extends ValidateResultBase {
    private IKnownUser knownUser;
    private boolean isInitialValidationRequest;
    
    public IKnownUser getKnownUser() {
        return this.knownUser;
    }
            
    public boolean isInitialValidationRequest() {
        return this.isInitialValidationRequest;
    }
    
    AcceptedConfirmedResult(IQueue queue, IKnownUser knownUser, boolean initialRequest)
    {
        super(queue);
        this.knownUser = knownUser;
        this.isInitialValidationRequest = initialRequest;
    }
}
