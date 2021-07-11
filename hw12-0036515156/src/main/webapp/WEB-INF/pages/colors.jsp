<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
  <title>Color chooser</title>
  <style>
  	body{
  		background-color: <c:out value="${pickedBgCol}" />;
  	}
  </style>
</head>
<body>
	<p><a href="/webapp2">Home</a></p>
  <h1>Pick a color</h1>
  <p><a href="<% out.print(response.encodeURL(request.getContextPath()+"/setcolor/white")); %>">WHITE</a></p>
  <p><a href="<% out.print(response.encodeURL(request.getContextPath()+"/setcolor/red")); %>">RED</a></p>
  <p><a href="<% out.print(response.encodeURL(request.getContextPath()+"/setcolor/green")); %>">GREEN</a></p>
  <p><a href="<% out.print(response.encodeURL(request.getContextPath()+"/setcolor/cyan")); %>">CYAN</a></p>
</body>
</html>