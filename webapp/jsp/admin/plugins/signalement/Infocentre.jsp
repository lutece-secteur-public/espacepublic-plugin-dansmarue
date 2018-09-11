<%@page
	import="fr.paris.lutece.portal.service.workgroup.AdminWorkgroupService"%>
<%@page errorPage="../../ErrorPage.jsp"%>
<%@page import="fr.paris.lutece.util.ReferenceItem"%>
<%@page
	import="fr.paris.lutece.plugins.dansmarue.service.role.SignalementResourceIdService"%>
<html lang="fr">
<head>
<title>DansMaRue RAMEN - Administration</title>
<base href="http://r57-sira.srv.form.apps.mdp/sira/"></base>
<meta http-equiv="Content-Type" content="text/html;charset=UTF-8" />
<script type="text/javascript" src="js/tools.js"></script>
<script src="js/jquery/jquery.min.js" type="text/javascript"></script>
<link rel="stylesheet" type="text/css" href="css/plugins/gis/gis.css"
	media="screen, projection, print" />
<link rel="stylesheet" type="text/css"
	href="js/plugins/gis/openlayers/Legend.css" />
<link rel="stylesheet" type="text/css"
	href="js/plugins/gis/openlayers/GeolocalizationPanel.css" />
<link rel="stylesheet" type="text/css"
	href="js/plugins/gis/openlayers/LayerSearchPanel.css" />
<link rel="stylesheet" type="text/css"
	href="js/plugins/gis/openlayers/default/style.css" />
<style>
body {
	margin: 0;
	padding: 0;
}

.map {
	border: 0 !important;
}

#legend {
	border: 1px solid black;
	min-height: 100px;
	width: 40%;
}
</style>
</head>
<body>
	<div id="map_canvas" class="map"></div>
	<div id="legend">Ceci est une légende</div>
	<script src="js/plugins/formengine/modules/gis/jquery.geolocalize.js"
		type="text/javascript"></script>
	<script src="js/plugins/gis/LABjs-1.0.2rc1/LAB.js"
		type="text/javascript"></script>
	<script src="js/plugins/gis/gis.js" type="text/javascript"></script>
	<script src="js/proj4js-combined.js" type="text/javascript"></script>
	<script type="text/javascript">fr.paris.lutece.plugins.dansmarue.web.SignalementJspBean signalement = new fr.paris.lutece.plugins.dansmarue.web.SignalementJspBean();</script>
</body>
</html>