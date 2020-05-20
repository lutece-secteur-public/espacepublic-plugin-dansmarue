--
-- Dumping data for table core_admin_dashboard
--
DELETE FROM core_admin_dashboard;
INSERT INTO core_admin_dashboard (dashboard_name,dashboard_column,dashboard_order) VALUES 
('usersAdminDashboardComponent',1,1),
('searchAdminDashboardComponent',1,2),
('formEngineAdminDashboardComponent',1,1),
('workflowAdminDashboardComponent',1,1),
('myluteceAuthenticationFilterAdminDashboardComponent',1,3);

--
-- Dumping data for table core_admin_right
--
DELETE FROM core_admin_right;
INSERT INTO core_admin_right (id_right,name,level_right,admin_url,description,is_updatable,plugin_name,id_feature_group,icon_url,documentation_url,id_order,is_external_feature) VALUES 
('SIGNALEMENT_DASHBOARD','dansmarue.adminFeature.signalementDashboard.name',2,'jsp/admin/plugins/signalement/ManageSignalementDashboard.jsp','dansmarue.adminFeature.signalementDashboard.description',0,'signalement','SIGNALEMENT',NULL,NULL,4,0)
,('CORE_ADMIN_SITE','portal.site.adminFeature.admin_site.name',2,'jsp/admin/site/AdminSite.jsp','portal.site.adminFeature.admin_site.description',1,NULL,'SITE','images/admin/skin/features/admin_site.png','jsp/admin/documentation/AdminDocumentation.jsp?doc=admin-site',1,0)
,('CORE_CACHE_MANAGEMENT','portal.system.adminFeature.cache_management.name',0,'jsp/admin/system/ManageCaches.jsp','portal.system.adminFeature.cache_management.description',1,NULL,'SYSTEM','images/admin/skin/features/manage_caches.png',NULL,1,0)
,('CORE_SEARCH_INDEXATION','portal.search.adminFeature.indexer.name',0,'jsp/admin/search/ManageSearchIndexation.jsp','portal.search.adminFeature.indexer.description',0,NULL,'SYSTEM',NULL,NULL,2,0)
,('CORE_SEARCH_MANAGEMENT','portal.search.adminFeature.search_management.name',0,'jsp/admin/search/ManageSearch.jsp','portal.search.adminFeature.search_management.description',0,NULL,'SYSTEM',NULL,NULL,3,0)
,('CORE_LOGS_VISUALISATION','portal.system.adminFeature.logs_visualisation.name',0,'jsp/admin/system/ManageFilesSystem.jsp','portal.system.adminFeature.logs_visualisation.description',1,NULL,'SYSTEM','images/admin/skin/features/view_logs.png',NULL,4,0)
,('CORE_MODES_MANAGEMENT','portal.style.adminFeature.modes_management.name',0,'jsp/admin/style/ManageModes.jsp','portal.style.adminFeature.modes_management.description',1,NULL,'STYLE','images/admin/skin/features/manage_modes.png',NULL,1,0)
,('CORE_PAGE_TEMPLATE_MANAGEMENT','portal.style.adminFeature.page_template_management.name',0,'jsp/admin/style/ManagePageTemplates.jsp','portal.style.adminFeature.page_template_management.description',0,NULL,'STYLE','images/admin/skin/features/manage_page_templates.png',NULL,2,0)
,('CORE_PLUGINS_MANAGEMENT','portal.system.adminFeature.plugins_management.name',0,'jsp/admin/system/ManagePlugins.jsp','portal.system.adminFeature.plugins_management.description',1,NULL,'SYSTEM','images/admin/skin/features/manage_plugins.png',NULL,5,0)
,('CORE_PROPERTIES_MANAGEMENT','portal.site.adminFeature.properties_management.name',2,'jsp/admin/ManageProperties.jsp','portal.site.adminFeature.properties_management.description',0,NULL,'SITE',NULL,'jsp/admin/documentation/AdminDocumentation.jsp?doc=admin-properties',2,0)
;
INSERT INTO core_admin_right (id_right,name,level_right,admin_url,description,is_updatable,plugin_name,id_feature_group,icon_url,documentation_url,id_order,is_external_feature) VALUES 
('CORE_STYLESHEET_MANAGEMENT','portal.style.adminFeature.stylesheet_management.name',0,'jsp/admin/style/ManageStyleSheets.jsp','portal.style.adminFeature.stylesheet_management.description',1,NULL,'STYLE','images/admin/skin/features/manage_stylesheets.png',NULL,3,0)
,('CORE_STYLES_MANAGEMENT','portal.style.adminFeature.styles_management.name',0,'jsp/admin/style/ManageStyles.jsp','portal.style.adminFeature.styles_management.description',1,NULL,'STYLE','images/admin/skin/features/manage_styles.png',NULL,4,0)
,('CORE_USERS_MANAGEMENT','portal.users.adminFeature.users_management.name',2,'jsp/admin/user/ManageUsers.jsp','portal.users.adminFeature.users_management.description',1,'','MANAGERS','images/admin/skin/features/manage_users.png',NULL,1,0)
,('CORE_RBAC_MANAGEMENT','portal.rbac.adminFeature.rbac_management.name',0,'jsp/admin/rbac/ManageRoles.jsp','portal.rbac.adminFeature.rbac_management.description',0,'','MANAGERS','images/admin/skin/features/manage_rbac.png',NULL,2,0)
,('CORE_DAEMONS_MANAGEMENT','portal.system.adminFeature.daemons_management.name',0,'jsp/admin/system/ManageDaemons.jsp','portal.system.adminFeature.daemons_management.description',0,NULL,'SYSTEM',NULL,NULL,7,0)
,('CORE_WORKGROUPS_MANAGEMENT','portal.workgroup.adminFeature.workgroups_management.name',2,'jsp/admin/workgroup/ManageWorkgroups.jsp','portal.workgroup.adminFeature.workgroups_management.description',0,NULL,'MANAGERS','images/admin/skin/features/manage_workgroups.png',NULL,3,0)
,('CORE_ROLES_MANAGEMENT','portal.role.adminFeature.roles_management.name',2,'jsp/admin/role/ManagePageRole.jsp','portal.role.adminFeature.roles_management.description',0,NULL,'USERS','images/admin/skin/features/manage_roles.png',NULL,1,0)
,('CORE_MAILINGLISTS_MANAGEMENT','portal.mailinglist.adminFeature.mailinglists_management.name',2,'jsp/admin/mailinglist/ManageMailingLists.jsp','portal.mailinglist.adminFeature.mailinglists_management.description',0,NULL,'MANAGERS','images/admin/skin/features/manage_mailinglists.png',NULL,4,0)
,('CORE_LEVEL_RIGHT_MANAGEMENT','portal.users.adminFeature.level_right_management.name',2,'jsp/admin/features/ManageLevels.jsp','portal.users.adminFeature.level_right_management.description',0,NULL,'MANAGERS','images/admin/skin/features/manage_rights_levels.png',NULL,5,0)
,('CORE_LINK_SERVICE_MANAGEMENT','portal.insert.adminFeature.linkService_management.name',2,NULL,'portal.insert.adminFeature.linkService_management.description',0,NULL,NULL,NULL,NULL,1,0)
;
INSERT INTO core_admin_right (id_right,name,level_right,admin_url,description,is_updatable,plugin_name,id_feature_group,icon_url,documentation_url,id_order,is_external_feature) VALUES 
('CORE_RIGHT_MANAGEMENT','portal.users.adminFeature.right_management.name',0,'jsp/admin/features/ManageRights.jsp','portal.users.adminFeature.right_management.description',0,NULL,'MANAGERS','images/admin/skin/features/manage_rights_levels.png',NULL,5,0)
,('CORE_DASHBOARD_MANAGEMENT','portal.dashboard.adminFeature.dashboard_management.name',0,'jsp/admin/dashboard/ManageDashboards.jsp','portal.dashboard.adminFeature.dashboard_management.description',0,NULL,'SYSTEM','images/admin/skin/features/manage_dashboards.png',NULL,9,0)
,('PROFILES_MANAGEMENT','profiles.adminFeature.profiles_management.name',0,'jsp/admin/plugins/profiles/ManageProfiles.jsp','profiles.adminFeature.profiles_management.description',0,'profiles','MANAGERS','images/admin/skin/plugins/profiles/profiles.png',NULL,NULL,0)
,('PROFILES_VIEWS_MANAGEMENT','profiles.adminFeature.views_management.name',0,'jsp/admin/plugins/profiles/ManageViews.jsp','profiles.adminFeature.views_management.description',0,'profiles','MANAGERS','images/admin/skin/plugins/profiles/views.png',NULL,NULL,0)
,('UNITS_MANAGEMENT','unittree.adminFeature.unitsManagement.name',2,'jsp/admin/plugins/unittree/ManageUnits.jsp','unittree.adminFeature.unitsManagement.description',0,'unittree','MANAGERS','images/admin/skin/plugins/unittree/unittree.png','',NULL,0)
,('WORKFLOW_MANAGEMENT','workflow.adminFeature.workflow_management.name',2,'jsp/admin/plugins/workflow/ManageWorkflow.jsp','workflow.adminFeature.workflow_management.description',0,'workflow','APPLICATIONS','images/admin/skin/plugins/workflow/workflow.png','jsp/admin/documentation/AdminDocumentation.jsp?doc=admin-workflow',NULL,0)
,('CORE_XSL_EXPORT_MANAGEMENT','portal.xsl.adminFeature.xsl_export_management.name',2,'jsp/admin/xsl/ManageXslExport.jsp','portal.xsl.adminFeature.xsl_export_management.description',1,NULL,'SYSTEM',NULL,NULL,10,0)
,('CORE_ADMINDASHBOARD_MANAGEMENT','portal.admindashboard.adminFeature.right_management.name',0,'jsp/admin/admindashboard/ManageAdminDashboards.jsp','portal.admindashboard.adminFeature.right_management.description',0,NULL,'SIGNALEMENT','images/admin/skin/features/manage_admindashboards.png',NULL,8,0)
,('CORE_GLOBAL_MANAGEMENT','portal.globalmanagement.adminFeature.global_management.name',2,'jsp/admin/globalmanagement/GetGlobalManagement.jsp','portal.globalmanagement.adminFeature.global_management.description',1,NULL,'SYSTEM',NULL,NULL,10,0)
,('CORE_FEATURES_MANAGEMENT','portal.admin.adminFeature.features_management.name',0,'jsp/admin/features/DispatchFeatures.jsp','portal.admin.adminFeature.features_management.description',0,NULL,'SYSTEM','images/admin/skin/features/manage_features.png',NULL,6,0)
;
INSERT INTO core_admin_right (id_right,name,level_right,admin_url,description,is_updatable,plugin_name,id_feature_group,icon_url,documentation_url,id_order,is_external_feature) VALUES 
('MESSAGE_TRACKING_MANAGEMENT','module.workflow.dansmarue.adminFeature.messageTrackingManagement.name',2,'jsp/admin/plugins/workflow/modules/signalement/GetMessageTrackingManagement.jsp','module.workflow.signalement.adminFeature.messageTrackingManagement.description',0,'signalement','SIGNALEMENT',NULL,NULL,1,0)
,('MYLUTECE_MANAGEMENT','mylutece.adminFeature.mylutece_management.name',2,'jsp/admin/plugins/mylutece/attribute/ManageAttributes.jsp','mylutece.adminFeature.mylutece_management.description',0,'mylutece','USERS',NULL,NULL,NULL,0)
,('MYLUTECE_MANAGE_AUTHENTICATION_FILTER','mylutece.adminFeature.mylutece_management_authentication_filter.name',2,'jsp/admin/plugins/mylutece/security/ManageAuthenticationFilter.jsp','mylutece.adminFeature.mylutece_management_authentication_filter.description',0,'mylutece','USERS',NULL,NULL,NULL,0)
,('REFERENTIEL_MANAGEMENT_SIGNALEMENT','dansmarue.adminFeature.referentielmanagement.name',2,'jsp/admin/plugins/signalement/ManageReferentiel.jsp','dansmarue.adminFeature.referentielmanagement.description',0,'signalement','SIGNALEMENT',NULL,NULL,2,0)
,('SIGNALEMENT_MANAGEMENT','dansmarue.adminFeature.signalementManagement.name',2,'jsp/admin/plugins/signalement/ManageSignalement.jsp','dansmarue.adminFeature.signalementManagement.description',0,'signalement','SIGNALEMENT',NULL,NULL,1,0)
,('PIWIK_MANAGEMENT','piwik.adminFeature.ManagePiwik.name',2,'jsp/admin/plugins/piwik/Piwik.jsp','piwik.adminFeature.ManagePiwik.description',0,'piwik',NULL,NULL,NULL,2,0)
,('CORE_EXTERNAL_FEATURES_MANAGEMENT','portal.system.adminFeature.external_features_management.name',1,'jsp/admin/features/ManageExternalFeatures.jsp','portal.system.adminFeature.external_features_management.description',1,NULL,'SYSTEM',NULL,NULL,11,0)
,('SIGNALEMENT_DISPLAY','dansmarue.adminFeature.signalementdisplay.name',2,'jsp/admin/plugins/signalement/DisplaySignalement.jsp','dansmarue.adminFeature.signalementdisplay.description',0,'signalement','SIGNALEMENT',NULL,NULL,3,0)
,('SITELABELS_MANAGEMENT','sitelabels.adminFeature.ManageSiteLabels.name',0,'jsp/admin/plugins/sitelabels/ManageLabels.jsp','sitelabels.adminFeature.ManageSiteLabels.description',0,'sitelabels','SITE',NULL,NULL,3,0)
,('ELASTICDATA_MANAGEMENT','elasticdata.adminFeature.ManageElasticData.name',0,'jsp/admin/plugins/elasticdata/ManageElasticData.jsp','elasticdata.adminFeature.ManageElasticData.description',0,'elasticdata',NULL,NULL,NULL,3,0)
;

--
-- Dumping data for table core_admin_role
--
DELETE FROM core_admin_role;
INSERT INTO core_admin_role (role_key,role_description) VALUES 
('all_site_manager','Site Manager')
,('super_admin','Super Administrateur')
,('profiles_manager','Profiles management')
,('profiles_views_manager','Profiles Views management')
,('unittree_management','Gestion des entités')
,('workflow_manager','Workflow management')
,('DEVE_CODIR','Chef de division, son adjoint ou représentant')
,('DPE_CELLULETECHNIQUE','Cellule technique de la DPE')
,('DPE_CODIR','Chef de division, son adjoint ou représentant')
,('DPE_OBSERVATEUR','Observateur DPE (ne peut rien faire mais voit tout)')
;
INSERT INTO core_admin_role (role_key,role_description) VALUES 
('DPE_STANDARD','Standardiste d''une division de la DPE')
,('DVD_CHEFDESECTEUR','Chef de secteur de la DVD')
,('OBSERVATEUR_MAIRIES','observateur mairie d''arrondissement')
,('ADMIN_Projet','ADMIN_Projet')
,('DPE_FONCTIONNELLE','GESTION_DES_GRAFFITIS_DES_AFFICHAGES')
,('Observateur_12eme','Observateur_12eme')
,('OBSERVATEUR_MAIRIE_1er','OBSERVATEUR_MAIRIE_1er')
,('OBSERVATEUR_MAIRIE_2','OBSERVATEUR_MAIRIE_2')
,('OBSERVATEUR_MAIRIE_3','OBSERVATEUR_MAIRIE_3')
,('OBSERVATEUR_MAIRIE_4','OBSERVATEUR_MAIRIE_4')
;
INSERT INTO core_admin_role (role_key,role_description) VALUES 
('OBSERVATEUR_MAIRIE_5','OBSERVATEUR_MAIRIE_5')
,('OBSERVATEUR_MAIRIE_6','OBSERVATEUR_MAIRIE_6')
,('OBSERVATEUR_MAIRIE_7','OBSERVATEUR_MAIRIE_7')
,('OBSERVATEUR_MAIRIE_8','OBSERVATEUR_MAIRIE_8')
,('OBSERVATEUR_MAIRIE_9','OBSERVATEUR_MAIRIE_6')
,('OBSERVATEUR_MAIRIE_10','OBSERVATEUR_MAIRIE_10')
,('OBSERVATEUR_MAIRIE_11','OBSERVATEUR_MAIRIE_11')
,('OBSERVATEUR_MAIRIE_12','OBSERVATEUR_MAIRIE_12')
,('OBSERVATEUR_MAIRIE_13','OBSERVATEUR_MAIRIE_13')
,('OBSERVATEUR_MAIRIE_14','OBSERVATEUR_MAIRIE_14')
;
INSERT INTO core_admin_role (role_key,role_description) VALUES 
('OBSERVATEUR_MAIRIE_15','OBSERVATEUR_MAIRIE_15')
,('OBSERVATEUR_MAIRIE_16','OBSERVATEUR_MAIRIE_16')
,('OBSERVATEUR_MAIRIE_17','OBSERVATEUR_MAIRIE_17')
,('OBSERVATEUR_MAIRIE_18','OBSERVATEUR_MAIRIE_18')
,('OBSERVATEUR_MAIRIE_19','OBSERVATEUR_MAIRIE_19')
,('OBSERVATEUR_MAIRIE_20','OBSERVATEUR_MAIRIE_20')
,('DEVE_VEGETALISATION1','DEVE_VEGETALISATION')
,('SUPP_VEGE','SUPP_VEGE')
,('SUPPR_VEGETALISATION','SUPPR_VEGETALISATION')
,('DPE_GRAF_ALL','DPE_GRAF_ALL')
;
INSERT INTO core_admin_role (role_key,role_description) VALUES 
('DVD_PRESTA','DVD_PRESTA')
,('test','test')
,('test_jpc','test_jpc')
,('assign_roles','Assigner des roles aux utilisateurs')
,('assign_groups','Assigner des groupes aux utilisateurs')
,('mylutece_manager','Gérer les paramètres avancés Mylutece')
,('DVD_CODIR','Chef de division, son adjoint ou représentant')
,('PRESTATAIRE','Prestataire')
,('Consultation_DVD','Consultation des anomalies pour DVD')
,('Consultation_DPE','Consultation des anomalies pour DPE')
;
INSERT INTO core_admin_role (role_key,role_description) VALUES 
('testx','testx yyu')
,('Consultation_DVD_agent','Consultation pour agent de la DVD')
,('Consultation_DVD_CENTRE','Consultation_DVD_CENTRE')
,('Consultation_DVD_NORD_EST','Consultation_DVD_NORD_EST')
,('Consultation_DVD_NORD_OUEST','Consultation_DVD_NORD_OUEST')
,('Consultation_DVD_SUD','Consultation_DVD_SUD')
,('Consultation_DVD_SUD_EST','Consultation_DVD_SUD_EST')
,('Consultation_DVD_SUD_OUEST','Consultation_DVD_SUD_OUEST')
,('Consultation_DEVE','Consultation des anomalies pour DEVE sur tout Paris')
,('DEVE_Admin_User','Gestion des utilisateurs de la DEVE')
;
INSERT INTO core_admin_role (role_key,role_description) VALUES 
('DPE_Admin_User','Administration des utilisateurs de la DPE')
,('DAJ_Gestionnaire','Gestionnaire de la DAJ')
;

