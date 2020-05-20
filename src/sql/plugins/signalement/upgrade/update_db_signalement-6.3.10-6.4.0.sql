--DMR-1833: ajout du lien vers la vidéo dans les sitelabels
INSERT INTO core_datastore(entity_key,entity_value) VALUES ('sitelabels.site_property.video.guide.user.link', 'https://google.fr');

--
-- Création table signalement_source
--
CREATE TABLE signalement_source (
	id_source SERIAL NOT NULL,
	libelle varchar(255) NULL,
	CONSTRAINT id_source PRIMARY KEY (id_source,libelle)
)
WITH (
	OIDS=FALSE
) ;

--Insertion de la source chantier
INSERT INTO signalement_source
(libelle)
VALUES('Chantier');


-- Création de la table signalement_workflow_entite_value
DROP TABLE IF EXISTS signalement_workflow_entite_value;
CREATE TABLE signalement_workflow_entite_value (
	id_history int4 NOT NULL DEFAULT 0,
	id_task int4 NOT NULL,
	entite text NULL,
	CONSTRAINT signalement_workflow_entite_value_pk PRIMARY KEY (id_history,id_task),
	CONSTRAINT signalement_workflow_entite_value_history_fk FOREIGN KEY (id_history) REFERENCES workflow_resource_history(id_history),
	CONSTRAINT signalement_workflow_entite_value_task_fk FOREIGN KEY (id_task) REFERENCES workflow_task(id_task)
)
WITH (
	OIDS=FALSE
) ;
