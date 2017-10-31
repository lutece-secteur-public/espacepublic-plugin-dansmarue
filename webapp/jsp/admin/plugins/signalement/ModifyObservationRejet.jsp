<%@ page errorPage="../../ErrorPage.jsp"%>
<jsp:include page="../../AdminHeader.jsp" />


<jsp:useBean id="observationRejet" scope="session"
	class="fr.paris.lutece.plugins.dansmarue.web.ObservationRejetJspBean" />
<%
observationRejet.init( request, observationRejet.RIGHT_MANAGE_OBSERVATION_REJET );
%>
<%= observationRejet.getModifyObservationRejet( request ) %>

<%@ include file="../../AdminFooter.jsp"%>