--
-- Dumping data for table core_admin_role_resource
--
DELETE FROM core_admin_role_resource;
INSERT INTO core_admin_role_resource (rbac_id,role_key,resource_type,resource_id,permission) VALUES 
(57,'all_site_manager','PAGE','*','VIEW')
,(58,'all_site_manager','PAGE','*','MANAGE')
,(77,'super_admin','INSERT_SERVICE','*','*')
,(101,'all_site_manager','PORTLET_TYPE','*','*')
,(111,'all_site_manager','ADMIN_USER','*','*')
,(137,'all_site_manager','SEARCH_SERVICE','*','*')
,(233,'formengine_manager','FORMENGINE','*','*')
,(150,'profiles_manager','PROFILES','*','*')
,(151,'profiles_views_manager','PROFILES_VIEWS','*','*')
,(1637,'DAJ_Gestionnaire','GESTION_DES_SIGNALEMENTS','*','RECHERCHER_SIGNALEMENT')
;
INSERT INTO core_admin_role_resource (rbac_id,role_key,resource_type,resource_id,permission) VALUES 
(950,'DVD_CODIR','WORKFLOW_ACTION_TYPE','*','*')
,(951,'DVD_CODIR','WORKFLOW_STATE_TYPE','*','*')
,(1638,'DAJ_Gestionnaire','GESTION_DES_SIGNALEMENTS','*','MODIFICATION_SIGNALEMENT')
,(954,'DPE_CODIR','WORKFLOW_STATE_TYPE','*','*')
,(1639,'DAJ_Gestionnaire','GESTION_DES_SIGNALEMENTS','*','EXPORTER_SIGNALEMENT')
,(957,'DPE_STANDARD','GESTION_DES_SIGNALEMENTS','*','MODIFICATION_SIGNALEMENT')
,(958,'DPE_STANDARD','GESTION_DES_SIGNALEMENTS','*','CONSULTATION_SIGNALEMENT')
,(959,'DPE_STANDARD','WORKFLOW_ACTION_TYPE','*','*')
,(960,'DPE_STANDARD','WORKFLOW_STATE_TYPE','*','*')
,(1640,'DAJ_Gestionnaire','GESTION_DES_SIGNALEMENTS','*','CREATION_SIGNALEMENT')
;
INSERT INTO core_admin_role_resource (rbac_id,role_key,resource_type,resource_id,permission) VALUES 
(1641,'DAJ_Gestionnaire','GESTION_DES_SIGNALEMENTS','*','CONSULTATION_SIGNALEMENT')
,(970,'DPE_CELLULETECHNIQUE','WORKFLOW_STATE_TYPE','*','*')
,(976,'DPE_OBSERVATEUR','GESTION_DES_SIGNALEMENTS','*','RECHERCHER_SIGNALEMENT')
,(977,'DPE_OBSERVATEUR','GESTION_DES_SIGNALEMENTS','*','CONSULTATION_SIGNALEMENT')
,(979,'DPE_OBSERVATEUR','WORKFLOW_STATE_TYPE','*','*')
,(980,'DVD_CHEFDESECTEUR','GESTION_DES_SIGNALEMENTS','*','RECHERCHER_SIGNALEMENT')
,(981,'DVD_CHEFDESECTEUR','GESTION_DES_SIGNALEMENTS','*','MODIFICATION_SIGNALEMENT')
,(982,'DVD_CHEFDESECTEUR','GESTION_DES_SIGNALEMENTS','*','CONSULTATION_SIGNALEMENT')
,(984,'DVD_CHEFDESECTEUR','WORKFLOW_STATE_TYPE','*','*')
,(985,'DEVE_CODIR','GESTION_DES_SIGNALEMENTS','*','RECHERCHER_SIGNALEMENT')
;
INSERT INTO core_admin_role_resource (rbac_id,role_key,resource_type,resource_id,permission) VALUES 
(988,'DEVE_CODIR','WORKFLOW_ACTION_TYPE','*','*')
,(989,'DEVE_CODIR','WORKFLOW_STATE_TYPE','*','*')
,(991,'DPE_CODIR','GESTION_DES_SIGNALEMENTS','*','MODIFICATION_SIGNALEMENT')
,(992,'DPE_CODIR','GESTION_DES_SIGNALEMENTS','*','CONSULTATION_SIGNALEMENT')
,(994,'DVD_CODIR','GESTION_DES_SIGNALEMENTS','*','MODIFICATION_SIGNALEMENT')
,(1002,'DPE_CODIR','GESTION_DES_SIGNALEMENTS','*','RECHERCHER_SIGNALEMENT')
,(1005,'DPE_CODIR','GESTION_DES_SIGNALEMENTS','*','CREATION_SIGNALEMENT')
,(1014,'DPE_STANDARD','GESTION_DES_SIGNALEMENTS','*','RECHERCHER_SIGNALEMENT')
,(1017,'DPE_STANDARD','GESTION_DES_SIGNALEMENTS','*','CREATION_SIGNALEMENT')
,(1642,'DAJ_Gestionnaire','GESTION_DES_SIGNALEMENTS','*','ENVOI_MAIL_SIGNALEMENT')
;
INSERT INTO core_admin_role_resource (rbac_id,role_key,resource_type,resource_id,permission) VALUES 
(1643,'DAJ_Gestionnaire','GESTION_DES_SIGNALEMENTS','*','TRAITEMENT_MASSE')
,(1644,'DAJ_Gestionnaire','GESTION_SUIVI_DES_MESSAGES_DANSMARUE','*','*')
,(1646,'DAJ_Gestionnaire','WORKFLOW_ACTION_TYPE','*','*')
,(1647,'DAJ_Gestionnaire','SIGNALEMENT_ARRONDISSEMENT','*','*')
,(1648,'DAJ_Gestionnaire','TYPE_SIGNALEMENT','*','*')
,(1649,'DAJ_Gestionnaire','TYPE_SIGNALEMENT','12006','*')
,(1081,'DPE_CELLULETECHNIQUE','WORKFLOW_ACTION_TYPE','*','*')
,(1085,'DVD_CHEFDESECTEUR','WORKFLOW_ACTION_TYPE','*','*')
,(1650,'Consultation_DEVE','SIGNALEMENT_DOMAINE_FONCTIONNEL','23','*')
,(949,'super_admin','GESTION_DES_SIGNALEMENTS','*','*')
;
INSERT INTO core_admin_role_resource (rbac_id,role_key,resource_type,resource_id,permission) VALUES 
(1651,'Consultation_DEVE','SIGNALEMENT_DOMAINE_FONCTIONNEL','24','*')
,(1652,'Consultation_DEVE','SIGNALEMENT_DOMAINE_FONCTIONNEL','25','*')
,(1013,'DVD_CODIR','GESTION_DES_SIGNALEMENTS','*','CREATION_SIGNALEMENT')
,(210,'unittree_management','UNIT_TYPE','*','*')
,(912,'workflow_manager','WORKFLOW_ACTION_TYPE','*','*')
,(923,'workflow_manager','WORKFLOW_STATE_TYPE','*','*')
,(939,'super_admin','WORKFLOW_ACTION_TYPE','*','*')
,(947,'super_admin','WORKFLOW_STATE_TYPE','*','*')
,(1041,'DEVE_CODIR','GESTION_DES_SIGNALEMENTS','*','MODIFICATION_SIGNALEMENT')
,(1042,'DEVE_CODIR','GESTION_DES_SIGNALEMENTS','*','CONSULTATION_SIGNALEMENT')
;
INSERT INTO core_admin_role_resource (rbac_id,role_key,resource_type,resource_id,permission) VALUES 
(1043,'DEVE_CODIR','GESTION_DES_SIGNALEMENTS','*','CREATION_SIGNALEMENT')
,(1044,'DVD_CODIR','GESTION_DES_SIGNALEMENTS','*','RECHERCHER_SIGNALEMENT')
,(1046,'DVD_CODIR','GESTION_DES_SIGNALEMENTS','*','CONSULTATION_SIGNALEMENT')
,(1048,'DPE_CODIR','WORKFLOW_ACTION_TYPE','*','*')
,(1078,'DPE_CELLULETECHNIQUE','GESTION_DES_SIGNALEMENTS','*','RECHERCHER_SIGNALEMENT')
,(1080,'DPE_CELLULETECHNIQUE','GESTION_DES_SIGNALEMENTS','*','CONSULTATION_SIGNALEMENT')
,(1146,'DPE_CODIR','GESTION_DES_SIGNALEMENTS','*','ENVOI_MAIL_SIGNALEMENT')
,(1147,'DEVE_CODIR','GESTION_DES_SIGNALEMENTS','*','ENVOI_MAIL_SIGNALEMENT')
,(1148,'DVD_CODIR','GESTION_DES_SIGNALEMENTS','*','ENVOI_MAIL_SIGNALEMENT')
,(1149,'DVD_CHEFDESECTEUR','GESTION_DES_SIGNALEMENTS','*','ENVOI_MAIL_SIGNALEMENT')
;
INSERT INTO core_admin_role_resource (rbac_id,role_key,resource_type,resource_id,permission) VALUES 
(1150,'DPE_STANDARD','GESTION_DES_SIGNALEMENTS','*','ENVOI_MAIL_SIGNALEMENT')
,(1156,'DPE_CELLULETECHNIQUE','GESTION_DES_SIGNALEMENTS','*','ENVOI_MAIL_SIGNALEMENT')
,(1213,'OBSERVATEUR_MAIRIES','GESTION_DES_SIGNALEMENTS','*','RECHERCHER_SIGNALEMENT')
,(1214,'OBSERVATEUR_MAIRIES','GESTION_DES_SIGNALEMENTS','*','CONSULTATION_SIGNALEMENT')
,(1200,'super_admin','GESTION_SUIVI_DES_MESSAGES_DANSMARUE','*','*')
,(1249,'ADMIN_Projet','WORKFLOW_ACTION_TYPE','*','*')
,(1250,'ADMIN_Projet','WORKFLOW_STATE_TYPE','*','*')
,(1251,'ADMIN_Projet','GESTION_DES_SIGNALEMENTS','*','*')
,(1253,'ADMIN_Projet','GESTION_SUIVI_DES_MESSAGES_DANSMARUE','*','*')
,(1254,'DPE_FONCTIONNELLE','WORKFLOW_ACTION_TYPE','*','*')
;
INSERT INTO core_admin_role_resource (rbac_id,role_key,resource_type,resource_id,permission) VALUES 
(1255,'DPE_FONCTIONNELLE','WORKFLOW_STATE_TYPE','*','*')
,(1257,'DPE_FONCTIONNELLE','GESTION_DES_SIGNALEMENTS','*','RECHERCHER_SIGNALEMENT')
,(1258,'DPE_FONCTIONNELLE','GESTION_DES_SIGNALEMENTS','*','ENVOI_MAIL_SIGNALEMENT')
,(1259,'DPE_FONCTIONNELLE','GESTION_DES_SIGNALEMENTS','*','MODIFICATION_SIGNALEMENT')
,(1260,'DPE_FONCTIONNELLE','GESTION_DES_SIGNALEMENTS','*','CONSULTATION_SIGNALEMENT')
,(1261,'DVD_CODIR','GESTION_SUIVI_DES_MESSAGES_DANSMARUE','*','*')
,(1262,'DEVE_CODIR','GESTION_SUIVI_DES_MESSAGES_DANSMARUE','*','*')
,(1263,'DPE_CELLULETECHNIQUE','GESTION_SUIVI_DES_MESSAGES_DANSMARUE','*','*')
,(1265,'DPE_CODIR','GESTION_SUIVI_DES_MESSAGES_DANSMARUE','*','*')
,(1267,'DPE_FONCTIONNELLE','GESTION_SUIVI_DES_MESSAGES_DANSMARUE','*','*')
;
INSERT INTO core_admin_role_resource (rbac_id,role_key,resource_type,resource_id,permission) VALUES 
(1268,'DPE_OBSERVATEUR','GESTION_SUIVI_DES_MESSAGES_DANSMARUE','*','*')
,(1269,'DPE_STANDARD','GESTION_SUIVI_DES_MESSAGES_DANSMARUE','*','*')
,(1270,'DVD_CHEFDESECTEUR','GESTION_SUIVI_DES_MESSAGES_DANSMARUE','*','*')
,(1271,'OBSERVATEUR_MAIRIES','GESTION_SUIVI_DES_MESSAGES_DANSMARUE','*','*')
,(1275,'Observateur_12eme','SIGNALEMENT_ARRONDISSEMENT','12','VIEW_ARRONDISSEMENT_SIGNALEMENT')
,(1276,'OBSERVATEUR_MAIRIES','WORKFLOW_STATE_TYPE','7','*')
,(1277,'OBSERVATEUR_MAIRIES','WORKFLOW_STATE_TYPE','8','*')
,(1278,'OBSERVATEUR_MAIRIES','WORKFLOW_STATE_TYPE','9','*')
,(1279,'OBSERVATEUR_MAIRIES','WORKFLOW_STATE_TYPE','10','*')
,(1280,'OBSERVATEUR_MAIRIES','WORKFLOW_STATE_TYPE','11','*')
;
INSERT INTO core_admin_role_resource (rbac_id,role_key,resource_type,resource_id,permission) VALUES 
(1281,'OBSERVATEUR_MAIRIES','WORKFLOW_STATE_TYPE','12','*')
,(1282,'OBSERVATEUR_MAIRIES','WORKFLOW_STATE_TYPE','13','*')
,(1283,'OBSERVATEUR_MAIRIES','WORKFLOW_STATE_TYPE','14','*')
,(1284,'OBSERVATEUR_MAIRIES','WORKFLOW_STATE_TYPE','15','*')
,(1285,'OBSERVATEUR_MAIRIES','WORKFLOW_STATE_TYPE','16','*')
,(1286,'OBSERVATEUR_MAIRIES','WORKFLOW_STATE_TYPE','17','*')
,(1287,'OBSERVATEUR_MAIRIES','WORKFLOW_STATE_TYPE','18','*')
,(1288,'OBSERVATEUR_MAIRIE_1er','SIGNALEMENT_ARRONDISSEMENT','1','VIEW_ARRONDISSEMENT_SIGNALEMENT')
,(1289,'OBSERVATEUR_MAIRIE_2','SIGNALEMENT_ARRONDISSEMENT','2','VIEW_ARRONDISSEMENT_SIGNALEMENT')
,(1290,'OBSERVATEUR_MAIRIE_3','SIGNALEMENT_ARRONDISSEMENT','3','VIEW_ARRONDISSEMENT_SIGNALEMENT')
;
INSERT INTO core_admin_role_resource (rbac_id,role_key,resource_type,resource_id,permission) VALUES 
(1291,'OBSERVATEUR_MAIRIE_4','SIGNALEMENT_ARRONDISSEMENT','4','VIEW_ARRONDISSEMENT_SIGNALEMENT')
,(1292,'OBSERVATEUR_MAIRIE_5','SIGNALEMENT_ARRONDISSEMENT','5','VIEW_ARRONDISSEMENT_SIGNALEMENT')
,(1293,'OBSERVATEUR_MAIRIE_6','SIGNALEMENT_ARRONDISSEMENT','6','VIEW_ARRONDISSEMENT_SIGNALEMENT')
,(1294,'OBSERVATEUR_MAIRIE_7','SIGNALEMENT_ARRONDISSEMENT','7','VIEW_ARRONDISSEMENT_SIGNALEMENT')
,(1295,'OBSERVATEUR_MAIRIE_8','SIGNALEMENT_ARRONDISSEMENT','8','VIEW_ARRONDISSEMENT_SIGNALEMENT')
,(1296,'OBSERVATEUR_MAIRIE_8','SIGNALEMENT_ARRONDISSEMENT','8','VIEW_ARRONDISSEMENT_SIGNALEMENT')
,(1297,'OBSERVATEUR_MAIRIE_9','SIGNALEMENT_ARRONDISSEMENT','9','VIEW_ARRONDISSEMENT_SIGNALEMENT')
,(1298,'OBSERVATEUR_MAIRIE_10','SIGNALEMENT_ARRONDISSEMENT','10','VIEW_ARRONDISSEMENT_SIGNALEMENT')
,(1300,'OBSERVATEUR_MAIRIE_12','SIGNALEMENT_ARRONDISSEMENT','12','VIEW_ARRONDISSEMENT_SIGNALEMENT')
,(1301,'OBSERVATEUR_MAIRIE_13','SIGNALEMENT_ARRONDISSEMENT','13','VIEW_ARRONDISSEMENT_SIGNALEMENT')
;
INSERT INTO core_admin_role_resource (rbac_id,role_key,resource_type,resource_id,permission) VALUES 
(1302,'OBSERVATEUR_MAIRIE_14','SIGNALEMENT_ARRONDISSEMENT','14','VIEW_ARRONDISSEMENT_SIGNALEMENT')
,(1303,'OBSERVATEUR_MAIRIE_15','SIGNALEMENT_ARRONDISSEMENT','15','VIEW_ARRONDISSEMENT_SIGNALEMENT')
,(1304,'OBSERVATEUR_MAIRIE_16','SIGNALEMENT_ARRONDISSEMENT','16','VIEW_ARRONDISSEMENT_SIGNALEMENT')
,(1305,'OBSERVATEUR_MAIRIE_17','SIGNALEMENT_ARRONDISSEMENT','17','VIEW_ARRONDISSEMENT_SIGNALEMENT')
,(1306,'OBSERVATEUR_MAIRIE_18','SIGNALEMENT_ARRONDISSEMENT','18','VIEW_ARRONDISSEMENT_SIGNALEMENT')
,(1307,'OBSERVATEUR_MAIRIE_19','SIGNALEMENT_ARRONDISSEMENT','19','VIEW_ARRONDISSEMENT_SIGNALEMENT')
,(1308,'OBSERVATEUR_MAIRIE_20','SIGNALEMENT_ARRONDISSEMENT','20','VIEW_ARRONDISSEMENT_SIGNALEMENT')
,(1309,'OBSERVATEUR_MAIRIE_11','SIGNALEMENT_ARRONDISSEMENT','11','VIEW_ARRONDISSEMENT_SIGNALEMENT')
,(1389,'DEVE_VEGETALISATION1','WORKFLOW_STATE_TYPE','*','*')
,(1390,'DEVE_VEGETALISATION1','GESTION_DES_SIGNALEMENTS','*','RECHERCHER_SIGNALEMENT')
;
INSERT INTO core_admin_role_resource (rbac_id,role_key,resource_type,resource_id,permission) VALUES 
(1391,'DEVE_VEGETALISATION1','GESTION_DES_SIGNALEMENTS','*','ENVOI_MAIL_SIGNALEMENT')
,(1392,'DEVE_VEGETALISATION1','GESTION_DES_SIGNALEMENTS','*','MODIFICATION_SIGNALEMENT')
,(1393,'DEVE_VEGETALISATION1','GESTION_DES_SIGNALEMENTS','*','CONSULTATION_SIGNALEMENT')
,(1394,'DEVE_VEGETALISATION1','GESTION_DES_SIGNALEMENTS','*','EXPORTER_SIGNALEMENT')
,(1395,'DEVE_VEGETALISATION1','SIGNALEMENT_ARRONDISSEMENT','*','*')
,(1396,'DEVE_VEGETALISATION1','TYPE_SIGNALEMENT','10612','*')
,(1397,'DEVE_VEGETALISATION1','TYPE_SIGNALEMENT','10613','*')
,(1398,'DEVE_VEGETALISATION1','TYPE_SIGNALEMENT','10614','*')
,(1399,'DEVE_VEGETALISATION1','TYPE_SIGNALEMENT','10615','*')
,(1400,'DEVE_VEGETALISATION1','TYPE_SIGNALEMENT','10616','*')
;
INSERT INTO core_admin_role_resource (rbac_id,role_key,resource_type,resource_id,permission) VALUES 
(1401,'DEVE_VEGETALISATION1','TYPE_SIGNALEMENT','10617','*')
,(1408,'SUPP_VEGE','WORKFLOW_STATE_TYPE','*','*')
,(1410,'SUPPR_VEGETALISATION','WORKFLOW_STATE_TYPE','7','*')
,(1411,'SUPPR_VEGETALISATION','WORKFLOW_STATE_TYPE','8','*')
,(1412,'SUPPR_VEGETALISATION','WORKFLOW_ACTION_TYPE','1','*')
,(1413,'SUPPR_VEGETALISATION','GESTION_DES_SIGNALEMENTS','*','SUPPRESSION_SIGNALEMENT')
,(1414,'SUPPR_VEGETALISATION','GESTION_DES_SIGNALEMENTS','*','RECHERCHER_SIGNALEMENT')
,(1415,'SUPPR_VEGETALISATION','GESTION_DES_SIGNALEMENTS','*','TRAITEMENT_MASSE')
,(1416,'SUPP_VEGE','GESTION_DES_SIGNALEMENTS','*','SUPPRESSION_SIGNALEMENT')
,(1417,'SUPP_VEGE','GESTION_DES_SIGNALEMENTS','*','RECHERCHER_SIGNALEMENT')
;
INSERT INTO core_admin_role_resource (rbac_id,role_key,resource_type,resource_id,permission) VALUES 
(1418,'SUPP_VEGE','GESTION_DES_SIGNALEMENTS','*','TRAITEMENT_MASSE')
,(1419,'SUPP_VEGE','GESTION_DES_SIGNALEMENTS','*','CONSULTATION_SIGNALEMENT')
,(1420,'SUPP_VEGE','TYPE_SIGNALEMENT','10612','*')
,(1421,'SUPP_VEGE','TYPE_SIGNALEMENT','10613','*')
,(1422,'SUPP_VEGE','TYPE_SIGNALEMENT','10614','*')
,(1423,'SUPP_VEGE','TYPE_SIGNALEMENT','10615','*')
,(1424,'SUPP_VEGE','TYPE_SIGNALEMENT','10616','*')
,(1425,'SUPP_VEGE','TYPE_SIGNALEMENT','10617','*')
,(1427,'DPE_GRAF_ALL','WORKFLOW_STATE_TYPE','7','*')
,(1428,'DPE_GRAF_ALL','WORKFLOW_STATE_TYPE','8','*')
;
INSERT INTO core_admin_role_resource (rbac_id,role_key,resource_type,resource_id,permission) VALUES 
(1429,'DPE_GRAF_ALL','WORKFLOW_STATE_TYPE','9','*')
,(1430,'DPE_GRAF_ALL','WORKFLOW_STATE_TYPE','10','*')
,(1431,'DPE_GRAF_ALL','WORKFLOW_STATE_TYPE','11','*')
,(1432,'DPE_GRAF_ALL','WORKFLOW_STATE_TYPE','12','*')
,(1433,'DPE_GRAF_ALL','WORKFLOW_STATE_TYPE','13','*')
,(1434,'DPE_GRAF_ALL','WORKFLOW_STATE_TYPE','15','*')
,(1435,'DPE_GRAF_ALL','WORKFLOW_STATE_TYPE','16','*')
,(1436,'DPE_GRAF_ALL','WORKFLOW_STATE_TYPE','17','*')
,(1437,'DPE_GRAF_ALL','WORKFLOW_STATE_TYPE','18','*')
,(1438,'DPE_GRAF_ALL','WORKFLOW_STATE_TYPE','19','*')
;
INSERT INTO core_admin_role_resource (rbac_id,role_key,resource_type,resource_id,permission) VALUES 
(1439,'DPE_GRAF_ALL','WORKFLOW_STATE_TYPE','20','*')
,(1440,'DPE_GRAF_ALL','WORKFLOW_STATE_TYPE','21','*')
,(1453,'DVD_PRESTA','GESTION_DES_SIGNALEMENTS','*','RECHERCHER_SIGNALEMENT')
,(1454,'DVD_PRESTA','GESTION_DES_SIGNALEMENTS','*','ENVOI_MAIL_SIGNALEMENT')
,(1455,'DVD_PRESTA','GESTION_DES_SIGNALEMENTS','*','CONSULTATION_SIGNALEMENT')
,(1456,'DVD_PRESTA','GESTION_DES_SIGNALEMENTS','*','EXPORTER_SIGNALEMENT')
,(1457,'DVD_PRESTA','WORKFLOW_STATE_TYPE','*','*')
,(1458,'DVD_PRESTA','TYPE_SIGNALEMENT','6010','*')
,(1459,'DVD_PRESTA','TYPE_SIGNALEMENT','6020','*')
,(1460,'DVD_PRESTA','TYPE_SIGNALEMENT','6030','*')
;
INSERT INTO core_admin_role_resource (rbac_id,role_key,resource_type,resource_id,permission) VALUES 
(1461,'DVD_PRESTA','TYPE_SIGNALEMENT','6040','*')
,(1489,'DPE_Admin_User','PROFILES_VIEWS','*','*')
,(1491,'DPE_Admin_User','GESTION_DES_SIGNALEMENTS','*','*')
,(1503,'DVD_PRESTA','SIGNALEMENT_ARRONDISSEMENT','*','*')
,(1504,'DPE_GRAF_ALL','GESTION_DES_SIGNALEMENTS','*','RECHERCHER_SIGNALEMENT')
,(1505,'DPE_GRAF_ALL','GESTION_DES_SIGNALEMENTS','*','ENVOI_MAIL_SIGNALEMENT')
,(1506,'DPE_GRAF_ALL','GESTION_DES_SIGNALEMENTS','*','MODIFICATION_SIGNALEMENT')
,(1507,'DPE_GRAF_ALL','GESTION_DES_SIGNALEMENTS','*','TRAITEMENT_MASSE')
,(1508,'DPE_GRAF_ALL','GESTION_DES_SIGNALEMENTS','*','CONSULTATION_SIGNALEMENT')
,(1510,'DPE_GRAF_ALL','GESTION_DES_SIGNALEMENTS','*','EXPORTER_SIGNALEMENT')
;
INSERT INTO core_admin_role_resource (rbac_id,role_key,resource_type,resource_id,permission) VALUES 
(1511,'DPE_GRAF_ALL','GESTION_DES_SIGNALEMENTS','*','CREATION_SIGNALEMENT')
,(164,'all_site_manager','XSL_EXPORT','*','*')
,(205,'assign_roles','ROLE_TYPE','*','ASSIGN_ROLE')
,(206,'assign_groups','GROUP_TYPE','*','ASSIGN_GROUP')
,(207,'mylutece_manager','MYLUTECE','*','*')
,(1512,'DVD_CODIR','GESTION_DES_SIGNALEMENTS','*','EXPORTER_SIGNALEMENT')
,(1513,'PRESTATAIRE','WORKFLOW_STATE_TYPE','*','*')
,(1514,'PRESTATAIRE','GESTION_DES_SIGNALEMENTS','*','RECHERCHER_SIGNALEMENT')
,(1515,'PRESTATAIRE','GESTION_DES_SIGNALEMENTS','*','EXPORTER_SIGNALEMENT')
,(1516,'PRESTATAIRE','GESTION_DES_SIGNALEMENTS','*','CONSULTATION_SIGNALEMENT')
;
INSERT INTO core_admin_role_resource (rbac_id,role_key,resource_type,resource_id,permission) VALUES 
(1544,'Consultation_DVD','GESTION_DES_SIGNALEMENTS','*','RECHERCHER_SIGNALEMENT')
,(1545,'Consultation_DVD','GESTION_DES_SIGNALEMENTS','*','EXPORTER_SIGNALEMENT')
,(1546,'Consultation_DVD','GESTION_DES_SIGNALEMENTS','*','CONSULTATION_SIGNALEMENT')
,(1549,'Consultation_DVD','SIGNALEMENT_DOMAINE_FONCTIONNEL','4','*')
,(1553,'Consultation_DPE','GESTION_DES_SIGNALEMENTS','*','RECHERCHER_SIGNALEMENT')
,(1554,'Consultation_DPE','GESTION_DES_SIGNALEMENTS','*','EXPORTER_SIGNALEMENT')
,(1555,'Consultation_DPE','GESTION_DES_SIGNALEMENTS','*','CONSULTATION_SIGNALEMENT')
,(1557,'PRESTATAIRE','WORKFLOW_ACTION_TYPE','62','*')
,(1558,'PRESTATAIRE','WORKFLOW_ACTION_TYPE','64','*')
,(1559,'PRESTATAIRE','WORKFLOW_ACTION_TYPE','71','*')
;
INSERT INTO core_admin_role_resource (rbac_id,role_key,resource_type,resource_id,permission) VALUES 
(1560,'PRESTATAIRE','WORKFLOW_ACTION_TYPE','73','*')
,(1561,'PRESTATAIRE','WORKFLOW_ACTION_TYPE','70','*')
,(1562,'PRESTATAIRE','WORKFLOW_ACTION_TYPE','72','*')
,(1563,'PRESTATAIRE','WORKFLOW_ACTION_TYPE','68','*')
,(1564,'PRESTATAIRE','WORKFLOW_ACTION_TYPE','63','*')
,(1565,'Consultation_DVD_agent','GESTION_DES_SIGNALEMENTS','*','RECHERCHER_SIGNALEMENT')
,(1566,'Consultation_DVD_agent','GESTION_DES_SIGNALEMENTS','*','EXPORTER_SIGNALEMENT')
,(1567,'Consultation_DVD_agent','GESTION_DES_SIGNALEMENTS','*','CONSULTATION_SIGNALEMENT')
,(1569,'Consultation_DVD_CENTRE','GESTION_DES_SIGNALEMENTS','*','RECHERCHER_SIGNALEMENT')
,(1570,'Consultation_DVD_CENTRE','GESTION_DES_SIGNALEMENTS','*','EXPORTER_SIGNALEMENT')
;
INSERT INTO core_admin_role_resource (rbac_id,role_key,resource_type,resource_id,permission) VALUES 
(1571,'Consultation_DVD_CENTRE','GESTION_DES_SIGNALEMENTS','*','CONSULTATION_SIGNALEMENT')
,(1574,'Consultation_DVD_NORD_EST','GESTION_DES_SIGNALEMENTS','*','RECHERCHER_SIGNALEMENT')
,(1575,'Consultation_DVD_NORD_EST','GESTION_DES_SIGNALEMENTS','*','EXPORTER_SIGNALEMENT')
,(1576,'Consultation_DVD_NORD_EST','GESTION_DES_SIGNALEMENTS','*','CONSULTATION_SIGNALEMENT')
,(1578,'Consultation_DVD_NORD_OUEST','GESTION_DES_SIGNALEMENTS','*','RECHERCHER_SIGNALEMENT')
,(1579,'Consultation_DVD_NORD_OUEST','GESTION_DES_SIGNALEMENTS','*','EXPORTER_SIGNALEMENT')
,(1580,'Consultation_DVD_NORD_OUEST','GESTION_DES_SIGNALEMENTS','*','CONSULTATION_SIGNALEMENT')
,(1582,'Consultation_DVD_SUD','GESTION_DES_SIGNALEMENTS','*','RECHERCHER_SIGNALEMENT')
,(1583,'Consultation_DVD_SUD','GESTION_DES_SIGNALEMENTS','*','EXPORTER_SIGNALEMENT')
,(1584,'Consultation_DVD_SUD','GESTION_DES_SIGNALEMENTS','*','CONSULTATION_SIGNALEMENT')
;
INSERT INTO core_admin_role_resource (rbac_id,role_key,resource_type,resource_id,permission) VALUES 
(1586,'Consultation_DVD_SUD_EST','GESTION_DES_SIGNALEMENTS','*','RECHERCHER_SIGNALEMENT')
,(1587,'Consultation_DVD_SUD_EST','GESTION_DES_SIGNALEMENTS','*','EXPORTER_SIGNALEMENT')
,(1588,'Consultation_DVD_SUD_EST','GESTION_DES_SIGNALEMENTS','*','CONSULTATION_SIGNALEMENT')
,(1590,'Consultation_DVD_SUD_OUEST','GESTION_DES_SIGNALEMENTS','*','RECHERCHER_SIGNALEMENT')
,(1591,'Consultation_DVD_SUD_OUEST','GESTION_DES_SIGNALEMENTS','*','EXPORTER_SIGNALEMENT')
,(1592,'Consultation_DVD_SUD_OUEST','GESTION_DES_SIGNALEMENTS','*','CONSULTATION_SIGNALEMENT')
,(1595,'DEVE_Admin_User','GESTION_DES_SIGNALEMENTS','*','*')
,(1596,'DEVE_Admin_User','PROFILES','DEVE_CODIR','*')
,(1597,'DEVE_Admin_User','PROFILES','DEVE_VEGETALISATION','*')
,(1598,'DEVE_Admin_User','PROFILES_VIEWS','*','*')
;
INSERT INTO core_admin_role_resource (rbac_id,role_key,resource_type,resource_id,permission) VALUES 
(1599,'DEVE_Admin_User','SIGNALEMENT_ARRONDISSEMENT','*','*')
,(1600,'DEVE_Admin_User','TYPE_SIGNALEMENT','*','*')
,(1601,'DEVE_Admin_User','WORKFLOW_STATE_TYPE','*','*')
,(1602,'DEVE_Admin_User','WORKFLOW_ACTION_TYPE','*','*')
,(1603,'DEVE_Admin_User','PROFILES','DEVE_Admin_User','*')
,(1604,'DEVE_Admin_User','GESTION_SUIVI_DES_MESSAGES_DANSMARUE','*','*')
,(1615,'DPE_Admin_User','PROFILES','Consultation_anomalies_DPE','*')
,(1616,'DPE_Admin_User','PROFILES','DPE_Admin_User','*')
,(1617,'DPE_Admin_User','GESTION_SUIVI_DES_MESSAGES_DANSMARUE','*','*')
,(1618,'DPE_Admin_User','WORKFLOW_ACTION_TYPE','*','*')
;
INSERT INTO core_admin_role_resource (rbac_id,role_key,resource_type,resource_id,permission) VALUES 
(1619,'DPE_Admin_User','WORKFLOW_STATE_TYPE','*','*')
,(1620,'DPE_Admin_User','SIGNALEMENT_ARRONDISSEMENT','*','*')
,(1621,'DPE_Admin_User','TYPE_SIGNALEMENT','*','*')
,(1622,'DPE_Admin_User','SIGNALEMENT_DOMAINE_FONCTIONNEL','*','*')
,(1623,'Consultation_DVD_NORD_OUEST','WORKFLOW_STATE_TYPE','*','*')
,(1632,'PRESTATAIRE','WORKFLOW_ACTION_TYPE','79','*')
,(1636,'PRESTATAIRE','WORKFLOW_ACTION_TYPE','78','*')
;

