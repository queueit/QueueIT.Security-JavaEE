package queueit.security;

import queueit.security.RedirectType;
import queueit.security.Md5KnownUser;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 *
 * @author Queue-it
 */
public class Md5KnownUserTest {

    @Test
    public void Md5KnownUser_Constructor_Test() throws URISyntaxException {
        //Arrange
        UUID expectedQueueId = UUID.randomUUID();
        Date expectedTimeStamp = Calendar.getInstance().getTime();
        Integer expectedPlaceInQueue = 465;
        String expectedCustomerId = "somecust";
        String expectedEventId = "someevent";
        URI expectedOriginalUrl = new URI("http://google.com/");
        RedirectType expectedRedirectType = RedirectType.DirectLink;

        //Act
        Md5KnownUser knownUser = new Md5KnownUser(expectedQueueId, expectedPlaceInQueue, expectedTimeStamp, expectedCustomerId, expectedEventId, expectedRedirectType, expectedOriginalUrl);

        //Assert        
        assertEquals(expectedQueueId, knownUser.getQueueId());
        assertEquals(expectedPlaceInQueue, knownUser.getPlaceInQueue());
        assertEquals(expectedTimeStamp, knownUser.getTimeStamp());
        assertEquals(expectedCustomerId, knownUser.getCustomerId());
        assertEquals(expectedEventId, knownUser.getEventId());
        assertEquals(expectedOriginalUrl, knownUser.getOriginalUrl());
        assertEquals(expectedRedirectType, knownUser.getRedirectType());
    }

    @Test
    public void Md5KnownUser_Constructor_PlaceInQueueNotKnown_Test() {
        //Arrange
        UUID expectedQueueId = UUID.randomUUID();
        Date expectedTimeStamp = Calendar.getInstance().getTime();

        //Act
        Md5KnownUser knownUser = new Md5KnownUser(expectedQueueId, 9999999, expectedTimeStamp, null, null, RedirectType.Unknown, null);

        //Assert        
        assertEquals(expectedQueueId, knownUser.getQueueId());
        assertEquals(null, knownUser.getPlaceInQueue());
        assertEquals(expectedTimeStamp, knownUser.getTimeStamp());
    }
    
        @Test
    public void Md5KnownUser_Constructor_PlaceInQueueIsNull_Test() {
        //Arrange
        UUID expectedQueueId = UUID.randomUUID();
        Date expectedTimeStamp = Calendar.getInstance().getTime();

        //Act
        Md5KnownUser knownUser = new Md5KnownUser(expectedQueueId, null, expectedTimeStamp, null, null, RedirectType.Unknown, null);

        //Assert        
        assertEquals(expectedQueueId, knownUser.getQueueId());
        assertEquals(null, knownUser.getPlaceInQueue());
        assertEquals(expectedTimeStamp, knownUser.getTimeStamp());
    }
}
