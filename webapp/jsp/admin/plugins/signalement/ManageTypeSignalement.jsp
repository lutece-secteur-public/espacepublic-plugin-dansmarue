<%@page
	import="fr.paris.lutece.portal.web.pluginaction.IPluginActionResult"%>
<%@page import="org.apache.commons.lang.StringUtils"%>
<%@ page errorPage="../../ErrorPage.jsp"%>
<jsp:useBean id="typeSignalement" scope="session"
	class="fr.paris.lutece.plugins.dansmarue.web.TypeSignalementJspBean" />

<%
	typeSignalement.init( request, typeSignalement.RIGHT_MANAGE_TYPE_SIGNALEMENT );
	String result = null;
	if ( !StringUtils.isNotBlank( request.getParameter( "action" ) ) ){
	    result = typeSignalement.getManageTypeSignalement(request);
	}
	else{
	    response.sendRedirect( typeSignalement.getManageTypeSignalement( request ) );   
	}
	
%>
<jsp:include page="../../AdminHeader.jsp" />
<%= result %>
<%@ include file="../../AdminFooter.jsp"%>

