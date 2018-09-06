
<%@page
	import="fr.paris.lutece.plugins.dansmarue.service.role.SignalementResourceIdService"%>
<jsp:useBean id="referentielSignalement" scope="session"
	class="fr.paris.lutece.plugins.dansmarue.web.ReferentielJspBean" />
<%
referentielSignalement.init( request, referentielSignalement.RIGHT_MANAGE_REFERENTIEL,SignalementResourceIdService.KEY_ID_RESOURCE, SignalementResourceIdService.PERMISSION_GESTION_REFERENTIEL );
%>
<%@ page errorPage="../../ErrorPage.jsp"%>
<jsp:include page="../../AdminHeader.jsp" />

<%= referentielSignalement.getManageReferentiel( ) %>

<%@ include file="../../AdminFooter.jsp"%>
