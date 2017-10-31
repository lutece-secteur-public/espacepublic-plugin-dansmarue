<%@ page errorPage="../../ErrorPage.jsp"%>
<%@page
	import="fr.paris.lutece.plugins.dansmarue.service.role.SignalementResourceIdService"%>


<jsp:useBean id="signalement" scope="session"
	class="fr.paris.lutece.plugins.dansmarue.web.SignalementJspBean" />
<%
signalement.init( request, "SIGNALEMENT_MANAGEMENT", SignalementResourceIdService.KEY_ID_RESOURCE, SignalementResourceIdService.SUPPRIMER_SIGNALEMENT_MASSE );
    response.sendRedirect( signalement.getMassDeleteSignalement( request ) );
%>
