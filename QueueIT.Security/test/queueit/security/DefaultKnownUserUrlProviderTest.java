/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package queueit.security;

import javax.servlet.http.HttpServletRequest;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class DefaultKnownUserUrlProviderTest {
    
    public DefaultKnownUserUrlProviderTest() {
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

    
    @Test
    public void DefaultKnownUserUrlProvider_GetUrl_Test() {
        
        String expectedUrl = "http://some.url/somepath/default.aspx?x=sdfs";
        
        HttpServletRequest request = new MockHttpServletRequest(expectedUrl);
        RequestContext.newInstance(request, null);
        
        DefaultKnownUserUrlProvider provider = new DefaultKnownUserUrlProvider();
        String actualUrl = provider.getUrl();
        
        assertEquals(expectedUrl, actualUrl.toString());       
    }
    
    @Test
    public void DefaultKnownUserUrlProvider_GetUrl_SpecialChars_Test() {
        
        String expectedUrl = "http://some.url/somepath?__utma={646484654654}&__utmb=345345.345:34543&__utmc=1&__utmx=-&__utmz=345345.567.46..56.utmcsr=google|utmccn=%28organic%29|utmcmd=organic|utmctr=%28not%20provided%29&__utmv=-&__utmk=234324";
        
        HttpServletRequest request = new MockHttpServletRequest(expectedUrl);
        RequestContext.newInstance(request, null);
        
        DefaultKnownUserUrlProvider provider = new DefaultKnownUserUrlProvider();
        String actualUrl = provider.getUrl();
        
        assertEquals(expectedUrl, actualUrl.toString());       
    }
    
    @Test
    public void DefaultKnownUserUrlProvider_GetUrl_NoRequestContext_Test() {
        
        String expectedUrl = "http://some.url/somepath/default.aspx?x=sdfs";
        
        HttpServletRequest request = new MockHttpServletRequest(expectedUrl);
        
        DefaultKnownUserUrlProvider provider = new DefaultKnownUserUrlProvider(request);
        String actualUrl = provider.getUrl();
        
        assertEquals(expectedUrl, actualUrl.toString());       
    }
        
    @Test
    public void DefaultKnownUserUrlProvider_GetQueueId_Test() {
        
        String url = "http://q.queue-it.net/inqueue.aspx?prefixc=somecust&prefixe=someevent&prefixq=75985c0d-cfeb-4ede-9141-fc8c09f4bb75&prefixp=13b05d38-0109-46de-a7a5-f51e210b4fd7&prefixts=1367583725&prefixrt=&prefixh=f7d55d1101f4a11621f1006b86d163ce";
        
        HttpServletRequest request = new MockHttpServletRequest(url);
        RequestContext.newInstance(request, null);
        
        DefaultKnownUserUrlProvider provider = new DefaultKnownUserUrlProvider();
                
        assertEquals("75985c0d-cfeb-4ede-9141-fc8c09f4bb75", provider.getQueueId("prefix"));       
    }
    
    @Test
    public void DefaultKnownUserUrlProvider_GetCustomerId_Test() {
        
        String url = "http://q.queue-it.net/inqueue.aspx?prefixc=somecust&prefixe=someevent&prefixq=75985c0d-cfeb-4ede-9141-fc8c09f4bb75&prefixp=13b05d38-0109-46de-a7a5-f51e210b4fd7&prefixts=1367583725&prefixrt=&prefixh=f7d55d1101f4a11621f1006b86d163ce";
        
        HttpServletRequest request = new MockHttpServletRequest(url);
        RequestContext.newInstance(request, null);
        
        DefaultKnownUserUrlProvider provider = new DefaultKnownUserUrlProvider();
                
        assertEquals("somecust", provider.getCustomerId("prefix"));       
    }
    
    @Test
    public void DefaultKnownUserUrlProvider_GetEventId_Test() {
        
        String url = "http://q.queue-it.net/inqueue.aspx?prefixc=somecust&prefixe=someevent&prefixq=75985c0d-cfeb-4ede-9141-fc8c09f4bb75&prefixp=13b05d38-0109-46de-a7a5-f51e210b4fd7&prefixts=1367583725&prefixrt=&prefixh=f7d55d1101f4a11621f1006b86d163ce";
        
        HttpServletRequest request = new MockHttpServletRequest(url);
        RequestContext.newInstance(request, null);
        
        DefaultKnownUserUrlProvider provider = new DefaultKnownUserUrlProvider();
                
        assertEquals("someevent", provider.getEventId("prefix"));       
    }
    
    @Test
    public void DefaultKnownUserUrlProvider_GetPlaceInQueue_Test() {
        
        String url = "http://q.queue-it.net/inqueue.aspx?prefixc=somecust&prefixe=someevent&prefixq=75985c0d-cfeb-4ede-9141-fc8c09f4bb75&prefixp=13b05d38-0109-46de-a7a5-f51e210b4fd7&prefixts=1367583725&prefixrt=&prefixh=f7d55d1101f4a11621f1006b86d163ce";
        
        HttpServletRequest request = new MockHttpServletRequest(url);
        RequestContext.newInstance(request, null);
        
        DefaultKnownUserUrlProvider provider = new DefaultKnownUserUrlProvider();
                
        assertEquals("13b05d38-0109-46de-a7a5-f51e210b4fd7", provider.getPlaceInQueue("prefix"));       
    }
    
     @Test
    public void DefaultKnownUserUrlProvider_GetTimeStamp_Test() {
        
        String url = "http://q.queue-it.net/inqueue.aspx?prefixc=somecust&prefixe=someevent&prefixq=75985c0d-cfeb-4ede-9141-fc8c09f4bb75&prefixp=13b05d38-0109-46de-a7a5-f51e210b4fd7&prefixts=1367583725&prefixrt=&prefixh=f7d55d1101f4a11621f1006b86d163ce";
        
        HttpServletRequest request = new MockHttpServletRequest(url);
        RequestContext.newInstance(request, null);
        
        DefaultKnownUserUrlProvider provider = new DefaultKnownUserUrlProvider();
                
        assertEquals("1367583725", provider.getTimeStamp("prefix"));       
    }
     
    @Test
    public void DefaultKnownUserUrlProvider_GetRedirectType_Test() {
        
        String url = "http://q.queue-it.net/inqueue.aspx?prefixc=somecust&prefixe=someevent&prefixq=75985c0d-cfeb-4ede-9141-fc8c09f4bb75&prefixp=13b05d38-0109-46de-a7a5-f51e210b4fd7&prefixrt=SafetyNet&prefixts=1367583725&prefixh=f7d55d1101f4a11621f1006b86d163ce";
        
        HttpServletRequest request = new MockHttpServletRequest(url);
        RequestContext.newInstance(request, null);
        
        DefaultKnownUserUrlProvider provider = new DefaultKnownUserUrlProvider();
                
        assertEquals("SafetyNet", provider.getRedirectType("prefix"));       
    }
    
    @Test
    public void DefaultKnownUserUrlProvider_GetOriginalUrl_Test() {
        
        String expectedUrl = "http://q.queue-it.net/inqueue.aspx";
        String url = "http://q.queue-it.net/inqueue.aspx?prefixc=somecust&prefixe=someevent&prefixq=75985c0d-cfeb-4ede-9141-fc8c09f4bb75&prefixp=13b05d38-0109-46de-a7a5-f51e210b4fd7&prefixrt=SafetyNet&prefixts=1367583725&prefixh=f7d55d1101f4a11621f1006b86d163ce";
        
        HttpServletRequest request = new MockHttpServletRequest(url);
        RequestContext.newInstance(request, null);
        
        DefaultKnownUserUrlProvider provider = new DefaultKnownUserUrlProvider();
                
        assertEquals(expectedUrl, provider.getOriginalUrl("prefix").toString());       
    }
    
    @Test
    public void DefaultKnownUserUrlProvider_GetOriginalUrlWithParams_Test() {
        
        String expectedUrl = "http://q.queue-it.net/inqueue.aspx?somekey=somevalue";
        String url = "http://q.queue-it.net/inqueue.aspx?somekey=somevalue&prefixc=somecust&prefixe=someevent&prefixq=75985c0d-cfeb-4ede-9141-fc8c09f4bb75&prefixp=13b05d38-0109-46de-a7a5-f51e210b4fd7&prefixrt=SafetyNet&prefixts=1367583725&prefixh=f7d55d1101f4a11621f1006b86d163ce";
        
        HttpServletRequest request = new MockHttpServletRequest(url);
        RequestContext.newInstance(request, null);
        
        DefaultKnownUserUrlProvider provider = new DefaultKnownUserUrlProvider();
                
        assertEquals(expectedUrl, provider.getOriginalUrl("prefix").toString());       
    }
}