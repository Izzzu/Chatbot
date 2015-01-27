<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>

<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Hey!!!</title>
</head>
<body>
<div>
</div>
<c:forEach items="${ars}" var="ar">     
   <c:out value="${ar}"/>
</c:forEach>
<div>
<p>${mess }</p>

<form:form modelAttribute="Answer" method="POST" action="/Chatbot/chat" >
 <table>
    <tr>
	<td>Your answer</td>
	<td><form:input path="sentence" /></td>
	
	</tr>
	<tr><td><input type="submit" value="Submit"/></td><td></td></tr>
	
	</table>
</form:form>
</div>
</body>
</html>