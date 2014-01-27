package queueit.security;

import java.net.URI;

public abstract class KnownUserException extends SecurityException
{
    private URI originalUrl;

    public URI getOriginalUrl() {
        return this.originalUrl;
    }
    
    void setOriginalUrl(URI originalUrl) {
        this.originalUrl = originalUrl;
    }   

    public KnownUserException(String message, Throwable cause)
    {
         super(message, cause);
    }
}
