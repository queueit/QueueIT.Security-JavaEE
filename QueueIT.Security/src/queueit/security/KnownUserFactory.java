package queueit.security;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class KnownUserFactory {

    private static String defaultSecretKey;
    private static String defaultQuerystringPrefix;
    private static Callable<IKnownUserUrlProvider> defaultUrlProviderFactory;
    public static String getSecretKey() {
        return defaultSecretKey;
    }
    
    static {
        defaultUrlProviderFactory = new Callable<IKnownUserUrlProvider>() {
            public IKnownUserUrlProvider call() {
                return new DefaultKnownUserUrlProvider();
            }
        };
        
        loadConfiguration();
    }
    
    private static void loadConfiguration()
    {
        try {
            // Load the properties
            Properties props = QueueitProperties.getProperties("queueit.properties");
            defaultSecretKey = props.getProperty("secretKey", null);
            defaultQuerystringPrefix = props.getProperty("queryStringPrefix", null);
        } catch (Exception e) {
            // Ignore
        }        
    }
    
    public static void configure(String sharedEventKey) {
        configure(sharedEventKey, null, null);
    }
        
    public static void configure(String sharedEventKey, Callable<IKnownUserUrlProvider> urlProviderFactory) {
         configure(sharedEventKey, urlProviderFactory, null);       
    }

    public static void configure(String sharedEventKey, String querystringPrefix) {
         configure(sharedEventKey, null, querystringPrefix);       
    }
    public static void configure(
            String secretKey,
            Callable<IKnownUserUrlProvider> urlProviderFactory,
            String querystringPrefix) {
        if (secretKey != null) {
            defaultSecretKey = secretKey;
        }
        if (urlProviderFactory != null) {
            defaultUrlProviderFactory = urlProviderFactory;
        }
        if (querystringPrefix != null) {
            defaultQuerystringPrefix = querystringPrefix;
        }       
    }

    public static IKnownUser verifyMd5Hash() 
        throws InvalidKnownUserUrlException, InvalidKnownUserHashException {
        return verifyMd5Hash(null, null, null);
    }
        
    public static IKnownUser verifyMd5Hash(String secretKey, String querystringPrefix) 
        throws InvalidKnownUserUrlException, InvalidKnownUserHashException {
        return verifyMd5Hash(secretKey, null, querystringPrefix);
    }
        
    public static IKnownUser verifyMd5Hash(String secretKey) 
        throws InvalidKnownUserUrlException, InvalidKnownUserHashException {
        return verifyMd5Hash(secretKey, null, null);
    }
        
    public static IKnownUser verifyMd5Hash(String secretKey, IKnownUserUrlProvider urlProvider) 
        throws InvalidKnownUserUrlException, InvalidKnownUserHashException {
        return verifyMd5Hash(secretKey, urlProvider, null);
    }
            
    public static IKnownUser verifyMd5Hash(String secretKey, IKnownUserUrlProvider urlProvider, String querystringPrefix) 
            throws InvalidKnownUserUrlException, InvalidKnownUserHashException {

        if ((secretKey == null) || (secretKey.isEmpty())) {
            secretKey = defaultSecretKey;
        }
        
        if ((querystringPrefix == null) || (querystringPrefix.isEmpty())) {
            querystringPrefix = defaultQuerystringPrefix;
        }

        if (urlProvider == null) {
            try {
                urlProvider = defaultUrlProviderFactory.call();
            } catch (Exception ex) {
                throw new InvalidKnownUserUrlException();
            }
        }
        
        if ((secretKey == null) || (secretKey.isEmpty())) {
            throw new IllegalArgumentException("The Secret Key cannot be null. Invoke KnownUserFactory. Configure or add configuration in config file.");
        }
        if (querystringPrefix == null) {
            querystringPrefix = "";
        }
        
        String url = urlProvider.getUrl();
        
        validateUrl(url);
        
        String originalUrl = urlProvider.getOriginalUrl(querystringPrefix);
        
        try {
            UUID queueId = parseQueueId(urlProvider.getQueueId(querystringPrefix));
            String placeInQueueObfuscated = urlProvider.getPlaceInQueue(querystringPrefix);
            Integer placeInQueue = null; 
            if (placeInQueueObfuscated != null && !placeInQueueObfuscated.isEmpty())
            {
                try
                {
                    placeInQueue = (int)Hashing.decryptPlaceInQueue(placeInQueueObfuscated);
                }
                catch (Exception ex)
                {
                    throw new InvalidKnownUserUrlException();
                }
            }

            Date timeStamp = parseTimeStamp(urlProvider.getTimeStamp(querystringPrefix));
            String customerId = urlProvider.getCustomerId(querystringPrefix);
            String eventId = urlProvider.getEventId(querystringPrefix);
            RedirectType redirectType = parseRedirectType(urlProvider.getRedirectType(querystringPrefix));

            if (queueId == null && placeInQueue == null && timeStamp == null)
                return null;

            if (queueId == null || placeInQueue == null || timeStamp == null)
                throw new InvalidKnownUserUrlException();

            String expectedHash = getExpectedHash(url);

            validateHash(url, secretKey, expectedHash);

            return new Md5KnownUser(queueId, placeInQueue, timeStamp, customerId, eventId, redirectType, originalUrl);
        } catch (KnownUserException ex) {
            ex.setOriginalUrl(originalUrl);
            ex.setValidationUrl(url);
            throw ex;
        }
    }

    private static RedirectType parseRedirectType(String redirectType) {
        if (redirectType == null || redirectType.isEmpty()) {
            return RedirectType.Unknown;
        }       

        return RedirectType.fromString(redirectType);
    }
    
    private static UUID parseQueueId(String getQueueId) {
        if (getQueueId == null || getQueueId.isEmpty()) {
            return null;
        }
        try {
        return UUID.fromString(getQueueId);
        } catch (IllegalArgumentException ex) {
            throw new InvalidKnownUserUrlException();
        }
    }

    private static Integer getPlaceInQueue(Map<String, List<String>> querystringParms, String querystringPrefix) {
        try {
            if (!querystringParms.containsKey(querystringPrefix.concat("p"))) {
                return null;
            }
            String placeInQueue = querystringParms.get(querystringPrefix.concat("p")).get(0);
            if (placeInQueue.length() != 36) {
                throw new InvalidKnownUserUrlException();
            }
            return Hashing.decryptPlaceInQueue(placeInQueue);
        } catch (Exception e) {
            throw new InvalidKnownUserUrlException();
        }
    }

    private static Date parseTimeStamp(String timeStamp) {
        try {
            if (timeStamp == null || timeStamp.isEmpty()) {
                return null;
            }
            
            Integer timestampSeconds = Integer.parseInt(timeStamp);
            return new java.util.Date((long) timestampSeconds * 1000);
        } catch (Exception e) {
            throw new InvalidKnownUserUrlException();
        }
    }

    private static String getCustomerId(Map<String, List<String>> querystringParms, String querystringPrefix) {
        if (!querystringParms.containsKey(querystringPrefix.concat("c"))) {
            return null;
        }
        return querystringParms.get(querystringPrefix.concat("c")).get(0);
    }

    private static String getEventId(Map<String, List<String>> querystringParms, String querystringPrefix) {
        if (!querystringParms.containsKey(querystringPrefix.concat("e"))) {
            return null;
        }
        return querystringParms.get(querystringPrefix.concat("e")).get(0);
    }

    private static String getExpectedHash(String url)
    {
        String fullUrl = url;

        if (fullUrl == null || fullUrl.length() < 32)
            throw new InvalidKnownUserHashException();

        return fullUrl.substring(fullUrl.length() - 32);
    }

    private static void validateHash(String requestUrl, String sharedEventKey, String expectedHash) {
        String stringToHash = requestUrl.substring(0, requestUrl.length() - 32) + sharedEventKey; //Remove hash value and add SharedEventKey
        String actualHashHttp = Hashing.getMd5Hash(stringToHash.replaceFirst("^https://(.*)$", "http://$1"));
        String actualHashHttps = Hashing.getMd5Hash(stringToHash.replaceFirst("^http://(.*)$", "https://$1"));

        if (actualHashHttp.equals(expectedHash) || actualHashHttps.equals(expectedHash)) {
            return;
        }
        
        throw new InvalidKnownUserHashException();
    }

    private static void validateUrl(String url) {
        if (url == null)
            throw new InvalidKnownUserUrlException();
        if (url.isEmpty())
            throw new InvalidKnownUserUrlException();
        
        Pattern pattern = Pattern.compile("^https?://.*$");
        Matcher matcher = pattern.matcher(url);
        if (!matcher.matches())
            throw new InvalidKnownUserUrlException();
    }
}
