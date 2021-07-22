<%@ page errorPage="../../ErrorPage.jsp"%>

<jsp:useBean id="checkNameFdt" scope="session"
	class="fr.paris.lutece.plugins.dansmarue.web.ManageFeuilleDeTourneeJspBean" />
<% 
	checkNameFdt.checkNameFDT(request, response); 
%>
