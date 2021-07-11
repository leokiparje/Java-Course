<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
  <title>Index</title>
  <style>
  	body{
  		background-color: ${pickedBgCol};
  	}
  </style>
</head>
<body>
  <h1>Ovo je početna stranica</h1>
  <p><a href="<c:url value="/colors" />">Background color chooser</a></p>
  <p><a href="<c:url value="/trigonometric?a=0&b=90" />">Trigonometric</a></p>
  <p><a href="<c:url value="/powers?a=1&b=100&n=3" />">Powers</a></p>
  <p><a href="<c:url value="/appInfo" />">App info</a></p>
  <p><a href="stories/funny.jsp">Funny story</a></p>
  <p><a href="<c:url value="/reportImage" />">Report</a></p>
  <p><a href="<c:url value="/glasanje" />">Glasaj</a></p>
  	<form action="trigonometric" method="GET">
		Početni kut:<br><input type="number" name="a" min="0" max="360" step="1" value="0"><br>
		Završni kut:<br><input type="number" name="b" min="0" max="360" step="1" value="360"><br>
	<input type="submit" value="Tabeliraj"><input type="reset" value="Reset">
	</form>
</body>
</html>
