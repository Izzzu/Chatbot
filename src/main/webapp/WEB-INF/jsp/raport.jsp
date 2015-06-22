<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="ISO-8859-1"%>

<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html style="background:#505D6E url(../../assets/images/body6_raport.jpg) no-repeat center center fixed;">
<head>
  <title>Chatbot Eustachy</title>
  <meta http-equiv="content-type" content="text/html; charset=UTF-8" />
  <meta name="description" content="Chatbot rozmawiający o problemach interpersonalnych i doświadczeniach życiowych" />
  <meta name="keywords" content="chatbot" />
  <link href="http://netdna.bootstrapcdn.com/font-awesome/4.0.3/css/font-awesome.min.css" rel="stylesheet" type="text/css">
  <link href='http://fonts.googleapis.com/css?family=Wire+One' rel='stylesheet' type='text/css'>
  <link rel="stylesheet" href="<c:url value="/assets/css/styles.css"/>">
  <script>
    function scrollBox()
    {
      var objDiv = document.getElementById("textarea");
      objDiv.scrollTop = objDiv.scrollHeight;

    }
    function keepFocus() {
      document.getElementById("answerArea").focus();
    };

  </script>
</head>

<body style="font-family: boardFont">

<br>
<header>
  <h1>Z naszej rozmowy wywnioskowałem:</h1>
</header>
<section id="main">

  <div>
      <c:forEach items="${personalityTypes}" var="personalityType">

        <c:out value="${personalityType}"/></p>
      </c:forEach>


  </div>


</section>
<footer>
  autor: Izabela Kułakowska <a href="mailto:%20izabel.kulak@gmail.com?subject=Temat&amp;">napisz do mnie</a>
</footer>

</body>
</html>