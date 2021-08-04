<%@ include file="/includes/header.jsp" %>

<h2>Registreren rubriek</h2>
<p class="foutmelding">${foutmelding}</p>

<p>Vul gegevens nieuwe rubriek in</p>

<form action="<c:url value='ControllerServlet?bron=rubriek_registratie' />" method="post">
<table cellspacing="5" border="0">
  <tr>
    <td><label for="naam">Naam:</label></td>
    <td><input type="text" name="naam" value="${rubriek.naam}"></td>
  </tr>
  <tr>
    <td>&nbsp;</td>
    <td><input type="submit" value="Verzenden"></td>
    
  </tr>
</table>
</form>

<%@ include file="/includes/footer.jsp" %>
