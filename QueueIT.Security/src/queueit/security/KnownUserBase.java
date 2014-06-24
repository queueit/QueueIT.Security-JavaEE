package queueit.security;

import java.net.URI;
import java.util.Date;
import java.util.UUID;

/**
 *
 * @author Queue-it
 * last update: 2012-10-25
 */
abstract class KnownUserBase implements IKnownUser {
    protected UUID queueId;
    protected Integer placeInQueue;
    protected Date timeStamp;
    protected String customerId;
    protected String eventId;
    protected URI originalUrl;
    protected RedirectType redirectType;
    
    @Override
    public Integer getPlaceInQueue() {
        return placeInQueue;
    }
    
    @Override
    public UUID getQueueId() {
        return this.queueId;
    } 

    @Override
    public Date getTimeStamp() {
        return this.timeStamp;
    }

    @Override
    public String getCustomerId() {
        return this.customerId;
    }
   
    @Override
    public String getEventId() {
        return this.eventId;
    }

    @Override
    public URI getOriginalUrl() {
        return this.originalUrl;
    }

    @Override
    public RedirectType getRedirectType() {
        return this.redirectType;
    }   
    
    void setPlaceInQueue(Integer value) {
        if (value == null)
            this.placeInQueue = null;
        else if (value <= 0 || value >= 9999999) {
            this.placeInQueue = null;
        } else {
            this.placeInQueue = value;
        }
    }
}
