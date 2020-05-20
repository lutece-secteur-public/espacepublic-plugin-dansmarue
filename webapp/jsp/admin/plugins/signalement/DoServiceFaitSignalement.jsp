<%@ page errorPage="../../../../ErrorPage.jsp"%>
<%@page
	import="fr.paris.lutece.plugins.dansmarue.web.SignalementJspBean"%>
<jsp:useBean id="signalement" scope="session"
	class="fr.paris.lutece.plugins.dansmarue.web.SignalementJspBean" />
<%
	signalement.init( request, SignalementJspBean.RIGHT_MANAGE_SIGNALEMENT );
    response.sendRedirect( signalement.doServiceFaitSignalement( request ) );
%>


