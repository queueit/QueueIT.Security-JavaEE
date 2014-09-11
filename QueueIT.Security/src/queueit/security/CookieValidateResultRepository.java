package queueit.security;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Properties;
import java.util.TimeZone;
import java.util.UUID;
import javax.servlet.http.*;
import javax.xml.bind.DatatypeConverter;

public class CookieValidateResultRepository extends ValidateResultRepositoryBase {

    static String defaultCookieDomain;
    static int defaultCookieExpiration = 1200;
    static int defaultIdleExpiration = 180;
    static int defaultDisabledExpiration = 180;
    
    static {       
        loadConfiguration();
    }
    
    private static void loadConfiguration()
    { 
        try {
            // Load the properties
            Properties props = QueueitProperties.getProperties("queueit.properties");
            defaultCookieDomain = props.getProperty("cookieDomain", null);
            defaultCookieExpiration =  Integer.parseInt(props.getProperty("cookieExpiration", "1200"));
            defaultIdleExpiration =  Integer.parseInt(props.getProperty("idleExpiration", "180"));
            defaultDisabledExpiration =  Integer.parseInt(props.getProperty("disabledExpiration", "180"));
        } catch (Exception e) {
            // no need to handle exception
        }    
    }
    
    public static void configure(String cookieDomain, Integer cookieExpiration, Integer idleExpiration, Integer disabledExpiration)
    {
        if (cookieDomain != null)
            defaultCookieDomain = cookieDomain;
        if (cookieExpiration != null)
            defaultCookieExpiration = cookieExpiration;
        if (idleExpiration != null)
            defaultIdleExpiration = idleExpiration;        
        if (disabledExpiration != null)
            defaultDisabledExpiration = disabledExpiration;        
    }
    
    @Override
    public IValidateResult getValidationResult(IQueue queue) {
        
        String key = generateKey(queue.getCustomerId(), queue.getEventId());
        HttpServletRequest request = RequestContext.getCurrentInstance().getRequest();
        
        String queueId = null;
        String originalUrl = null;
        String encryptedPlaceInQueue = null;
        RedirectType redirectType = null;
        String timeStamp = null;
        String actualHash = null;
        Integer placeInQueue = 0;
        
        Cookie[] cookies = request.getCookies();
        for (int i = 0; i < cookies.length; i++)
        {
            if (cookies[i].getName().equals(key + "-QueueId"))
                queueId = cookies[i].getValue();
            if (cookies[i].getName().equals(key + "-OriginalUrl"))
                originalUrl = cookies[i].getValue();
            if (cookies[i].getName().equals(key + "-PlaceInQueue"))
                encryptedPlaceInQueue = cookies[i].getValue();
            if (cookies[i].getName().equals(key + "-RedirectType"))
                redirectType = RedirectType.fromString(cookies[i].getValue());
            if (cookies[i].getName().equals(key + "-TimeStamp"))
                timeStamp = cookies[i].getValue();
            if (cookies[i].getName().equals(key + "-Hash"))
                actualHash = cookies[i].getValue();
        }
        
        if (queueId == null || originalUrl == null || encryptedPlaceInQueue == null || redirectType == null || timeStamp == null)
            return null;
        
        try
        {
            placeInQueue = Hashing.decryptPlaceInQueue(encryptedPlaceInQueue);
        } catch (InvalidKnownUserUrlException ex) {
            return null;
        }
            
        String expectedHash = generateHash(queueId, originalUrl, placeInQueue, redirectType.toString(), timeStamp);
        
        if (!expectedHash.equals(actualHash))
            return null;
        
        if (redirectType != RedirectType.Disabled && redirectType != RedirectType.Idle)
            setCookie(queue, queueId, originalUrl, placeInQueue, redirectType, timeStamp, actualHash, null);
        
        return new AcceptedConfirmedResult(
                queue, 
                new Md5KnownUser(
                    UUID.fromString(queueId),
                    placeInQueue, 
                    new Date(Integer.parseInt(timeStamp)), 
                    queue.getCustomerId(), 
                    queue.getEventId(), 
                    redirectType, 
                    URI.create(originalUrl)), 
                false);
    }

    @Override
    public void setValidationResult(IQueue queue, IValidateResult validationResult) {
        this.setValidationResult(queue, validationResult, null);
    }
    
