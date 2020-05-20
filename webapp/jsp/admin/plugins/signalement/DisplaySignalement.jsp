<%@page
	import="fr.paris.lutece.plugins.dansmarue.service.role.DomaineFonctionnelSignalementResourceIdService"%>
	<%@page
	import="fr.paris.lutece.plugins.dansmarue.business.entities.DomaineFonctionnel"%>

<%@page
	import="fr.paris.lutece.plugins.dansmarue.web.SignalementJspBean"%>
<jsp:useBean id="signalementdisplay" scope="session"
	class="fr.paris.lutece.plugins.dansmarue.web.SignalementJspBean" />

<% 
signalementdisplay.init( request,  "SIGNALEMENT_DISPLAY");
signalementdisplay.checkUserDomains( );
%>
<%@ page errorPage="../../ErrorPage.jsp"%>
<jsp:include page="../../AdminHeader.jsp" />
<%= signalementdisplay.getDisplaySignalement( request ) %>

<%@ include file="../../AdminFooter.jsp"%>