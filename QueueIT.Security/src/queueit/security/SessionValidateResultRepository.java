package queueit.security;

import java.util.Date;
import java.util.Properties;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class SessionValidateResultRepository extends ValidateResultRepositoryBase {
            
    static int defaultIdleExpiration = 180;
    static boolean defaultExtendValidity = true;
        
    static {       
        loadConfiguration();
    }
    
    private static void loadConfiguration()
    { 
        try {
            // Load the properties
            Properties props = QueueitProperties.getProperties("queueit.properties");
            defaultIdleExpiration =  Integer.parseInt(props.getProperty("idleExpiration", "180"));
            defaultExtendValidity =  Boolean.parseBoolean(props.getProperty("extendValidity", "true"));
        } catch (Exception e) {
            // no need to handle exception
        }    
    }
    
    public static void configure(Integer idleExpiration, Boolean extendValidity)
    {
        if (idleExpiration != null)
            defaultIdleExpiration = idleExpiration;        
        if (extendValidity != null)
            defaultExtendValidity = extendValidity;  
    }
    @Override
    public IValidateResult getValidationResult(IQueue queue) {
        HttpServletRequest request = RequestContext.getCurrentInstance().getRequest();
        HttpSession session = request.getSession(true);
        
        String key = generateKey(queue.getCustomerId(), queue.getEventId());
        SessionStateModel model = (SessionStateModel)session.getAttribute(key);
        
        if (model == null)
            return null;
        
        if (model.Expiration != null && model.Expiration.getTime() < (new Date()).getTime())
            return null;
        
        return new AcceptedConfirmedResult(
            queue, 
            new Md5KnownUser(
                model.QueueId, 
                model.PlaceInQueue, 
                model.TimeStamp, 
                queue.getCustomerId(), 
                queue.getEventId(), 
                model.RedirectType, 
                model.OriginalUrl), 
            true);
    }

    @Override
    public void setValidationResult(IQueue queue, IValidateResult validationResult) {
        this.setValidationResult(queue, validationResult, null);
    }
   
    @Override
    public void setValidationResult(IQueue queue, IValidateResult validationResult, Date expirationTime) {
    
        if (validationResult instanceof AcceptedConfirmedResult)
        {
            if (defaultExtendValidity && expirationTime == null) 
            {
                HttpServletRequest request = RequestContext.getCurrentInstance().getRequest();
                HttpSession session = request.getSession(true);
        
                expirationTime = new Date(System.currentTimeMillis()+(session.getMaxInactiveInterval()*1000));
            }
            
            AcceptedConfirmedResult confirmedResult = (AcceptedConfirmedResult)validationResult;
            
            HttpServletRequest request = RequestContext.getCurrentInstance().getRequest();
            HttpSession session = request.getSession(true);
        
            String key = generateKey(queue.getCustomerId(), queue.getEventId());
            
            SessionStateModel model = new SessionStateModel();
            model.QueueId = confirmedResult.getKnownUser().getQueueId();
            model.OriginalUrl = confirmedResult.getKnownUser().getOriginalUrl();
            model.TimeStamp = confirmedResult.getKnownUser().getTimeStamp();
            model.RedirectType = confirmedResult.getKnownUser().getRedirectType();
            model.PlaceInQueue = confirmedResult.getKnownUser().getPlaceInQueue();
            
            if (expirationTime != null)
                model.Expiration = expirationTime;
            else if (confirmedResult.getKnownUser().getRedirectType() == RedirectType.Idle)
                model.Expiration = new Date(System.currentTimeMillis()+(defaultIdleExpiration*1000));
          
            session.setAttribute(key, model);
        }
    }       

    @Override
    public void cancel(IQueue queue, IValidateResult validationResult) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
