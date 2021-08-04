
<c:set var="isbeheerder" scope="page" value="${gebruiker != null && gebruiker.beheerder}"/>
<c:set var="islid" scope="page" value="${gebruiker != null && !gebruiker.beheerder }"/>

<div class="menu">
<p>

<br>

<a href="<c:url value='ControllerServlet?bron=home' />">Home</a>
<br>

<c:if test="${!islid}">
<a href="<c:url value='ControllerServlet?bron=registratie' />">Registreer</a>
<br>
</c:if>

<c:if test="${!isbeheerder && !islid}">
<a href="<c:url value='ControllerServlet?bron=login' />">Login</a>
<br>
</c:if>

<c:if test="${isbeheerder || islid}">
<a href="<c:url value='ControllerServlet?bron=logout' />">Logout</a>
<br>
</c:if>

<c:if test="${islid}">
<a href="<c:url value='ControllerServlet?bron=aanbod_registratie' />">Veil artikel</a>
<br>
</c:if>

<c:if test="${isbeheerder}">
<a href="<c:url value='ControllerServlet?bron=rubriek_registratie' />">Nieuwe rubriek</a>
<br>
</c:if>

<c:if test="${isbeheerder}">
<a href="<c:url value='ControllerServlet?bron=financieel_overzicht' />">Financieel overzicht</a>
<br>
</c:if>

<br>
Toon aanbod in rubriek:
<c:forEach var="rubriek" items="${rubrieken}">
  <br>
  <a href="<c:url value='ControllerServlet?bron=aanbod_tonen&rubriek=${rubriek.id}' />">${rubriek.naam}</a>
</c:forEach>
</p>
</div>
