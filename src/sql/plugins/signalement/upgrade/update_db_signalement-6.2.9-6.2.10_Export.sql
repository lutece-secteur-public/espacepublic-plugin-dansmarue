-- Suppression
DROP TRIGGER IF EXISTS exp_aft_upd_sign ON signalement_signalement;
DROP TRIGGER IF EXISTS exp_aft_ins_sign ON signalement_signalement;
DROP FUNCTION IF EXISTS test_exp_ins_sign();

DROP TRIGGER IF EXISTS exp_aft_upd_adr ON signalement_adresse;
DROP TRIGGER IF EXISTS exp_aft_ins_adr ON signalement_adresse;
DROP FUNCTION IF EXISTS test_exp_ins_adr();

DROP TRIGGER IF EXISTS exp_aft_upd_wkf ON workflow_resource_workflow;
DROP TRIGGER IF EXISTS exp_aft_ins_wkf ON workflow_resource_workflow;
DROP FUNCTION IF EXISTS test_exp_ins_wkf ();

DROP TABLE IF EXISTS signalement_export ;

--
-- Création de la table d'export
--
create table signalement_export (
id_signalement integer UNIQUE,
id_type_signalement integer,
id_direction integer,
id_sector integer,
id_wkf_state integer,
id_arrondissement integer,
id_quartier integer,
numero varchar(15),
priorite varchar(50),
type_signalement text,
alias varchar(255),
alias_mobile varchar (255),
direction varchar(255),
quartier varchar(255),
adresse varchar(255),
coord_x decimal,
coord_y decimal,
arrondissement varchar (255),
secteur varchar (255),
date_creation varchar (10),
heure_creation varchar (5),
etat varchar (255),
mail_usager varchar(255) default '',
commentaire_usager varchar (255) default '',
nb_photos integer,
raisons_rejet text,
nb_suivis integer,
nb_felicitations integer,
date_cloture varchar (10),
is_photo_service_fait integer,
mail_destinataire_courriel varchar (255) default '',
courriel_expediteur varchar (255) default '',
date_envoi_courriel varchar (10) default '',
id_mail_service_fait integer,
executeur_service_fait varchar (255),
date_derniere_action varchar(10) default '',
date_prevu_traitement varchar(10) default '',
commentaire_agent_terrain varchar default ''


);


--
-- Alimente les champs liés à la table signalement
--
CREATE OR REPLACE FUNCTION test_exp_ins_sign()
  RETURNS trigger AS
$$
declare
 Newnumero varchar(15) := concat(new.prefix,new.annee,new.mois,new.numero);
 Newpriorite varchar(50);
 TypeAno text;
 TypeAnoTemp varchar(255);
 Fk_type_parent integer;
 NewTypeSignalementAlias varchar(255);
 NewTypeSignalementAliasMobile varchar(255);
 NewLabelDirection varchar(255);
 NewIdDirection integer;
 NewArrondissement varchar (255);
 NewSecteurAffectation varchar (255);
 NewMailUsager varchar (255);
 NewNombrePhotos integer ;
 NewRaisonRejet text;
 NewPhotoServiceFait integer;
