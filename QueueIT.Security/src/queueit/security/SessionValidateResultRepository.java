package queueit.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class SessionValidateResultRepository implements IValidateResultRepository {
    private static String SessionQueueId = "QueueITAccepted-SDFrts345E-";
        
    @Override
    public IValidateResult GetValidationResult(IQueue queue) {
        HttpServletRequest request = RequestContext.getCurrentInstance().getRequest();
        HttpSession session = request.getSession(true);
        
        String key = generateKey(queue.getCustomerId(), queue.getEventId());
        return (IValidateResult)session.getAttribute(key);
    }

    @Override
    public void SetValidationResult(IQueue queue, IValidateResult validationResult) {
        HttpServletRequest request = RequestContext.getCurrentInstance().getRequest();
        HttpSession session = request.getSession(true);
        
        String key = generateKey(queue.getCustomerId(), queue.getEventId());
        session.setAttribute(key, validationResult);
    }
    
    private static String generateKey(String customerId, String eventId)
    {
        return SessionQueueId + customerId + "-" + eventId;
    }
}
