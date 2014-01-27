package queueit.security;

import java.net.URI;

public interface IKnownUserUrlProvider {
    URI getUrl();
    String getQueueId(String queryStringPrefix);
    String getPlaceInQueue(String queryStringPrefix);
    String getTimeStamp(String queryStringPrefix);
    String getEventId(String queryStringPrefix);
    String getCustomerId(String queryStringPrefix);
    URI getOriginalUrl(String queryStringPrefix);
    String getRedirectType(String queryStringPrefix);
}
