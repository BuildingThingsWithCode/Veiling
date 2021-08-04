<%@ include file="/includes/header.jsp"%>

<h2>Fout opgetreden</h2>
<p class="foutmelding">
Er is een systeemfout opgetreden waardoor het verzoek niet kon worden
uitgevoerd. Ten behoeve van het systeembeheer volgen hier enkele details.
</p>
<p>
Bron = ${param.bron}<br>
Method = ${method}<br>
</p>

<%@ include file="/includes/footer.jsp"%>