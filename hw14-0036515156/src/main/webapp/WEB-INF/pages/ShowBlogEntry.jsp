<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
	<head>
		<title>Registracija</title>
		
		<style type="text/css">
		.greska {
		   font-family: fantasy;
		   font-weight: bold;
		   font-size: 0.9em;
		   color: #FF0000;
		   padding-left: 110px;
		}
		.formLabel {
		   display: inline-block;
		   width: 100px;
                   font-weight: bold;
		   text-align: right;
                   padding-right: 10px;
		}
		.formControls {
		  margin-top: 10px;
		}
		* {box-sizing: border-box;}
		body { 
		  margin: 0;
		  font-family: Arial, Helvetica, sans-serif;
		}
		
		.header {
		  overflow: hidden;
		  background-color: #f1f1f1;
		  padding: 20px 10px;
		}
		
		.header a {
		  float: left;
		  color: black;
		  text-align: center;
		  padding: 12px;
		  text-decoration: none;
		  font-size: 18px; 
		  line-height: 25px;
		  border-radius: 4px;
		}
		
		.header a.logo {
		  font-size: 25px;
		  font-weight: bold;
		}
		
		.header a:hover {
		  background-color: #ddd;
		  color: black;
		}
		
		.header a.active {
		  background-color: dodgerblue;
		  color: white;
		}
		
		.header-right {
		  float: right;
		}
		
		@media screen and (max-width: 500px) {
		  .header a {
		    float: none;
		    display: block;
		    text-align: left;
		  }
		  
		  .header-right {
		    float: none;
		  }
		}
		</style>
	</head>

	<body>

		<div class="header"> 
	  <c:choose>
	  	<c:when test="${user!=null}">
	  		<p class="logo"> <c:out value="${user.firstName} ${user.lastName}"/> </p>
	  	</c:when>
	  	<c:otherwise>
	  		<p class="logo"> not loged in </p>
	  	</c:otherwise>
	  </c:choose>
	  
	    <div class="header-right">
	    
	  	<c:choose>
	  	<c:when test="${user!=null}">
	  		<a href="/blog/servleti/logout">Logout</a>
	  	</c:when>
	  	<c:otherwise>
	  		<a href="/blog/servleti/main">Login</a>
	  	</c:otherwise>
	  </c:choose>
	  </div>
	</div>

		<h1>
			Blog entry
		</h1>

		  <h2> ${entry.title} </h2>
			<p> ${entry.text} </p>
		
			<c:if test="${entry.comments!=null && !entry.comments.isEmpty()}">
			  <ul>
			  <c:forEach var="com" items="${entry.comments}">
			    <li><div style="font-weight: bold">Korisnik => <c:out value="${com.usersEMail}"/> : <c:out value="${com.postedOn}"/></div><div style="padding-left: 10px;"><c:out value="${com.message}"/></div></li>
			  </c:forEach>
			  </ul>
			</c:if>
			
			<form action="/blog/servleti/author/${author.nick}/newComment?eid=${entry.id}" method="post">
		
				<div>
				  <span class="formLabel">Comment: </span><input type="text" name="comment" size="50">
				</div>
				<c:if test="${form!=null && form.isEmpty()}">
				 <div class="greska"><c:out value="${form.getErrorMessage()}"/></div>
				 </c:if>
		
				<div class="formControls">
				  <span class="formLabel">&nbsp;</span>
				  <input type="submit" name="metoda" value="Submit">
				</div>
				
				</form>
			
			<p><a href="/blog/servleti/author/${author.nick}">Back</a></p>
	</body>
</html>
