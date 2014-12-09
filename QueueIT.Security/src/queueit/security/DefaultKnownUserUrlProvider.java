/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package queueit.security;

import java.util.Map;
import javax.servlet.http.HttpServletRequest;

public class DefaultKnownUserUrlProvider implements IKnownUserUrlProvider {
    private Map<String, String[]> querystringParms;
    private String requestUrl;
    
    public DefaultKnownUserUrlProvider() {      
        HttpServletRequest request = RequestContext.getCurrentInstance().getRequest();
        parseRequest(request);
    }
    
    public DefaultKnownUserUrlProvider(HttpServletRequest request) {      
        parseRequest(request);
    }
        
    @Override
    public String getUrl() {
        return this.requestUrl;
    }

    @Override
    public String getQueueId(String queryStringPrefix) {
        return getParam(queryStringPrefix, "q");
    }

    @Override
    public String getPlaceInQueue(String queryStringPrefix) {
        return getParam(queryStringPrefix, "p");
    }

    @Override
    public String getTimeStamp(String queryStringPrefix) {
        return getParam(queryStringPrefix, "ts");
    }

    @Override
    public String getEventId(String queryStringPrefix) {
        return getParam(queryStringPrefix, "e");
    }

    @Override
    public String getCustomerId(String queryStringPrefix) {
        return getParam(queryStringPrefix, "c");
    }

    @Override
    public String getOriginalUrl(String queryStringPrefix) {
        String url = this.requestUrl;
	url = url.replaceAll("(?i)([\\?&])(" + queryStringPrefix + "q=[^&]*&?)", "$1");
	url = url.replaceAll("(?i)([\\?&])(" + queryStringPrefix + "p=[^&]*&?)", "$1");
	url = url.replaceAll("(?i)([\\?&])(" + queryStringPrefix + "ts=[^&]*&?)", "$1");
	url = url.replaceAll("(?i)([\\?&])(" + queryStringPrefix + "c=[^&]*&?)", "$1");
	url = url.replaceAll("(?i)([\\?&])(" + queryStringPrefix + "e=[^&]*&?)", "$1");
	url = url.replaceAll("(?i)([\\?&])(" + queryStringPrefix + "rt=[^&]*&?)", "$1");
	url = url.replaceAll("(?i)([\\?&])(" + queryStringPrefix + "h=[^&]*&?)", "$1");
	url = url.replaceAll("[\\?&]$", "");
        
        return url;
    }

    @Override
    public String getRedirectType(String queryStringPrefix) {
        return getParam(queryStringPrefix, "rt");
    }
    
    private String getParam(String queryStringPrefix, String key) {
            if (!this.querystringParms.containsKey(queryStringPrefix.concat(key))) {
            return null;
        }
        return this.querystringParms.get(queryStringPrefix.concat(key))[0];
    }
    
    private void parseRequest(HttpServletRequest request) {
        if (request != null) {
            StringBuffer requestURL = request.getRequestURL();
            String queryString = request.getQueryString();

            if (queryString == null) {
                this.requestUrl = requestURL.toString();
            } else {
                this.requestUrl = requestURL.append('?').append(queryString).toString();
            }
            
            this.querystringParms = request.getParameterMap();
        }
    }
}
