<%@ page errorPage="../../ErrorPage.jsp"%>

<jsp:useBean id="manageRequalificationMasse" scope="session"
	class="fr.paris.lutece.plugins.dansmarue.web.ManageRequalificationMasseJspBean" />
<% 
manageRequalificationMasse.init( request, manageRequalificationMasse.RIGHT_MANAGE_REQUALIFICATION_MASSE );
String result = manageRequalificationMasse.processController( request, response );
%>

<jsp:include page="../../AdminHeader.jsp" />
<%= result %>
<%@ include file="../../AdminFooter.jsp"%>
