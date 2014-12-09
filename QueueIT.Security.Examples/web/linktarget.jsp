<%@page import="java.net.URLEncoder"%>
<%@page import="java.util.Date"%>
<%@page import="queueit.security.*"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%
    try
    {
        IKnownUser knownUser = KnownUserFactory.verifyMd5Hash();

        if (knownUser == null) {
            response.sendRedirect("link.jsp");
            return;
        }

        if (knownUser.getTimeStamp().getTime()  < ((new Date()).getTime() - 180 * 1000)) {
            response.sendRedirect("link.jsp");
            return;
        }
    }
    catch (KnownUserException ex)
    {
        String targetUrl = request.getRequestURL().toString().replaceAll("linktarget.jsp", "link.jsp");   
        response.sendRedirect("error.jsp?queuename=link&t=" + URLEncoder.encode(targetUrl, "UTF-8"));
        return;
    }
%>

<t:master>
    <jsp:attribute name="title">
        Link Target
    </jsp:attribute>
    <jsp:attribute name="body">
	<h3>Setting up the queue:</h3>
        <ol class="round">
            <li class="one">
               <h5>Write Known User code</h5>
               Add Known User code to the php page. The target php 
               page contains code to extract and persist information about a queue number. </li>
        </ol>
        
    </jsp:attribute>
</t:master>