--
-- Dumping data for table core_admin_user_field
--
DELETE FROM core_admin_user_field;
INSERT INTO core_admin_user_field (id_user_field,id_user,id_attribute,id_field,id_file,user_field_value) VALUES 
(3,4,1,10,NULL,'DEVE_CODIR')
,(8,11,1,2,NULL,'DPE_CODIR')
,(44,46,1,10,NULL,'DEVE_CODIR')
,(45,49,1,10,NULL,'DEVE_CODIR')
,(46,48,1,10,NULL,'DEVE_CODIR')
,(47,45,1,10,NULL,'DEVE_CODIR')
,(48,47,1,10,NULL,'DEVE_CODIR')
,(49,50,1,10,NULL,'DEVE_CODIR')
,(50,51,1,10,NULL,'DEVE_CODIR')
,(51,58,1,10,NULL,'DEVE_CODIR')
;
INSERT INTO core_admin_user_field (id_user_field,id_user,id_attribute,id_field,id_file,user_field_value) VALUES 
(52,52,1,10,NULL,'DEVE_CODIR')
,(56,54,1,10,NULL,'DEVE_CODIR')
,(58,55,1,10,NULL,'DEVE_CODIR')
,(59,56,1,10,NULL,'DEVE_CODIR')
,(84,181,1,2,NULL,'DPE_CODIR')
,(86,150,1,3,NULL,'DPE_STANDARD')
,(89,153,1,2,NULL,'DPE_CODIR')
,(90,154,1,2,NULL,'DPE_CODIR')
,(92,157,1,2,NULL,'DPE_CODIR')
,(3353,1175,1,11,NULL,'ADMIN')
;
INSERT INTO core_admin_user_field (id_user_field,id_user,id_attribute,id_field,id_file,user_field_value) VALUES 
(3354,1450,1,30,NULL,'DAJ_Gestionnaire')
,(103,85,1,3,NULL,'DPE_STANDARD')
,(105,87,1,3,NULL,'DPE_STANDARD')
,(108,90,1,3,NULL,'DPE_STANDARD')
,(111,93,1,3,NULL,'DPE_STANDARD')
,(116,105,1,3,NULL,'DPE_STANDARD')
,(135,179,1,3,NULL,'DPE_STANDARD')
,(145,143,1,5,NULL,'DPE_CELLULETECHNIQUE')
,(149,187,1,5,NULL,'DPE_CELLULETECHNIQUE')
,(151,189,1,5,NULL,'DPE_CELLULETECHNIQUE')
;
INSERT INTO core_admin_user_field (id_user_field,id_user,id_attribute,id_field,id_file,user_field_value) VALUES 
(153,191,1,5,NULL,'DPE_CELLULETECHNIQUE')
,(3355,867,1,2,NULL,'DPE_CODIR')
,(263,275,1,10,NULL,'DEVE_CODIR')
,(303,344,1,10,NULL,'DEVE_CODIR')
,(304,345,1,10,NULL,'DEVE_CODIR')
,(355,397,1,10,NULL,'DEVE_CODIR')
,(356,396,1,10,NULL,'DEVE_CODIR')
,(358,311,1,10,NULL,'DEVE_CODIR')
,(359,309,1,10,NULL,'DEVE_CODIR')
,(360,310,1,10,NULL,'DEVE_CODIR')
;
INSERT INTO core_admin_user_field (id_user_field,id_user,id_attribute,id_field,id_file,user_field_value) VALUES 
(362,314,1,10,NULL,'DEVE_CODIR')
,(363,312,1,10,NULL,'DEVE_CODIR')
,(364,297,1,10,NULL,'DEVE_CODIR')
,(365,298,1,10,NULL,'DEVE_CODIR')
,(366,299,1,10,NULL,'DEVE_CODIR')
,(367,300,1,10,NULL,'DEVE_CODIR')
,(368,301,1,10,NULL,'DEVE_CODIR')
,(369,302,1,10,NULL,'DEVE_CODIR')
,(370,304,1,10,NULL,'DEVE_CODIR')
,(371,303,1,10,NULL,'DEVE_CODIR')
;
INSERT INTO core_admin_user_field (id_user_field,id_user,id_attribute,id_field,id_file,user_field_value) VALUES 
(372,306,1,10,NULL,'DEVE_CODIR')
,(373,308,1,10,NULL,'DEVE_CODIR')
,(374,305,1,10,NULL,'DEVE_CODIR')
,(375,307,1,10,NULL,'DEVE_CODIR')
,(407,401,1,5,NULL,'DPE_CELLULETECHNIQUE')
,(408,402,1,5,NULL,'DPE_CELLULETECHNIQUE')
,(412,406,1,5,NULL,'DPE_CELLULETECHNIQUE')
,(142,135,1,5,NULL,'DPE_CELLULETECHNIQUE')
,(143,137,1,5,NULL,'DPE_CELLULETECHNIQUE')
,(144,139,1,5,NULL,'DPE_CELLULETECHNIQUE')
;
INSERT INTO core_admin_user_field (id_user_field,id_user,id_attribute,id_field,id_file,user_field_value) VALUES 
(146,145,1,5,NULL,'DPE_CELLULETECHNIQUE')
,(154,192,1,5,NULL,'DPE_CELLULETECHNIQUE')
,(156,194,1,5,NULL,'DPE_CELLULETECHNIQUE')
,(648,196,1,5,NULL,'DPE_CELLULETECHNIQUE')
,(160,198,1,5,NULL,'DPE_CELLULETECHNIQUE')
,(161,199,1,5,NULL,'DPE_CELLULETECHNIQUE')
,(164,204,1,5,NULL,'DPE_CELLULETECHNIQUE')
,(3356,1470,1,3,NULL,'DPE_STANDARD')
,(179,159,1,5,NULL,'DPE_CELLULETECHNIQUE')
,(182,162,1,5,NULL,'DPE_CELLULETECHNIQUE')
;
INSERT INTO core_admin_user_field (id_user_field,id_user,id_attribute,id_field,id_file,user_field_value) VALUES 
(183,163,1,5,NULL,'DPE_CELLULETECHNIQUE')
,(184,164,1,5,NULL,'DPE_CELLULETECHNIQUE')
,(185,165,1,5,NULL,'DPE_CELLULETECHNIQUE')
,(652,104,1,5,NULL,'DPE_CELLULETECHNIQUE')
,(188,168,1,5,NULL,'DPE_CELLULETECHNIQUE')
,(189,169,1,5,NULL,'DPE_CELLULETECHNIQUE')
,(653,102,1,5,NULL,'DPE_CELLULETECHNIQUE')
,(657,99,1,5,NULL,'DPE_CELLULETECHNIQUE')
,(264,277,1,10,NULL,'DEVE_CODIR')
,(265,276,1,10,NULL,'DEVE_CODIR')
;
INSERT INTO core_admin_user_field (id_user_field,id_user,id_attribute,id_field,id_file,user_field_value) VALUES 
(266,274,1,10,NULL,'DEVE_CODIR')
,(346,389,1,10,NULL,'DEVE_CODIR')
,(348,388,1,10,NULL,'DEVE_CODIR')
,(349,390,1,10,NULL,'DEVE_CODIR')
,(353,395,1,10,NULL,'DEVE_CODIR')
,(415,409,1,5,NULL,'DPE_CELLULETECHNIQUE')
,(418,412,1,5,NULL,'DPE_CELLULETECHNIQUE')
,(437,243,1,5,NULL,'DPE_CELLULETECHNIQUE')
,(438,244,1,5,NULL,'DPE_CELLULETECHNIQUE')
,(440,246,1,5,NULL,'DPE_CELLULETECHNIQUE')
;
INSERT INTO core_admin_user_field (id_user_field,id_user,id_attribute,id_field,id_file,user_field_value) VALUES 
(443,249,1,5,NULL,'DPE_CELLULETECHNIQUE')
,(446,252,1,5,NULL,'DPE_CELLULETECHNIQUE')
,(448,254,1,5,NULL,'DPE_CELLULETECHNIQUE')
,(457,263,1,3,NULL,'DPE_STANDARD')
,(460,266,1,2,NULL,'DPE_CODIR')
,(465,220,1,5,NULL,'DPE_CELLULETECHNIQUE')
,(466,221,1,5,NULL,'DPE_CELLULETECHNIQUE')
,(468,223,1,5,NULL,'DPE_CELLULETECHNIQUE')
,(469,225,1,5,NULL,'DPE_CELLULETECHNIQUE')
,(470,226,1,5,NULL,'DPE_CELLULETECHNIQUE')
;
INSERT INTO core_admin_user_field (id_user_field,id_user,id_attribute,id_field,id_file,user_field_value) VALUES 
(471,227,1,5,NULL,'DPE_CELLULETECHNIQUE')
,(472,228,1,5,NULL,'DPE_CELLULETECHNIQUE')
,(474,230,1,5,NULL,'DPE_CELLULETECHNIQUE')
,(482,236,1,2,NULL,'DPE_CODIR')
,(486,240,1,3,NULL,'DPE_STANDARD')
,(490,219,1,5,NULL,'DPE_CELLULETECHNIQUE')
,(501,279,1,10,NULL,'DEVE_CODIR')
,(502,280,1,10,NULL,'DEVE_CODIR')
,(512,521,1,5,NULL,'DPE_CELLULETECHNIQUE')
,(513,519,1,5,NULL,'DPE_CELLULETECHNIQUE')
;
INSERT INTO core_admin_user_field (id_user_field,id_user,id_attribute,id_field,id_file,user_field_value) VALUES 
(516,523,1,5,NULL,'DPE_CELLULETECHNIQUE')
,(517,518,1,5,NULL,'DPE_CELLULETECHNIQUE')
,(521,512,1,5,NULL,'DPE_CELLULETECHNIQUE')
,(524,524,1,5,NULL,'DPE_CELLULETECHNIQUE')
,(525,525,1,5,NULL,'DPE_CELLULETECHNIQUE')
,(527,520,1,5,NULL,'DPE_CELLULETECHNIQUE')
,(528,511,1,5,NULL,'DPE_CELLULETECHNIQUE')
,(540,491,1,5,NULL,'DPE_CELLULETECHNIQUE')
,(543,490,1,5,NULL,'DPE_CELLULETECHNIQUE')
,(659,313,1,10,NULL,'DEVE_CODIR')
;
INSERT INTO core_admin_user_field (id_user_field,id_user,id_attribute,id_field,id_file,user_field_value) VALUES 
(561,468,1,5,NULL,'DPE_CELLULETECHNIQUE')
,(562,466,1,5,NULL,'DPE_CELLULETECHNIQUE')
,(567,480,1,3,NULL,'DPE_STANDARD')
,(568,483,1,2,NULL,'DPE_CODIR')
,(570,482,1,2,NULL,'DPE_CODIR')
,(572,486,1,3,NULL,'DPE_STANDARD')
,(574,446,1,5,NULL,'DPE_CELLULETECHNIQUE')
,(578,449,1,5,NULL,'DPE_CELLULETECHNIQUE')
,(579,447,1,5,NULL,'DPE_CELLULETECHNIQUE')
,(581,452,1,5,NULL,'DPE_CELLULETECHNIQUE')
;
INSERT INTO core_admin_user_field (id_user_field,id_user,id_attribute,id_field,id_file,user_field_value) VALUES 
(582,448,1,5,NULL,'DPE_CELLULETECHNIQUE')
,(587,459,1,3,NULL,'DPE_STANDARD')
,(590,462,1,3,NULL,'DPE_STANDARD')
,(591,463,1,3,NULL,'DPE_STANDARD')
,(593,444,1,3,NULL,'DPE_STANDARD')
,(607,504,1,2,NULL,'DPE_CODIR')
,(611,484,1,3,NULL,'DPE_STANDARD')
,(614,467,1,2,NULL,'DPE_CODIR')
,(620,536,1,10,NULL,'DEVE_CODIR')
,(621,537,1,10,NULL,'DEVE_CODIR')
;
INSERT INTO core_admin_user_field (id_user_field,id_user,id_attribute,id_field,id_file,user_field_value) VALUES 
(624,540,1,3,NULL,'DPE_STANDARD')
,(641,503,1,2,NULL,'DPE_CODIR')
,(667,556,1,3,NULL,'DPE_STANDARD')
,(896,241,1,3,NULL,'DPE_STANDARD')
,(901,645,1,5,NULL,'DPE_CELLULETECHNIQUE')
,(920,649,1,11,NULL,'ADMIN')
,(983,695,1,3,NULL,'DPE_STANDARD')
,(1014,426,1,3,NULL,'DPE_STANDARD')
,(1055,766,1,5,NULL,'DPE_CELLULETECHNIQUE')
,(1063,211,1,2,NULL,'DPE_CODIR')
;
INSERT INTO core_admin_user_field (id_user_field,id_user,id_attribute,id_field,id_file,user_field_value) VALUES 
(1087,549,1,3,NULL,'DPE_STANDARD')
,(1089,550,1,3,NULL,'DPE_STANDARD')
,(1106,551,1,3,NULL,'DPE_STANDARD')
,(1124,151,1,3,NULL,'DPE_STANDARD')
,(1134,201,1,5,NULL,'DPE_CELLULETECHNIQUE')
,(1147,173,1,3,NULL,'DPE_STANDARD')
,(1148,175,1,3,NULL,'DPE_STANDARD')
,(1149,826,1,10,NULL,'DEVE_CODIR')
,(1153,83,1,2,NULL,'DPE_CODIR')
,(1161,86,1,3,NULL,'DPE_STANDARD')
;
INSERT INTO core_admin_user_field (id_user_field,id_user,id_attribute,id_field,id_file,user_field_value) VALUES 
(1162,669,1,3,NULL,'DPE_STANDARD')
,(1169,103,1,5,NULL,'DPE_CELLULETECHNIQUE')
,(1171,92,1,5,NULL,'DPE_CELLULETECHNIQUE')
,(1173,82,1,2,NULL,'DPE_CODIR')
,(1181,97,1,3,NULL,'DPE_STANDARD')
,(1182,95,1,3,NULL,'DPE_STANDARD')
,(1185,831,1,3,NULL,'DPE_STANDARD')
,(1211,108,1,2,NULL,'DPE_CODIR')
,(1212,183,1,2,NULL,'DPE_CODIR')
,(1216,116,1,5,NULL,'DPE_CELLULETECHNIQUE')
;
INSERT INTO core_admin_user_field (id_user_field,id_user,id_attribute,id_field,id_file,user_field_value) VALUES 
(1221,124,1,5,NULL,'DPE_CELLULETECHNIQUE')
,(1223,125,1,5,NULL,'DPE_CELLULETECHNIQUE')
,(1228,115,1,3,NULL,'DPE_STANDARD')
,(1234,106,1,2,NULL,'DPE_CODIR')
,(1235,127,1,5,NULL,'DPE_CELLULETECHNIQUE')
,(1236,120,1,5,NULL,'DPE_CELLULETECHNIQUE')
,(1256,37,1,5,NULL,'DPE_CELLULETECHNIQUE')
,(1263,24,1,2,NULL,'DPE_CODIR')
,(1265,28,1,3,NULL,'DPE_STANDARD')
,(1300,126,1,5,NULL,'DPE_CELLULETECHNIQUE')
;
INSERT INTO core_admin_user_field (id_user_field,id_user,id_attribute,id_field,id_file,user_field_value) VALUES 
(1314,413,1,3,NULL,'DPE_STANDARD')
,(1316,416,1,3,NULL,'DPE_STANDARD')
,(1321,849,1,10,NULL,'DEVE_CODIR')
,(1322,851,1,5,NULL,'DPE_CELLULETECHNIQUE')
,(1325,853,1,13,NULL,'DPE_FONCTIONNELLE')
,(1326,854,1,13,NULL,'DPE_FONCTIONNELLE')
,(1328,855,1,13,NULL,'DPE_FONCTIONNELLE')
,(1331,856,1,5,NULL,'DPE_CELLULETECHNIQUE')
,(1351,445,1,3,NULL,'DPE_STANDARD')
,(1369,253,1,3,NULL,'DPE_STANDARD')
;
INSERT INTO core_admin_user_field (id_user_field,id_user,id_attribute,id_field,id_file,user_field_value) VALUES 
(1379,506,1,2,NULL,'DPE_CODIR')
,(1382,877,1,7,NULL,'DPE_OBSERVATEUR')
,(1384,686,1,3,NULL,'DPE_STANDARD')
,(1385,508,1,3,NULL,'DPE_STANDARD')
,(1386,31,1,3,NULL,'DPE_STANDARD')
,(1387,30,1,3,NULL,'DPE_STANDARD')
,(1396,850,1,10,NULL,'DEVE_CODIR')
,(3128,1039,1,22,NULL,'DVD_Consultation_Agent')
,(1403,824,1,7,NULL,'DPE_OBSERVATEUR')
,(1407,852,1,13,NULL,'DPE_FONCTIONNELLE')
;
INSERT INTO core_admin_user_field (id_user_field,id_user,id_attribute,id_field,id_file,user_field_value) VALUES 
(1408,871,1,3,NULL,'DPE_STANDARD')
,(1411,889,1,5,NULL,'DPE_CELLULETECHNIQUE')
,(1465,932,1,7,NULL,'DPE_OBSERVATEUR')
,(1471,934,1,10,NULL,'DEVE_CODIR')
,(1474,415,1,5,NULL,'DPE_CELLULETECHNIQUE')
,(1483,10,1,2,NULL,'DPE_CODIR')
,(1514,879,1,5,NULL,'DPE_CELLULETECHNIQUE')
,(1515,432,1,3,NULL,'DPE_STANDARD')
,(1517,949,1,3,NULL,'DPE_STANDARD')
,(1539,953,1,5,NULL,'DPE_CELLULETECHNIQUE')
;
INSERT INTO core_admin_user_field (id_user_field,id_user,id_attribute,id_field,id_file,user_field_value) VALUES 
(1540,160,1,5,NULL,'DPE_CELLULETECHNIQUE')
,(1542,510,1,5,NULL,'DPE_CELLULETECHNIQUE')
,(1544,907,1,5,NULL,'DPE_CELLULETECHNIQUE')
,(1548,956,1,3,NULL,'DPE_STANDARD')
,(1553,676,1,5,NULL,'DPE_CELLULETECHNIQUE')
,(1560,229,1,5,NULL,'DPE_CELLULETECHNIQUE')
,(1564,958,1,5,NULL,'DPE_CELLULETECHNIQUE')
,(1591,964,1,12,NULL,'OBSERVATEUR_MAIRIES')
,(1580,960,1,12,NULL,'OBSERVATEUR_MAIRIES')
,(1581,959,1,12,NULL,'OBSERVATEUR_MAIRIES')
;
INSERT INTO core_admin_user_field (id_user_field,id_user,id_attribute,id_field,id_file,user_field_value) VALUES 
(1583,963,1,12,NULL,'OBSERVATEUR_MAIRIES')
,(1578,976,1,12,NULL,'OBSERVATEUR_MAIRIES')
,(1574,977,1,12,NULL,'OBSERVATEUR_MAIRIES')
,(1582,971,1,12,NULL,'OBSERVATEUR_MAIRIES')
,(1588,967,1,12,NULL,'OBSERVATEUR_MAIRIES')
,(1589,966,1,12,NULL,'OBSERVATEUR_MAIRIES')
,(1590,978,1,12,NULL,'OBSERVATEUR_MAIRIES')
,(1592,965,1,12,NULL,'OBSERVATEUR_MAIRIES')
,(1593,968,1,12,NULL,'OBSERVATEUR_MAIRIES')
,(1573,961,1,12,NULL,'OBSERVATEUR_MAIRIES')
;
INSERT INTO core_admin_user_field (id_user_field,id_user,id_attribute,id_field,id_file,user_field_value) VALUES 
(1579,974,1,12,NULL,'OBSERVATEUR_MAIRIES')
,(1584,973,1,12,NULL,'OBSERVATEUR_MAIRIES')
,(1594,979,1,12,NULL,'OBSERVATEUR_MAIRIES')
,(1597,535,1,5,NULL,'DPE_CELLULETECHNIQUE')
,(1598,980,1,12,NULL,'OBSERVATEUR_MAIRIES')
,(1599,962,1,12,NULL,'OBSERVATEUR_MAIRIES')
,(1600,981,1,12,NULL,'OBSERVATEUR_MAIRIES')
,(1610,985,1,10,NULL,'DEVE_CODIR')
,(1611,986,1,10,NULL,'DEVE_CODIR')
,(1612,987,1,10,NULL,'DEVE_CODIR')
;
INSERT INTO core_admin_user_field (id_user_field,id_user,id_attribute,id_field,id_file,user_field_value) VALUES 
(1614,990,1,10,NULL,'DEVE_CODIR')
,(1615,989,1,10,NULL,'DEVE_CODIR')
,(1617,992,1,10,NULL,'DEVE_CODIR')
,(1618,991,1,10,NULL,'DEVE_CODIR')
,(1619,993,1,10,NULL,'DEVE_CODIR')
,(1620,995,1,10,NULL,'DEVE_CODIR')
,(1621,994,1,10,NULL,'DEVE_CODIR')
,(1623,997,1,10,NULL,'DEVE_CODIR')
,(1624,998,1,10,NULL,'DEVE_CODIR')
,(1625,999,1,10,NULL,'DEVE_CODIR')
;
INSERT INTO core_admin_user_field (id_user_field,id_user,id_attribute,id_field,id_file,user_field_value) VALUES 
(1626,25,1,2,NULL,'DPE_CODIR')
,(1627,1000,1,12,NULL,'OBSERVATEUR_MAIRIES')
,(1628,267,1,3,NULL,'DPE_STANDARD')
,(1631,327,1,10,NULL,'DEVE_CODIR')
,(1633,393,1,10,NULL,'DEVE_CODIR')
,(1635,156,1,2,NULL,'DPE_CODIR')
,(1636,461,1,2,NULL,'DPE_CODIR')
,(1644,1003,1,12,NULL,'OBSERVATEUR_MAIRIES')
,(1648,970,1,12,NULL,'OBSERVATEUR_MAIRIES')
,(1649,969,1,12,NULL,'OBSERVATEUR_MAIRIES')
;
INSERT INTO core_admin_user_field (id_user_field,id_user,id_attribute,id_field,id_file,user_field_value) VALUES 
(1653,1010,1,10,NULL,'DEVE_CODIR')
,(1712,430,1,3,NULL,'DPE_STANDARD')
,(1732,1041,1,3,NULL,'DPE_STANDARD')
,(1734,1043,1,3,NULL,'DPE_STANDARD')
,(1736,27,1,3,NULL,'DPE_STANDARD')
,(1746,834,1,11,NULL,'ADMIN')
,(1749,1051,1,7,NULL,'DPE_OBSERVATEUR')
,(1758,411,1,3,NULL,'DPE_STANDARD')
,(1759,1055,1,2,NULL,'DPE_CODIR')
,(1760,407,1,5,NULL,'DPE_CELLULETECHNIQUE')
;
INSERT INTO core_admin_user_field (id_user_field,id_user,id_attribute,id_field,id_file,user_field_value) VALUES 
(1762,876,1,7,NULL,'DPE_OBSERVATEUR')
,(1769,1057,1,8,NULL,'DVD_CODIR')
,(1777,1058,1,8,NULL,'DVD_CODIR')
,(1780,1036,1,10,NULL,'DEVE_CODIR')
,(1813,1061,1,8,NULL,'DVD_CODIR')
,(1816,1063,1,8,NULL,'DVD_CODIR')
,(1862,1069,1,8,NULL,'DVD_CODIR')
,(1863,1070,1,8,NULL,'DVD_CODIR')
,(1926,1082,1,7,NULL,'DPE_OBSERVATEUR')
,(1929,429,1,3,NULL,'DPE_STANDARD')
;
INSERT INTO core_admin_user_field (id_user_field,id_user,id_attribute,id_field,id_file,user_field_value) VALUES 
(1961,176,1,5,NULL,'DPE_CELLULETECHNIQUE')
,(1969,1089,1,7,NULL,'DPE_OBSERVATEUR')
,(1970,1086,1,7,NULL,'DPE_OBSERVATEUR')
,(1973,1087,1,7,NULL,'DPE_OBSERVATEUR')
,(1975,1090,1,7,NULL,'DPE_OBSERVATEUR')
,(1976,1091,1,14,NULL,'DEVE_VEGETALISATION')
,(1979,1094,1,15,NULL,'Admin_users_metiers')
,(1980,1095,1,15,NULL,'Admin_users_metiers')
,(1981,1092,1,15,NULL,'Admin_users_metiers')
,(1982,1096,1,10,NULL,'DEVE_CODIR')
;
INSERT INTO core_admin_user_field (id_user_field,id_user,id_attribute,id_field,id_file,user_field_value) VALUES 
(1984,494,1,5,NULL,'DPE_CELLULETECHNIQUE')
,(1985,488,1,5,NULL,'DPE_CELLULETECHNIQUE')
,(1986,507,1,3,NULL,'DPE_STANDARD')
,(1988,548,1,3,NULL,'DPE_STANDARD')
,(3129,1063,1,22,NULL,'DVD_Consultation_Agent')
,(1996,328,1,10,NULL,'DEVE_CODIR')
,(1999,1105,1,3,NULL,'DPE_STANDARD')
,(2001,1107,1,5,NULL,'DPE_CELLULETECHNIQUE')
,(2005,1004,1,11,NULL,'ADMIN')
,(2006,1049,1,11,NULL,'ADMIN')
;
INSERT INTO core_admin_user_field (id_user_field,id_user,id_attribute,id_field,id_file,user_field_value) VALUES 
(2016,408,1,3,NULL,'DPE_STANDARD')
,(2017,400,1,3,NULL,'DPE_STANDARD')
,(2023,325,1,10,NULL,'DEVE_CODIR')
,(2024,996,1,10,NULL,'DEVE_CODIR')
,(2031,988,1,10,NULL,'DEVE_CODIR')
,(2042,1115,1,10,NULL,'DEVE_CODIR')
,(2043,278,1,10,NULL,'DEVE_CODIR')
,(2044,237,1,10,NULL,'DEVE_CODIR')
,(2047,1103,1,10,NULL,'DEVE_CODIR')
,(2048,1117,1,10,NULL,'DEVE_CODIR')
;
INSERT INTO core_admin_user_field (id_user_field,id_user,id_attribute,id_field,id_file,user_field_value) VALUES 
(2049,1118,1,14,NULL,'DEVE_VEGETALISATION')
,(2050,391,1,10,NULL,'DEVE_CODIR')
,(2051,1119,1,10,NULL,'DEVE_CODIR')
,(2052,1120,1,8,NULL,'DVD_CODIR')
,(2126,1128,1,10,NULL,'DEVE_CODIR')
,(2127,1129,1,10,NULL,'DEVE_CODIR')
,(2128,1130,1,3,NULL,'DPE_STANDARD')
,(2134,1131,1,3,NULL,'DPE_STANDARD')
,(2145,1141,1,7,NULL,'DPE_OBSERVATEUR')
,(2158,1151,1,2,NULL,'DPE_CODIR')
;
INSERT INTO core_admin_user_field (id_user_field,id_user,id_attribute,id_field,id_file,user_field_value) VALUES 
(2189,1169,1,2,NULL,'DPE_CODIR')
,(2200,1176,1,10,NULL,'DEVE_CODIR')
,(2204,235,1,3,NULL,'DPE_STANDARD')
,(2208,1093,1,15,NULL,'Admin_users_metiers')
,(2209,1179,1,10,NULL,'DEVE_CODIR')
,(2232,1193,1,3,NULL,'DPE_STANDARD')
,(2234,91,1,5,NULL,'DPE_CELLULETECHNIQUE')
,(2275,1156,1,3,NULL,'DPE_STANDARD')
,(2276,268,1,3,NULL,'DPE_STANDARD')
,(2280,1202,1,3,NULL,'DPE_STANDARD')
;
INSERT INTO core_admin_user_field (id_user_field,id_user,id_attribute,id_field,id_file,user_field_value) VALUES 
(2281,1215,1,7,NULL,'DPE_OBSERVATEUR')
,(2295,1219,1,7,NULL,'DPE_OBSERVATEUR')
,(2296,1085,1,7,NULL,'DPE_OBSERVATEUR')
,(2299,1005,1,2,NULL,'DPE_CODIR')
,(2305,155,1,2,NULL,'DPE_CODIR')
,(2309,1143,1,12,NULL,'OBSERVATEUR_MAIRIES')
,(2317,955,1,3,NULL,'DPE_STANDARD')
,(2318,1084,1,3,NULL,'DPE_STANDARD')
,(2319,522,1,3,NULL,'DPE_STANDARD')
,(2320,1139,1,7,NULL,'DPE_OBSERVATEUR')
;
INSERT INTO core_admin_user_field (id_user_field,id_user,id_attribute,id_field,id_file,user_field_value) VALUES 
(2326,984,1,2,NULL,'DPE_CODIR')
,(2346,1232,1,11,NULL,'ADMIN')
,(2347,423,1,8,NULL,'DVD_CODIR')
,(2348,1233,1,8,NULL,'DVD_CODIR')
,(2355,532,1,3,NULL,'DPE_STANDARD')
,(2356,680,1,5,NULL,'DPE_CELLULETECHNIQUE')
,(2357,683,1,5,NULL,'DPE_CELLULETECHNIQUE')
,(2367,239,1,3,NULL,'DPE_STANDARD')
,(2369,425,1,2,NULL,'DPE_CODIR')
,(2370,1240,1,8,NULL,'DVD_CODIR')
;
INSERT INTO core_admin_user_field (id_user_field,id_user,id_attribute,id_field,id_file,user_field_value) VALUES 
(2371,1241,1,8,NULL,'DVD_CODIR')
,(2372,514,1,5,NULL,'DPE_CELLULETECHNIQUE')
,(3131,1385,1,24,NULL,'DVD_NORD_EST_Consultation_Agent')
,(2376,1243,1,8,NULL,'DVD_CODIR')
,(2387,860,1,11,NULL,'ADMIN')
,(2395,1253,1,5,NULL,'DPE_CELLULETECHNIQUE')
,(2398,975,1,12,NULL,'OBSERVATEUR_MAIRIES')
,(2412,1261,1,10,NULL,'DEVE_CODIR')
,(2413,1262,1,3,NULL,'DPE_STANDARD')
,(2414,1252,1,3,NULL,'DPE_STANDARD')
;
INSERT INTO core_admin_user_field (id_user_field,id_user,id_attribute,id_field,id_file,user_field_value) VALUES 
(2416,1263,1,5,NULL,'DPE_CELLULETECHNIQUE')
,(2417,915,1,5,NULL,'DPE_CELLULETECHNIQUE')
,(2428,862,1,11,NULL,'ADMIN')
,(2429,1270,1,11,NULL,'ADMIN')
,(2434,1271,1,3,NULL,'DPE_STANDARD')
,(2435,1218,1,2,NULL,'DPE_CODIR')
,(2448,1172,1,16,NULL,'DPE_Graf_all')
,(2460,1148,1,3,NULL,'DPE_STANDARD')
,(2462,1282,1,3,NULL,'DPE_STANDARD')
,(2468,417,1,3,NULL,'DPE_STANDARD')
;
INSERT INTO core_admin_user_field (id_user_field,id_user,id_attribute,id_field,id_file,user_field_value) VALUES 
(3132,282,1,24,NULL,'DVD_NORD_EST_Consultation_Agent')
,(2486,607,1,8,NULL,'DVD_CODIR')
,(3133,621,1,24,NULL,'DVD_NORD_EST_Consultation_Agent')
,(3134,612,1,24,NULL,'DVD_NORD_EST_Consultation_Agent')
,(3136,1421,1,24,NULL,'DVD_NORD_EST_Consultation_Agent')
,(3137,269,1,24,NULL,'DVD_NORD_EST_Consultation_Agent')
,(3139,1217,1,24,NULL,'DVD_NORD_EST_Consultation_Agent')
,(3140,617,1,24,NULL,'DVD_NORD_EST_Consultation_Agent')
,(3141,1123,1,24,NULL,'DVD_NORD_EST_Consultation_Agent')
,(3142,281,1,24,NULL,'DVD_NORD_EST_Consultation_Agent')
;
INSERT INTO core_admin_user_field (id_user_field,id_user,id_attribute,id_field,id_file,user_field_value) VALUES 
(3143,1283,1,24,NULL,'DVD_NORD_EST_Consultation_Agent')
,(3144,1420,1,24,NULL,'DVD_NORD_EST_Consultation_Agent')
,(3145,1378,1,24,NULL,'DVD_NORD_EST_Consultation_Agent')
,(3146,332,1,24,NULL,'DVD_NORD_EST_Consultation_Agent')
,(3147,618,1,24,NULL,'DVD_NORD_EST_Consultation_Agent')
,(3148,1301,1,24,NULL,'DVD_NORD_EST_Consultation_Agent')
,(3149,270,1,24,NULL,'DVD_NORD_EST_Consultation_Agent')
,(3150,1222,1,24,NULL,'DVD_NORD_EST_Consultation_Agent')
,(3152,1008,1,24,NULL,'DVD_NORD_EST_Consultation_Agent')
,(3153,609,1,24,NULL,'DVD_NORD_EST_Consultation_Agent')
;
INSERT INTO core_admin_user_field (id_user_field,id_user,id_attribute,id_field,id_file,user_field_value) VALUES 
(3154,286,1,24,NULL,'DVD_NORD_EST_Consultation_Agent')
,(3155,287,1,24,NULL,'DVD_NORD_EST_Consultation_Agent')
,(3156,1302,1,24,NULL,'DVD_NORD_EST_Consultation_Agent')
,(3157,619,1,24,NULL,'DVD_NORD_EST_Consultation_Agent')
,(3158,611,1,24,NULL,'DVD_NORD_EST_Consultation_Agent')
,(3159,1395,1,25,NULL,'DVD_NORD_OUEST_Consultation_Agent')
,(2541,1060,1,8,NULL,'DVD_CODIR')
,(3160,76,1,25,NULL,'DVD_NORD_OUEST_Consultation_Agent')
,(3161,77,1,25,NULL,'DVD_NORD_OUEST_Consultation_Agent')
,(3162,600,1,25,NULL,'DVD_NORD_OUEST_Consultation_Agent')
;
INSERT INTO core_admin_user_field (id_user_field,id_user,id_attribute,id_field,id_file,user_field_value) VALUES 
(3163,334,1,25,NULL,'DVD_NORD_OUEST_Consultation_Agent')
,(3164,1011,1,25,NULL,'DVD_NORD_OUEST_Consultation_Agent')
,(3165,73,1,25,NULL,'DVD_NORD_OUEST_Consultation_Agent')
,(2552,291,1,8,NULL,'DVD_CODIR')
,(3166,74,1,25,NULL,'DVD_NORD_OUEST_Consultation_Agent')
,(3167,1236,1,25,NULL,'DVD_NORD_OUEST_Consultation_Agent')
,(3168,335,1,25,NULL,'DVD_NORD_OUEST_Consultation_Agent')
,(3169,1278,1,25,NULL,'DVD_NORD_OUEST_Consultation_Agent')
,(3170,336,1,25,NULL,'DVD_NORD_OUEST_Consultation_Agent')
,(3171,1224,1,25,NULL,'DVD_NORD_OUEST_Consultation_Agent')
;
INSERT INTO core_admin_user_field (id_user_field,id_user,id_attribute,id_field,id_file,user_field_value) VALUES 
(3172,1394,1,25,NULL,'DVD_NORD_OUEST_Consultation_Agent')
,(3173,383,1,25,NULL,'DVD_NORD_OUEST_Consultation_Agent')
,(3174,597,1,25,NULL,'DVD_NORD_OUEST_Consultation_Agent')
,(3176,1080,1,25,NULL,'DVD_NORD_OUEST_Consultation_Agent')
,(3177,384,1,25,NULL,'DVD_NORD_OUEST_Consultation_Agent')
,(3178,1259,1,25,NULL,'DVD_NORD_OUEST_Consultation_Agent')
,(3179,1198,1,25,NULL,'DVD_NORD_OUEST_Consultation_Agent')
,(3180,1275,1,25,NULL,'DVD_NORD_OUEST_Consultation_Agent')
,(3181,385,1,25,NULL,'DVD_NORD_OUEST_Consultation_Agent')
,(3182,640,1,25,NULL,'DVD_NORD_OUEST_Consultation_Agent')
;
INSERT INTO core_admin_user_field (id_user_field,id_user,id_attribute,id_field,id_file,user_field_value) VALUES 
(2582,1024,1,8,NULL,'DVD_CODIR')
,(3256,1408,1,19,NULL,'PRESTATAIRE')
,(2621,71,1,8,NULL,'DVD_CODIR')
,(2576,1018,1,8,NULL,'DVD_CODIR')
,(2590,1127,1,8,NULL,'DVD_CODIR')
,(2602,331,1,8,NULL,'DVD_CODIR')
,(2632,599,1,8,NULL,'DVD_CODIR')
,(2648,1053,1,8,NULL,'DVD_CODIR')
,(2650,1075,1,8,NULL,'DVD_CODIR')
,(2653,1122,1,8,NULL,'DVD_CODIR')
;
INSERT INTO core_admin_user_field (id_user_field,id_user,id_attribute,id_field,id_file,user_field_value) VALUES 
(2661,1029,1,8,NULL,'DVD_CODIR')
,(2672,1267,1,11,NULL,'ADMIN')
,(2688,1304,1,3,NULL,'DPE_STANDARD')
,(2689,414,1,3,NULL,'DPE_STANDARD')
,(2690,410,1,3,NULL,'DPE_STANDARD')
,(2691,1256,1,3,NULL,'DPE_STANDARD')
,(2693,1113,1,5,NULL,'DPE_CELLULETECHNIQUE')
,(2696,152,1,5,NULL,'DPE_CELLULETECHNIQUE')
,(2698,259,1,2,NULL,'DPE_CODIR')
,(2701,213,1,7,NULL,'DPE_OBSERVATEUR')
;
INSERT INTO core_admin_user_field (id_user_field,id_user,id_attribute,id_field,id_file,user_field_value) VALUES 
(2703,1186,1,2,NULL,'DPE_CODIR')
,(2707,1308,1,5,NULL,'DPE_CELLULETECHNIQUE')
,(2708,1309,1,5,NULL,'DPE_CELLULETECHNIQUE')
,(2709,1310,1,5,NULL,'DPE_CELLULETECHNIQUE')
,(2710,130,1,7,NULL,'DPE_OBSERVATEUR')
,(2712,180,1,2,NULL,'DPE_CODIR')
,(2713,1311,1,5,NULL,'DPE_CELLULETECHNIQUE')
,(2715,1239,1,2,NULL,'DPE_CODIR')
,(2724,1320,1,5,NULL,'DPE_CELLULETECHNIQUE')
,(2725,1321,1,5,NULL,'DPE_CELLULETECHNIQUE')
;
INSERT INTO core_admin_user_field (id_user_field,id_user,id_attribute,id_field,id_file,user_field_value) VALUES 
(2727,1323,1,2,NULL,'DPE_CODIR')
,(2728,1324,1,5,NULL,'DPE_CELLULETECHNIQUE')
,(2730,1325,1,5,NULL,'DPE_CELLULETECHNIQUE')
,(2731,1326,1,5,NULL,'DPE_CELLULETECHNIQUE')
,(2732,1327,1,5,NULL,'DPE_CELLULETECHNIQUE')
,(2733,1328,1,5,NULL,'DPE_CELLULETECHNIQUE')
,(2735,1330,1,5,NULL,'DPE_CELLULETECHNIQUE')
,(2736,1088,1,5,NULL,'DPE_CELLULETECHNIQUE')
,(2737,1334,1,5,NULL,'DPE_CELLULETECHNIQUE')
,(2739,1332,1,5,NULL,'DPE_CELLULETECHNIQUE')
;
INSERT INTO core_admin_user_field (id_user_field,id_user,id_attribute,id_field,id_file,user_field_value) VALUES 
(2740,1333,1,5,NULL,'DPE_CELLULETECHNIQUE')
,(2741,1335,1,5,NULL,'DPE_CELLULETECHNIQUE')
,(2742,1336,1,2,NULL,'DPE_CODIR')
,(2769,1357,1,5,NULL,'DPE_CELLULETECHNIQUE')
,(2768,1355,1,5,NULL,'DPE_CELLULETECHNIQUE')
,(2764,1353,1,5,NULL,'DPE_CELLULETECHNIQUE')
,(2766,1221,1,2,NULL,'DPE_CODIR')
,(2775,1361,1,2,NULL,'DPE_CODIR')
,(2776,1362,1,2,NULL,'DPE_CODIR')
,(2779,1364,1,5,NULL,'DPE_CELLULETECHNIQUE')
;
INSERT INTO core_admin_user_field (id_user_field,id_user,id_attribute,id_field,id_file,user_field_value) VALUES 
(2780,1365,1,5,NULL,'DPE_CELLULETECHNIQUE')
,(2781,250,1,5,NULL,'DPE_CELLULETECHNIQUE')
,(2790,1366,1,7,NULL,'DPE_OBSERVATEUR')
,(2791,205,1,7,NULL,'DPE_OBSERVATEUR')
,(2799,1372,1,5,NULL,'DPE_CELLULETECHNIQUE')
,(2800,1312,1,5,NULL,'DPE_CELLULETECHNIQUE')
,(2802,1374,1,5,NULL,'DPE_CELLULETECHNIQUE')
,(2803,1375,1,5,NULL,'DPE_CELLULETECHNIQUE')
,(3183,595,1,25,NULL,'DVD_NORD_OUEST_Consultation_Agent')
,(3184,1195,1,26,NULL,'DVD_SUD_Consultation_Agent')
;
INSERT INTO core_admin_user_field (id_user_field,id_user,id_attribute,id_field,id_file,user_field_value) VALUES 
(2823,1048,1,8,NULL,'DVD_CODIR')
,(3185,579,1,26,NULL,'DVD_SUD_Consultation_Agent')
,(2832,13,1,3,NULL,'DPE_STANDARD')
,(2834,1382,1,8,NULL,'DVD_CODIR')
,(2835,1384,1,8,NULL,'DVD_CODIR')
,(3186,568,1,26,NULL,'DVD_SUD_Consultation_Agent')
,(3187,353,1,26,NULL,'DVD_SUD_Consultation_Agent')
,(2839,65,1,5,NULL,'DPE_CELLULETECHNIQUE')
,(2840,1386,1,5,NULL,'DPE_CELLULETECHNIQUE')
,(2841,38,1,5,NULL,'DPE_CELLULETECHNIQUE')
;
INSERT INTO core_admin_user_field (id_user_field,id_user,id_attribute,id_field,id_file,user_field_value) VALUES 
(2843,29,1,5,NULL,'DPE_CELLULETECHNIQUE')
,(2850,1387,1,5,NULL,'DPE_CELLULETECHNIQUE')
,(2851,1388,1,5,NULL,'DPE_CELLULETECHNIQUE')
,(2852,1389,1,5,NULL,'DPE_CELLULETECHNIQUE')
,(2854,1284,1,2,NULL,'DPE_CODIR')
,(3188,1269,1,26,NULL,'DVD_SUD_Consultation_Agent')
,(2858,873,1,7,NULL,'DPE_OBSERVATEUR')
,(3190,1104,1,26,NULL,'DVD_SUD_Consultation_Agent')
,(3191,330,1,26,NULL,'DVD_SUD_Consultation_Agent')
,(3192,354,1,26,NULL,'DVD_SUD_Consultation_Agent')
;
INSERT INTO core_admin_user_field (id_user_field,id_user,id_attribute,id_field,id_file,user_field_value) VALUES 
(3193,381,1,26,NULL,'DVD_SUD_Consultation_Agent')
,(2877,1400,1,3,NULL,'DPE_STANDARD')
,(2878,1401,1,5,NULL,'DPE_CELLULETECHNIQUE')
,(2880,516,1,5,NULL,'DPE_CELLULETECHNIQUE')
,(3194,1279,1,26,NULL,'DVD_SUD_Consultation_Agent')
,(3195,1403,1,26,NULL,'DVD_SUD_Consultation_Agent')
,(3196,1078,1,26,NULL,'DVD_SUD_Consultation_Agent')
,(3257,1411,1,19,NULL,'PRESTATAIRE')
,(2890,1181,1,3,NULL,'DPE_STANDARD')
,(2893,1410,1,19,NULL,'PRESTATAIRE')
;
INSERT INTO core_admin_user_field (id_user_field,id_user,id_attribute,id_field,id_file,user_field_value) VALUES 
(2895,1412,1,19,NULL,'PRESTATAIRE')
,(2897,1414,1,19,NULL,'PRESTATAIRE')
,(2898,1415,1,19,NULL,'PRESTATAIRE')
,(3260,1432,1,25,NULL,'DVD_NORD_OUEST_Consultation_Agent')
,(3261,1422,1,24,NULL,'DVD_NORD_EST_Consultation_Agent')
,(3262,1433,1,28,NULL,'DVD_SUD_OUEST_Consultation_Agent')
,(3263,1434,1,8,NULL,'DVD_CODIR')
,(3264,1434,1,27,NULL,'DVD_SUD_EST_Consultation_Agent')
,(3266,1203,1,10,NULL,'DEVE_CODIR')
,(3270,1137,1,10,NULL,'DEVE_CODIR')
;
INSERT INTO core_admin_user_field (id_user_field,id_user,id_attribute,id_field,id_file,user_field_value) VALUES 
(3271,1359,1,2,NULL,'DPE_CODIR')
,(3272,1416,1,19,NULL,'PRESTATAIRE')
,(3273,1417,1,19,NULL,'PRESTATAIRE')
,(3276,1418,1,19,NULL,'PRESTATAIRE')
,(3277,1223,1,2,NULL,'DPE_CODIR')
,(3283,427,1,3,NULL,'DPE_STANDARD')
,(3284,1443,1,3,NULL,'DPE_STANDARD')
,(3286,284,1,24,NULL,'DVD_NORD_EST_Consultation_Agent')
,(3287,1439,1,24,NULL,'DVD_NORD_EST_Consultation_Agent')
,(3288,1444,1,8,NULL,'DVD_CODIR')
;
INSERT INTO core_admin_user_field (id_user_field,id_user,id_attribute,id_field,id_file,user_field_value) VALUES 
(3291,1446,1,5,NULL,'DPE_CELLULETECHNIQUE')
,(3293,1447,1,5,NULL,'DPE_CELLULETECHNIQUE')
,(3294,563,1,9,NULL,'DVD_CHEFDESECTEUR')
,(3295,1413,1,19,NULL,'PRESTATAIRE')
,(3298,1449,1,11,NULL,'ADMIN')
,(3302,1053,1,22,NULL,'DVD_Consultation_Agent')
,(3303,1029,1,22,NULL,'DVD_Consultation_Agent')
,(3304,563,1,23,NULL,'DVD_CENTRE_Consultation_Agent')
,(3305,284,1,8,NULL,'DVD_CODIR')
,(3306,1422,1,8,NULL,'DVD_CODIR')
;
INSERT INTO core_admin_user_field (id_user_field,id_user,id_attribute,id_field,id_file,user_field_value) VALUES 
(3308,1392,1,8,NULL,'DVD_CODIR')
,(3309,1392,1,8,NULL,'DVD_CODIR')
,(3310,1452,1,8,NULL,'DVD_CODIR')
,(3311,583,1,26,NULL,'DVD_SUD_Consultation_Agent')
,(3197,1425,1,26,NULL,'DVD_SUD_Consultation_Agent')
,(3198,379,1,26,NULL,'DVD_SUD_Consultation_Agent')
,(3199,1019,1,26,NULL,'DVD_SUD_Consultation_Agent')
,(3200,1211,1,26,NULL,'DVD_SUD_Consultation_Agent')
,(3201,1212,1,26,NULL,'DVD_SUD_Consultation_Agent')
,(3202,1196,1,26,NULL,'DVD_SUD_Consultation_Agent')
;
INSERT INTO core_admin_user_field (id_user_field,id_user,id_attribute,id_field,id_file,user_field_value) VALUES 
(3203,358,1,26,NULL,'DVD_SUD_Consultation_Agent')
,(3204,1035,1,26,NULL,'DVD_SUD_Consultation_Agent')
,(3205,1023,1,26,NULL,'DVD_SUD_Consultation_Agent')
,(3206,1214,1,26,NULL,'DVD_SUD_Consultation_Agent')
,(3207,359,1,26,NULL,'DVD_SUD_Consultation_Agent')
,(3208,333,1,26,NULL,'DVD_SUD_Consultation_Agent')
,(3209,380,1,26,NULL,'DVD_SUD_Consultation_Agent')
,(3210,578,1,26,NULL,'DVD_SUD_Consultation_Agent')
,(3289,1379,1,3,NULL,'DPE_STANDARD')
,(3075,1120,1,22,NULL,'DVD_Consultation_Agent')
;
INSERT INTO core_admin_user_field (id_user_field,id_user,id_attribute,id_field,id_file,user_field_value) VALUES 
(3076,1057,1,22,NULL,'DVD_Consultation_Agent')
,(3078,1240,1,22,NULL,'DVD_Consultation_Agent')
,(3080,423,1,22,NULL,'DVD_Consultation_Agent')
,(3081,1241,1,22,NULL,'DVD_Consultation_Agent')
,(3082,834,1,22,NULL,'DVD_Consultation_Agent')
,(3084,1069,1,22,NULL,'DVD_Consultation_Agent')
,(3085,1070,1,22,NULL,'DVD_Consultation_Agent')
,(3086,1243,1,22,NULL,'DVD_Consultation_Agent')
,(3087,1058,1,22,NULL,'DVD_Consultation_Agent')
,(3088,1061,1,22,NULL,'DVD_Consultation_Agent')
;
INSERT INTO core_admin_user_field (id_user_field,id_user,id_attribute,id_field,id_file,user_field_value) VALUES 
(3089,1048,1,22,NULL,'DVD_Consultation_Agent')
,(3212,289,1,27,NULL,'DVD_SUD_EST_Consultation_Agent')
,(3110,607,1,23,NULL,'DVD_CENTRE_Consultation_Agent')
,(3213,1424,1,27,NULL,'DVD_SUD_EST_Consultation_Agent')
,(3214,631,1,27,NULL,'DVD_SUD_EST_Consultation_Agent')
,(3215,633,1,27,NULL,'DVD_SUD_EST_Consultation_Agent')
,(3216,346,1,27,NULL,'DVD_SUD_EST_Consultation_Agent')
,(3217,1185,1,27,NULL,'DVD_SUD_EST_Consultation_Agent')
,(3218,1030,1,27,NULL,'DVD_SUD_EST_Consultation_Agent')
,(3219,1066,1,27,NULL,'DVD_SUD_EST_Consultation_Agent')
;
INSERT INTO core_admin_user_field (id_user_field,id_user,id_attribute,id_field,id_file,user_field_value) VALUES 
(3220,68,1,27,NULL,'DVD_SUD_EST_Consultation_Agent')
,(3221,635,1,27,NULL,'DVD_SUD_EST_Consultation_Agent')
,(3222,1190,1,27,NULL,'DVD_SUD_EST_Consultation_Agent')
,(3125,1384,1,22,NULL,'DVD_Consultation_Agent')
,(3127,1242,1,22,NULL,'DVD_Consultation_Agent')
,(3224,1201,1,27,NULL,'DVD_SUD_EST_Consultation_Agent')
,(3225,1268,1,27,NULL,'DVD_SUD_EST_Consultation_Agent')
,(3226,1138,1,27,NULL,'DVD_SUD_EST_Consultation_Agent')
,(3227,351,1,27,NULL,'DVD_SUD_EST_Consultation_Agent')
,(3228,1272,1,27,NULL,'DVD_SUD_EST_Consultation_Agent')
;
INSERT INTO core_admin_user_field (id_user_field,id_user,id_attribute,id_field,id_file,user_field_value) VALUES 
(3229,1292,1,27,NULL,'DVD_SUD_EST_Consultation_Agent')
,(3230,639,1,27,NULL,'DVD_SUD_EST_Consultation_Agent')
,(3231,626,1,27,NULL,'DVD_SUD_EST_Consultation_Agent')
,(3232,352,1,27,NULL,'DVD_SUD_EST_Consultation_Agent')
,(3233,1407,1,28,NULL,'DVD_SUD_OUEST_Consultation_Agent')
,(3234,586,1,28,NULL,'DVD_SUD_OUEST_Consultation_Agent')
,(3235,361,1,28,NULL,'DVD_SUD_OUEST_Consultation_Agent')
,(3236,587,1,28,NULL,'DVD_SUD_OUEST_Consultation_Agent')
,(3237,370,1,28,NULL,'DVD_SUD_OUEST_Consultation_Agent')
,(3238,591,1,28,NULL,'DVD_SUD_OUEST_Consultation_Agent')
;
INSERT INTO core_admin_user_field (id_user_field,id_user,id_attribute,id_field,id_file,user_field_value) VALUES 
(3239,399,1,28,NULL,'DVD_SUD_OUEST_Consultation_Agent')
,(3240,331,1,28,NULL,'DVD_SUD_OUEST_Consultation_Agent')
,(3241,1127,1,28,NULL,'DVD_SUD_OUEST_Consultation_Agent')
,(3242,582,1,28,NULL,'DVD_SUD_OUEST_Consultation_Agent')
,(3243,1274,1,28,NULL,'DVD_SUD_OUEST_Consultation_Agent')
,(3244,1427,1,28,NULL,'DVD_SUD_OUEST_Consultation_Agent')
,(3245,364,1,28,NULL,'DVD_SUD_OUEST_Consultation_Agent')
,(3246,365,1,28,NULL,'DVD_SUD_OUEST_Consultation_Agent')
,(3247,375,1,28,NULL,'DVD_SUD_OUEST_Consultation_Agent')
,(3248,372,1,28,NULL,'DVD_SUD_OUEST_Consultation_Agent')
;
INSERT INTO core_admin_user_field (id_user_field,id_user,id_attribute,id_field,id_file,user_field_value) VALUES 
(3249,366,1,28,NULL,'DVD_SUD_OUEST_Consultation_Agent')
,(3250,943,1,28,NULL,'DVD_SUD_OUEST_Consultation_Agent')
,(3251,376,1,28,NULL,'DVD_SUD_OUEST_Consultation_Agent')
,(3252,367,1,28,NULL,'DVD_SUD_OUEST_Consultation_Agent')
,(3253,368,1,28,NULL,'DVD_SUD_OUEST_Consultation_Agent')
,(3259,1428,1,3,NULL,'DPE_STANDARD')
,(3313,1453,1,8,NULL,'DVD_CODIR')
,(3314,1454,1,5,NULL,'DPE_CELLULETECHNIQUE')
,(3315,1455,1,29,NULL,'DEVE_Admin_User')
,(3320,1458,1,29,NULL,'DEVE_Admin_User')
;
INSERT INTO core_admin_user_field (id_user_field,id_user,id_attribute,id_field,id_file,user_field_value) VALUES 
(3321,1459,1,17,NULL,'DPE_Admin_User')
,(3322,1460,1,8,NULL,'DVD_CODIR')
,(3325,972,1,11,NULL,'ADMIN')
,(3326,1462,1,11,NULL,'ADMIN')
,(3331,1463,1,5,NULL,'DPE_CELLULETECHNIQUE')
,(3332,1464,1,5,NULL,'DPE_CELLULETECHNIQUE')
,(3333,1465,1,5,NULL,'DPE_CELLULETECHNIQUE')
,(3334,1466,1,7,NULL,'DPE_OBSERVATEUR')
,(3335,1409,1,19,NULL,'PRESTATAIRE')
,(3336,552,1,11,NULL,'ADMIN')
;
INSERT INTO core_admin_user_field (id_user_field,id_user,id_attribute,id_field,id_file,user_field_value) VALUES 
(3337,566,1,2,NULL,'DPE_CODIR')
,(3338,861,1,11,NULL,'ADMIN')
,(3339,1399,1,7,NULL,'DPE_OBSERVATEUR')
,(3340,1406,1,3,NULL,'DPE_STANDARD')
,(3341,642,1,3,NULL,'DPE_STANDARD')
,(3344,326,1,10,NULL,'DEVE_CODIR')
,(3345,42,1,25,NULL,'DVD_NORD_OUEST_Consultation_Agent')
,(3346,394,1,10,NULL,'DEVE_CODIR')
,(3348,1180,1,3,NULL,'DPE_STANDARD')
,(3349,1423,1,11,NULL,'ADMIN')
;
INSERT INTO core_admin_user_field (id_user_field,id_user,id_attribute,id_field,id_file,user_field_value) VALUES 
(3350,1468,1,11,NULL,'ADMIN')
,(3351,1469,1,11,NULL,'ADMIN')
;