    @Override
    public void setValidationResult(IQueue queue, IValidateResult validationResult, Date expirationTime) {
        
        if (validationResult instanceof AcceptedConfirmedResult)
        {   
            AcceptedConfirmedResult confirmedResult = (AcceptedConfirmedResult)validationResult;

            String queueId = confirmedResult.getKnownUser().getQueueId().toString();
            String originalUrl = confirmedResult.getKnownUser().getOriginalUrl().toString();
            Integer placeInQueue = confirmedResult.getKnownUser().getPlaceInQueue();
            RedirectType redirectType = confirmedResult.getKnownUser().getRedirectType();
            Long timeStamp = confirmedResult.getKnownUser().getTimeStamp().getTime() / 1000;
            
            String hash = generateHash(queueId, originalUrl, placeInQueue, redirectType.toString(), timeStamp.toString());

            setCookie(queue, queueId, originalUrl, placeInQueue, redirectType, timeStamp.toString(), hash, expirationTime);
        }                
    }
    
    private void setCookie(
        IQueue queue, 
        String queueId, 
        String originalUrl, 
        Integer placeInQueue, 
        RedirectType redirectType, 
        String timeStamp, 
        String hash,
        Date expirationTime)
    {
        String key = generateKey(queue.getCustomerId(), queue.getEventId());
        HttpServletResponse response = RequestContext.getCurrentInstance().getResponse();
        
        int expiration;
        Date now = new Date();
        
        if (expirationTime != null)
            expiration = (int)(expirationTime.getTime() - now.getTime());
        else if (redirectType == RedirectType.Disabled)
            expiration = defaultDisabledExpiration;
        else if (redirectType == RedirectType.Idle)
            expiration = defaultIdleExpiration;
        else
            expiration = defaultCookieExpiration;

        this.clearExistingCookies(response, key, expiration);
        
        addCookie(new Cookie(key + "-QueueId", queueId), response, expiration);
        addCookie(new Cookie(key + "-OriginalUrl", originalUrl), response, expiration);
        addCookie(new Cookie(key + "-PlaceInQueue", Hashing.encryptPlaceInQueue(placeInQueue)), response, expiration);
        addCookie(new Cookie(key + "-RedirectType", redirectType.toString()), response, expiration);
        addCookie(new Cookie(key + "-TimeStamp", timeStamp.toString()), response, expiration);
        addCookie(new Cookie(key + "-Hash", hash), response, expiration);           
    }
    
    private void addCookie(Cookie cookie, HttpServletResponse response, int maxAge)
    {                     
        cookie.setHttpOnly(true);      
        cookie.setMaxAge(maxAge < 0 ? 0 : maxAge);
        if (defaultCookieDomain != null)
            cookie.setDomain(defaultCookieDomain);
               
        response.addCookie(cookie);
    }
    
    private void clearExistingCookies(HttpServletResponse response, String key, int maxAge)
    {
        Collection<String> cookieHeaders = response.getHeaders("Set-Cookie");
        
        if (maxAge > 0)
        {
            Date expdate= new Date();
            expdate.setTime (expdate.getTime() + (maxAge * 1000));
            DateFormat df = new SimpleDateFormat("dd-MMM-yyyy kk:mm:ss z");
            df.setTimeZone(TimeZone.getTimeZone("GMT"));

            response.setHeader("Set-Cookie", key + "=true; Expires=" + df.format(expdate) + ";");
        }
        else
        {
            response.setHeader("Set-Cookie", key + "=true; Expires=Thu, 11-Sep-2014 11:27:49 GMT;");
        }
        
        for (String cookieHeader : cookieHeaders)
        {
            if (!cookieHeader.contains(key))
            {
                response.addHeader("Set-Cookie", cookieHeader);
            }
        }
    }

    private String generateHash(String queueId, String originalUrl, Integer placeInQueue, String redirectType, String timeStamp) {
        try {
            StringBuilder sb = new StringBuilder();
            sb.append(queueId).append(originalUrl).append(placeInQueue != null ? placeInQueue.toString() : "0").append(redirectType).append(timeStamp).append(KnownUserFactory.getSecretKey());
                    
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(sb.toString().getBytes("UTF-8"));
            
            return DatatypeConverter.printHexBinary(hash).toLowerCase();
            
        } catch (NoSuchAlgorithmException ex) {
            // No such exception
            return null;
        } catch (UnsupportedEncodingException ex) {
            // No such exception
            return null;
        }
    }

    @Override
    public void cancel(IQueue queue, IValidateResult validationResult) {
        this.setValidationResult(queue, validationResult, new Date(System.currentTimeMillis()-24*60*60*1000));
    }
}
