
package queueit.security;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.URI;
import java.util.Date;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class SessionStateModelTest {
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

    
    @Test
    public void SessionStateModel_Serialize_Test(){
        SessionStateModel model = new SessionStateModel();
        model.OriginalUrl = URI.create("http://www.queue-it.net");
        model.PlaceInQueue = 1574;
        model.QueueId = UUID.randomUUID();
        model.RedirectType = RedirectType.AfterEvent;
        model.TimeStamp = new Date();

         ByteArrayOutputStream stream = new ByteArrayOutputStream(1024);
         ObjectOutputStream out;
        try {
            out = new ObjectOutputStream(stream);
             out.writeObject(model);
             out.close();
             stream.close();        
        } catch (IOException ex) {
            assertTrue(false);
        }

        assertTrue(true);
    }
}
