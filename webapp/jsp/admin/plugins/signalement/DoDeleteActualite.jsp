<%@ page errorPage="../../ErrorPage.jsp"%>



<jsp:useBean id="actualite" scope="session"
	class="fr.paris.lutece.plugins.dansmarue.web.ActualiteJspBean" />
<%
actualite.init( request, actualite.RIGHT_MANAGE_ACTUALITE );
    response.sendRedirect( actualite.doDeleteActualite( request ) );
%>

