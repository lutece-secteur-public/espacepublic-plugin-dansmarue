<jsp:useBean id="signalementMap" scope="session"
	class="fr.paris.lutece.plugins.dansmarue.web.SignalementMapJspBean" />

<%= signalementMap.getMap( request ) %>