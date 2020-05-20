<%@ page errorPage="../../ErrorPage.jsp"%>

<jsp:useBean id="messageTypeSignalement" scope="session"
	class="fr.paris.lutece.plugins.dansmarue.web.MessageTypeSignalementJspBean" />
<% 
messageTypeSignalement.init( request, messageTypeSignalement.RIGHT_MANAGE_TYPE_SIGNALEMENT );
response.sendRedirect( messageTypeSignalement.doSaveMessageTypeSignalement( request ) );
%>
