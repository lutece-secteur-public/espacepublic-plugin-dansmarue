/**********************************************
 * REJETS
 **********************************************/
-- Modification table observation rejet
ALTER TABLE signalement_observation_rejet ALTER COLUMN libelle TYPE text;
ALTER TABLE signalement_observation_rejet DROP COLUMN IF EXISTS ordre;
ALTER TABLE signalement_observation_rejet ADD ordre int;

-- Mise à jour des raisons de rejet
INSERT INTO signalement_observation_rejet(id_observation_rejet,libelle,actif,ordre) VALUES (nextval('seq_signalement_observation_rejet_id_observation_rejet'),'Préciser ou vérifier la localisation',1,1);
INSERT INTO signalement_observation_rejet(id_observation_rejet,libelle,actif,ordre) VALUES (nextval('seq_signalement_observation_rejet_id_observation_rejet'),'Ajouter une ou deux photos (par priorité, une de loin et une de près)',1,2);
INSERT INTO signalement_observation_rejet(id_observation_rejet,libelle,actif,ordre) VALUES (nextval('seq_signalement_observation_rejet_id_observation_rejet'),'Corriger et adapter la qualité de la photo (par priorité, une de loin et une de près)',1,3);
INSERT INTO signalement_observation_rejet(id_observation_rejet,libelle,actif,ordre) VALUES (nextval('seq_signalement_observation_rejet_id_observation_rejet'),'La Mairie de Paris n''a pas la compétence pour intervenir',1,4);
INSERT INTO signalement_observation_rejet(id_observation_rejet,libelle,actif,ordre) VALUES (nextval('seq_signalement_observation_rejet_id_observation_rejet'),'Après avoir mis en sécurité, l''anomalie que vous avez signalée a fait l''objet d''une analyse approfondie par les services compétents. Sa résolution complète nécessite des travaux qui ne peuvent être engagés dans l''immédiat. L''anomalie est cependant enregistrée et fera l''objet d''une surveillance jusqu''à la planficication de son traitement définitif',1,5);
UPDATE signalement_observation_rejet SET ordre = 6 WHERE id_observation_rejet = 1;
UPDATE signalement_observation_rejet SET ordre = 7 WHERE id_observation_rejet = 3;

