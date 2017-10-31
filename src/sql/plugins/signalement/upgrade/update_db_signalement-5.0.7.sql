-- Table de stockage des préférences utilisateur du dashboard
DROP TABLE IF EXISTS signalement_dashboard_user_preferences;
CREATE TABLE signalement_dashboard_user_preferences(
	fk_id_user integer,
	fk_id_state integer,
	CONSTRAINT signalement_dashboard_user_preferences_pk PRIMARY KEY (fk_id_user,fk_id_state),
	CONSTRAINT signalement_dashboard_user_fk FOREIGN KEY (fk_id_user) REFERENCES core_admin_user(id_user),
	CONSTRAINT signalement_dashboard_state_fk FOREIGN KEY(fk_id_state) REFERENCES workflow_state(id_state)
);

DROP FUNCTION IF EXISTS initSignalementTypeLeafRoot();
DROP TABLE IF EXISTS signalement_type_leaf_root;

DELETE FROM workflow_state WHERE id_state IN (22,23);