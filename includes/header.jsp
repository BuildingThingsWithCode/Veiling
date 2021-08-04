<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
  <link href="css/verkocht.css" rel="stylesheet" type="text/css">
  <title>Veiling Verkocht!</title>
  <meta http-equiv="refresh" content="${pageContext.session.maxInactiveInterval};url=index.jsp">
</head>

<%@include file="/includes/begintags.jsp"%>

<body>

<table width="100%" cellpadding="10" cellspacing="0" border="0">
  <tr>
    <td width="160">
      <img src="images/veilinglogo.bmp" height="80">
    </td>
    <td align="center">
      <h1>Veiling Verkocht!</h1>
    </td>
  </tr>
  <tr>
    <td></td>
    <td>
      <c:choose>
        <c:when test="${gebruiker != null}">
          Ingelogd: ${gebruiker.naam}
        </c:when>
        <c:otherwise>Niet ingelogd
        </c:otherwise>
      </c:choose>
    </td>
  </tr>
  <tr>
    <td valign="top">
<%@ include file="/includes/menu.jsp" %>
    </td>
    <td valign="top" height="100%" class="content">