-- Mise à jour des messages : 
-- Rejet
/*INSERT INTO signalement_workflow_notification_user_config(id_task,subject,sender,title,message)
SELECT task2.id_task,'Signalement d''un incident','Mairie de Paris','Message à destination de l''usager',
'<p>Bonjour,</p>
<p>Nous avons bien re&ccedil;u votre description d''anomalie &laquo; ${type} &raquo; que vous avez signal&eacute;e au &laquo; ${adresse} &raquo;.<br />Mais les informations transmises ne nous permettent pas d''y donner suite pour la ou les raisons suivantes :&nbsp;</p>
<p>${r"${raisons_rejet}"}</p>
<p>Cela nous permettra d''am&eacute;liorer notre r&eacute;activit&eacute; et de vous apporter toute satisfaction dans notre r&eacute;ponse.<br />Nous vous remercions de votre participation.</p>
<p>Les &eacute;quipes de la Mairie de Paris en charge de DansMaVille.</p>
<p>&nbsp;</p>'
 FROM workflow_task task
INNER JOIN workflow_action action ON action.id_action = task.id_action
INNER JOIN workflow_task task2 ON task2.id_action = action.id_action
WHERE id_workflow=2 AND task.task_type_key='taskSignalementRejet' AND task2.task_type_key = 'taskSignalementUserNotification' ;

-- Programmation
INSERT INTO signalement_workflow_notification_user_config(id_task,subject,sender,title,message)
SELECT task2.id_task,'Signalement d''un incident','Mairie de Paris','Message à destination de l''usager',
'<p>Bonjour,</p>
<p>L''anomalie&nbsp;&laquo; ${type} &raquo; que vous avez signal&eacute;e au&nbsp;&laquo; ${adresse} &raquo; a &eacute;t&eacute; prise en compte.</p>
<p>Nous allons mettre en oeuvre sous 1 mois maximum les actions appropri&eacute;es.</p>
<p>&nbsp;</p>
<p>Nous vous remercions de l''int&eacute;r&ecirc;t que vous portez &agrave; l''espace public parisien.</p>
<p>&nbsp;</p>
<p>Les &eacute;quipes de la Mairie de Paris en charge de DansMaVille.</p>'
FROM workflow_task task
INNER JOIN workflow_action action ON action.id_action = task.id_action
INNER JOIN workflow_task task2 ON task2.id_action = action.id_action
WHERE id_workflow=2 AND task.task_type_key='taskSignalementProgrammation'  AND task2.task_type_key = 'taskSignalementUserNotification';

-- Service fait
select * from signalement_workflow_notifuser_3contents_config config
INNER JOIN workflow_task task ON task.id_task = config.id_task
INNER JOIN workflow_action action ON task.id_action = action.id_action
INNER JOIN workflow_task task2 ON task2.id_action = action.id_action
WHERE 
action.id_workflow = 2 AND
task.task_type_key='taskSignalementUserNotification3Contents' AND task2.task_type_key='taskSignalementServiceFait';


-- Update messages
UPDATE signalement_workflow_notifuser_3contents_config config
SET message1=
'<p>Bonjour,
</p>
<p>L’anomalie " ${type}" que vous avez signalée au "${adresse}"&nbsp; a été traitée. Les services municipaux ont opéré les actions correctrices appropriées.
</p>
<p>Votre demande est clôturée.
</p>
<p>Nous vous remercions une nouvelle fois de votre participation qui a permis d’améliorer la qualité de notre environnement urbain.
</p>
<p>Les équipes de la Mairie de Paris en charge de DansMaVille.
</p>',
message2='<p>Bonjour,
</p>
<p>L’anomalie " ${type}" que vous avez signalée au "${adresse}" a fait l’objet d’une analyse approfondie par les services compétents afin d’assurer la sécurité du site en tant que de besoin. Sa résolution complète nécessite des travaux qui ne peuvent être engagés dans l’immédiat. L’anomalie est cependant enregistrée et fera l’objet d’une surveillance jusqu’à la planification de son traitement définitif.
</p>	
<p>Votre demande est clôturée.</p>
<p>Nous vous remercions une nouvelle fois de votre participation qui a permis d’améliorer la qualité de notre environnement urbain.
</p>
<p>Les équipes de la Mairie de Paris en charge de DansMaVille.
</p>',
message3='<p>Bonjour,
</p>
<p>L’anomalie " ${type}" que vous avez signalée au "${adresse}" concerne une adresse parisienne ou une activité en dehors du champ de compétence de la Ville de Paris. Vos remarques ont été transmises par messagerie au service public compétent (CONCESSIONNAIRE).
</p>	
<p>Votre demande est clôturée.</p>
<p>Nous vous remercions une nouvelle fois de votre participation qui a permis d’améliorer la qualité de notre environnement urbain.
</p>
<p>Les équipes de la Mairie de Paris en charge de DansMaVille.
</p>'
WHERE 

config.id_task IN (
SELECT task.id_task FROM workflow_task task
INNER JOIN workflow_action action ON task.id_action = action.id_action
INNER JOIN workflow_task task2 ON task2.id_action = action.id_action
WHERE
action.id_workflow = 2 AND
task.task_type_key='taskSignalementUserNotification3Contents' AND task2.task_type_key='taskSignalementServiceFait');
*/


-- Table de jointure type feuille <-> racine
DROP TABLE IF EXISTS signalement_type_leaf_root;
CREATE TABLE signalement_type_leaf_root(
	id_type_signalement_leaf integer NOT NULL,
	id_type_signalement_root integer NOT NULL,
	alias varchar(255),
	CONSTRAINT signalement_type_leaf_root_pk PRIMARY KEY(id_type_signalement_leaf),
	CONSTRAINT fk_id_signalement_type_root FOREIGN KEY(id_type_signalement_root)
	REFERENCES signalement_type_signalement(id_type_signalement)
);


-- ALIMENTATION DE LA TABLE DE CORRESPONDANCE
CREATE OR REPLACE FUNCTION initSignalementTypeLeafRoot() RETURNS VOID AS 
$$
DECLARE
	r signalement_type_signalement%rowtype;
