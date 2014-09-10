<%@page import="java.net.URI"%>
<%@page import="queueit.security.*"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%
    // IMPORTANT !
	// Never call request validation from error handling pages (e.g. error.jsp) which will cause users to get looped arround.
	
	String queueName = request.getParameter("queuename");
    URI targetUrl = new URI(request.getParameter("t"));
    IQueue queue = QueueFactory.createQueue(queueName);
    String cancelUrl = queue.getCancelUrl(targetUrl).toString();
    
    request.setAttribute("cancelUrl", cancelUrl);
%>
<t:master>
    <jsp:attribute name="title">
        Queue-it
    </jsp:attribute>
    <jsp:attribute name="body">
        <div>An error occured.</div>
        <div>
            <a href="index.php">Back To Home</a> <a href="${cancelUrl}">Go to queue</a>
        </div>
    </jsp:attribute>
</t:master>
