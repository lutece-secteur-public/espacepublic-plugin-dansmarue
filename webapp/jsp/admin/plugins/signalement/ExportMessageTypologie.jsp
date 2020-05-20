<%@ page import="fr.paris.lutece.portal.service.util.AppLogService"%>
<%@ page errorPage="../../ErrorPage.jsp"%>
<jsp:useBean id="messageTypeSignalement" scope="session"
	class="fr.paris.lutece.plugins.dansmarue.web.MessageTypeSignalementJspBean" />
<%
    response.setHeader( "Content-Disposition", "attachment;filename=\"export.csv\"" );
    response.setHeader( "Cache-Control", "must-revalidate" );
    response.setContentType( "text/csv" );
    messageTypeSignalement.exportMessageTypologie( request, response );

%>
