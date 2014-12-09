package queueit.security;

public abstract class KnownUserException extends SecurityException
{
    private String originalUrl;
    private String validationUrl;

    public String getOriginalUrl() {
        return this.originalUrl;
    }

    public String getValidationUrl() {
        return this.validationUrl;
    }

    void setOriginalUrl(String originalUrl) {
        this.originalUrl = originalUrl;
    }   

    void setValidationUrl(String validationUrl) {
        this.validationUrl = validationUrl;
    }   

    public KnownUserException(String message, Throwable cause)
    {
         super(message, cause);
    }
}
