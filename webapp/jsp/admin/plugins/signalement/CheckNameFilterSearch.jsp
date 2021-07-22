<%@ page errorPage="../../ErrorPage.jsp"%>

<jsp:useBean id="checkNameFilterSearch" scope="session"
	class="fr.paris.lutece.plugins.dansmarue.web.ManageFeuilleDeTourneeJspBean" />
<% 
checkNameFilterSearch.checkNameFilterSearch(request, response); 
%>
