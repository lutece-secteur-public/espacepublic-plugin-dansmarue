<%@ page errorPage="../../ErrorPage.jsp"%>

<jsp:useBean id="signalementdisplay" scope="session"
	class="fr.paris.lutece.plugins.dansmarue.web.SignalementJspBean" />
<% 
signalementdisplay.init( request, signalementdisplay.RIGHT_MANAGE_SIGNALEMENT );
signalementdisplay.getSignalementsJsonForMap(request, response); 
%>
