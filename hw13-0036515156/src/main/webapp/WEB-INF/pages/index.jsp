<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page import="hr.fer.zemris.java.model.Poll"%>
<%@page import="java.util.List"%>

<html>
  <body>

  <b>Pronađeni su sljedeće ankete:</b><br>
	
	<c:choose>
		<c:when test="${polls.isEmpty()}">
    		<p>Nema anketi.</p>
    	</c:when>
    	<c:otherwise>
    		<ul>
			    <c:forEach var="poll" items="${polls}">
			    	<li><a href="/voting-app/servleti/glasanje?pollID=${poll.id}">${poll.title}</a></li>  
			    </c:forEach>
		    </ul>
    	</c:otherwise>
  	</c:choose>
  
  </body>
</html>
