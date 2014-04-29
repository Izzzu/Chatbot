<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>

<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="en">
<head>
	<meta charset="utf-8">
	<meta name="viewport"    content="width=device-width, initial-scale=1.0">
	<meta name="description" content="">
	<meta name="author"      content="Sergey Pozhilov (GetTemplate.com)">
	
	
	<title>Magister - Free html5 template by GetTemplate</title>

	
	<link rel="shortcut icon" href="/Chatbot/WebContent/WEB-INF/assets/images/gt_favicon.png">
	
	<!-- Bootstrap itself -->
	<link href="http://netdna.bootstrapcdn.com/bootstrap/3.0.3/css/bootstrap.min.css" rel="stylesheet" type="text/css">

	<!-- Custom styles -->
	
	<link rel="stylesheet" href="<c:url value="/assets/css/magister.css"/>">

	<!-- Fonts -->
	<link href="http://netdna.bootstrapcdn.com/font-awesome/4.0.3/css/font-awesome.min.css" rel="stylesheet" type="text/css">
	<link href='http://fonts.googleapis.com/css?family=Wire+One' rel='stylesheet' type='text/css'>
</head>

<!-- use "theme-invert" class on bright backgrounds, also try "text-shadows" class -->
<body class="theme-invert">

<nav class="mainmenu">
	<div class="container">
		<div class="dropdown">
		<!--
			<button type="button" class="navbar-toggle" data-toggle="dropdown"><span class="icon-bar"></span> <span class="icon-bar"></span> <span class="icon-bar"></span> </button>
			<a data-toggle="dropdown" href="#">Dropdown trigger</a> 
			<ul class="dropdown-menu" role="menu" aria-labelledby="dLabel">
				<li><a href="#head" class="active">Hello</a></li>
				<li><a href="#about">About me</a></li>
				<li><a href="#themes">Themes</a></li>
				<li><a href="#contact">Get in touch</a></li>
			</ul>
			-->
		</div>
	</div>
</nav>
<script src="http://ajax.googleapis.com/ajax/libs/jquery/1.11.0/jquery.min.js">
</script>
<script>
$(document).ready(function(){
   
        $('#TextArea').scrollTop($('#TextArea').scrollHeight);
});
    

	function checkTextareaHeight(){
		   var textarea = document.getElementById("TextArea");
		   document.write(textarea.scrollTop);
		   textarea.scrollTop = textarea.scrollHeight;
		}
	checkTextareaHeight();
		
	</script>

<!-- Main (Home) section -->
<section class="section" id="head">
	<div class="container">

		<div class="row">
			<div class="col-md-10 col-lg-10 col-md-offset-1 col-lg-offset-1 text-center">	

				<!-- Site Title, your name, HELLO msg, etc. -->
				<h1 class="title">Magister</h1>
				<div id="TextArea" style="height:120px; width:80px; overflow:auto">
				
					<c:forEach items="${ars}" var="ar">  
					   <c:out value="${ar}"/>
					   <br>
					</c:forEach>

				</div>
				<!-- Short introductory (optional) -->
				<form:form modelAttribute="Answer" method="POST" action="/Chatbot/" >
				<h3 class="tagline">
					<form:input class="form-control" path="sentence" />
					<!--<input type="text" class="form-control" placeholder="Text input"></input> -->
				</h3>
				
				<input class="btn btn-default btn-lg" type="submit" value="Submit"/>
				<!-- Nice place to describe your site in a sentence or two -->
				
				</form:form>
			</div> <!-- /col -->
		</div> <!-- /row -->
	
	</div>
</section>







<!-- Load js libs only when the page is loaded. -->
<script src="http://ajax.googleapis.com/ajax/libs/jquery/1.10.2/jquery.min.js"></script>
<script src="http://netdna.bootstrapcdn.com/bootstrap/3.0.3/js/bootstrap.min.js"></script>
<script src="/WebContent/WEB-INF/assets/js/modernizr.custom.72241.js"></script>
<!-- Custom template scripts -->
<script src="/WebContent/WEB-INF/assets/js/magister.js"></script>
</body>
</html>