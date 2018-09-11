---------------------------------------------------------
-- Gestion du suivi des anomalies
---------------------------------------------------------
-- Ajout du guid
ALTER TABLE signalement_signaleur RENAME id_gru TO guid;

-- Création table suiveur
DROP TABLE IF EXISTS signalement_suivi;
DROP TABLE IF EXISTS sira_user;

DROP SEQUENCE IF EXISTS seq_sira_user_id;
CREATE SEQUENCE seq_sira_user_id;

DROP SEQUENCE IF EXISTS seq_signalement_suivi_id;
CREATE SEQUENCE seq_signalement_suivi_id;

CREATE TABLE sira_user (
	id_sira_user bigint,
	user_guid varchar(255),
	user_udid varchar(255),
	user_device varchar(255),
	user_email varchar(255),
	CONSTRAINT user_guid_pk PRIMARY KEY(id_sira_user)
);

-- Suivi des signalements
CREATE TABLE signalement_suivi(
	id_suivi bigint,
	fk_id_signalement bigint,
	fk_user_guid varchar(255),
	CONSTRAINT suivi_pk PRIMARY KEY (id_suivi),
	CONSTRAINT suivi_signalement_fk FOREIGN KEY (fk_id_signalement) REFERENCES signalement_signalement(id_signalement)
);


-- Tâche suivi des signalements
DROP TABLE IF EXISTS signalement_workflow_notification_suivi_config;
CREATE TABLE signalement_workflow_notification_suivi_config(
	id_task integer NOT NULL,
	subject varchar(255),
	mobile_subject varchar(255),
	sender varchar(255),
	mail_message text,
	mobile_message text,
	CONSTRAINT signalement_workflow_notification_suivi_config_pk PRIMARY KEY (id_task)
);

DROP TABLE IF EXISTS signalement_workflow_notification_suivi_value;
CREATE TABLE signalement_workflow_notification_suivi_value(
	id_history integer NOT NULL DEFAULT 0,
	id_task integer NOT NULL,
	mail_notification_value text,
	mobile_notification_value text,
	CONSTRAINT signalement_workflow_notification_suivi_value_pk PRIMARY KEY (id_history,id_task),
	CONSTRAINT signalement_workflow_notification_suivi_value_task_fk FOREIGN KEY (id_task) REFERENCES workflow_task(id_task),
	CONSTRAINT signalement_workflow_notification_suivi_value_history_fk FOREIGN KEY (id_history) REFERENCES workflow_resource_history(id_history)
);

ALTER TABLE sira_user DROP IF EXISTS user_token;
ALTER TABLE sira_user ADD COLUMN user_token varchar(255);

--------------------------------------------------------------
-- Gestion du tableau de bord
--------------------------------------------------------------

-- Ajout du droit
DELETE FROM core_admin_right WHERE id_right = 'SIGNALEMENT_DASHBOARD';
INSERT INTO core_admin_right(id_right,name,level_right,admin_url,description,is_updatable,plugin_name,id_feature_group,icon_url,documentation_url,id_order,is_external_feature) 
VALUES ('SIGNALEMENT_DASHBOARD','dansmarue.adminFeature.signalementDashboard.name',2,'jsp/admin/plugins/signalement/ManageSignalementDashboard.jsp','dansmarue.adminFeature.signalementDashboard.description',
0,'signalement','SIGNALEMENT',null,null,5,0);

DROP SEQUENCE IF EXISTS seq_signalement_dashboard_period_id;
CREATE SEQUENCE seq_signalement_dashboard_period_id;

-- Référentiel des périodes du tableau de bord
DROP TABLE IF EXISTS signalement_dashboard_period;
CREATE TABLE signalement_dashboard_period(
	id_period bigint NOT NULL,
	libelle varchar(255) NOT NULL,
	lower_bound integer,
	higher_bound integer,
	unit varchar(50) NOT NULL,
	category varchar(100),
	ordre integer,
	CONSTRAINT signalement_dashboard_period_pk PRIMARY KEY (id_period)
);

-- Insertion des périodes
INSERT INTO signalement_dashboard_period(id_period,libelle,lower_bound,higher_bound,unit,category,ordre) VALUES (nextval('seq_signalement_dashboard_period_id'),'Mois courant',-1,null,'month',null,0);
INSERT INTO signalement_dashboard_period(id_period,libelle,lower_bound,higher_bound,unit,category,ordre) VALUES (nextval('seq_signalement_dashboard_period_id'),'3 derniers mois',-3,null,'month',null,1);
INSERT INTO signalement_dashboard_period(id_period,libelle,lower_bound,higher_bound,unit,category,ordre) VALUES (nextval('seq_signalement_dashboard_period_id'),'6 derniers mois',-6,null,'month',null,2);

INSERT INTO signalement_dashboard_period(id_period,libelle,lower_bound,higher_bound,unit,category,ordre) VALUES (nextval('seq_signalement_dashboard_period_id'),'10 jours et +',null,-11,'day','other',0);
INSERT INTO signalement_dashboard_period(id_period,libelle,lower_bound,higher_bound,unit,category,ordre) VALUES (nextval('seq_signalement_dashboard_period_id'),'- de 10 jours',-10,null,'day','other',1);
INSERT INTO signalement_dashboard_period(id_period,libelle,lower_bound,higher_bound,unit,category,ordre) VALUES (nextval('seq_signalement_dashboard_period_id'),'- de 48 h',-2,null,'day','other',2);

INSERT INTO signalement_dashboard_period(id_period,libelle,lower_bound,higher_bound,unit,category,ordre) VALUES (nextval('seq_signalement_dashboard_period_id'),'- de 8 jours avant la date programmée',0,8,'day','planified',0);
INSERT INTO signalement_dashboard_period(id_period,libelle,lower_bound,higher_bound,unit,category,ordre) VALUES (nextval('seq_signalement_dashboard_period_id'),'- de 10 jours de retard',-10,0,'day','planified',1);
INSERT INTO signalement_dashboard_period(id_period,libelle,lower_bound,higher_bound,unit,category,ordre) VALUES (nextval('seq_signalement_dashboard_period_id'),'Retard de 10 jours et +',null,-11,'day','planified',2);

DELETE FROM core_admin_role_resource WHERE permission like '%DASHBOARD%';

DELETE FROM core_user_right WHERE id_user=1 AND id_right = 'SIGNALEMENT_DASHBOARD';
INSERT INTO core_user_right(id_right,id_user) VALUES ('SIGNALEMENT_DASHBOARD',1);
