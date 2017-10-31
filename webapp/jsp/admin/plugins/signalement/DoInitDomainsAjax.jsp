<%@ page errorPage="../../ErrorPage.jsp"%>

<jsp:useBean id="signalement" scope="session"
	class="fr.paris.lutece.plugins.dansmarue.web.SignalementJspBean" />
<% 
	signalement.init( request, "SIGNALEMENT_DISPLAY" );
	signalement.initDomains(request, response);
%>
