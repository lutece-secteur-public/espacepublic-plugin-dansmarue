<jsp:useBean id="manageFeuilleTournee" scope="session" class="fr.paris.lutece.plugins.dansmarue.web.ManageFeuilleDeTourneeJspBean" />
<% String strContent = manageFeuilleTournee.processController ( request , response ); %>

<%@ page errorPage="../../ErrorPage.jsp" %>
<jsp:include page="../../AdminHeader.jsp" />

<%= strContent %>

<%@ include file="../../AdminFooter.jsp" %>