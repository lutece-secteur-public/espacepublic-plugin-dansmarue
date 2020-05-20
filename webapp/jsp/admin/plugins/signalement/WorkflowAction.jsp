<%@page
	import="fr.paris.lutece.plugins.dansmarue.web.SignalementJspBean"%>
<jsp:useBean id="signalement" scope="session"
	class="fr.paris.lutece.plugins.dansmarue.web.SignalementJspBean" />
<% 
	signalement.init( request, SignalementJspBean.RIGHT_MANAGE_SIGNALEMENT );
	String strResult = signalement.getWorkflowActionForm( request );
%>
<%@ page errorPage="../../ErrorPage.jsp"%>
<jsp:include page="../../AdminHeader.jsp" />

<%= strResult  %>

<%@ include file="../../AdminFooter.jsp"%>
