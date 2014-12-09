<%@page import="java.util.concurrent.Callable"%>
<%@page import="queueit.security.*"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%
    String cancelUrl = null;
    String expireUrl = null;
    
    try
    {
        IValidateResult result = SessionValidationController.validateRequest();

        
        // Check if user must be enqueued
        if (result instanceof EnqueueResult)
        {
            response.sendRedirect(((EnqueueResult)result).getRedirectUrl().toString());
            return;
        }
            
        if (result instanceof AcceptedConfirmedResult)
        {
            String currentUrl = request.getRequestURL().toString();
            cancelUrl = result.getQueue().getCancelUrl(
                    currentUrl.substring(0, currentUrl.indexOf("simple.jsp")) + "cancel.jsp?eventId=simple");
            expireUrl = "expire.jsp?eventid=simple";
            
            request.setAttribute("cancelUrl", cancelUrl);
            request.setAttribute("expireUrl", expireUrl);
        }
    }
    catch (ExpiredValidationException ex)
    {
        // Known user has has expired - Show error page and use GetCancelUrl to get user back in the queue
        response.sendRedirect("error.jsp?queuename=default&t=" + ex.getKnownUser().getOriginalUrl());
        return;
    }
    catch (KnownUserValidationException ex)
    {
        // Known user is invalid - Show error page and use GetCancelUrl to get user back in the queue
        response.sendRedirect("error.jsp?queuename=default&t=" + ((KnownUserException)ex.getCause()).getOriginalUrl());
        return;
    }
%>
<t:master>
    <jsp:attribute name="title">
        Simple Queue Configuration
    </jsp:attribute>
    <jsp:attribute name="body">
        <h3>Setting up the queue:</h3>
    <ol class="round">
        <li class="one">
            <h5>Add JSP Servlet Filter</h5>
            Add the queueit.security.RequestContextFilter to the web.xml configuration</li>
        <li class="two">
            <h5>Add configuration section to queueit-default.properties config file</h5>
            This example uses the queue with name &#39;default&#39; from the web config file. The 
            entry contains the minimum required attributes.</li>
        <li class="two">
            <h5>Write controller code</h5>
            Add controller code to the jsp files. The simple.jsp file 
            contains the minimum code required to set up the queue.</li>
    </ol>
    <div><a href="${cancelUrl}">Cancel queue validation token</a></div>
    <div><a href="${expireUrl}">Change expiration</a></div>
    </jsp:attribute>
</t:master>
