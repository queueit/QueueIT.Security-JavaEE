package queueit.security;

import java.util.Locale;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import queueit.security.uribuilder.UriComponents;

import queueit.security.uribuilder.UriComponentsBuilder;

class Queue implements IQueue {
    private String defaultDomainAlias;
    private boolean defaultSslEnabled;
    private boolean defaultIncludeTargetUrl;
    private String defaultLayoutName;
    private Locale defaultLanguage;
    private URI defaultQueueUrl;
    private URI defaultLandingPageUrl;
    private URI defaultCancelUrl;
    private String eventId;
    private String customerId;
    
    public Queue(
            String customerId, 
            String eventId, 
            String domainAlias, 
            URI landingPage, 
            Boolean sslEnabled, 
            Boolean includeTargetUrl,
            Locale language,
            String layoutName) {
        this.customerId = customerId;
        this.eventId = eventId;
        this.defaultLanguage = language;
        this.defaultLayoutName = layoutName;
        
        this.defaultQueueUrl = generateQueueUrl(sslEnabled, domainAlias, language, layoutName).build().toUri();
        this.defaultCancelUrl = generateCancelUrl(sslEnabled, domainAlias).build().toUri();

        this.defaultDomainAlias = domainAlias;
        this.defaultLandingPageUrl = landingPage;
        if (this.defaultLandingPageUrl != null && !this.defaultLandingPageUrl.isAbsolute())
        {
            HttpServletRequest request = RequestContext.getCurrentInstance().getRequest();
            if (request != null) {
                URI currentUrl = URI.create(request.getRequestURL().toString());
                UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUri(currentUrl);

                uriBuilder.uri(this.defaultLandingPageUrl);
                if (uriBuilder.build().getScheme() == null)
                    uriBuilder.scheme(currentUrl.getScheme());
                
                this.defaultLandingPageUrl = uriBuilder.build(true).toUri();
            }
        }
        this.defaultSslEnabled = sslEnabled;
        this.defaultIncludeTargetUrl = includeTargetUrl;
    }
    
    public String getEventId() {
        return eventId;
    }
    
    public String getCustomerId() {
        return customerId;
    }
    
    public URI getQueueUrl() {
        return getQueueUrlFromCurrentUrl(null, null, null, null, null);
    }
    
    public URI getQueueUrl(Boolean includeTargetUrl) {
        return getQueueUrlFromCurrentUrl(includeTargetUrl, null, null, null, null);
    }
    
    public URI getQueueUrl(Boolean includeTargetUrl, Boolean sslEnabled) {
        return getQueueUrlFromCurrentUrl(includeTargetUrl, sslEnabled, null, null, null);
    }
    
    public URI getQueueUrl(Boolean includeTargetUrl, Boolean sslEnabled, String domainAlias) {
        return getQueueUrlFromCurrentUrl(includeTargetUrl, sslEnabled, domainAlias, null, null);
    }

    public URI getQueueUrl(Boolean includeTargetUrl, Boolean sslEnabled, String domainAlias, Locale language) {
        return getQueueUrlFromCurrentUrl(includeTargetUrl, sslEnabled, domainAlias, language, null);
    }
        
    public URI getQueueUrl(Boolean includeTargetUrl, Boolean sslEnabled, String domainAlias, Locale language, String layoutName) {
        return getQueueUrlFromCurrentUrl(includeTargetUrl, sslEnabled, domainAlias, language, layoutName);
    }
    private URI getQueueUrlFromCurrentUrl(Boolean includeTargetUrl, Boolean sslEnabled, String domainAlias, Locale language, String layoutName) {
        UriComponentsBuilder queueUrl = getQueueUrlWithoutTarget(sslEnabled, domainAlias, language, layoutName);

        includeTargetUrl(includeTargetUrl, queueUrl);

        return queueUrl.build(true).toUri();
    }
    
    public URI getQueueUrl(URI targetUrl) {
        return getQueueUrl(targetUrl, null, null, null, null);
    }
    
    public URI getQueueUrl(URI targetUrl, Boolean sslEnabled) {
        return getQueueUrl(targetUrl, sslEnabled, null, null, null);
    }

    public URI getQueueUrl(URI targetUrl, Boolean sslEnabled, String domainAlias) {
        return getQueueUrl(targetUrl, sslEnabled, domainAlias, null, null);
    }
        
    public URI getQueueUrl(URI targetUrl, Boolean sslEnabled, String domainAlias, Locale language) {
        return getQueueUrl(targetUrl, sslEnabled, domainAlias, language, null);
    }
            
    public URI getQueueUrl(URI targetUrl, Boolean sslEnabled, String domainAlias, Locale language, String layoutName) {
       UriComponentsBuilder queueUrl = getQueueUrlWithoutTarget(sslEnabled, domainAlias, language, layoutName);

        includeTargetUrl(targetUrl, queueUrl);

        return queueUrl.build(true).toUri();
    }
    
    public URI getCancelUrl() {
        return this.getCancelUrl(null, null, null, null);
    }
    
    public URI getCancelUrl(URI landingPage) {
        return this.getCancelUrl(landingPage, null, null, null);    
    }
    
    public URI getCancelUrl(URI landingPage, UUID queueId) {
        return this.getCancelUrl(landingPage, queueId, null, null);    
    }
    
    public URI getCancelUrl(URI landingPage, UUID queueId, Boolean sslEnabled) {
        return this.getCancelUrl(landingPage, queueId, sslEnabled, null);    
    }
    
