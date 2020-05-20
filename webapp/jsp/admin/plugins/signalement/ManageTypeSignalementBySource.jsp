<%@ page errorPage="../../ErrorPage.jsp"%>

<jsp:useBean id="manageTypeSignalementBySource" scope="session"
	class="fr.paris.lutece.plugins.dansmarue.web.ManageTypeSignalementBySourceJspBean" />
<% 
manageTypeSignalementBySource.init( request, manageTypeSignalementBySource.RIGHT_MANAGE_TYPE_SIGNALEMENT );
String result = manageTypeSignalementBySource.processController( request, response );
%>

<jsp:include page="../../AdminHeader.jsp" />
<%= result %>
<%@ include file="../../AdminFooter.jsp"%>
