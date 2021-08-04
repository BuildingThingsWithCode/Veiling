<%@ include file="/includes/header.jsp" %>

<h2>Op dit moment ter veiling</h2>

<p>Op dit moment worden deze artikelen aangeboden ter veiling:</p>

<c:set var="islid" scope="page" value="${gebruiker != null && !gebruiker.beheerder }"/>
<c:forEach var="aanbod" items="${inveiling}" varStatus="status">
  <p>
  ${status.count} <b>${aanbod.naam}</b>
  <br>${aanbod.omschrijving}
  <br>Startbod: <fmt:formatNumber value="${aanbod.minbod}" pattern="0.00" />
  <br>Hoogste bod: <fmt:formatNumber value="${aanbod.maxbod}" pattern="0.00" />
  <br>Veiling eindigt: <fmt:formatDate pattern="dd-MM-yyyy" value="${aanbod.einddatum}" />
  
  <c:if test="${islid}">
  <form action="<c:url value='ControllerServlet?bron=aanbod_bieden"' />" method="post">
    <label for="biedbedrag">Biedbedrag: </label>
    <input type="text" name="biedbedrag"">
    <input type="hidden" name="aanbodid" value="${aanbod.id}">
    <input type="checkbox" name="autobod" value="aangevinkt" >Automatisch bod
    <input type="submit" value="Bied">
    <div id="uitleg"> Indien u "automatisch aanbod" aanvinkt, dient u het maximum bedrag
    in te geven dat namens u mag geboden worden. Wanneer iemand een 
    tegenbod doet, wordt er namens u automatisch een bod gedaan dat 0,5 euro 
    hoger is. En dit tot aan het maximum dat u ingeeft.</div>
  </form>
  </c:if>
</c:forEach>

<%@ include file="/includes/footer.jsp" %>
