<%@ page errorPage="../../ErrorPage.jsp"%>
<jsp:useBean id="deleteFeuilleDeTournee" scope="session"
	class="fr.paris.lutece.plugins.dansmarue.web.ManageFeuilleDeTourneeJspBean" />
<%
    response.sendRedirect( deleteFeuilleDeTournee.doDeleteFeuilleDeTournee( request ) );
%>
