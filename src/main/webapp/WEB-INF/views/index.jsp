<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<html>
<head>
    <title>Index</title>
    <meta charset="UTF-8">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/style.css">
</head>
<body>

<h1>Welcome!</h1>

<form:form method="post" action="${pageContext.request.contextPath}/logout">
    <input type="submit" value="Logout">
</form:form>

</body>
</html>
