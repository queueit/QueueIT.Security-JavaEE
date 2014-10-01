package queueit.security;

import queueit.security.KnownUserFactory;
import queueit.security.InvalidKnownUserUrlException;
import queueit.security.RedirectType;
import queueit.security.IKnownUser;
import queueit.security.InvalidKnownUserHashException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Queue-it
 */
public class KnownUserFactoryTest {

    public KnownUserFactoryTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }
    private String SharedSecreteEventKey = "zaqxswcdevfrbgtnhymjukiloZAQCDEFRBGTNHYMJUKILOPlkjhgfdsapoiuytrewqmnbvcx";

    private IKnownUser VerifyMd5Hash_Test(String expectedQueueId, long unixTimestamp, Integer expectedPlaceInQueue, String placeInQueueEncrypted, String hash, String querystringPrefix) {
        //Arrange
        Date expectedTimeStamp = null;
        if (querystringPrefix == null) {
        querystringPrefix = "";
        }

        try {
            if (unixTimestamp > 0) {
                expectedTimeStamp = Calendar.getInstance().getTime();
                expectedTimeStamp.setTime(unixTimestamp * 1000);
            }
        } catch (Exception e) {
            //Ignore
        }
        RedirectType expectedRedirectType = RedirectType.Queue;
        String expectedCustomerId = "somecust";
        String expectedEventId = "someevent";
        String expectedOriginalUrl = "http://www.ticketanina.com/shop.aspx?x=sdfsdf";
        String urlWithHash = expectedOriginalUrl
                + "&" + querystringPrefix + "q=" + expectedQueueId
                + "&" + querystringPrefix + "p=" + placeInQueueEncrypted
                + "&" + querystringPrefix + "ts=" + unixTimestamp
                + "&" + querystringPrefix + "c=" + expectedCustomerId
                + "&" + querystringPrefix + "e=" + expectedEventId
                + "&" + querystringPrefix + "h=" + hash;

        MockUrlProvider urlProvider = null; 
        try {
            urlProvider = new MockUrlProvider(new URI(urlWithHash), expectedQueueId, placeInQueueEncrypted, String.valueOf(unixTimestamp), expectedEventId, expectedCustomerId, new URI(expectedOriginalUrl), expectedRedirectType.toString());
        } catch (Exception ex) {
            //ignore
        }
            
        
        //Act
        IKnownUser knownUser = KnownUserFactory.verifyMd5Hash(SharedSecreteEventKey, urlProvider, querystringPrefix);

        //Assert  
        assertEquals(expectedQueueId, knownUser.getQueueId().toString());
        assertEquals(expectedPlaceInQueue, knownUser.getPlaceInQueue());
        assertEquals(expectedTimeStamp, knownUser.getTimeStamp());
        assertEquals(expectedCustomerId, knownUser.getCustomerId());
        assertEquals(expectedEventId, knownUser.getEventId());
        assertEquals(expectedRedirectType, knownUser.getRedirectType());
        assertEquals(expectedOriginalUrl, knownUser.getOriginalUrl().toString());

        return knownUser;
    }

        @Test
    public void VerifyMd5Hash_Test2() {
        //Arrange
        String expectedQueueId = "09a00566-8627-4304-84b0-c0a727805c82";
        Date expectedTimeStamp = null;
        long unixTimestamp = 1351597037;
        try {
            if (unixTimestamp > 0) {
                expectedTimeStamp = Calendar.getInstance().getTime();
                expectedTimeStamp.setTime(unixTimestamp * 1000);
            }
        } catch (Exception e) {
            //Ignore
        }
        String expectedCustomerId = "unic";
        String expectedEventId = "ftuansoger";
        Integer expectedPlaceInQueue = null;
        String urlWithHash = "https://beaks.ssowls.emu.dk/fou/private_queueit_fasttest.do?q=09a00566-8627-4304-84b0-c0a727805c82&p=5809edd9-9590-46a4-b92d-09938a93775f&ts=1351597037&c=unic&e=ftuansoger&h=7c8df4905a913ab978c4ac1d0f2e29d3";
        RedirectType expectedRedirectType = RedirectType.Queue;
        String expectedOriginalUrl = "https://beaks.ssowls.emu.dk/fou/private_queueit_fasttest.do";
        
        SharedSecreteEventKey= "aa487481-b40a-42e5-89e6-b33951edaf9a5ee0b5a2-cb29-4561-8188-8a9380cc6924";
        
        MockUrlProvider urlProvider = null;
        
        try {
            urlProvider = new MockUrlProvider(new URI(urlWithHash), expectedQueueId, "5809edd9-9590-46a4-b92d-09938a93775f", String.valueOf(unixTimestamp), expectedEventId, expectedCustomerId, new URI(expectedOriginalUrl), expectedRedirectType.toString());
        } catch (Exception ex) {
            //ignore
        }
        
        //Act
        try
        {
            IKnownUser knownUser = KnownUserFactory.verifyMd5Hash(SharedSecreteEventKey, urlProvider, "");

            //Assert  
            assertEquals(expectedQueueId, knownUser.getQueueId().toString());
            assertEquals(expectedPlaceInQueue, knownUser.getPlaceInQueue());
            assertEquals(expectedTimeStamp, knownUser.getTimeStamp());
            assertEquals(expectedCustomerId, knownUser.getCustomerId());
            assertEquals(expectedEventId, knownUser.getEventId());
        } catch (Exception ex) {
            assertTrue(false);
        }
    }
    
    @Test
    public void KnownUserFactory_VerifyMd5HashTest_noQueryPrefix_Test() {
        VerifyMd5Hash_Test("a5fcf983-6570-46fa-94bc-4f53aca626ea", 1349166878, 7810, "60506718-010a-40c0-9784-691ba60151ce", "1821d5855c95aa8a380a8aae848707df", "");
    }

    @Test
    public void KnownUserFactory_VerifyMd5HashTest_withQueryPrefix_Test2() {
        VerifyMd5Hash_Test("becb0723-fee7-4a82-87d0-622475c7cf36", 1349168841, 7810, "88401038-0306-4831-b7c0-e115da0bdc46", "d4e47d0b1a732c5dbf3e10d92881ed92", "mpro");
    }

    @Test
    public void KnownUserFactory_VerifyMd5HashTest_nullQueryPrefix_Test2() {
        VerifyMd5Hash_Test("a5fcf983-6570-46fa-94bc-4f53aca626ea", 1349166878, 7810, "60506718-010a-40c0-9784-691ba60151ce", "1821d5855c95aa8a380a8aae848707df", null);
    }
    
    @Test(expected = InvalidKnownUserUrlException.class)
    public void KnownUserFactory_VerifyMd5HashTest_InvalidQueueId_Test() {
        VerifyMd5Hash_Test("INVALID", 1349168841, 7810, "88401038-0306-4831-b7c0-e115da0bdc46", "d4e47d0b1a732c5dbf3e10d92881ed92", "mpro");
    }

    @Test(expected = InvalidKnownUserUrlException.class)
    public void KnownUserFactory_VerifyMd5HashTest_InvalidQueueId2_Test() {
        VerifyMd5Hash_Test("becb0723-", 1349168841, 7810, "88401038-0306-4831-b7c0-e115da0bdc46", "d4e47d0b1a732c5dbf3e10d92881ed92", "mpro");
    }

    @Test(expected = InvalidKnownUserUrlException.class)
    public void KnownUserFactory_VerifyMd5HashTest_InvalidTimestamp_Test() {
        VerifyMd5Hash_Test("becb0723-", -121, 7810, "88401038-0306-4831-b7c0-e115da0bdc46", "d4e47d0b1a732c5dbf3e10d92881ed92", "mpro");
    }

    @Test(expected = InvalidKnownUserHashException.class)
    public void KnownUserFactory_VerifyMd5HashTest_InvalidPlaceInQueue_Test() {
        // "88401038-0306-4831-b7c0-e115da0bdc46" is valid
        // "88201038-0306-4831-b7c0-e115da0bdc46" is NOT valid
        VerifyMd5Hash_Test("becb0723-fee7-4a82-87d0-622475c7cf36", 1349168841, 7810, "88201038-0306-4831-b7c0-e115da0bdc46", "d4e47d0b1a732c5dbf3e10d92881ed92", "mpro");
    }

    @Test(expected = InvalidKnownUserHashException.class)
    public void KnownUserFactory_VerifyMd5HashTest_InvalidHash_Test() {
        // "d4e47d0b1a732c5dbf3e10d92881ed92" is valid hash
        // "d4e47d0b1a732c5dbf3e10d92881ed93" is NOT valid hash
        VerifyMd5Hash_Test("becb0723-fee7-4a82-87d0-622475c7cf36", 1349168841, 7810, "88401038-0306-4831-b7c0-e115da0bdc46", "d4e47d0b1a732c5dbf3e10d92881ed93", "mpro");
    }

    @Test()
    public void KnownUserFactory_VerifyMd5HashTest_KnownUserException_Test() {
        //Arrange
        Date expectedTimeStamp = null;
        String querystringPrefix = "mpro";
        int unixTimestamp = 1349168841;
        String expectedQueueId = "becb0723-fee7-4a82-87d0-622475c7cf36";
        String placeInQueueEncrypted = "88401038-0306-4831-b7c0-e115da0bdc46";
        String hash = "d4e47d0b1a732c5dbf3e10d92881ed93";

        try {
            if (unixTimestamp > 0) {
                expectedTimeStamp = Calendar.getInstance().getTime();
                expectedTimeStamp.setTime(unixTimestamp * 1000);
            }
        } catch (Exception e) {
            //Ignore
        }
        RedirectType expectedRedirectType = RedirectType.Queue;
        String expectedCustomerId = "somecust";
        String expectedEventId = "someevent";
        String expectedOriginalUrl = "http://www.ticketanina.com/shop.aspx?x=sdfsdf";
        String urlWithHash = expectedOriginalUrl
                + "&" + querystringPrefix + "q=" + expectedQueueId
                + "&" + querystringPrefix + "p=" + placeInQueueEncrypted
                + "&" + querystringPrefix + "ts=" + unixTimestamp
                + "&" + querystringPrefix + "c=" + expectedCustomerId
                + "&" + querystringPrefix + "e=" + expectedEventId
                + "&" + querystringPrefix + "h=" + hash;

        MockUrlProvider urlProvider = null; 
        try {
            urlProvider = new MockUrlProvider(new URI(urlWithHash), expectedQueueId, placeInQueueEncrypted, String.valueOf(unixTimestamp), expectedEventId, expectedCustomerId, new URI(expectedOriginalUrl), expectedRedirectType.toString());
        } catch (Exception ex) {
            //ignore
        }
                    
        //Act
        try
        {
            IKnownUser knownUser = KnownUserFactory.verifyMd5Hash(SharedSecreteEventKey, urlProvider, querystringPrefix);
        } catch (KnownUserException ex) {
            //Assert
            assertEquals(expectedOriginalUrl, ex.getOriginalUrl().toString());
            assertEquals(urlWithHash, ex.getValidationUrl().toString());
        }     
    }
        
    @Test(expected = InvalidKnownUserUrlException.class)
    public void KnownUserFactory_VerifyMd5HashTest_NoParameters_Test() {
        // "d4e47d0b1a732c5dbf3e10d92881ed92" is valid hash
        // "d4e47d0b1a732c5dbf3e10d92881ed93" is NOT valid hash
        VerifyMd5Hash_Test(null, 0, 7810, null, null, "");
    }

    @Test(expected = InvalidKnownUserUrlException.class)
    public void KnownUserFactory_VerifyMd5HashTest_OnlyQParameters_Test() {
        // "d4e47d0b1a732c5dbf3e10d92881ed92" is valid hash
        // "d4e47d0b1a732c5dbf3e10d92881ed93" is NOT valid hash
        VerifyMd5Hash_Test("becb0723-fee7-4a82-87d0-622475c7cf36", 0, 7810, null, null, "");
    }

    @Test(expected = InvalidKnownUserUrlException.class)
    public void KnownUserFactory_VerifyMd5HashTest_OnlyPParameters_Test() {
        // "d4e47d0b1a732c5dbf3e10d92881ed92" is valid hash
        // "d4e47d0b1a732c5dbf3e10d92881ed93" is NOT valid hash
        VerifyMd5Hash_Test(null, 0, 7810, "88401038-0306-4831-b7c0-e115da0bdc46", null, "");
    }

    @Test(expected = InvalidKnownUserUrlException.class)
    public void KnownUserFactory_VerifyMd5HashTest_OnlyTSParameters_Test() {
        // "d4e47d0b1a732c5dbf3e10d92881ed92" is valid hash
        // "d4e47d0b1a732c5dbf3e10d92881ed93" is NOT valid hash
        VerifyMd5Hash_Test(null, 1349168841, 7810, null, null, "");
    }

    @Test(expected = InvalidKnownUserUrlException.class)
    public void KnownUserFactory_OriginalUri_NoParameters_Test() {
        IKnownUser knownUser = VerifyMd5Hash_Test(null, 0, 7810, null, null, "");
        assertEquals("http://www.ticketanina.com/shop.aspx?x=sdfsdf", knownUser.getOriginalUrl());
    }

    @Test
    public void KnownUserFactory_OriginalUri_InvalidHash_Test() throws MalformedURLException {
        try {
            VerifyMd5Hash_Test("becb0723-fee7-4a82-87d0-622475c7cf36", 1349168841, 7810, "88401038-0306-4831-b7c0-e115da0bdc46", "d4e47d0b1a732c5dbf3e10d92881ed93", "mpro");
        } catch (InvalidKnownUserHashException e) {
            assertEquals("http://www.ticketanina.com/shop.aspx?x=sdfsdf", e.getOriginalUrl().toString());
        }
    }
    
    @Test
    public void KnownUserFactory_OriginalUri_InvalidUrl_Test() throws MalformedURLException {
        try {
            VerifyMd5Hash_Test("INVALID", 1349168841, 7810, null, null, "mpro");
        } catch (InvalidKnownUserUrlException e) {
            assertEquals("http://www.ticketanina.com/shop.aspx?x=sdfsdf", e.getOriginalUrl().toString());
        }
    }    
}