BEGIN
	TRUNCATE signalement_type_leaf_root;
	FOR r IN SELECT * FROM signalement_type_signalement WHERE (fk_id_unit > 0 AND fk_id_unit is not null)
	LOOP
		WITH RECURSIVE parents(id_type_signalement,fk_id_type_signalement)
		AS (
		SELECT id_type_signalement, fk_id_type_signalement FROM signalement_type_signalement WHERE id_type_signalement = r.id_type_signalement
		UNION ALL
		SELECT type.id_type_signalement, type.fk_id_type_signalement FROM parents p JOIN signalement_type_signalement type ON p.fk_id_type_signalement = type.id_type_signalement
		)
		INSERT INTO signalement_type_leaf_root (id_type_signalement_leaf, id_type_signalement_root) SELECT r.id_type_signalement, id_type_signalement from parents where fk_id_type_signalement is null;
	END LOOP;
	RETURN;
END
$$
LANGUAGE 'plpgsql';

SELECT initSignalementTypeLeafRoot();

/******************************************************
 * DOMAINES
 ******************************************************/
-- CREATION DES TABLES DOMAINE FONCTIONNEL
DROP TABLE IF EXISTS signalement_domaine_arrondissement;
DROP TABLE IF EXISTS signalement_domaine_type_signalement;
DROP TABLE IF EXISTS signalement_domaine_unit;
DROP TABLE IF EXISTS signalement_domaine_fonctionnel;
CREATE TABLE signalement_domaine_fonctionnel(
	id_domaine_fonctionnel integer NOT NULL,
	libelle varchar(255) NOT NULL,
	actif integer NOT NULL,
	CONSTRAINT pk_domaine_fonctionnel PRIMARY KEY (id_domaine_fonctionnel)
);

DROP SEQUENCE IF EXISTS seq_signalement_domaine_fonctionnel;
CREATE SEQUENCE seq_signalement_domaine_fonctionnel; 

-- TABLE INTERMEDIAIRE DOMAINE ARRONDISSEMENT
CREATE TABLE signalement_domaine_arrondissement(
	fk_id_domaine_fonctionnel INTEGER NOT NULL,
	fk_id_arrondissement INTEGER NOT NULL,
	CONSTRAINT pk_domaine_arrondissement PRIMARY KEY(fk_id_domaine_fonctionnel,fk_id_arrondissement),
	CONSTRAINT fk_domaine FOREIGN KEY (fk_id_domaine_fonctionnel) REFERENCES signalement_domaine_fonctionnel(id_domaine_fonctionnel),
	CONSTRAINT fk_arrondissement FOREIGN KEY (fk_id_arrondissement) REFERENCES signalement_arrondissement(id_arrondissement)
);

-- TABLE INTERMEDIAIRE DOMAINE TYPE SIGNALEMENT
CREATE TABLE signalement_domaine_type_signalement(
	fk_id_domaine_fonctionnel INTEGER NOT NULL,
	fk_id_type_signalement INTEGER NOT NULL,
	CONSTRAINT pk_domaine_type_signalement PRIMARY KEY(fk_id_domaine_fonctionnel,fk_id_type_signalement),
	CONSTRAINT fk_domaine FOREIGN KEY (fk_id_domaine_fonctionnel) REFERENCES signalement_domaine_fonctionnel(id_domaine_fonctionnel),
	CONSTRAINT fk_type_signalement FOREIGN KEY (fk_id_type_signalement) REFERENCES signalement_type_signalement(id_type_signalement)
);

-- TABLE INTERMEDIAIRE DOMAINE UNIT
CREATE TABLE signalement_domaine_unit(
	fk_id_domaine_fonctionnel INTEGER NOT NULL,
	fk_id_unit INTEGER NOT NULL,
	CONSTRAINT pk_domaine_type_unit PRIMARY KEY(fk_id_domaine_fonctionnel,fk_id_unit),
	CONSTRAINT fk_domaine FOREIGN KEY (fk_id_domaine_fonctionnel) REFERENCES signalement_domaine_fonctionnel(id_domaine_fonctionnel),
	CONSTRAINT fk_id_unit FOREIGN KEY (fk_id_unit) REFERENCES unittree_unit(id_unit)
);


/********************************************************
* CONSULTATION DES SIGNALEMENTS
*********************************************************/
DELETE FROM core_admin_right WHERE id_right = 'SIGNALEMENT_DISPLAY';
INSERT INTO core_admin_right (id_right,name,level_right,admin_url,description,is_updatable,plugin_name,id_feature_group,icon_url,documentation_url,id_order)
VALUES ('SIGNALEMENT_DISPLAY','dansmarue.adminFeature.signalementdisplay.name','2','jsp/admin/plugins/signalement/DisplaySignalement.jsp','dansmarue.adminFeature.signalementdisplay.description',0,'signalement','SIGNALEMENT',null,null,4);