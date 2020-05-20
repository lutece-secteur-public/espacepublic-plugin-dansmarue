-- Requalification Debut
ALTER TABLE signalement_type_signalement ADD COLUMN isAgent int4 NULL;

ALTER TABLE signalement_signalement ADD COLUMN commentaire_agent_terrain varchar NULL;

update workflow_task_comment_config set title='Remarque interne non vu par l''usager' where title='Commentaire interne non vu par l’usager (facultatif)';

INSERT INTO workflow_task (task_type_key, id_action, display_order) VALUES('taskWebServiceComment', 75, null);
INSERT INTO workflow_task (task_type_key, id_action, display_order) VALUES('taskWebServiceComment', 45, null);
INSERT INTO workflow_task (task_type_key, id_action, display_order) VALUES('taskWebServiceComment', 14, null);
INSERT INTO workflow_task (task_type_key, id_action, display_order) VALUES('taskWebServiceComment', 23, null);

-- Requalification Fin

--DMR1227
-- Change field commentaire to description
update signalement_workflow_notification_config 
set message = REPLACE(message,'<strong><br />Commentaire : </strong>${commentaire}','<strong><br />Description : </strong>${description}')

--DMR-1377
--Create new table
CREATE TABLE signalement_message_typologie (
	id_message int8 NOT NULL,
	fk_id_type_signalement int8 NOT NULL,
	type_message varchar(255) NULL,
	contenu_message varchar(255) NULL,
	actif int4 NULL,
	ordre int4 NULL,
	CONSTRAINT signalement_message_typologie_pkey PRIMARY KEY (id_message),
	CONSTRAINT fk_signalement_type_message_typologi FOREIGN KEY (fk_id_type_signalement) REFERENCES public.signalement_type_signalement(id_type_signalement)
)
WITH (
	OIDS=FALSE
) ;

--Création entete et pied de page des messages par typologie--
INSERT INTO core_datastore(entity_key,entity_value) VALUES ('sitelabels.site_property.message.typologie.entete.htmlblock','<p>Bonjour,<br /><br /> L''anomalie ${numero} concernant ${alias_anomalie} au ${adresse} a &eacute;t&eacute; prise en charge.<br /><br />');
INSERT INTO core_datastore(entity_key,entity_value) VALUES ('sitelabels.site_property.message.typologie.pieddepage.htmlblock','<br /> <br /> Les &eacute;quipes de la Ville de Paris en charge de l''application DansMaRue vous remercient de votre participation &agrave; l&rsquo;am&eacute;lioration de la qualit&eacute; de l&rsquo;environnement urbain.</p>');


-- DMR-1439
INSERT INTO core_datastore (entity_key, entity_value) VALUES ( 'core.daemon.suppressionPhotosDaemon.interval', '86400' );
INSERT INTO core_datastore (entity_key, entity_value) VALUES ( 'core.daemon.suppressionPhotosDaemon.onStartUp', false );


--Message de confirmation de suppression des messages de service fait
INSERT INTO core_datastore(entity_key,entity_value) VALUES ('sitelabels.site_property.message.typologie.validation.suppression','La modification de cette typologie va générer la suppression des messages configurés pour cette typologie.');
INSERT INTO core_datastore(entity_key,entity_value) VALUES ('sitelabels.site_property.message.typologie.validation.suppression.ajout','L''ajout d''un sous type va générer la suppression des messages configurés pour la typologie du niveau supérieur.');