--
-- Dumping data for table core_attribute_field
--
DELETE FROM core_attribute_field;
INSERT INTO core_attribute_field (id_field,id_attribute,title,default_value,is_default_value,height,width,max_size_enter,is_multiple,field_position) VALUES 
(1,1,NULL,NULL,0,0,0,0,0,1)
,(2,1,'DPE_CODIR','Chef de division, son adjoint ou représentant',0,0,0,0,0,2)
,(3,1,'DPE_STANDARD','Standardiste d''une division de la DPE',0,0,0,0,0,3)
,(5,1,'DPE_CELLULETECHNIQUE','Cellule technique de la DPE',0,0,0,0,0,5)
,(6,1,'DPE_FEUILLEDEROUTE','Chef d''équipe responsable des encombrants',0,0,0,0,0,6)
,(7,1,'DPE_OBSERVATEUR','Observateur DPE (ne peut rien faire mais voit tout)',0,0,0,0,0,7)
,(8,1,'DVD_CODIR','Chef de division, son adjoint ou représentant',0,0,0,0,0,8)
,(9,1,'DVD_CHEFDESECTEUR','Chef de secteur de la DVD',0,0,0,0,0,9)
,(10,1,'DEVE_CODIR','Chef de division, son adjoint ou représentant',0,0,0,0,0,10)
,(13,1,'DPE_FONCTIONNELLE','Graffitis, Affichages',0,0,0,0,0,13)
;
INSERT INTO core_attribute_field (id_field,id_attribute,title,default_value,is_default_value,height,width,max_size_enter,is_multiple,field_position) VALUES 
(11,1,'ADMIN','Administrateur',0,0,0,0,0,11)
,(12,1,'OBSERVATEUR_MAIRIES','observateur mairie d''arrondissement',0,0,0,0,0,12)
,(14,1,'DEVE_VEGETALISATION','DEVE_VEGETALISATION',0,0,0,0,0,14)
,(16,1,'DPE_Graf_all','DPE_Graf_all',0,0,0,0,0,16)
,(17,1,'DPE_Admin_User','Administration des utilisateurs de la DPE',0,0,0,0,0,17)
,(18,1,'DVD_Presta','DVD_Presta',0,0,0,0,0,18)
,(15,1,'Admin_users_metiers','Admin_users_metiers',0,0,0,0,0,15)
,(20,1,'Consultation_anomalies_DVD','Consultation des anomalies selon domaines fonctionnels DVD',0,0,0,0,0,20)
,(21,1,'Consultation_anomalies_DPE','Consultation des anomalies selon domaines fonctionnels DPE',0,0,0,0,0,21)
,(22,1,'DVD_Consultation_Agent','Profil Consutation pour agents de la DVD',0,0,0,0,0,22)
;
INSERT INTO core_attribute_field (id_field,id_attribute,title,default_value,is_default_value,height,width,max_size_enter,is_multiple,field_position) VALUES 
(23,1,'DVD_CENTRE_Consultation_Agent','Profil ConsutaLtion pour agents de la DVD_CENTRE',0,0,0,0,0,23)
,(24,1,'DVD_NORD_EST_Consultation_Agent','Profil Consultation pour agents de la DVD_NORD_EST',0,0,0,0,0,24)
,(25,1,'DVD_NORD_OUEST_Consultation_Agent','Profil Consultation pour agents de la DVD_NORD_OUEST',0,0,0,0,0,25)
,(27,1,'DVD_SUD_EST_Consultation_Agent','Profil Consultation pour agents de la DVD_SUD_EST',0,0,0,0,0,27)
,(26,1,'DVD_SUD_Consultation_Agent','Profil Consultation pour agents de la DVD_SUD',0,0,0,0,0,26)
,(28,1,'DVD_SUD_OUEST_Consultation_Agent','Profil Consultation pour agents de la DVD_SUD_OUEST',0,0,0,0,0,28)
,(19,1,'PRESTATAIRE','Prestataire',0,0,0,0,0,19)
,(29,1,'DEVE_Admin_User','Administration des utilisateurs de la DEVE',0,0,0,0,0,29)
,(30,1,'DAJ_Gestionnaire','Gestion des anomalies au niveau de la DAJ',0,0,0,0,0,30)
;

