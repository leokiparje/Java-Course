<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="java.util.Random,java.awt.Color" %>

<!DOCTYPE html>
<html>
<head>
  <title>Funny story</title>
  <style>
  	body{
  		background-color: ${pickedBgCol};
  		color: <% 
	  		Random rand = new Random();
	  		float r = rand.nextFloat();
	  		float g = rand.nextFloat();
	  		float b = rand.nextFloat();
	  		Color color = new Color(r,g,b);
  			String hex = Integer.toHexString(color.getRGB() & 0xffffff);
  			if (hex.length()<6) {
  				hex = "0"+hex;
  			}
  			out.write("#"+hex);
  		%>;
  	}
  </style>
</head>
<body>
  <h1>Funny story</h1>
  <p> <%= color %> </p>
  <p>One of my wife’s third graders was wearing a Fitbit watch, which prompted my wife to ask,</p>
  <p>“Are you tracking your steps?”</p>
  <p>“No,” said the little girl.</p>
  <p> “I wear this for Mommy so she can show Daddy when he gets home.”</p>
</body>
</html>
