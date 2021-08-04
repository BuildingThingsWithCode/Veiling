<%@ include file="/includes/header.jsp" %>

<h2>Registreren als gebruiker</h2>
<p class="foutmelding">${foutmelding}</p>

<p>Vul uw gegevens in dit formulier</p>

<form action="<c:url value='ControllerServlet?bron=registratie' />" method="post">
<table cellspacing="5" border="0">
  <tr>
    <td><label for="naam">Naam:</label></td>
    <td><input type="text" name="naam" value="${geregistreed.naam}"></td>
  </tr>
  <tr>
    <td><label for="adres">Adres:</label></td>
    <td><input type="text" name="adres" value="${geregistreerd.adres}"></td>
  </tr>
  <tr>
    <td><label for="postcode">Postcode:</label></td>
    <td><input type="text" name="postcode" value="${geregistreerd.postcode}"></td>
  </tr>
  <tr>
    <td><label for="plaats">Woonplaats:</label></td>
    <td><input type="text" name="plaats" value="${geregistreerd.plaats}"></td>
  </tr>
  <tr>
    <td ><label for="email">Email adres:</label></td>
    <td><input type="text" name="email" value="${geregistreerd.email}"></td>
  </tr> 
  <tr>
    <td><label for="wachtwoord1">Wachtwoord:</label></td>
    <td><input type="password" name="wachtwoord1" value="${geregistreerd.wachtwoord}"></td>
  </tr> 
  <tr>
    <td><label for="wachtwoord2">Wachtwoord nogmaals:</label></td>
    <td><input type="password" name="wachtwoord2" value="${geregistreerd.wachtwoord}"></td>
  </tr> 
  <tr>
    <td>&nbsp;</td>
    <td><input type="submit" value="Verzenden"></td>
    
  </tr>
</table>
</form>

<%@ include file="/includes/footer.jsp" %>
