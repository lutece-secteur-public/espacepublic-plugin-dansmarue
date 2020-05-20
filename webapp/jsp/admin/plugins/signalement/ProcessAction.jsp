<%@page import="org.apache.commons.lang.StringUtils"%>
<%@page import="fr.paris.lutece.portal.web.pluginaction.IPluginActionResult"%>
<%@page errorPage="../../ErrorPage.jsp"%>
<jsp:useBean id="signalement" scope="session" class="fr.paris.lutece.plugins.dansmarue.web.SignalementJspBean" />
<jsp:useBean id="signalementdisplay" scope="session" class="fr.paris.lutece.plugins.dansmarue.web.SignalementJspBean" />

<%
	signalement.init( request, signalement.RIGHT_MANAGE_SIGNALEMENT );
	IPluginActionResult result = null;
	if( StringUtils.isNotBlank( request.getParameter( "exportcsv" ) ) ){
	    response.setHeader( "Content-Disposition", "attachment;filename=\"export.csv\"" );
	    response.setHeader( "Cache-Control", "must-revalidate" );
	    response.setContentType( "text/csv" );
	    signalement.exportSignalement( request, response);
	}else if(StringUtils.isNotBlank( request.getParameter( "exportcsvdisplay" ) ) ){
		response.setHeader( "Content-Disposition", "attachment;filename=\"export.csv\"" );
	    response.setHeader( "Cache-Control", "must-revalidate" );
	    response.setContentType( "text/csv" );
	    signalementdisplay.exportSignalement( request, response);
	}
	else if (StringUtils.isNotBlank(request.getParameter("servicefaits"))){
	  	result = signalement.processActionServicefait( request, response );
	}
	else{
	    result = signalement.processAction( request, response );
	}
	
	if ( result != null && result.getRedirect() != null ) {
		response.sendRedirect(result.getRedirect());
	} else if ( result != null && result.getHtmlContent() != null ) {
	
%>
<jsp:include page="../../AdminHeader.jsp" />
<%= result.getHtmlContent(  ) %>
<%@ include file="../../AdminFooter.jsp" %>
<% } %>

