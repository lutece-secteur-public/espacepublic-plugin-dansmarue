<%@ page errorPage="../../ErrorPage.jsp"%>
<jsp:include page="../../AdminHeader.jsp" />


<jsp:useBean id="typeSignalement" scope="session"
	class="fr.paris.lutece.plugins.dansmarue.web.TypeSignalementJspBean" />
<%
typeSignalement.init( request, typeSignalement.RIGHT_MANAGE_TYPE_SIGNALEMENT );
%>
<%= typeSignalement.getModifyTypeSignalement( request ) %>

<%@ include file="../../AdminFooter.jsp"%>