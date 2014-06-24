package queueit.security;

import queueit.security.InvalidKnownUserUrlException;
import queueit.security.Hashing;
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
public class HashingTest {
    
    public HashingTest() {
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

    /**
     * Test of DecryptPlaceInQueue method, of class Hashing.
     */
    @Test
    public void testDecryptPlaceInQueue() {
        String encryptedPlaceInQueue = "21206da6-3f0a-468d-9325-471d070bbbfd";
        Integer expResult = 3613;
        Integer result = Hashing.decryptPlaceInQueue(encryptedPlaceInQueue);
        assertEquals(expResult, result);
    }
    
    @Test (expected=InvalidKnownUserUrlException.class)
    public void testDecryptPlaceInQueue_null_arg() {
        String encryptedPlaceInQueue = null;
        Integer expResult = 7;
        Integer result = Hashing.decryptPlaceInQueue(encryptedPlaceInQueue);
        assertEquals(expResult, result);
    }
    
    @Test (expected=InvalidKnownUserUrlException.class)
    public void testDecryptPlaceInQueue_empty_arg() {
        String encryptedPlaceInQueue = "";
        Integer expResult = 7;
        Integer result = Hashing.decryptPlaceInQueue(encryptedPlaceInQueue);
        assertEquals(expResult, result);
    }
    
    @Test
    public void encryptPlaceInQueue_null_arg() {
        Integer placeInQueue = null;
        String result = Hashing.encryptPlaceInQueue(placeInQueue);
        char[] resultChars = result.toCharArray();
        assertEquals('0', resultChars[9]);
        assertEquals('0', resultChars[26]);
        assertEquals('0', resultChars[7]);
        assertEquals('0', resultChars[20]);
        assertEquals('0', resultChars[11]);
        assertEquals('0', resultChars[3]);
        assertEquals('0', resultChars[30]);
    }
    
}
