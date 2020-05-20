<%@ page errorPage="../../ErrorPage.jsp"%>

<jsp:useBean id="manageServiceFaitMAsse" scope="session"
	class="fr.paris.lutece.plugins.dansmarue.web.ManageServiceFaitMasseJspBean" />
<% 
manageServiceFaitMAsse.init( request, manageServiceFaitMAsse.RIGHT_MANAGE_TYPE_SIGNALEMENT );
String result = manageServiceFaitMAsse.processController( request, response );
%>

<jsp:include page="../../AdminHeader.jsp" />
<%= result %>
<%@ include file="../../AdminFooter.jsp"%>
