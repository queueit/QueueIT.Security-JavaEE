package queueit.security;

import java.net.URI;
import java.util.UUID;

public interface IQueue {
    String getEventId();
    String getCustomerId();
    URI getQueueUrl();
    URI getQueueUrl(Boolean includeTargetUrl);
    URI getQueueUrl(Boolean includeTargetUrl, Boolean sslEnabled);    
    URI getQueueUrl(Boolean includeTargetUrl, Boolean sslEnabled, String domainAlias);   
    URI getQueueUrl(URI targetUrl);
    URI getQueueUrl(URI targetUrl, Boolean sslEnabled);
    URI getQueueUrl(URI targetUrl, Boolean sslEnabled, String domainAlias);
    URI getCancelUrl();
    URI getCancelUrl(URI landingPage);
    URI getCancelUrl(URI landingPage, UUID queueId);
    URI getCancelUrl(URI landingPage, UUID queueId, Boolean sslEnabled);
    URI getCancelUrl(URI landingPage, UUID queueId, Boolean sslEnabled, String domainAlias);
    URI getLandingPageUrl();
    URI getLandingPageUrl(Boolean includeTargetUrl);
    URI getLandingPageUrl(URI targetUrl);
}
