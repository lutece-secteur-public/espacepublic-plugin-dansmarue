<%@ page errorPage="../../ErrorPage.jsp"%>
<jsp:include page="../../AdminHeader.jsp" />

<jsp:useBean id="actualite" scope="session"
	class="fr.paris.lutece.plugins.dansmarue.web.ActualiteJspBean" />
<%
actualite.init( request, actualite.RIGHT_MANAGE_ACTUALITE );
%>
<%= actualite.getSaveActualite( request ) %>


<%@ include file="../../AdminFooter.jsp"%>