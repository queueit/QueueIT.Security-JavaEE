package queueit.security;

import java.util.Date;

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
    
    public void cancel()
    {
        SessionValidationController.cancel(this);
    }
    
    public void setExpiration(Date expirationTime)
    {
        SessionValidationController.setExpiration(this, expirationTime);
    }
}
