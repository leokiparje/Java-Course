<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
<body>
 <p><a href="/voting-app/index.html">Home</a></p>
<h1>${title}</h1>
<p>${message}</p>
<ol>
	<c:forEach var="poolOption" items="${poolOptions}">
		<li><a href="glasanje-glasaj?pollID=${pollID}&pollOptionID=${poolOption.id}">${poolOption.optionTitle}</a></li>
	</c:forEach>
</ol>
</body>
</html>