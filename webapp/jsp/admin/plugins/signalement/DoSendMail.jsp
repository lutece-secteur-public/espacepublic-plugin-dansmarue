<%@page
	import="fr.paris.lutece.plugins.dansmarue.service.role.SignalementResourceIdService"%>
<jsp:useBean id="mailSignalement" scope="session"
	class="fr.paris.lutece.plugins.dansmarue.web.MailSignalementJspBean" />
<%@ page errorPage="../../ErrorPage.jsp"%>
<%
	mailSignalement.init( request, "SIGNALEMENT_MANAGEMENT", SignalementResourceIdService.KEY_ID_RESOURCE, SignalementResourceIdService.PERMISSION_ENVOI_MAIL_SIGNALEMENT );
	String urlToGo = mailSignalement.doSendMail( request );
	response.sendRedirect( urlToGo );
%>

