<%@ include file="/includes/header.jsp" %>

<h2>Login</h2>
<p class="foutmelding">${foutmelding}</p>

<p>Als geregistreerd gebruiker kunt u inloggen door uw emailadres en wachtwoord
hieronder op te geven.
Wanneer u niet geregistreerd bent, dient u zich eerst te 
<a href="<c:url value='ControllerServlet?bron=registratie' />">registreren</a>.</p>
<br>


<form action="<c:url value='ControllerServlet?bron=login' />" method="post">
<table cellspacing="5" border="0">
  <tr>
    <td ><label for="naam">Email adres:</label></td>
    <td><input type="text" name="naam" ></td>
  </tr> 
  <tr>
    <td><label for="wachtwoord">Wachtwoord:</label></td>
    <td><input type="password" name="wachtwoord" ></td>
  </tr> 
  <tr>
    <td>&nbsp;</td>
    <td><input type="submit" value="Verzenden"></td>    
  </tr>
</table>
</form>

<%@ include file="/includes/footer.jsp" %>
