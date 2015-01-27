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
	
	
	<title>Chatbot</title>

	
	<link rel="shortcut icon" href="/Chatbot/WebContent/WEB-INF/assets/images/gt_favicon.png">
	
	<!-- Bootstrap itself -->
	<link href="http://netdna.bootstrapcdn.com/bootstrap/3.0.3/css/bootstrap.min.css" rel="stylesheet" type="text/css">

	<!-- Custom styles -->
	
	<link rel="stylesheet" href="<c:url value="/assets/css/magister.css"/>">

	<!-- Fonts -->
	<link href="http://netdna.bootstrapcdn.com/font-awesome/4.0.3/css/font-awesome.min.css" rel="stylesheet" type="text/css">
	<link href='http://fonts.googleapis.com/css?family=Wire+One' rel='stylesheet' type='text/css'>
	<script src="http://netdna.bootstrapcdn.com/bootstrap/3.0.3/js/bootstrap.min.js"></script>
	<script src="http://ajax.googleapis.com/ajax/libs/jquery/1.10.2/jquery.min.js"></script>
</head>

<body>
	
	<section class="section" id="head">
	<div class="container">

		<div class="row">
			<div class="col-md-10 col-lg-10 col-md-offset-1 col-lg-offset-1 text-center">	

				<!-- Site Title, your name, HELLO msg, etc. -->
				<h1 class="title">Chatbot</h1>
				
				<textarea class="form-control" rows="5">
					<c:forEach items="${ars}" var="ar">  
					   <c:out value="${ar}"/>
					   <br>
					</c:forEach>
				</textarea>
		
				
				<form:form modelAttribute="Answer" method="POST" action="/Chatbot/" >
					<form:input class="form-control" path="sentence" />
					<input class="btn btn-default btn-lg" type="submit" value="Submit"/>
				</form:form>
			</div> <!-- /col -->
		</div> <!-- /row -->
	
	</div>
</section>

	</body>
	</html>