<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%
	long appStartedTime = (long) request.getServletContext().getAttribute("started");
	long time = System.currentTimeMillis()-appStartedTime;
	StringBuilder sb = new StringBuilder();
	long miliseconds = time%1000;
	long seconds = (time/1000)%60;
	long minutes = (time/(1000*60))%(60);
	long hours = (time/(1000*60*60))%24;
	long days = time/(1000*60*60*24);
	sb.append(days+" days "+hours+" hours "+minutes+" minutes "+seconds+" seconds and "+miliseconds+" miliseconds");
	String timeElapsed = sb.toString();
%>

<!DOCTYPE html>
<html>
<head>
  <title>Application info</title>
  <style>
  	body{
  		background-color: <c:out value="${pickedBgCol}" />;
  	}
  </style>
</head>
<body>
	<p><a href="<c:url value="/" />">home</a></p>
	<p>
		Application is running for: <%= timeElapsed %>
	</p>
</body>
</html>