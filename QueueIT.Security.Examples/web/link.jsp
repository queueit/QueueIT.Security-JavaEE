<%@page import="queueit.security.*"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%
    IQueue queue = QueueFactory.createQueue("link");
        
    String targetUrl = request.getRequestURL().toString().replaceAll("link.jsp", "linktarget.jsp");
    String queueUrl = queue.getQueueUrl(targetUrl);
    
    request.setAttribute("queueUrl", queueUrl);
%>

<t:master>
    <jsp:attribute name="title">
        Link Queue Configuration
    </jsp:attribute>
    <jsp:attribute name="body">
        <h3>Setting up the queue:</h3>
        <ol class="round">
            <li class="one">
                <h5>Add JSP Servlet Filter</h5>
                Add the queueit.security.RequestContextFilter to the web.xml configuration</li>
            <li class="two">
                <h5>Add properties file</h5>
                This example uses the queue with name &#39;link&#39; from the queueit-link.properties config file. The 
                entry contains a domain alias which is used when users are redirected to the 
                queue as well as a landing page (split page) allowing users to choose if the 
                want to be redirected to the queue.</li>
            <li class="one">
                <h5>Write Known User code</h5>
                Add Known User code to the jsp page. The target jsp 
                page contains code to extract and persist information about a queue number. </li>
        </ol>
    
        <div><a href="${queueUrl}">Goto Queue</a></div>
        
    </jsp:attribute>
</t:master>