begin

		 -- Priorité
		 select libelle into Newpriorite from signalement_priorite where id_priorite = new.fk_id_priorite;

		 -- Alias et Alias Mobile
		 select alias, alias_mobile into NewTypeSignalementAlias, NewTypeSignalementAliasMobile from signalement_type_signalement_alias where fk_id_type_signalement =  new.fk_id_type_signalement;	     
		 
		 -- Recherche du type d'anomalie (concaténation jusqu'au dernier parent)
		 select libelle, fk_id_type_signalement into TypeAno, Fk_type_parent from signalement_type_signalement where id_type_signalement = new.fk_id_type_signalement;	 		 
		 WHILE Fk_type_parent IS NOT NULL LOOP
			select libelle into TypeAnoTemp from signalement_type_signalement where id_type_signalement = Fk_type_parent;
			TypeAno := concat(TypeAnoTemp, ' > ',  TypeAno);

			select fk_id_type_signalement into Fk_type_parent from signalement_type_signalement where id_type_signalement = Fk_type_parent;
			
		 END LOOP;
		 
		 -- Direction et secteur d'affectation
		 select uus.id_unit, unit.label, sector.name into NewIdDirection, NewLabelDirection, NewSecteurAffectation 
			from unittree_unit_sector as uus 
			inner join unittree_unit as unit on unit.id_unit = uus.id_unit
			inner join unittree_sector as sector on uus.id_sector=sector.id_sector
			where uus.id_sector = new.fk_id_sector and unit.id_parent = 0;
		 
		 -- Arrondissement
		 select numero_arrondissement into NewArrondissement from signalement_arrondissement where id_arrondissement = new.fk_id_arrondissement;
		 
		 -- Mail usager
		 select mail into NewMailUsager from signalement_signaleur where fk_id_signalement = new.id_signalement;
		 
		 -- Photos
		 select count (id_photo) into NewNombrePhotos from signalement_photo where date_photo is not null and fk_id_signalement = new.id_signalement;
		 
		 -- Raison de rejet
		 select array_to_string(array(select libelle from signalement_observation_rejet as sor
		 left join signalement_observation_rejet_signalement as sors on sors.fk_id_observation_rejet = sor.id_observation_rejet
		 where sors.fk_id_signalement=new.id_signalement),', ') into NewRaisonRejet ;
		 
		 -- Photo service fait
		 select (exists (select 1 from signalement_photo where fk_id_signalement=new.id_signalement and vue_photo=2))::int into NewPhotoServiceFait;

         
		 
		if exists (select 1 from signalement_export where id_signalement=new.id_signalement) 
			then 
				update signalement_export set id_type_signalement=new.fk_id_type_signalement, id_direction=NewIdDirection, id_sector=new.fk_id_sector, id_arrondissement = new.fk_id_arrondissement, numero =Newnumero, priorite=Newpriorite, type_signalement=TypeAno, alias=NewTypeSignalementAlias, alias_mobile=NewTypeSignalementAliasMobile, direction=NewLabelDirection, arrondissement=NewArrondissement, secteur=NewSecteurAffectation, date_creation=to_char(new.date_creation,'DD/MM/YYYY'), heure_creation=to_char(new.date_creation,'HH24:MI'),mail_usager=NewMailUsager, commentaire_usager=new.commentaire, nb_photos=NewNombrePhotos, raisons_rejet=NewRaisonRejet, nb_suivis=new.suivi, nb_felicitations=new.felicitations, is_photo_service_fait=NewPhotoServiceFait, mail_destinataire_courriel=new.courriel_destinataire, courriel_expediteur=new.courriel_expediteur, date_envoi_courriel=to_char(new.courriel_date,'DD/MM/YYYY'), date_prevu_traitement = to_char(new.date_prevue_traitement,'DD/MM/YYYY'),commentaire_agent_terrain=new.commentaire_agent_terrain
				where id_signalement=new.id_signalement;
			else
				insert into signalement_export(id_signalement, id_type_signalement, id_direction, id_sector, id_arrondissement, numero, priorite, type_signalement, alias, alias_mobile, direction, arrondissement, secteur, date_creation, heure_creation, mail_usager, commentaire_usager, nb_photos, raisons_rejet, nb_suivis, nb_felicitations, is_photo_service_fait, mail_destinataire_courriel, courriel_expediteur, date_envoi_courriel, date_prevu_traitement, commentaire_agent_terrain)
				values(new.id_signalement,new.fk_id_type_signalement, NewIdDirection, new.fk_id_sector, new.fk_id_arrondissement, Newnumero, Newpriorite, TypeAno, NewTypeSignalementAlias, NewTypeSignalementAliasMobile, NewLabelDirection, NewArrondissement, NewSecteurAffectation, to_char(new.date_creation,'DD/MM/YYYY'), to_char(new.date_creation,'HH24:MI'),NewMailUsager, new.commentaire, NewNombrePhotos, NewRaisonRejet, new.suivi, new.felicitations, NewPhotoServiceFait, new.courriel_destinataire, new.courriel_expediteur, to_char(new.courriel_date,'DD/MM/YYYY'), to_char(new.date_prevue_traitement,'DD/MM/YYYY'), new.commentaire_agent_terrain);
		end if;
		 

		 
 
    RETURN NEW;
END;
$$
LANGUAGE 'plpgsql';

--
-- trigger after insert signalement
--
CREATE TRIGGER exp_aft_ins_sign
  AFTER insert
  ON signalement_signalement
  FOR EACH ROW
  EXECUTE PROCEDURE test_exp_ins_sign();
  
--
-- trigger after update signalement
--
 CREATE TRIGGER exp_aft_upd_sign
  AFTER update
  ON signalement_signalement
  FOR EACH ROW
  EXECUTE PROCEDURE test_exp_ins_sign();


--
--Alimente l'adresse, le quartier et les coordonnés x y
--
 CREATE OR REPLACE FUNCTION test_exp_ins_adr()
  RETURNS trigger AS
$$
declare
 idQuartier integer;
 nomQuartier varchar(255);
begin
	
