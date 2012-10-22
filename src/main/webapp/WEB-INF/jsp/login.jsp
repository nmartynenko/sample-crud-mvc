<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<html>
<head>
    <title><spring:message code="sample.message.login.title"/></title>
</head>
<body>

<c:if test="${param['error'] ne null}">
    <div>
        <spring:message code="sample.error.wrong.credentials"/>
    </div>
</c:if>


<form method="POST" action="/login">

    <div>
        <label for="j_username"><spring:message code="sample.message.username"/></label>
        <input type="text" id="j_username" name="j_username">
    </div>

    <div><label for="j_password"><spring:message code="sample.message.password"/></label>
        <input type="password" id="j_password" name="j_password">
    </div>

    <div>
        <input type="submit">
    </div>
</form>
</body>
</html>