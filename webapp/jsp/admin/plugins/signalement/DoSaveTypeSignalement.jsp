<%@ page errorPage="../../ErrorPage.jsp"%>


<jsp:useBean id="typeSignalement" scope="session"
	class="fr.paris.lutece.plugins.dansmarue.web.TypeSignalementJspBean" />
<%
typeSignalement.init( request, typeSignalement.RIGHT_MANAGE_TYPE_SIGNALEMENT );
     response.sendRedirect( typeSignalement.doSaveTypeSignalement( request ) );
%>

