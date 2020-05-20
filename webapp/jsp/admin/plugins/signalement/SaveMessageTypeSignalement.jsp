<%@ page errorPage="../../ErrorPage.jsp"%>
<jsp:include page="../../AdminHeader.jsp" />

<jsp:useBean id="messageTypeSignalement" scope="session"
	class="fr.paris.lutece.plugins.dansmarue.web.MessageTypeSignalementJspBean" />
<% 
messageTypeSignalement.init( request, messageTypeSignalement.RIGHT_MANAGE_TYPE_SIGNALEMENT );
%>

<%= messageTypeSignalement.getSaveMessageTypeSignalement(request) %>

<%@ include file="../../AdminFooter.jsp"%>
