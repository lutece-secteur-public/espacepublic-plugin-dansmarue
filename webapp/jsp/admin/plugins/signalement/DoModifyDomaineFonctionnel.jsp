<%@ page errorPage="../../ErrorPage.jsp"%>



<jsp:useBean id="domaineFonctionnel" scope="session"
	class="fr.paris.lutece.plugins.dansmarue.web.DomaineFonctionnelJspBean" />
<%
domaineFonctionnel.init( request, domaineFonctionnel.RIGHT_MANAGE_DOMAINE_FONCTIONNEL );
    response.sendRedirect( domaineFonctionnel.doModifyDomaineFonctionnel( request ) );
%>



