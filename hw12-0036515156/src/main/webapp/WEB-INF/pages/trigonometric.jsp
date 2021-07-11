<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
  <title>Trigonometric</title>
  <style>
  	body{
  		background-color: ${pickedBgCol};
  	}
  </style>
</head>
<body>
 	<p><a href="/webapp2">Home</a></p>
 		
 	<table>
 		<tr>
 			<th>Angle</th>
 			<th>Sin</th>
 			<th>Cos</th>
 		</tr>
 		
 	<c:forEach var="angleData" items="${angleList}">
        <tr>
            <td>${angleData.angle}</td>
            <td>${angleData.sin}</td>
            <td>${angleData.cos}</td>
        </tr>
    </c:forEach>
 		
 	</table>
</body>
</html>
