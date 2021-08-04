<%@ include file="/includes/header.jsp" %>

<h2>Artikel voor veiling</h2>

<p class="foutmelding">${foutmelding}</p>

<p>Vul hier de gegegens van het te veilen artikel in</p>
<form action="<c:url value='ControllerServlet?bron=aanbod_registratie' />" method="post">
<table cellspacing="5" border="0">
  <tr>
    <td><label for="naam">Naam:</label></td>
    <td><input type="text" name="naam" value="${aanbod.naam}"></td>
  </tr>
  <tr>
    <td><label for="omschrijving">Omschrijving:</label></td>
    <td><input type="text" name="omschrijving" value="${aanbod.omschrijving}"></td>
  </tr>
  <tr>
    <td><label for="minbod">Minimaal bod:</label></td>
    <fmt:formatNumber var="_minbod" value="${aanbod.minbod}" pattern="0.00" />
    <td><input type="text" name="minbod" value="${_minbod}"></td>
  </tr>
  <tr>
    <td><label for="startdatum">Startdatum:</label></td>
    <fmt:formatDate var="_startdatum" value="${aanbod.startdatum}" pattern="dd-MM-yyyy" />
    <td><input type="text" name="startdatum" value="${_startdatum}" ></td>
  </tr>
  <tr>
    <td ><label for="einddatum">Einddatum:</label></td>
    <fmt:formatDate var="_einddatum" value="${aanbod.einddatum}" pattern="dd-MM-yyyy" />
    <td><input type="text" name="einddatum" value="${_einddatum}"></td>
  </tr> 
  <tr>
    <td><label for="rubriek">Rubriek:</label></td>
    <td>
      <select name="rubriek">
      <option value="">--- selecteer ---</option>
      <c:forEach var="rubriek" items="${rubrieken}">
        <option value="${rubriek.naam}">${rubriek.naam}</option>
      </c:forEach>
      </select>
    </td>
  </tr> 
  <tr>
    <td>&nbsp;</td>
    <td><input type="submit" value="Verzenden"></td>    
  </tr>
</table>
</form>

<%@ include file="/includes/footer.jsp" %>
