<%@ page errorPage="../../ErrorPage.jsp"%>


<jsp:useBean id="signalement" scope="session"
	class="fr.paris.lutece.plugins.dansmarue.web.SignalementJspBean" />
<%
	signalement.init( request, signalement.RIGHT_MANAGE_SIGNALEMENT );
	response.sendRedirect( signalement.doProcessMassAction( request ) );

%>

