<jsp:useBean id="aide" scope="session" class="fr.paris.lutece.plugins.dansmarue.web.AideJspBean" />
<% String strContent = aide.processController ( request , response ); %>

<%@ page errorPage="../../ErrorPage.jsp" %>
<jsp:include page="../../AdminHeader.jsp" />

<%= strContent %>

<%@ include file="../../AdminFooter.jsp" %>