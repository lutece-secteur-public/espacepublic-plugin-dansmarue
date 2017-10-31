<%@ page errorPage="../../ErrorPage.jsp"%>
<%@page	import="fr.paris.lutece.plugins.dansmarue.web.ReferentielJspBean"%>
<jsp:include page="../../AdminHeader.jsp" />


<jsp:useBean id="signalementUnit" scope="session"
	class="fr.paris.lutece.plugins.dansmarue.web.SignalementUnitJspBean" />
<%
signalementUnit.init( request, ReferentielJspBean.RIGHT_MANAGE_REFERENTIEL );
%>
<%= signalementUnit.getManageSignalementUnit( request ) %>

<%@ include file="../../AdminFooter.jsp"%>