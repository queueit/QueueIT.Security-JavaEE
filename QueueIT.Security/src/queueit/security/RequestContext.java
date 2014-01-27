package queueit.security;

import javax.servlet.http.HttpServletRequest;

public class RequestContext {
    private static ThreadLocal<RequestContext> instance = new ThreadLocal<RequestContext>();
    private HttpServletRequest request;

    private RequestContext(HttpServletRequest request) {
        this.request = request;
    }

    public static RequestContext getCurrentInstance() {
        return instance.get();
    }

    public static RequestContext newInstance(HttpServletRequest request) {
        RequestContext context = new RequestContext(request);
        instance.set(context);
        return context;
    }

    public void release() {
        instance.remove();
    }

    public HttpServletRequest getRequest() {
        return request;
    }
}
