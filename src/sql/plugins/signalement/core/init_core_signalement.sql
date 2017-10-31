--
-- Dumping data for table core_admin_right
--
INSERT INTO core_admin_right (id_right, name, level_right, admin_url, description, is_updatable, plugin_name, id_feature_group, icon_url, documentation_url, id_order)
 VALUES ('REFERENTIEL_MANAGEMENT_SIGNALEMENT', 'dansmarue.adminFeature.referentielmanagement.name', 2, 'jsp/admin/plugins/signalement/ManageReferentiel.jsp', 'signalement.adminFeature.referentielmanagement.description', 0, 'signalement', 'SIGNALEMENT', NULL, NULL, 2);
INSERT INTO core_admin_right (id_right, name, level_right, admin_url, description, is_updatable, plugin_name, id_feature_group, icon_url, documentation_url, id_order)
 VALUES ('SIGNALEMENT_MANAGEMENT', 'dansmarue.adminFeature.signalementManagement.name', 2, 'jsp/admin/plugins/signalement/ManageSignalement.jsp', 'signalement.adminFeature.signalementManagement.description', 0, 'signalement', 'SIGNALEMENT', NULL, NULL, 1);

--
-- Dumping data for table core_user_right
--
INSERT INTO core_user_right (id_right, id_user) VALUES ('SIGNALEMENT_MANAGEMENT', 1);
INSERT INTO core_user_right (id_right, id_user) VALUES ('REFERENTIEL_MANAGEMENT_SIGNALEMENT', 1);