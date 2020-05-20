<%@page
	import="fr.paris.lutece.plugins.dansmarue.service.role.SignalementResourceIdService"%>
<%@page import="fr.paris.lutece.portal.service.util.AppPathService" %>

<%@page
	import="fr.paris.lutece.plugins.dansmarue.web.SignalementDashboardJspBean"%>
<jsp:useBean id="signalementDashboard" scope="session"
	class="fr.paris.lutece.plugins.dansmarue.web.SignalementDashboardJspBean" />

<% 
	String action = request.getParameter("action");
	if(null != action && action.equals("manageSignalement")){
		signalementDashboard.redirectToManageSignalement(request);
		response.sendRedirect(AppPathService.getBaseUrl( request ) + "jsp/admin/plugins/signalement/ManageSignalement.jsp");
	}
%>

<%@ page errorPage="../../ErrorPage.jsp"%>
<jsp:include page="../../AdminHeader.jsp" />

<%
signalementDashboard.init( request,  "SIGNALEMENT_DASHBOARD");
		out.print(signalementDashboard.doGetManageSignalementDashboard( request ));
%>

<%@ include file="../../AdminFooter.jsp"%>