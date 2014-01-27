package queueit.security;

import java.net.URI;

public abstract class KnownUserException extends SecurityException
{
    private URI originalUrl;
    private URI validationUrl;

    public URI getOriginalUrl() {
        return this.originalUrl;
    }

    public URI getValidationUrl() {
        return this.validationUrl;
    }

    void setOriginalUrl(URI originalUrl) {
        this.originalUrl = originalUrl;
    }   

    void setValidationUrl(URI validationUrl) {
        this.validationUrl = validationUrl;
    }   

    public KnownUserException(String message, Throwable cause)
    {
         super(message, cause);
    }
}
