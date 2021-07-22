--Création de la table de configuration de la tache 
CREATE TABLE signalement_workflow_notification_service_programme_config (
	id_task int4 NOT NULL,
	subject varchar(255) NULL,
	message text NULL,
	sender varchar(255) NULL,
	CONSTRAINT signalement_workflow_notification_service_programme_config_pkey PRIMARY KEY (id_task)
)
WITH (
	OIDS=FALSE
) ;


--Ajout de l'action
INSERT INTO workflow_action
(id_action, "name", description, id_workflow, id_state_before, id_state_after, id_icon, is_automatic, is_mass_action, display_order, is_automatic_reflexive_action)
VALUES(93, 'Notification abonnés service programmé', 'Notification des abonnés aux services programmés', 2, 9, 9, 15, 0, 0, 0, 0);


--Incrémentation de la séquence
ALTER SEQUENCE seq_workflow_action RESTART WITH 93;



------
-- DMR-1930 : ajout des champs "Rejeté par" et "Sous Surveillance par" dans l'export
------
ALTER TABLE signalement_export ADD COLUMN executeur_rejet varchar(255) NULL;
ALTER TABLE signalement_export ADD COLUMN executeur_mise_surveillance varchar(255) NULL;

--Modification de la procédure existante
 CREATE OR REPLACE FUNCTION test_exp_ins_wkf_res_hist()
  RETURNS trigger AS
$$
declare
  NewExecuteurServiceFait varchar (255) :='';
 UserAdminEmail varchar (255);
 NewExecuteurRejet varchar (255) :='';
 NewExecuteurMiseEnSurveillance varchar (255) :='';
begin
	
	if new.id_action in (62,70,22,18,49,53,41)
		then
		    -- Mail exécuteur service fait
			select email into UserAdminEmail from  core_admin_user where access_code=new.user_access_code;
			
			if new.user_access_code='auto'
				then 
					if (((select length(id_telephone) from  signalement_signaleur where fk_id_signalement = new.id_resource) > 0) and ( (select length(mail) from  signalement_signaleur where fk_id_signalement = new.id_resource) = 0))
						then NewExecuteurServiceFait := 'Mobil user';
					else 
						select mail into  NewExecuteurServiceFait from  signalement_signaleur where fk_id_signalement = new.id_resource;
					end if;
			elseif length(UserAdminEmail)>0
				then NewExecuteurServiceFait := UserAdminEmail;
			elseif new.user_access_code LIKE '%@%'
				then NewExecuteurServiceFait := new.user_access_code;
			end if;
			
			if exists (select 1 from signalement_export where id_signalement=new.id_resource)
				then 
					UPDATE signalement_export
					set executeur_service_fait=NewExecuteurServiceFait
					where id_signalement = new.id_resource;
			end if;
	elseif new.id_action in (16,21,64,71)
		then
			-- Mail exécuteur rejet
			select email into NewExecuteurRejet from  core_admin_user where access_code=new.user_access_code;
			
			if new.user_access_code='auto'
				then NewExecuteurRejet := 'auto';
			end if;
			
			if exists (select 1 from signalement_export where id_signalement=new.id_resource)
				then 
					UPDATE signalement_export
					set executeur_rejet=NewExecuteurRejet
					where id_signalement = new.id_resource;
			end if;
	elseif new.id_action in (76,80,81,85,86,87)
		then
			-- Mail exécuteur mise en surveillance
			select email into NewExecuteurMiseEnSurveillance from  core_admin_user where access_code=new.user_access_code;
			
			if new.user_access_code='auto'
				then NewExecuteurMiseEnSurveillance := 'auto';
			end if;
			
			if exists (select 1 from signalement_export where id_signalement=new.id_resource)
				then 
					UPDATE signalement_export
					set executeur_mise_surveillance=NewExecuteurMiseEnSurveillance
					where id_signalement = new.id_resource;
			end if;
	end if;

    RETURN NEW;
END;
$$
LANGUAGE 'plpgsql';

--Ajout du trigger lors de l'insert
create trigger exp_aft_ins_wkf_res_hist after insert
on workflow_resource_history for each row execute procedure test_exp_ins_wkf_res_hist();

--Rattrapage des données:
	--Sous surveillance
update signalement_export export
set executeur_mise_surveillance=
(select email from (select distinct(adminUser.email),id_history
from  core_admin_user adminUser 
join workflow_resource_history history on history.user_access_code = adminUser.access_code
where history.id_action in (76,80,81,85,86,87)
and history.id_resource = export.id_signalement
order by id_history desc limit 1) as query )
where (EXTRACT( year from TO_TIMESTAMP(export.date_creation,'DD/MM/YYYY')) = 2020 
or EXTRACT( year from TO_TIMESTAMP(export.date_creation,'DD/MM/YYYY')) = 2019) and export.etat='Sous surveillance';
	--Rejeté
update signalement_export export
set executeur_rejet=
(select email from (select distinct(adminUser.email),id_history
from  core_admin_user adminUser 
join workflow_resource_history history on history.user_access_code = adminUser.access_code
where history.id_action in (16,21,64,71)
and history.id_resource = export.id_signalement
order by id_history desc limit 1) as query )
where (EXTRACT( year from TO_TIMESTAMP(export.date_creation,'DD/MM/YYYY')) = 2020 
or EXTRACT( year from TO_TIMESTAMP(export.date_creation,'DD/MM/YYYY')) = 2019) and export.etat='Rejeté';

--DMR-1933 - Ajout paramètre date de recherche
INSERT INTO core_datastore (entity_key, entity_value) VALUES ('sitelabels.site_property.mobile.nb.jour.recherche.ano.max', '90');

-- DMR-1937 Message d'information
INSERT INTO core_datastore (entity_key, entity_value) VALUES ('sitelabels.site_property.signalement.mobile.message.information', '');

-- DMR-1929 - Ajout colonne compteur requalification
ALTER TABLE signalement_export ADD COLUMN nb_requalifications int4 DEFAULT NULL;
DROP TRIGGER IF EXISTS exp_aft_ins_sign_requalification ON signalement_requalification;
--
--Alimente colonne Nombre de requalification
--
 CREATE OR REPLACE FUNCTION test_exp_ins_sign_requalification()
  RETURNS trigger AS
$$
begin

	if exists (select 1 from signalement_export where id_signalement=new.id_signalement) 
		then
		if (select nb_requalifications from signalement_export where id_signalement = new.id_signalement) IS NULL
			then
				update signalement_export set nb_requalifications = 1 where id_signalement=new.id_signalement;
			else
				update signalement_export set nb_requalifications = nb_requalifications +1 where id_signalement=new.id_signalement;
		end if;
	end if;
	
	RETURN NEW;
END;
$$
LANGUAGE 'plpgsql';
--
-- trigger after insert signalement_requalification
--
CREATE TRIGGER exp_aft_ins_sign_requalification
  AFTER insert
  ON signalement_requalification
  FOR EACH ROW
  EXECUTE PROCEDURE test_exp_ins_sign_requalification();
-- reprise 2020
update signalement_export set nb_requalifications = requalification.nbRequalification
  from (
	select  sr.id_signalement , count(id_signalement ) as nbRequalification from signalement_requalification sr 
    group by sr.id_signalement 
  ) as requalification
  where signalement_export.id_signalement = requalification.id_signalement
  and EXTRACT( year from TO_TIMESTAMP(signalement_export.date_creation,'DD/MM/YYYY')) = 2020;