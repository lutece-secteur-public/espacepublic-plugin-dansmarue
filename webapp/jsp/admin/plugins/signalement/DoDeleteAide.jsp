<%@ page errorPage="../../ErrorPage.jsp"%>



<jsp:useBean id="aide" scope="session"
    class="fr.paris.lutece.plugins.dansmarue.web.AideJspBean" />
<%

    response.sendRedirect( aide.doRemoveAide( request ) );
%>