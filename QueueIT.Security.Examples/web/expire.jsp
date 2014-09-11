<%@page import="java.util.Date"%>
<%@page import="java.util.concurrent.Callable"%>
<%@page import="queueit.security.*"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%
    try
    {
        IValidateResult result = SessionValidationController.validateRequest("ticketania", request.getParameter("eventid"));

        // Check if user must be enqueued
        if (result instanceof AcceptedConfirmedResult)
        {
            ((AcceptedConfirmedResult)result).setExpiration(new Date((new Date()).getTime() + 15));
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
        Expire Validation
    </jsp:attribute>
    <jsp:attribute name="body">
        <h3>Expire validation result</h3>
    <p>Your validation result has set to expire in 15 sec</p>
    </jsp:attribute>
</t:master>
