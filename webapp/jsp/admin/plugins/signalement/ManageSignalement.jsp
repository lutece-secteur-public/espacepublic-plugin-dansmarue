<%@page
	import="fr.paris.lutece.plugins.dansmarue.service.role.SignalementResourceIdService"%>

<%@page
	import="fr.paris.lutece.plugins.dansmarue.web.SignalementJspBean"%>
<jsp:useBean id="signalement" scope="session"
	class="fr.paris.lutece.plugins.dansmarue.web.SignalementJspBean" />

<% 
signalement.init( request,  "SIGNALEMENT_MANAGEMENT", SignalementResourceIdService.KEY_ID_RESOURCE, SignalementResourceIdService.PERMISSION_RECHERCHER_SIGNALEMENT );
%>
<%@ page errorPage="../../ErrorPage.jsp"%>
<jsp:include page="../../AdminHeader.jsp" />
<%= signalement.getManageSignalement( request ) %>

<%@ include file="../../AdminFooter.jsp"%>