
<%@ page errorPage="../../ErrorPage.jsp"%>
<jsp:include page="../../AdminHeader.jsp" />

<%@page
	import="fr.paris.lutece.plugins.dansmarue.service.role.SignalementResourceIdService"%>
<jsp:useBean id="mailSignalement" scope="session"
	class="fr.paris.lutece.plugins.dansmarue.web.MailSignalementJspBean" />

<%
	mailSignalement.init( request, "SIGNALEMENT_MANAGEMENT", SignalementResourceIdService.KEY_ID_RESOURCE, SignalementResourceIdService.PERMISSION_ENVOI_MAIL_SIGNALEMENT );
	String strResult = mailSignalement.getManageMail( request );
%>




<%= strResult %>

<%@ include file="../../AdminFooter.jsp"%>