<%@ page errorPage="../../ErrorPage.jsp"%>



<jsp:useBean id="observationRejet" scope="session"
	class="fr.paris.lutece.plugins.dansmarue.web.ObservationRejetJspBean" />
<%
observationRejet.init( request, observationRejet.RIGHT_MANAGE_OBSERVATION_REJET );
    response.sendRedirect( observationRejet.doIncreaseObservationRejetOrder( request ) );
%>



