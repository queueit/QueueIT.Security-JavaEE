package queueit.security;

import java.net.URI;
import java.util.Date;
import java.util.UUID;

/**
 *
 * @author Queue-it
 * last update: 2012-10-25
 */
class Md5KnownUser extends KnownUserBase {
    Md5KnownUser(UUID queueId, Integer placeInQueue, Date timeStamp, String customerId, String eventId, RedirectType redirectType, URI originalUrl) {
        this.queueId = queueId;
        this.setPlaceInQueue(placeInQueue);
        this.timeStamp = timeStamp;
        this.customerId = customerId;
        this.eventId = eventId;
        this.originalUrl = originalUrl;
        this.redirectType = redirectType;
    }   
}
