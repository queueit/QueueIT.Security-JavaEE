package queueit.security;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.Callable;
import javax.servlet.http.*;
import javax.xml.bind.DatatypeConverter;

public class CookieValidateResultRepository extends ValidateResultRepositoryBase {

    static String defaultCookieDomain;
    
    static {       
        loadConfiguration();
    }
    
    private static void loadConfiguration()
    {
        try {
            // Load the properties
            Properties props = new Properties();
            ClassLoader classLoader = CookieValidateResultRepository.class.getClassLoader();     
            InputStream configFile = classLoader.getResourceAsStream("queueit.properties");
            if (configFile != null) {
                props.load(configFile);
                defaultCookieDomain = props.getProperty("cookieDomain", null);
            }
        } catch (Exception e) {
            // no need to handle exception
        }      
    }
    @Override
    public IValidateResult getValidationResult(IQueue queue) {
        
        String key = generateKey(queue.getCustomerId(), queue.getEventId());
        HttpServletRequest request = RequestContext.getCurrentInstance().getRequest();
        
        String queueId = null;
        String originalUrl = null;
        String encryptedPlaceInQueue = null;
        String redirectType = null;
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
                redirectType = cookies[i].getValue();
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
            
        String expectedHash = generateHash(queueId, originalUrl, placeInQueue.toString(), redirectType, timeStamp);
        
        if (!expectedHash.equals(actualHash))
            return null;
        
        return new AcceptedConfirmedResult(
                queue, 
                new Md5KnownUser(
                    UUID.fromString(queueId),
                    placeInQueue, 
                    new Date(Integer.parseInt(timeStamp)), 
                    queue.getCustomerId(), 
                    queue.getEventId(), 
                    RedirectType.valueOf(redirectType), 
                    URI.create(originalUrl)), 
                false);
    }

    @Override
    public void setValidationResult(IQueue queue, IValidateResult validationResult) {
        
        if (validationResult instanceof AcceptedConfirmedResult)
        {   
            AcceptedConfirmedResult confirmedResult = (AcceptedConfirmedResult)validationResult;
            
            String key = generateKey(queue.getCustomerId(), queue.getEventId());
            HttpServletResponse response = RequestContext.getCurrentInstance().getResponse();
            
            String queueId = confirmedResult.getKnownUser().getQueueId().toString();
            String originalUrl = confirmedResult.getKnownUser().getOriginalUrl().toString();
            Integer placeInQueue = confirmedResult.getKnownUser().getPlaceInQueue();
            String redirectType = confirmedResult.getKnownUser().getRedirectType().toString();
            Long timeStamp = confirmedResult.getKnownUser().getTimeStamp().getTime() / 1000;
            
            String hash = generateHash(queueId, originalUrl, placeInQueue.toString(), redirectType, timeStamp.toString());

            addCookie(new Cookie(key + "-QueueId", queueId), response);
            addCookie(new Cookie(key + "-OriginalUrl", originalUrl), response);
            addCookie(new Cookie(key + "-PlaceInQueue", Hashing.encryptPlaceInQueue(placeInQueue)), response);
            addCookie(new Cookie(key + "-RedirectType", redirectType), response);
            addCookie(new Cookie(key + "-TimeStamp", timeStamp.toString()), response);
            addCookie(new Cookie(key + "-Hash", hash), response);           
        }                
    }
    
    private void addCookie(Cookie cookie, HttpServletResponse response)
    {
        cookie.setHttpOnly(true);      
        if (defaultCookieDomain != null)
            cookie.setDomain(defaultCookieDomain);
        
        response.addCookie(cookie);
    }

    private String generateHash(String queueId, String originalUrl, String placeInQueue, String redirectType, String timeStamp) {
        try {
            StringBuilder sb = new StringBuilder();
            sb.append(queueId).append(originalUrl).append(placeInQueue).append(redirectType).append(timeStamp).append(KnownUserFactory.getSecretKey());
                    
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
}