SELECT id_consqrt, nom_consqrt into idQuartier, nomQuartier
from signalement_conseil_quartier 
where ST_Intersects( ( select ST_Union(new.geom) )::geometry, signalement_conseil_quartier.geom::geometry );

	if exists (select 1 from signalement_export where id_signalement=new.fk_id_signalement)
		then 
			UPDATE signalement_export
			set id_quartier =idQuartier ,quartier = nomQuartier, adresse = new.adresse, coord_x=ST_X(new.geom), coord_y=ST_Y(new.geom)
			where id_signalement = new.fk_id_signalement;
	end if;
			
    RETURN NEW;
END;
$$
LANGUAGE 'plpgsql';



--
-- trigger after insert adresse
--
CREATE TRIGGER exp_aft_ins_adr
  AFTER insert
  ON signalement_adresse
  FOR EACH ROW
  EXECUTE PROCEDURE test_exp_ins_adr();

--
-- trigger after update adresse
--
CREATE TRIGGER exp_aft_upd_adr
  AFTER update
  ON signalement_adresse
  FOR EACH ROW
  EXECUTE PROCEDURE test_exp_ins_adr();
  



--
--Alimente l'état
--
 CREATE OR REPLACE FUNCTION test_exp_ins_wkf()
  RETURNS trigger AS
$$
declare
 NewEtat varchar(255);
 NewIdMailServiceFait integer;
 NewDateCloture varchar (10) ;
 UserAccessCode varchar (255);
 IdLastHistory integer;
 NewExecuteurServiceFait varchar (255) :='';
 UserAdminEmail varchar (255);
 NewDerniereDateMAJ varchar (10);
begin

	-- état
	SELECT ws.name into NewEtat from workflow_state as ws where ws.id_state=new.id_state;

	-- date de cloture
	if new.id_state=10 
		THEN select to_char(service_fait_date_passage, 'DD/MM/YYYY') into NewDateCloture from signalement_signalement where new.id_resource=id_signalement ;
	elseif new.id_state=22 
		THEN select to_char(date_mise_surveillance, 'DD/MM/YYYY') into NewDateCloture  from signalement_signalement where new.id_resource=id_signalement;
	elseif new.id_state=11 
		THEN select to_char(date_rejet, 'DD/MM/YYYY') into NewDateCloture  from signalement_signalement where new.id_resource=id_signalement;
	else NewDateCloture  :='';       
	end if;
	
	-- id mail service fait
	select val.id_message into NewIdMailServiceFait
	from signalement_workflow_notifuser_multi_contents_value as val 
	inner join workflow_resource_history as history on history.id_history = val.id_history 
	where history.id_resource=new.id_resource order by history.id_history desc limit 1;

	-- date de dernière prise en compte
	select id_history, to_char(creation_date, 'DD/MM/YYYY') into IdLastHistory, NewDerniereDateMAJ from workflow_resource_history where id_resource = new.id_resource order by creation_date desc limit 1;
	
	
	-- Exécuteur service fait (code user)
	select user_access_code into UserAccessCode from workflow_resource_history where id_resource = new.id_resource and id_action in (62,70,22,18,49,53,41);
	
	-- Mail exécuteur service fait
	select email into UserAdminEmail from  core_admin_user where access_code=UserAccessCode;
	
	-- Nom exécuteur service fait
	if UserAccessCode ='auto'
		then if (select length(id_telephone) from  signalement_signaleur where fk_id_signalement = new.id_resource) > 0
				then NewExecuteurServiceFait := 'Mobil user';
			 else select mail into  NewExecuteurServiceFait from  signalement_signaleur where fk_id_signalement = new.id_resource;
			 end if;
	elseif length(UserAdminEmail)>0
		then NewExecuteurServiceFait := UserAdminEmail;
		
	end if;
	
	if exists (select 1 from signalement_export where id_signalement=new.id_resource)
		then 
			UPDATE signalement_export
			set id_wkf_state= new.id_state,etat=NewEtat, id_mail_service_fait=NewIdMailServiceFait, date_cloture=NewDateCloture, executeur_service_fait=NewExecuteurServiceFait, date_derniere_action=NewDerniereDateMAJ
			where id_signalement = new.id_resource;
	end if;
			
    RETURN NEW;
END;
$$
LANGUAGE 'plpgsql';



--
-- trigger after insert workflow_resource_workflow
--
CREATE TRIGGER exp_aft_ins_wkf
  AFTER insert
  ON workflow_resource_workflow
  FOR EACH ROW
  EXECUTE PROCEDURE test_exp_ins_wkf();

--
-- trigger after update workflow_resource_workflow
--
CREATE TRIGGER exp_aft_upd_wkf
  AFTER update
  ON workflow_resource_workflow
  FOR EACH ROW
  EXECUTE PROCEDURE test_exp_ins_wkf();