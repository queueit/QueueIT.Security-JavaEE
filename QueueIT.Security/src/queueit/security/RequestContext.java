package queueit.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RequestContext {
    private static ThreadLocal<RequestContext> instance = new ThreadLocal<RequestContext>();
    private HttpServletRequest request;
    private HttpServletResponse response;

    private RequestContext(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;                
    }

    public static RequestContext getCurrentInstance() {
        return instance.get();
    }

    public static RequestContext newInstance(HttpServletRequest request, HttpServletResponse response) {
        RequestContext context = new RequestContext(request, response);
        instance.set(context);
        return context;
    }

    public void release() {
        instance.remove();
    }

    public HttpServletRequest getRequest() {
        return request;
    }
    
    public HttpServletResponse getResponse() {
        return response;
    }
}