--
-- Dumping data for table core_dashboard
--
DELETE FROM core_dashboard;
INSERT INTO core_dashboard (dashboard_name,dashboard_column,dashboard_order) VALUES 
('CORE_USER',4,1)
,('SIGNALEMENT',2,1)
,('WORKFLOW',-1,1)
,('CORE_USERS',-1,2)
,('CORE_PAGES',-1,3)
,('CORE_SYSTEM',-1,4)
;

--
-- Dumping data for table core_datastore
--
DELETE FROM core_datastore;
INSERT INTO core_datastore (entity_key,entity_value) VALUES 
('core_banned_domain_names','yopmail.com')
,('core.backOffice.defaultEditor','tinymce')
,('core.frontOffice.defaultEditor','markitupbbcode')
,('portal.site.site_property.noreply_email','no-reply@mydomain.com')
,('portal.site.site_property.admin_home_url','jsp/admin/AdminMenu.jsp')
,('core.advanced_parameters.password_duration','120')
,('core.advanced_parameters.default_user_level','2')
,('core.advanced_parameters.default_user_notification','1')
,('core.advanced_parameters.default_user_language','fr')
,('core.advanced_parameters.default_user_status','0')
;
INSERT INTO core_datastore (entity_key,entity_value) VALUES 
('core.advanced_parameters.force_change_password_reinit','false')
,('core.advanced_parameters.password_minimum_length','8')
,('core.advanced_parameters.password_format_upper_lower_case','false')
,('core.advanced_parameters.password_format_numero','false')
,('core.advanced_parameters.password_format_special_characters','false')
,('core.advanced_parameters.password_history_size','')
,('core.advanced_parameters.maximum_number_password_change','')
,('core.advanced_parameters.tsw_size_password_change','')
,('core.advanced_parameters.use_advanced_security_parameters','')
,('core.advanced_parameters.account_life_time','12')
;
INSERT INTO core_datastore (entity_key,entity_value) VALUES 
('core.advanced_parameters.time_before_alert_account','30')
,('core.advanced_parameters.nb_alert_account','2')
,('core.advanced_parameters.time_between_alerts_account','10')
,('core.advanced_parameters.access_failures_max','3')
,('core.advanced_parameters.access_failures_interval','10')
,('core.advanced_parameters.expired_alert_mail_sender','lutece@nowhere.com')
,('core.advanced_parameters.expired_alert_mail_subject','Votre compte a expiré')
,('core.advanced_parameters.first_alert_mail_sender','lutece@nowhere.com')
,('core.advanced_parameters.first_alert_mail_subject','Votre compte va bientot expirer')
,('core.advanced_parameters.other_alert_mail_sender','lutece@nowhere.com')
;
INSERT INTO core_datastore (entity_key,entity_value) VALUES 
('core.advanced_parameters.other_alert_mail_subject','Votre compte va bientot expirer')
,('core.advanced_parameters.account_reactivated_mail_sender','lutece@nowhere.com')
,('core.advanced_parameters.account_reactivated_mail_subject','Votre compte a bien été réactivé')
,('core.advanced_parameters.access_failures_captcha','1')
,('core.advanced_parameters.notify_user_password_expired','')
,('core.advanced_parameters.password_expired_mail_sender','lutece@nowhere.com')
,('core.advanced_parameters.password_expired_mail_subject','Votre mot de passe a expiré')
,('portal.site.site_property.avatar_default','images/admin/skin/unknown.png')
,('portal.site.site_property.back_images','''images/admin/skin/bg_login1.jpg'' , ''images/admin/skin/bg_login2.jpg'' , ''images/admin/skin/bg_login3.jpg'' , ''images/admin/skin/bg_login4.jpg''')
,('mylutece.security.public_url.mylutece.url.login.page','jsp/site/Portal.jsp?page=mylutece&action=login')
;
INSERT INTO core_datastore (entity_key,entity_value) VALUES 
('mylutece.security.public_url.mylutece.url.doLogin','jsp/site/plugins/mylutece/DoMyLuteceLogin.jsp')
,('mylutece.security.public_url.mylutece.url.doLogout','jsp/site/plugins/mylutece/DoMyLuteceLogout.jsp')
,('mylutece.security.public_url.mylutece.url.createAccount.page','jsp/site/Portal.jsp?page=mylutece&action=createAccount')
,('mylutece.security.public_url.mylutece.url.modifyAccount.page','jsp/site/Portal.jsp?page=mylutece&action=modifyAccount')
,('mylutece.security.public_url.mylutece.url.lostPassword.page','jsp/site/Portal.jsp?page=mylutece&action=lostPassword')
,('mylutece.security.public_url.mylutece.url.lostLogin.page','jsp/site/Portal.jsp?page=mylutecedatabase&action=lostLogin')
,('mylutece.security.public_url.mylutece.url.doActionsAll','jsp/site/plugins/mylutece/Do*')
,('mylutece.security.public_url.mylutece.url.js','js/*')
,('mylutece.security.public_url.mylutece.url.gis','jsp/site/plugins/gis*')
,('mylutece.security.public_url.mylutece.url.plugin.adresse','jsp/site/plugins/address/*')
;
INSERT INTO core_datastore (entity_key,entity_value) VALUES 
('mylutece.security.public_url.mylutece.url.formengine.doAction','jsp/site/plugins/formengine/DoAction.jsp')
,('mylutece.security.public_url.mylutece.formengine.signalement.forms','jsp/site/plugins/*?form=signalement*')
,('mylutece.security.public_url.mylutece.url.upload','jsp/site/upload')
,('mylutece.security.public_url.mylutece.url.annulation','<base_URL_BO>/*')
,('core.cache.status.DocumentResourceServletCache.enabled','1')
,('core.cache.status.LuteceUserCacheService.enabled','1')
,('core.cache.status.MailAttachmentCacheService.overflowToDisk','true')
,('core.cache.status.LuteceUserCacheService.maxElementsInMemory','1000')
,('core.cache.status.StaticFilesCachingFilter.timeToLiveSeconds','604800')
,('core.cache.status.MailAttachmentCacheService.diskPersistent','true')
;
INSERT INTO core_datastore (entity_key,entity_value) VALUES 
('core.cache.status.BaseUserPreferencesCacheService.maxElementsInMemory','1000')
,('core.cache.status.MyPortalWidgetContentService.enabled','1')
,('core.cache.status.MailAttachmentCacheService.timeToLiveSeconds','7200')
,('core.cache.status.MailAttachmentCacheService.maxElementsInMemory','10')
,('core.cache.status.MyPortalWidgetService.enabled','1')
,('core.plugins.status.signalement.pool','portal')
,('core.plugins.status.ganalytics.installed','true')
,('core.plugins.status.lucene.installed','true')
,('core.plugins.status.signalement-ramen.installed','true')
,('core.plugins.status.adminauthenticationwsso.installed','true')
;
INSERT INTO core_datastore (entity_key,entity_value) VALUES 
('core.plugins.status.formengine-signalement.installed','false')
,('portal.site.site_property.meta.author','Mairie de Paris')
,('portal.site.site_property.meta.copyright','Mairie de Paris')
,('portal.site.site_property.popup_credits.textblock','Mairie de Paris')
,('portal.site.site_property.popup_legal_info.copyright.textblock','Mairie de Paris')
,('portal.site.site_property.popup_legal_info.privacy.textblock','Mairie de Paris')
,('portal.site.site_property.email','magali.lemaire@paris.fr')
,('core.plugins.status.formengine-dansmarue.pool','portal')
,('core.plugins.status.jasper.installed','true')
,('mylutece.security.public_url.mylutece.url.geoserver','R59-GEO-APP')
;
INSERT INTO core_datastore (entity_key,entity_value) VALUES 
('portal.site.site_property.name','DMR - Signalement')
,('portal.site.site_property.meta.keywords','encombrant, dépôt, déchet, anomalie, incivilité')
,('core.plugins.status.signalement-rest.installed','true')
,('core.plugins.status.unittree.pool','portal')
,('core.plugins.status.unittree.installed','true')
,('core.plugins.status.core_extensions.installed','true')
,('core.plugins.status.plugin-rest.installed','true')
,('core.plugins.status.address.installed','true')
,('core.plugins.status.dansmarue.installed','true')
,('core.plugins.status.profiles.installed','true')
;
INSERT INTO core_datastore (entity_key,entity_value) VALUES 
('core.plugins.status.signalement-ramen.pool','portal')
,('core.plugins.status.gis-address.installed','true')
,('core.plugins.status.unittree-profiles.installed','true')
,('core.plugins.status.gis.installed','true')
,('core.plugins.status.workflow-dansmarue.pool','portal')
,('core.plugins.status.workflow-rest.installed','true')
,('core.plugins.status.jasper.pool','portal')
,('core.plugins.status.identity-provider.installed','true')
,('core.plugins.status.workflow-dansmarue.installed','true')
,('core.plugins.status.profiles.pool','portal')
;
INSERT INTO core_datastore (entity_key,entity_value) VALUES 
('core.plugins.status.unittree-sira.installed','true')
,('core.plugins.status.formengine-gis.installed','true')
,('core.plugins.status.mylutece.installed','true')
,('core.plugins.status.dansmarue-ramen.pool','portal')
,('core.plugins.status.dansmarue-rest.installed','true')
,('core.plugins.status.formengine-dansmarue.installed','true')
,('core.plugins.status.signalement.installed','true')
,('core.plugins.status.workflow.installed','true')
,('core.plugins.status.ganalytics.pool','portal')
,('core.plugins.status.workflow-signalement.installed','true')
;
INSERT INTO core_datastore (entity_key,entity_value) VALUES 
('core.plugins.status.sira-rest.installed','true')
,('core.plugins.status.gis.pool','portal')
,('core.plugins.status.workflow-signalement.pool','portal')
,('core.plugins.status.dansmarue.pool','portal')
,('core.plugins.status.mylutece-openam.installed','true')
,('core.plugins.status.rest.installed','true')
,('core.plugins.status.mylutece.pool','portal')
,('core.plugins.status.dansmarue-ramen.installed','true')
,('core.plugins.status.address-autocomplete.installed','true')
,('core.plugins.status.workflow.pool','portal')
;
INSERT INTO core_datastore (entity_key,entity_value) VALUES 
('core.daemon.indexer.interval','300')
,('core.daemon.mailSender.interval','60')
,('core.daemon.anonymizationDaemon.interval','86400')
,('core.daemon.anonymizationDaemon.onStartUp','false')
,('core.daemon.accountLifeTimeDaemon.interval','86400')
,('core.daemon.threadLauncherDaemon.interval','60')
,('core.daemon.jasperPurgeImage.interval','10')
,('core.daemon.jasperPurgeImage.onStartUp','false')
,('core.daemon.automaticActionDaemon.interval','14400')
,('core.daemon.formengineNotification.interval','10')
;
INSERT INTO core_datastore (entity_key,entity_value) VALUES 
('mylutece.portal.authentication.required','false')
,('core.advanced_parameters.email_pattern_verify_by','')
,('piwik.site_property.server.http.url','https://piwik.apps.paris.fr/piwik/')
,('piwik.site_property.server.https.url','https://piwik.apps.paris.fr/piwik/')
,('piwik.site_property.widget.auth.token','ec26b24ba6a05fcc883f50a2a8742c90&')
,('piwik.site_property.site.id','248')
,('sitelabels.site_property.mobile.apple.link.href','https://itunes.apple.com/fr/app/dansmarue/id662045577?mt=8')
,('sitelabels.site_property.mobile.android.link.href','https://play.google.com/store/apps/details?id=fr.paris.android.signalement&hl=fr')
,('sitelabels.site_property.footer.paris.url','http://www.paris.fr')
;
INSERT INTO core_datastore (entity_key,entity_value) VALUES 
('sitelabels.site_property.footer.mentions.legales.url','http://www.paris.fr/mentions-legales')
,('sitelabels.site_property.footer.faq.url','jsp/site/Portal.jsp?page=helpdesk&faq_id=1')
,('sitelabels.site_property.footer.contact.url','http://www.paris.fr/contact')
,('sitelabels.site_property.condition_utilisation.link.href','https://teleservices.paris.fr/sira/DansMaRue_conditions_d_utilisation.pdf ')
,('core.plugins.status.leaflet-sira.installed','true')
,('core.plugins.status.formengine.pool','portal')
,('core.plugins.status.formengine-signalement.pool','portal')
,('core.plugins.status.formengine.installed','false')
,('core.plugins.status.identitystoreopenamprovider.installed','true')
,('core.plugins.status.workflow-notifygru.installed','true')
;
INSERT INTO core_datastore (entity_key,entity_value) VALUES 
('core.plugins.status.leaflet.installed','true')
,('core.plugins.status.piwik.installed','true')
,('portal.site.site_property.meta.description','Prise de rendez-vous pour l''enlèvement des encombrants à Paris, DansMaRue, gestion des anomalies')
,('core.daemon.ExportWssoAdminUsersDaemon.interval','3600')
,('core.plugins.status.sitelabels.installed','true')
,('core.cache.status.JasperService.enabled','0')
,('portal.site.site_property.logo_url','images/logo-header-icon.png')
,('sitelabels.site_property.footer.paris','Mairie de paris')
,('sitelabels.site_property.footer.mentions.legales','Mensions legales')
,('sitelabels.site_property.footer.contact','Contact')
;
INSERT INTO core_datastore (entity_key,entity_value) VALUES 
('core.plugins.status.disconnection.pool','portal')
,('core.plugins.status.disconnection.installed','true')
,('core.daemon.formengineNotification.onStartUp','true')
,('core.daemon.fullIndexingDaemon.interval','86400')
,('core.plugins.status.fosignalement.installed','true')
,('core.plugins.status.fosignalement.pool','portal')
,('dansmarue.site_property.compression_quality','0.4')
,('dansmarue.site_property.taille_max','300')
,('sitelabels.site_property.title.ramen','RAMEN')
,('sitelabels.site_property.menu.ramen.dossier','Gestion des dossiers')
;
INSERT INTO core_datastore (entity_key,entity_value) VALUES 
('core.cache.status.PageCachingFilter.enabled','0')
,('core.daemon.threadLauncherDaemon.onStartUp','true')
,('core.daemon.accountLifeTimeDaemon.onStartUp','true')
,('core.daemon.mailSender.onStartUp','true')
,('core.daemon.ExportWssoAdminUsersDaemon.onStartUp','true')
,('core.cache.status.PortalMenuService.enabled','0')
,('sitelabels.site_property.menu.ramen.referentiel','Gestion du référentiel')
,('sitelabels.site_property.menu.ramen.feuille.de.route','Gestion des feuilles de route')
,('sitelabels.site_property.menu.ramen.service.fait','Déclaration des services faits')
,('sitelabels.site_property.ramen.url','https://teleservices.rec.apps.tmma.paris.mdp/ramen/')
;
INSERT INTO core_datastore (entity_key,entity_value) VALUES 
('core.cache.status.SiteMapService.enabled','1')
,('core.plugins.status.elasticdata.installed','true')
,('core.plugins.status.elasticdata.pool','portal')
,('core.plugins.status.elasticdata-dansmarue.installed','true')
,('core.plugins.status.elasticdata-dansmarue.pool','portal')
,('core.plugins.status.leaflet-dansmarue.installed','true')
,('core.plugins.status.unittree-signalement.installed','true')
,('portal.site.site_property.home_url','jsp/site/Portal.jsp?page=dansmarue')
,('core.cache.status.asynchronousupload.asynchronousUploadCacheService.enabled','0')
,('core.daemon.webservicePartnerDaemon.interval','300')
;
INSERT INTO core_datastore (entity_key,entity_value) VALUES 
('sitelabels.site_property.app.content','Utilisez l''application mobile DansMaRue depuis votre smartphone pour transmettre directement vos anomalies depuis les rues ou les parcs de Paris.')
,('sitelabels.site_property.app.title','DANSMARUE SUR VOTRE MOBILE')
,('sitelabels.site_property.finalisation.photo.detaillee.label','Photo détaillée')
,('sitelabels.site_property.finalisation.photo.ensemble.label','Photo d''ensemble')
,('sitelabels.site_property.finalisation.precisions.label','Précisions complémentaires concernant la demande')
,('sitelabels.site_property.finalisation.priotrie.label','Priorité')
,('sitelabels.site_property.finalisation.priotrie.radio.1','Dangereux')
,('sitelabels.site_property.finalisation.priotrie.radio.2','Gênant')
,('sitelabels.site_property.finalisation.priotrie.radio.3','Très gênant')
,('sitelabels.site_property.general.facultatilf.label','(facultatif)')
;
INSERT INTO core_datastore (entity_key,entity_value) VALUES 
('core.plugins.status.workflow-ramen.installed','true')
,('core.plugins.status.workflow-ramen.pool','portal')
,('core.plugins.status.ramen.installed','true')
,('core.plugins.status.ramen.pool','portal')
,('core.plugins.status.ramen-rest.installed','true')
,('core.daemon.indexer.onStartUp','true')
,('core.daemon.automaticActionDaemon.onStartUp','true')
,('core.startup.time','Aug 24, 2018 3:12:37 PM')
,('core.cache.status.DatastoreCacheService.enabled','0')
,('core.cache.status.MailAttachmentCacheService.enabled','0')
;
INSERT INTO core_datastore (entity_key,entity_value) VALUES 
('core.cache.status.StaticFilesCachingFilter.enabled','0')
,('core.cache.status.XMLTransformerCacheService(XSLT).enabled','1')
,('core.cache.status.pathCacheService.enabled','0')
,('core.cache.status.LinksIncludeCacheService.enabled','0')
,('core.cache.status.PageCacheService.enabled','0')
,('core.cache.status.PortletCacheService.enabled','0')
,('core.cache.status.BaseUserPreferencesCacheService.enabled','0')
,('core.cache.status.workflow.notifyGruConfigCacheService.enabled','0')
,('core.daemon.fullIndexingDaemon.onStartUp','true')
,('core.daemon.webservicePartnerDaemon.onStartUp','true')
;

