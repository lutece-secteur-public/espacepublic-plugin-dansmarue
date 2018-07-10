update signalement_dashboard_period set ordre=0 where id_period=18;
update signalement_dashboard_period set ordre=2 where id_period=16;

delete from core_attribute_field where title='DPE_CHEFDESECTEUR';


/* DMR-1088 */

/* Création de la nouvelle table de messages */
CREATE SEQUENCE seq_signalement_workflow_notifuser_multi_contents_config;
CREATE TABLE signalement_workflow_notifuser_multi_contents_config (
	id_message bigint NOT NULL,
	subject character varying(255),
	sender character varying(255),
	title character varying(255),
	message text,
	id_task bigint,
	CONSTRAINT id_message_pk PRIMARY KEY(id_message)
);
SELECT setval( 'seq_signalement_workflow_notifuser_multi_contents_config', ( SELECT MAX( signalement_workflow_notifuser_multi_contents_config.id_message )+1 FROM signalement_workflow_notifuser_multi_contents_config) );

/* Rename des tables notifuser_3contents */
alter table signalement_workflow_notifuser_3contents_value
rename to signalement_workflow_notifuser_multi_contents_value;

/* Création de la table association */
CREATE TABLE signalement_workflow_notifuser_multi_contents_task (
	id_task bigint NOT NULL,
	id_message bigint NOT null,
	CONSTRAINT signalement_workflow_notifuser_multi_contents_config_message_fk FOREIGN KEY (id_message) REFERENCES signalement_workflow_notifuser_multi_contents_config(id_message)
);

/* Remplissage table association et table message */
insert into signalement_workflow_notifuser_multi_contents_config(id_message,subject,sender,title,message,id_task)
	select nextval('seq_signalement_workflow_notifuser_multi_contents_config'),subject, sender, title1, message1, id_task
	from signalement_workflow_notifuser_3contents_config;

insert into signalement_workflow_notifuser_multi_contents_config(id_message,subject,sender,title,message,id_task)
	select nextval('seq_signalement_workflow_notifuser_multi_contents_config'),subject, sender, title2, message2, id_task
	from signalement_workflow_notifuser_3contents_config;

insert into signalement_workflow_notifuser_multi_contents_config(id_message,subject,sender,title,message,id_task)
	select nextval('seq_signalement_workflow_notifuser_multi_contents_config'),subject, sender, title3, message3, id_task
	from signalement_workflow_notifuser_3contents_config;

insert into signalement_workflow_notifuser_multi_contents_task
	select id_task, id_message
	from signalement_workflow_notifuser_multi_contents_config;
	
--- Supprime l'entrée id_task servant juste à remplir la table précédente et delete l'ancienne table 
alter table signalement_workflow_notifuser_multi_contents_config
	drop column id_task;
drop table signalement_workflow_notifuser_3contents_config;

/* Changement du nom de la tâche */ 
update workflow_task
set task_type_key = 'taskSignalementUserNotificationMultiContents'
where task_type_key = 'taskSignalementUserNotification3Contents'

/* DMR-1072 */
CREATE TABLE signalement_requalification (
	id_signalement bigint NOT NULL,
	id_type_signalement bigint,
	adresse character varying(255),
	id_sector bigInt,
	date_requalification timestamp  without time zone;
	CONSTRAINT id_signalement_fk FOREIGN KEY(id_signalement) REFERENCES signalement_signalement(id_signalement) ON DELETE CASCADE
);

/* Mise à jour notify gru */
ALTER TABLE workflow_task_notify_gru_cf ADD COLUMN billing_account_sms VARCHAR(255) DEFAULT NULL;
ALTER TABLE workflow_task_notify_gru_history ADD COLUMN billing_account_sms VARCHAR(255) DEFAULT null;
ALTER TABLE workflow_task_notify_gru_cf ADD COLUMN billing_group_sms VARCHAR(255) DEFAULT NULL;
ALTER TABLE workflow_task_notify_gru_history ADD COLUMN billing_group_sms VARCHAR(255) DEFAULT NULL;



