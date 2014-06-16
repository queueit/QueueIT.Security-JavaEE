<%@page import="java.util.concurrent.Callable"%>
<%@page import="queueit.security.*"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%               
    KnownUserFactory.configure("a774b1e2-8da7-4d51-b1a9-7647147bb13bace77210-a488-4b6f-afc9-8ba94551a7d7");

    try
    {
        IValidateResult result = SessionValidationController.validateRequest("ticketania", "codeonly", true);

        // Check if user must be enqueued
        if (result instanceof EnqueueResult)
        {
            response.sendRedirect(((EnqueueResult)result).getRedirectUrl().toString());
            return;
        }
    }
    catch (ExpiredValidationException ex)
    {
        // Known user has has expired - Show error page and use GetCancelUrl to get user back in the queue
         response.sendRedirect("error.jsp?queuename=&t=" + ex.getKnownUser().getOriginalUrl());
        return;
    }
    catch (KnownUserValidationException ex)
    {
        // Known user is invalid - Show error page and use GetCancelUrl to get user back in the queue
        response.sendRedirect("error.jsp?queuename=&t=" + ((KnownUserException)ex.getCause()).getOriginalUrl());
        return;
    }
%>
<t:master>
    <jsp:attribute name="title">
        Queue-it
    </jsp:attribute>
    <jsp:attribute name="body">
    <h3>Setting up the queue:</h3>
    <ol class="round">
        <li class="one">
            <h5>Add JSP Servlet Filter</h5>
            Add the queueit.security.RequestContextFilter to the web.xml configuration</li>
        <li class="two">
            <h5>Add configuration using code</h5>
            All configuration that is supported using the configuration section is also 
            supported in code. In this example it is configured in the
            &#39;codeonly.jsp&#39; file.</li>
        <li class="three">
            <h5>Write controller code</h5>
            Add controller code to the php files. The codeonly.jsp file 
            configures the queue with Customer ID and Event ID and thereby bypasses the 
            configuration section.</li>
    </ol>
    </jsp:attribute>
</t:master>