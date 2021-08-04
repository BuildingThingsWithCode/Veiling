<%@ include file="/includes/header.jsp" %>
<%@ include file="/includes/begintags.jsp" %>
<h2>Financieel Overzicht</h2>
<p class="foutmelding">${foutmelding}</p>

<p>Vul een startdatum en een einddatum in</p>

<form action="<c:url value='ControllerServlet?bron=financieel_overzicht' />" method="post">
<table cellspacing="5" border="0">
  <tr>
    <td><label for="startdatum">Startdatum:</label></td>
    <td><input type="text" name="startdatum" value="${startdatum}"></td>
    <td><label for="einddatum">Einddatum:</label></td>
    <td><input type="text" name="einddatum" value="${einddatum}"></td>
    <td><input type="submit" value="Verzenden"></td>
   </tr>
</table>
</form>
<table cellspacing="5" border="0">
<tr><td>${titel}</td></tr>
<c:forEach items="${verkocht}" var="item">
<tr>
<td><textarea name="textarea" rows="3" cols="55">
Artikel: ${item.naam}
Omschrijving: ${item.omschrijving}
Verkoopbedrag: ${item.maxbod}
</textarea>
</td>
</tr>
</c:forEach>
<tr><td>${totaleVerkoop}</td></tr>
<tr><td>${winst}</td></tr>

</table>

<%@ include file="/includes/footer.jsp" %>
