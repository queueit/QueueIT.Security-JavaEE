package queueit.security;

import java.util.Date;
import java.util.UUID;

public class SessionStateModel implements java.io.Serializable {
    public UUID QueueId;
    public String OriginalUrl;
    public Date TimeStamp;
    public RedirectType RedirectType;
    public Integer PlaceInQueue;
    public Date Expiration;
}
