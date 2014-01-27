package queueit.security;

import java.net.URI;

public class EnqueueResult extends ValidateResultBase {
    private URI redirectUrl;
    
    public URI getRedirectUrl() {
        return this.redirectUrl;
    }

    EnqueueResult(IQueue queue, URI redirectUrl)
    {
        super(queue);
        this.redirectUrl = redirectUrl;
    }
}