package queueit.security;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

public class RequestContextFilter implements Filter {

    @Override
    public void init(FilterConfig fc) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        RequestContext context = RequestContext.newInstance((HttpServletRequest) request);
        try {
           chain.doFilter(request, response);
        } finally {
            context.release();
        }
    }

    @Override
    public void destroy() {

    }
    
}
