package queueit.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class SessionValidateResultRepository extends ValidateResultRepositoryBase {
    
        
    @Override
    public IValidateResult getValidationResult(IQueue queue) {
        HttpServletRequest request = RequestContext.getCurrentInstance().getRequest();
        HttpSession session = request.getSession(true);
        
        String key = generateKey(queue.getCustomerId(), queue.getEventId());
        return (IValidateResult)session.getAttribute(key);
    }

    @Override
    public void setValidationResult(IQueue queue, IValidateResult validationResult) {
        HttpServletRequest request = RequestContext.getCurrentInstance().getRequest();
        HttpSession session = request.getSession(true);
        
        String key = generateKey(queue.getCustomerId(), queue.getEventId());
        session.setAttribute(key, validationResult);
    }       
}
