<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
<title>OS usage report</title>
    <style>body {
        background-color: <c:out value="${pickedBgCol}" />;
    }
    </style>
</head>
<body>
<a href="/webapp2">Home</a>
<hr>
<h1>OS usage</h1>
<p>Here are the results of OS usage in survey that we completed.</p>
<img src="osUsage">
</body>
</html>