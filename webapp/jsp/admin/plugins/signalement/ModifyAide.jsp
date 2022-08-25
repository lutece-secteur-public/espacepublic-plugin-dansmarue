<%@ page errorPage="../../ErrorPage.jsp"%>
<jsp:include page="../../AdminHeader.jsp" />


<jsp:useBean id="aide" scope="session"
	class="fr.paris.lutece.plugins.dansmarue.web.AideJspBean" />
<%
aide.init( request, aide.RIGHT_MANAGE_AIDE );
%>
<%= aide.getModifyAide( request ) %>

<%@ include file="../../AdminFooter.jsp"%>