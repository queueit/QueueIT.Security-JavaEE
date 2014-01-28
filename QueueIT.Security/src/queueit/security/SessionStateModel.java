package queueit.security;

import java.net.URI;
import java.util.Date;
import java.util.UUID;

public class SessionStateModel implements java.io.Serializable {
    public UUID QueueId;
    public URI OriginalUrl;
    public Date TimeStamp;
    public RedirectType RedirectType;
    public Integer PlaceInQueue;
}
