<%@ page errorPage="../../ErrorPage.jsp"%>
<jsp:include page="../../AdminHeader.jsp" />

<jsp:useBean id="domaineFonctionnel" scope="session"
	class="fr.paris.lutece.plugins.dansmarue.web.DomaineFonctionnelJspBean" />
<%
domaineFonctionnel.init( request, domaineFonctionnel.RIGHT_MANAGE_DOMAINE_FONCTIONNEL );
%>
<%= domaineFonctionnel.getSaveDomaineFonctionnel( request ) %>


<%@ include file="../../AdminFooter.jsp"%>