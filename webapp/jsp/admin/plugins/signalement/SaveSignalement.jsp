<%@page
	import="fr.paris.lutece.plugins.dansmarue.service.role.SignalementResourceIdService"%>

<jsp:useBean id="signalement" scope="session"
	class="fr.paris.lutece.plugins.dansmarue.web.SignalementJspBean" />
<% 
	signalement.init( request, "SIGNALEMENT_MANAGEMENT", SignalementResourceIdService.KEY_ID_RESOURCE, SignalementResourceIdService.PERMISSION_CREATION_SIGNALEMENT );
	String strResult = signalement.getSaveSignalement( request );
%>
<%@ page errorPage="../../ErrorPage.jsp"%>

<jsp:include page="../../AdminHeader.jsp" />

<%= strResult %>

<%@ include file="../../AdminFooter.jsp"%>