--
-- Dumping data for table core_page
--
DELETE FROM core_page;
INSERT INTO core_page (id_page,id_parent,name,description,date_update,status,page_order,id_template,date_creation,"role",code_theme,node_status,image_content,mime_type,meta_keywords,meta_description,id_authorization_node,display_date_update,is_manual_date_update) VALUES 
(1,0,'accueil','Page d''accueil','2009-05-01 22:19:33.000',1,1,4,'2003-09-09 04:38:01.000','none','default',0,NULL,'application/octet-stream',NULL,NULL,1,0,0)
,(3,1,'Documentation','Tout ce dont vous avez besoin pour utiliser Lutece','2006-10-18 13:39:24.000',0,2,2,'2002-09-09 04:46:46.000','none','default',0,NULL,'application/octet-stream',NULL,NULL,1,0,0)
,(6,3,'Guide utilisateur','Accès au guide utilisateur','2006-09-19 12:20:13.000',0,1,1,'2006-02-15 15:39:59.000','none','default',1,NULL,'',NULL,NULL,1,0,0)
,(5,1,'L''outil','Description du CMS Lutèce','2006-10-12 13:03:49.000',0,1,2,'2006-02-15 15:37:26.000','none','default',1,NULL,'',NULL,NULL,1,0,0)
,(7,3,'Guide technique','Accès à documentation technique','2006-09-19 12:19:45.000',0,2,1,'2006-02-15 15:40:30.000','none','default',1,NULL,'image/gif',NULL,NULL,1,0,0)
,(10,1,'Développeurs','Ressources pour les contributeurs/développeurs Lutèce','2009-05-01 22:25:13.000',0,4,2,'2006-10-12 13:03:20.000','none','default',1,NULL,NULL,NULL,NULL,1,0,0)
,(11,5,'Répondez à notre questionnaire','Plugin form','2009-06-16 10:54:35.000',0,1,2,'2009-06-16 10:53:39.000','none','default',1,NULL,NULL,NULL,NULL,1,0,0)
,(12,10,'Générateur de code','','2009-07-13 13:49:14.000',0,1,1,'2009-07-13 13:49:14.000','none','default',1,NULL,NULL,NULL,NULL,1,0,0)
;

--
-- Dumping data for table core_feature_group
--
DELETE FROM core_feature_group;
INSERT INTO public.core_feature_group (id_feature_group,feature_group_description,feature_group_label,feature_group_order) VALUES 
('CONTENT','portal.features.group.content.description','portal.features.group.content.label',1)
,('APPLICATIONS','portal.features.group.applications.description','portal.features.group.applications.label',3)
,('SYSTEM','portal.features.group.system.description','portal.features.group.system.label',7)
,('SITE','portal.features.group.site.description','portal.features.group.site.label',2)
,('STYLE','portal.features.group.charter.description','portal.features.group.charter.label',6)
,('USERS','portal.features.group.users.description','portal.features.group.users.label',4)
,('MANAGERS','portal.features.group.managers.description','portal.features.group.managers.label',5)
,('SIGNALEMENT','dansmarue.features.group.signalement.description','dansmarue.features.group.signalement.label',9)
;


--
-- Dumping data for table core_user_right
--
INSERT INTO core_user_right (id_right, id_user) VALUES ('SIGNALEMENT_MANAGEMENT', 1);
INSERT INTO core_user_right (id_right, id_user) VALUES ('REFERENTIEL_MANAGEMENT_SIGNALEMENT', 1);