package queueit.security;

import java.io.File;
import java.io.IOException;
import java.util.Locale;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.UUID;
import java.util.jar.Attributes;
import static java.util.jar.Attributes.Name.IMPLEMENTATION_VERSION;
import java.util.jar.JarFile;
import java.util.jar.Manifest;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;

import queueit.security.uribuilder.UriComponentsBuilder;

class Queue implements IQueue {
    private String defaultDomainAlias;
    private boolean defaultSslEnabled;
    private boolean defaultIncludeTargetUrl;
    private String defaultLayoutName;
    private Locale defaultLanguage;
    private String defaultQueueUrl;
    private String defaultLandingPageUrl;
    private String defaultCancelUrl;
    private String eventId;
    private String customerId;
    
    public Queue(
            String customerId, 
            String eventId, 
            String domainAlias, 
            String landingPage, 
            Boolean sslEnabled, 
            Boolean includeTargetUrl,
            Locale language,
            String layoutName) {
        this.customerId = customerId;
        this.eventId = eventId;
        this.defaultLanguage = language;
        this.defaultLayoutName = layoutName;
        
        this.defaultQueueUrl = generateQueueUrl(sslEnabled, domainAlias, language, layoutName).build().toUriString();
        this.defaultCancelUrl = generateCancelUrl(sslEnabled, domainAlias).build().toUriString();

        this.defaultDomainAlias = domainAlias;
        this.defaultLandingPageUrl = landingPage;
        if (this.defaultLandingPageUrl != null && !this.defaultLandingPageUrl.startsWith("http"))
        {          
            HttpServletRequest request = RequestContext.getCurrentInstance().getRequest();
            if (request != null) {
                String currentUrl = request.getRequestURL().toString();
                
                Pattern pattern = Pattern.compile("^(https?://[^/]+)", Pattern.CASE_INSENSITIVE);
                Matcher matcher = pattern.matcher(currentUrl);
                
                if (matcher.find()) 
                {
                    this.defaultLandingPageUrl = 
                        currentUrl.substring(0, matcher.end()) + (this.defaultLandingPageUrl.startsWith("/") ? this.defaultLandingPageUrl : ("/" + this.defaultLandingPageUrl));
                }
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
    
    public String getQueueUrl() {
        return getQueueUrlFromCurrentUrl(null, null, null, null, null);
    }
    
    public String getQueueUrl(Boolean includeTargetUrl) {
        return getQueueUrlFromCurrentUrl(includeTargetUrl, null, null, null, null);
    }
    
    public String getQueueUrl(Boolean includeTargetUrl, Boolean sslEnabled) {
        return getQueueUrlFromCurrentUrl(includeTargetUrl, sslEnabled, null, null, null);
    }
    
    public String getQueueUrl(Boolean includeTargetUrl, Boolean sslEnabled, String domainAlias) {
        return getQueueUrlFromCurrentUrl(includeTargetUrl, sslEnabled, domainAlias, null, null);
    }

    public String getQueueUrl(Boolean includeTargetUrl, Boolean sslEnabled, String domainAlias, Locale language) {
        return getQueueUrlFromCurrentUrl(includeTargetUrl, sslEnabled, domainAlias, language, null);
    }
        
    public String getQueueUrl(Boolean includeTargetUrl, Boolean sslEnabled, String domainAlias, Locale language, String layoutName) {
        return getQueueUrlFromCurrentUrl(includeTargetUrl, sslEnabled, domainAlias, language, layoutName);
    }
    private String getQueueUrlFromCurrentUrl(Boolean includeTargetUrl, Boolean sslEnabled, String domainAlias, Locale language, String layoutName) {
        UriComponentsBuilder queueUrl = getQueueUrlWithoutTarget(sslEnabled, domainAlias, language, layoutName);

        includeTargetUrl(includeTargetUrl, queueUrl);

        return queueUrl.build(true).toUriString();
    }
    
    public String getQueueUrl(String targetUrl) {
        return getQueueUrl(targetUrl, null, null, null, null);
    }
    
    public String getQueueUrl(String targetUrl, Boolean sslEnabled) {
        return getQueueUrl(targetUrl, sslEnabled, null, null, null);
    }

    public String getQueueUrl(String targetUrl, Boolean sslEnabled, String domainAlias) {
        return getQueueUrl(targetUrl, sslEnabled, domainAlias, null, null);
    }
        
    public String getQueueUrl(String targetUrl, Boolean sslEnabled, String domainAlias, Locale language) {
        return getQueueUrl(targetUrl, sslEnabled, domainAlias, language, null);
    }
            
    public String getQueueUrl(String targetUrl, Boolean sslEnabled, String domainAlias, Locale language, String layoutName) {
       UriComponentsBuilder queueUrl = getQueueUrlWithoutTarget(sslEnabled, domainAlias, language, layoutName);

        includeTargetUrl(targetUrl, queueUrl);

        return queueUrl.build(true).toUriString();
    }
    
    public String getCancelUrl() {
        return this.getCancelUrl(null, null, null, null);
    }
    
    public String getCancelUrl(String landingPage) {
        return this.getCancelUrl(landingPage, null, null, null);    
    }
    
    public String getCancelUrl(String landingPage, UUID queueId) {
        return this.getCancelUrl(landingPage, queueId, null, null);    
    }
    
    public String getCancelUrl(String landingPage, UUID queueId, Boolean sslEnabled) {
        return this.getCancelUrl(landingPage, queueId, sslEnabled, null);    
    }
    
    public String getCancelUrl(String landingPage, UUID queueId, Boolean sslEnabled, String domainAlias) {
        UriComponentsBuilder cancelUrl = domainAlias != null
            ? generateCancelUrl(sslEnabled, domainAlias)
            : UriComponentsBuilder.fromUriString(this.defaultCancelUrl);

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

        return cancelUrl.build(true).toUriString();  
    }
    
    public String getLandingPageUrl() {
        return getLandingPageUrlFromCurrentUrl(null);
    }
    
    public String getLandingPageUrl(Boolean includeTargetUrl) {
        return getLandingPageUrlFromCurrentUrl(includeTargetUrl);
    }
    
    private String getLandingPageUrlFromCurrentUrl(Boolean includeTargetUrl) {
        if (this.defaultLandingPageUrl == null)
            return null;

        if ((includeTargetUrl == null || !includeTargetUrl) && !this.defaultIncludeTargetUrl)
            return this.defaultLandingPageUrl.toString();

        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(this.defaultLandingPageUrl);

        includeTargetUrl(includeTargetUrl, builder);

        return builder.build(true).toUriString();
    }
    
    public String getLandingPageUrl(String targetUrl) {
        if (this.defaultLandingPageUrl == null)
            return null;

        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(this.defaultLandingPageUrl);

        includeTargetUrl(targetUrl, builder);

        return builder.build(true).toUriString();
    }
    
    public Boolean getDefaultSslEnabled() {
        return this.defaultSslEnabled;
    }
    
    public Boolean getDefaultIncludeTargetUrl() {
        return this.defaultIncludeTargetUrl;
    }
    
    public String getDefaultDomainAlias() {
        return this.defaultDomainAlias;
    }

    public Locale getDefaultLanguage() {
        return this.defaultLanguage;
    }
        
    public String getDefaultLayoutName() {
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
        {
            String encodedLayoutName = encodeURIComponent(layoutName);
            uri.queryParam("l", encodedLayoutName);      
        }
        
        uri.queryParam("ver", "j" + getVersionNumber());
        
        return uri;
    }
    
    private String getVersionNumber() {
        // Get jarfile url
        String jarUrl = Queue.class
            .getProtectionDomain().getCodeSource()
            .getLocation().getFile();

        try {
            JarFile jar = new JarFile(new File(jarUrl));
            
            Manifest manifest = jar.getManifest();
            Attributes attributes = manifest.getMainAttributes();

            return attributes.getValue(IMPLEMENTATION_VERSION);
            
        } catch (IOException ex) {
            return "unknown";
        } 
    }
    
    private static String encodeURIComponent(String s) {
        String result;

        try {
            result = URLEncoder.encode(s, "UTF-8")
                .replaceAll("\\+", "%20")
                .replaceAll("\\%21", "!")
                .replaceAll("\\%27", "'")
                .replaceAll("\\%28", "(")
                .replaceAll("\\%29", ")")
                .replaceAll("\\%7E", "~");
        } catch (UnsupportedEncodingException e) {
            result = s;
        }

        return result;
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
            : UriComponentsBuilder.fromUriString(this.defaultQueueUrl);

        if (sslEnabled != null)
        {
            queueUrl.scheme(sslEnabled ? "https" : "http");    
        }
        
        return queueUrl;
    }
    
    private void includeTargetUrl(Boolean includeTargetUrl, UriComponentsBuilder queueUrl)
    {
        if (includeTargetUrl == null)
            includeTargetUrl = this.defaultIncludeTargetUrl;

        if (!includeTargetUrl)
            return;
              
        includeTargetUrl(KnownUserFactory.getKnownUserUrlProvider().getUrl(), queueUrl);
    }

    private static void includeTargetUrl(String targetUrl, UriComponentsBuilder queueUrl)
    {
        queueUrl.replaceQueryParam("t", encodeURIComponent(targetUrl));
    }
}