    public URI getCancelUrl(URI landingPage, UUID queueId, Boolean sslEnabled, String domainAlias) {
        UriComponentsBuilder cancelUrl = domainAlias != null
            ? generateCancelUrl(sslEnabled, domainAlias)
            : UriComponentsBuilder.fromUri(this.defaultCancelUrl);

        if (sslEnabled != null)
        {
            cancelUrl.scheme(sslEnabled ? "https" : "http");
            cancelUrl.port(sslEnabled ? 443 : 80);
        }

        if (queueId != null){
            cancelUrl.replaceQueryParam("q", queueId.toString());
        }
        if (landingPage != null) {
            try {
                cancelUrl.replaceQueryParam("r", URLEncoder.encode(landingPage.toString(), "UTF-8"));
            } catch (UnsupportedEncodingException ex) {
                cancelUrl.replaceQueryParam("r", landingPage.toString());
            }
        }
        if (landingPage == null && this.defaultLandingPageUrl != null) {
            cancelUrl.replaceQueryParam("r", this.defaultLandingPageUrl.toString());
        }

        return cancelUrl.build(true).toUri();  
    }
    
    public URI getLandingPageUrl() {
        return getLandingPageUrlFromCurrentUrl(null);
    }
    
    public URI getLandingPageUrl(Boolean includeTargetUrl) {
        return getLandingPageUrlFromCurrentUrl(includeTargetUrl);
    }
    
    private URI getLandingPageUrlFromCurrentUrl(Boolean includeTargetUrl) {
        if (this.defaultLandingPageUrl == null)
            return null;

        if ((includeTargetUrl == null || !includeTargetUrl) && !this.defaultIncludeTargetUrl)
            return this.defaultLandingPageUrl;

        UriComponentsBuilder builder = UriComponentsBuilder.fromUri(this.defaultLandingPageUrl);

        includeTargetUrl(includeTargetUrl, builder);

        return builder.build(true).toUri();
    }
    
    public URI getLandingPageUrl(URI targetUrl) {
        if (this.defaultLandingPageUrl == null)
            return null;

        UriComponentsBuilder builder = UriComponentsBuilder.fromUri(this.defaultLandingPageUrl);

        includeTargetUrl(targetUrl, builder);

        return builder.build(true).toUri();
    }
    
    Boolean getDefaultSslEnabled() {
        return this.defaultSslEnabled;
    }
    
    Boolean getDefaultIncludeTargetUrl() {
        return this.defaultIncludeTargetUrl;
    }
    
    String getDefaultDomainAlias() {
        return this.defaultDomainAlias;
    }

    Locale getDefaultLanguage() {
        return this.defaultLanguage;
    }
        
    String getDefaultLayoutName() {
        return this.defaultLayoutName;
    }
    private UriComponentsBuilder generateQueueUrl(Boolean sslEnabled, String domainAlias, Locale language, String layoutName)
    {
        if (domainAlias == null || domainAlias.isEmpty())
            domainAlias = this.defaultDomainAlias;

        UriComponentsBuilder uri = UriComponentsBuilder.fromUriString("http://" + domainAlias);
        if (sslEnabled != null && sslEnabled) {
            uri.scheme("https");
        }                

        uri.queryParam("c", this.customerId);
        uri.queryParam("e", this.eventId);

        if (language != null)
            uri.queryParam("cid", language.toString());
        if (layoutName != null && !layoutName.isEmpty())
            uri.queryParam("l", layoutName);
        
        return uri;
    }
    
    private UriComponentsBuilder generateCancelUrl(Boolean sslEnabled, String domainAlias)
    {
        if (domainAlias == null || domainAlias.isEmpty())
            domainAlias = this.defaultDomainAlias;

        UriComponentsBuilder uri = UriComponentsBuilder.fromUriString("http://" + domainAlias);
        if (sslEnabled) {
            uri.scheme("https");
        }                
        uri.path("cancel.aspx");
        uri.queryParam("c", this.customerId);
        uri.queryParam("e", this.eventId);

        return uri;
    }
    
    private UriComponentsBuilder getQueueUrlWithoutTarget(Boolean sslEnabled, String domainAlias, Locale language, String layoutName)
    {
        UriComponentsBuilder queueUrl = (domainAlias != null)
            ? generateQueueUrl(sslEnabled, domainAlias, language, layoutName)
            : UriComponentsBuilder.fromUri(this.defaultQueueUrl);

        if (sslEnabled != null)
        {
            queueUrl.scheme(sslEnabled ? "https" : "http");    
        }
        
        return queueUrl;
    }
    
    private void includeTargetUrl(Boolean includeTargetUrl, UriComponentsBuilder queueUrl)
    {
        HttpServletRequest request = RequestContext.getCurrentInstance().getRequest();
        if (request == null) {
            return;
        }

        if (includeTargetUrl == null)
            includeTargetUrl = this.defaultIncludeTargetUrl;

        if (!includeTargetUrl)
            return;
        try {
            includeTargetUrl(new URI(request.getRequestURL().toString()), queueUrl);
        } catch (URISyntaxException ex) {
            
        }

    }

    private static void includeTargetUrl(URI targetUrl, UriComponentsBuilder queueUrl)
    {
        try {       
            queueUrl.replaceQueryParam("t", URLEncoder.encode(targetUrl.toString(), "UTF-8"));
        } catch (UnsupportedEncodingException ex) {
            queueUrl.replaceQueryParam("t", targetUrl.toString());
        }
    }

}
