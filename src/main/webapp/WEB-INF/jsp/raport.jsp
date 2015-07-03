<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>

<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html style="overflow: scroll; background:#505D6E url(../../assets/images/body6_raport.jpg) no-repeat center center fixed;">
<head>
    <title>Chatbot Eustachy</title>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8"/>
    <meta name="description" content="Chatbot rozmawiający o problemach interpersonalnych i doświadczeniach życiowych"/>
    <meta name="keywords" content="chatbot"/>
    <link href="http://netdna.bootstrapcdn.com/font-awesome/4.0.3/css/font-awesome.min.css" rel="stylesheet"
          type="text/css">
    <link href='http://fonts.googleapis.com/css?family=Wire+One' rel='stylesheet' type='text/css'>
    <link rel="stylesheet" href="<c:url value="/assets/css/styles.css"/>">
    <script>
        function scrollBox() {
            var objDiv = document.getElementById("textarea");
            objDiv.scrollTop = objDiv.scrollHeight;

        }
        function keepFocus() {
            document.getElementById("answerArea").focus();
        }
        ;

    </script>
</head>

<body style="font-family: boardFont">

<br>

<div class="raport">
    <h1>Info</h1>

    <div>

        <p>Imię: <c:out value="${name}"/></p>

        <p>Wiek: <c:out value="${age}"/></p>

        <p>Płeć: <c:out value="${gender}"/></p>

        <p>Poziom stresu w skali LCU: <c:out value="${lcu}"/></p>

        <p>Stresory:
            <c:forEach items="${topics}" var="topic">
                <c:if test = "${topic ne 'INNE'}">  <p><c:out value="${topic}"/></p></c:if>

            </c:forEach>
        </p>

    </div>
    <h1>Analiza osobowości</h1>

    <div>
        <c:forEach items="${personalityTypes}" var="personalityType">

            <p><c:out value="${personalityType}"/></p>
        </c:forEach>


    </div>

</div>
<footer>
    autor: Izabela Kułakowska <a href="mailto:%20izabel.kulak@gmail.com?subject=Temat&amp;">napisz do mnie</a>
</footer>

</body>
</html>