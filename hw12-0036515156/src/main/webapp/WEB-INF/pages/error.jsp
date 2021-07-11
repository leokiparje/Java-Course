<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
  <title>Greška</title>
  <style>
  	body{
  		background-color: ${pickedBgCol};
  	}
  </style>
</head>
<body>
	<p><a href="<c:url value="/" />">home</a></p>
	<c:forEach var="message" items="${messages}">
		<h1> ${message} </h1>
	</c:forEach>
</body>
</html>
