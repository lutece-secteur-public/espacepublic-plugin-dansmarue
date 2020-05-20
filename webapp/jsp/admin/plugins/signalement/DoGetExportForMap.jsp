<%@ page errorPage="../../ErrorPage.jsp"%>

<jsp:useBean id="signalement" scope="session"
	class="fr.paris.lutece.plugins.dansmarue.web.SignalementJspBean" />
<% 
	signalement.init( request, signalement.RIGHT_MANAGE_SIGNALEMENT );
	response.setHeader( "Content-Disposition", "attachment;filename=\"export.csv\"" );
	response.setHeader( "Cache-Control", "must-revalidate" );
	response.setContentType( "text/csv" );
	signalement.getExportForMap(request, response); 
%>
