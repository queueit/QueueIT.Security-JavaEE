package queueit.security;

public class EnqueueResult extends ValidateResultBase {
    private String redirectUrl;
    
    public String getRedirectUrl() {
        return this.redirectUrl;
    }

    EnqueueResult(IQueue queue, String redirectUrl)
    {
        super(queue);
        this.redirectUrl = redirectUrl;
    }
}