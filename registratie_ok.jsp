<%@ include file="/includes/header.jsp"%>

<h2>Registratie geslaagd</h2>

<p>Welkom als geregistreerd gebruiker van Veiling Verkocht!</p>
<p>We hebben van u de volgende gegevens geregistreerd:</p>
<table cellspacing="5" border="0">
  <tr>
    <td>Naam:</td>
    <td>${geregistreerd.naam}</td>
  </tr>
  <tr>
    <td>Adres:</td>
    <td>${geregistreerd.adres}</td>
  </tr>
  <tr>
    <td>Postcode:</td>
    <td>${geregistreerd.postcode}</td>
  </tr>
  <tr>
    <td>Woonplaats:</td>
    <td>${geregistreerd.plaats}</td>
  </tr>
  <tr>
    <td >Email adres:</td>
    <td>${geregistreerd.email}</td>
  </tr> 
</table>

<%@ include file="/includes/footer.jsp" %>
