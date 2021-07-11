<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
	<head>
	<title>Edit blog entry</title>
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
	
	<c:choose>
			<c:when test="${!author.equals(user)}">
				<h1> Error, expected user: <c:out value="${author.nick}"/></h1>
			</c:when>
			
			<c:otherwise>
			
				<h1>
					Edit blog entry
				</h1>
		
				<form action="/blog/servleti/author/${author.nick}/edit?id=${id}" method="post">
		
				<div>
				  <span class="formLabel">Title</span><input type="text" name="title" value='<c:out value="${form.title}"/>' size="50">
				</div>
				 <c:if test="${form.hasError('title')}">
				 <div class="greska"><c:out value="${form.getError('title')}"/></div>
				 </c:if>
				
				 <div>
				  <span class="formLabel">Text</span><input type="text" name="text" value='<c:out value="${form.text}"/>' size="100">
				 </div>
				 <c:if test="${form.hasError('text')}">
				 <div class="greska"><c:out value="${form.getError('text')}"/></div>
				 </c:if>
		
				<div class="formControls">
				  <span class="formLabel">&nbsp;</span>
				  <input type="submit" name="metoda" value="Submit">
				</div>
				
				</form>
			</c:otherwise>
		</c:choose>
	</body>
</html>
