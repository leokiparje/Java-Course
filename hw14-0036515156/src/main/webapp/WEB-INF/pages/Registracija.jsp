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
		<c:choose>
		<c:when test="${userForm.id.isEmpty()}">
		Registracija
		</c:when>
		<c:otherwise>
		Uređivanje korisnika
		</c:otherwise>
		</c:choose>
		</h1>

		<form action="save" method="post">
		
		<div>
		 <div>
		  <span class="formLabel">ID</span><input type="text" name="id" value='<c:out value="${userForm.id}"/>' size="5">
		 </div>
		 <c:if test="${userForm.hasError('id')}">
		 <div class="greska"><c:out value="${userForm.getError('id')}"/></div>
		 </c:if>
		</div>

		<div>
		 <div>
		  <span class="formLabel">Ime</span><input type="text" name="ime" value='<c:out value="${userForm.firstName}"/>' size="20">
		 </div>
		 <c:if test="${userForm.hasError('firstName')}">
		 <div class="greska"><c:out value="${userForm.getError('firstName')}"/></div>
		 </c:if>
		</div>

		<div>
		 <div>
		  <span class="formLabel">Prezime</span><input type="text" name="prezime" value='<c:out value="${userForm.lastName}"/>' size="20">
		 </div>
		 <c:if test="${userForm.hasError('lastName')}">
		 <div class="greska"><c:out value="${userForm.getError('lastName')}"/></div>
		 </c:if>
		</div>

		<div>
		 <div>
		  <span class="formLabel">EMail</span><input type="text" name="email" value='<c:out value="${userForm.email}"/>' size="50">
		 </div>
		 <c:if test="${userForm.hasError('email')}">
		 <div class="greska"><c:out value="${userForm.getError('email')}"/></div>
		 </c:if>
		</div>

		<div>
		 <div>
		  <span class="formLabel">Korisničko ime</span><input type="text" name="nick" value='<c:out value="${userForm.nick}"/>' size="50">
		 </div>
		 <c:if test="${userForm.hasError('nick')}">
		 <div class="greska"><c:out value="${userForm.getError('nick')}"/></div>
		 </c:if>
		</div>
		
		<div>
		 <div>
		  <span class="formLabel">Zaporuka</span><input type="text" name="password" value='<c:out value=""/>' size="50">
		 </div>
		 <c:if test="${userForm.hasError('password')}">
		 <div class="greska"><c:out value="${userForm.getError('password')}"/></div>
		 </c:if>
		</div>

		<div class="formControls">
		  <span class="formLabel">&nbsp;</span>
		  <input type="submit" name="metoda" value="Pohrani">
		  <input type="submit" name="metoda" value="Odustani">
		</div>
		
		</form>

	<p><a href="/blog/servleti/main">Home</a></p>

	</body>
</html>
