<%@ page errorPage="../../ErrorPage.jsp"%>
<%@page	import="fr.paris.lutece.plugins.dansmarue.web.ReferentielJspBean"%>


<jsp:useBean id="signalementUnit" scope="session"
	class="fr.paris.lutece.plugins.dansmarue.web.SignalementUnitJspBean" />
<%
signalementUnit.init( request, ReferentielJspBean.RIGHT_MANAGE_REFERENTIEL );
    response.sendRedirect( signalementUnit.doSaveSignalementUnit( request ) );
%>
