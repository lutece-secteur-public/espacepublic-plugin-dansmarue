-- Tâche notification push
DROP TABLE IF EXISTS signalement_workflow_notification_push_config;
CREATE TABLE signalement_workflow_notification_push_config(
	id_task integer NOT NULL,
	mobile_subject varchar(255),
	mobile_message text,
	is_diffusion_declarant int4 NULL,
	is_diffusion_suiveur int4 NULL,
	CONSTRAINT signalement_workflow_notification_push_config_pk PRIMARY KEY (id_task)
);
-- DMR-1244 (Déjà exécuté)

-- id_unit = 1532 en recette
--INSERT INTO unittree_unit_sector
--(id_unit, id_sector)
--VALUES(1532, 99031);
-- id_unit = 1531 en prod
--INSERT INTO unittree_unit_sector
--(id_unit, id_sector)
--VALUES(1531, 99031);

DROP TABLE IF EXISTS signalement_workflow_notification_push_value;
CREATE TABLE signalement_workflow_notification_push_value(
	id_history integer NOT NULL DEFAULT 0,
	id_task integer NOT NULL,
	mobile_notification_value text,
	CONSTRAINT signalement_workflow_notification_push_value_pk PRIMARY KEY (id_history,id_task),
	CONSTRAINT signalement_workflow_notification_push_value_task_fk FOREIGN KEY (id_task) REFERENCES workflow_task(id_task),
	CONSTRAINT signalement_workflow_notification_push_value_history_fk FOREIGN KEY (id_history) REFERENCES workflow_resource_history(id_history)
);
-- id_unit = 1532 en recette
--UPDATE signalement_type_signalement
--SET fk_id_unit=1532
--WHERE id_type_signalement=7210;
-- id_unit = 1531 en prod
--UPDATE signalement_type_signalement
--SET fk_id_unit=1531
--WHERE id_type_signalement=7210;

ALTER TABLE signalement_workflow_notification_suivi_config DROP COLUMN mobile_subject, DROP COLUMN mobile_message;
ALTER TABLE signalement_workflow_notification_suivi_value DROP COLUMN mobile_notification_value;
-- Même paramétrage prod et recette
--update
--	signalement_signalement
--set
--	fk_id_sector = 99031
--where fk_id_type_signalement = 7210
--and annee = '2018';


-- DMR-1239
ALTER TABLE signalement_workflow_notification_config_unit ADD COLUMN id_type_signalement int;
alter table
	signalement_workflow_notification_config_unit drop
		constraint signalement_workflow_notification_config_unit_pkey;

alter table
	signalement_workflow_notification_config_unit alter column id_unit drop
		not null;		
ALTER TABLE signalement_workflow_notification_config_unit alter column destinataires TYPE varchar(200);
ALTER TABLE signalement_workflow_notification_config_unit alter column destinataires TYPE text;