<%@page import="java.net.URI"%>
<%@page import="queueit.security.*"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%
    try
    {
            IValidateResult result = SessionValidationController.validateRequest("advanced", new URI("http://www.google.com"));

            // Check if user must be enqueued
            if (result instanceof EnqueueResult)
            {
                response.sendRedirect(((EnqueueResult)result).getRedirectUrl().toString());
                return;
            }

            // Check if user has been through the queue (will be invoked for every page request after the user has been validated)
            if (result instanceof AcceptedConfirmedResult)
            {
                AcceptedConfirmedResult accepted = (AcceptedConfirmedResult)result;
                    if (accepted.isInitialValidationRequest())
                    {
                        Object[] model = new Object[] {
                                    accepted.getQueue().getCustomerId(),
                                    accepted.getQueue().getEventId(),
                                    accepted.getKnownUser().getQueueId(),
                                    accepted.getKnownUser().getPlaceInQueue(),
                                    accepted.getKnownUser().getTimeStamp()
                        };
                    }
            }
    }
    catch (ExpiredValidationException ex)
    {
        // Known user has has expired - Show error page and use GetCancelUrl to get user back in the queue
        response.sendRedirect("error.jsp?queuename=advanced&t=" + ex.getKnownUser().getOriginalUrl());
        return;
    }
    catch (KnownUserValidationException ex)
    {
        // Known user is invalid - Show error page and use GetCancelUrl to get user back in the queue
        response.sendRedirect("error.jsp?queuename=advanced&t=" + ((KnownUserException)ex.getCause()).getOriginalUrl());
        return;
    }
%>
<t:master>
    <jsp:attribute name="title">
        Advanced
    </jsp:attribute>
    <jsp:attribute name="body">
        <h3>Setting up the queue:</h3>
        <ol class="round">
            <li class="one">
                <h5>Add JSP Servlet Filter</h5>
                Add the queueit.security.RequestContextFilter to the web.xml configuration</li>
            <li class="two">
                <h5>Add properties file</h5>
                This example uses the queue with name &#39;advanced&#39; from the queueit-advanced.properties config file. The 
                entry contains a domain alias which is used when users are redirected to the 
                queue as well as a landing page (split page) allowing users to choose if the 
                want to be redirected to the queue.</li>
            <li class="three">
                <h5>Write controller code</h5>
                Add controller code to the JSP pages (or whatever framework used). The advanced.jsp file
                contains code to extract and persist information about a queue number. The 
                advancedlanding.jsp file contains code to route the user to the queue and back to 
                the advanced.jsp page once the user has been through the queue. </li>
        </ol>
    </jsp:attribute>
</t:master>
