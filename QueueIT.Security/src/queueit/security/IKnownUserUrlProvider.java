package queueit.security;

public interface IKnownUserUrlProvider {
    String getUrl();
    String getQueueId(String queryStringPrefix);
    String getPlaceInQueue(String queryStringPrefix);
    String getTimeStamp(String queryStringPrefix);
    String getEventId(String queryStringPrefix);
    String getCustomerId(String queryStringPrefix);
    String getOriginalUrl(String queryStringPrefix);
    String getRedirectType(String queryStringPrefix);
}
