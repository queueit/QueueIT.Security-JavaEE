package queueit.security;

import java.util.Locale;
import java.util.UUID;

public interface IQueue {
    String getEventId();
    String getCustomerId();
    Boolean getDefaultSslEnabled();    
    Boolean getDefaultIncludeTargetUrl();
    String getDefaultDomainAlias();
    Locale getDefaultLanguage();  
    String getDefaultLayoutName();
    String getQueueUrl();
    String getQueueUrl(Boolean includeTargetUrl);
    String getQueueUrl(Boolean includeTargetUrl, Boolean sslEnabled);    
    String getQueueUrl(Boolean includeTargetUrl, Boolean sslEnabled, String domainAlias);   
    String getQueueUrl(Boolean includeTargetUrl, Boolean sslEnabled, String domainAlias, Locale language);
    String getQueueUrl(Boolean includeTargetUrl, Boolean sslEnabled, String domainAlias, Locale language, String layoutName);
    String getQueueUrl(String targetUrl);
    String getQueueUrl(String targetUrl, Boolean sslEnabled);
    String getQueueUrl(String targetUrl, Boolean sslEnabled, String domainAlias);
    String getQueueUrl(String targetUrl, Boolean sslEnabled, String domainAlias, Locale language);
    String getQueueUrl(String targetUrl, Boolean sslEnabled, String domainAlias, Locale language, String layoutName);
    String getCancelUrl();
    String getCancelUrl(String landingPage);
    String getCancelUrl(String landingPage, UUID queueId);
    String getCancelUrl(String landingPage, UUID queueId, Boolean sslEnabled);
    String getCancelUrl(String landingPage, UUID queueId, Boolean sslEnabled, String domainAlias);
    String getLandingPageUrl();
    String getLandingPageUrl(Boolean includeTargetUrl);
    String getLandingPageUrl(String targetUrl);
}
