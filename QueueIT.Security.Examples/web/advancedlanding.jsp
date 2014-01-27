<%@page import="java.net.URI"%>
<%@page import="queueit.security.*"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%
    IQueue queue = QueueFactory.createQueue("advanced");
    String targetUrl = request.getParameter("t");
    request.setAttribute("queueUrl", queue.getQueueUrl(new URI(targetUrl)));
%>
<t:master>
    <jsp:attribute name="title">
        Advanced
    </jsp:attribute>
    <jsp:attribute name="body">
        <a href="index.aspx">Back To Home</a> <a href="${queueUrl}">Go to queue</a>
    </jsp:attribute>
</t:master>
