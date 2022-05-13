<%@ page errorPage="../../ErrorPage.jsp"%>

<jsp:useBean id="fdt" scope="session"
	class="fr.paris.lutece.plugins.dansmarue.web.ManageFeuilleDeTourneeJspBean" />
<% 
	fdt.init( request, "FEUILLE_DE_TOURNEE" );
	fdt.getEntityListByIdDirection(request, response); 
%>
