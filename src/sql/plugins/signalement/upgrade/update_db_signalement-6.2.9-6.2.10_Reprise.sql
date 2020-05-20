DROP FUNCTION IF EXISTS getNbPhotos(idSignalement bigint);
DROP FUNCTION IF EXISTS getTypeSignalement(idTypeSignalement bigint);
DROP FUNCTION IF EXISTS getQuartier (geomAdr geometry) ;
DROP FUNCTION IF EXISTS getRaisonRejet(idSignalement bigint);
DROP FUNCTION IF EXISTS getDateCloture(idState integer, idResource integer);
DROP FUNCTION IF EXISTS getExecuteurServiceFait(idResource integer);
DROP FUNCTION IF EXISTS getIdMailServiceFait(idResource integer);
DROP FUNCTION IF EXISTS getDateDerniereAction (idResource integer);


-- GET Nombre de photos
CREATE FUNCTION getNbPhotos(idSignalement bigint) RETURNS integer AS $$
DECLARE
     NewNombrePhotos integer ;
BEGIN

    select count (id_photo) into NewNombrePhotos from signalement_photo where date_photo is not null and fk_id_signalement = idSignalement;
	RETURN NewNombrePhotos;
END;
$$ LANGUAGE plpgsql;



-- GET TYPE ANOMALIE
CREATE FUNCTION getTypeSignalement(idTypeSignalement bigint) RETURNS text AS $$
DECLARE
	TypeAno text;
	TypeAnoTemp text;
BEGIN
	 -- Recherche du type d'anomalie (concaténation jusqu'au dernier parent) 
	 WHILE idTypeSignalement IS NOT NULL LOOP
		select libelle into TypeAnoTemp from signalement_type_signalement where id_type_signalement = idTypeSignalement;
		TypeAno := concat(TypeAnoTemp, ' > ',  TypeAno);
		select fk_id_type_signalement into idTypeSignalement from signalement_type_signalement where id_type_signalement = idTypeSignalement;
			
	 END LOOP;
	 RETURN TypeAno;
END;
$$ LANGUAGE plpgsql;

-- Get quartier
CREATE OR REPLACE FUNCTION getQuartier (geomAdr geometry) 
   RETURNS TABLE (
      idConseil INTEGER,
      nomQuartier VARCHAR
) 
AS $$
BEGIN
   RETURN QUERY SELECT id_consqrt, nom_consqrt 
		from signalement_conseil_quartier 
		where ST_Intersects( ( select ST_Union(geomAdr) )::geometry, signalement_conseil_quartier.geom::geometry );
END; $$ 
 
LANGUAGE 'plpgsql';


-- Get Raisons de rejet
-- Raison de rejet
CREATE FUNCTION getRaisonRejet(idSignalement bigint) RETURNS text AS $$
 DECLARE
	NewRaisonRejet text;
 BEGIN
		select array_to_string(array(select libelle from signalement_observation_rejet as sor
		left join signalement_observation_rejet_signalement as sors on sors.fk_id_observation_rejet = sor.id_observation_rejet
		where sors.fk_id_signalement=idSignalement),', ') into NewRaisonRejet ;
	RETURN NewRaisonRejet ;
END;
$$ LANGUAGE plpgsql;

-- Get date de cloture
CREATE FUNCTION getDateCloture(idState integer, idResource integer) RETURNS varchar AS $$
 DECLARE
	NewDateCloture varchar (10);
 BEGIN
	if idState=10 
		THEN select to_char(service_fait_date_passage, 'DD/MM/YYYY') into NewDateCloture from signalement_signalement where id_signalement=idResource ;
	elseif idState=22 
		THEN select to_char(date_mise_surveillance, 'DD/MM/YYYY') into NewDateCloture  from signalement_signalement where id_signalement=idResource;
	elseif idState=11 
		THEN select to_char(date_rejet, 'DD/MM/YYYY') into NewDateCloture  from signalement_signalement where id_signalement=idResource;
	else NewDateCloture  :='';       
	end if;
	RETURN NewDateCloture;
END;
$$ LANGUAGE plpgsql;	

-- Date dernière action
CREATE FUNCTION getDateDerniereAction (idResource integer) RETURNS varchar AS $$
 DECLARE
	 NewDerniereDateMAJ varchar (10);
 BEGIN
	select to_char(creation_date, 'DD/MM/YYYY') into NewDerniereDateMAJ 
	from workflow_resource_history where id_resource = idResource order by creation_date desc limit 1;
	RETURN NewDerniereDateMAJ;
 END;
 $$ LANGUAGE plpgsql;

-- Id Mail service fait
CREATE FUNCTION getIdMailServiceFait(idResource integer) RETURNS integer AS $$
 DECLARE
	NewIdMailServiceFait integer;
  BEGIN
	-- id mail service fait
	select val.id_message into NewIdMailServiceFait
	from signalement_workflow_notifuser_multi_contents_value as val 
	inner join workflow_resource_history as history on history.id_history = val.id_history 
	where history.id_resource=idResource order by history.id_history desc limit 1;
	
	RETURN NewIdMailServiceFait;
END;
$$ LANGUAGE plpgsql;	

-- Executeur service fait
CREATE FUNCTION getExecuteurServiceFait(idResource integer) RETURNS varchar AS $$
 DECLARE
	NewExecuteurServiceFait varchar (255) :='';
	UserAdminEmail varchar (255);
	UserAccessCode varchar (255);
 BEGIN
 	-- Exécuteur service fait (code user)
	select user_access_code into UserAccessCode from workflow_resource_history where id_resource = idResource and id_action in (62,70,22,18,49,53,41);
	
	-- Mail exécuteur service fait
	select email into UserAdminEmail from  core_admin_user where access_code=UserAccessCode;
	
	-- Nom exécuteur service fait
	if UserAccessCode ='auto'
		then if (select length(id_telephone) from  signalement_signaleur where fk_id_signalement = idResource) > 0
				then NewExecuteurServiceFait := 'Mobil user';
			 else select mail into  NewExecuteurServiceFait from  signalement_signaleur where fk_id_signalement = idResource;
			 end if;
	elseif length(UserAdminEmail)>0
		then NewExecuteurServiceFait := UserAdminEmail;
		
	end if;
	RETURN NewExecuteurServiceFait;
END;
$$ LANGUAGE plpgsql;	

INSERT INTO signalement_export (
	id_signalement, 
	id_type_signalement, 
	id_direction,
	id_sector,
	id_wkf_state,
	id_arrondissement, 
	id_quartier,
	numero, 
	priorite, 
	type_signalement,
	alias, 
	alias_mobile,
	direction,
	quartier,
	adresse,
	coord_x,
	coord_y,
	arrondissement,
	secteur,
	date_creation,
	heure_creation,
	etat,
	mail_usager,
	commentaire_usager,
	nb_photos,
	raisons_rejet,
	nb_suivis,
	nb_felicitations,
	date_cloture,
	is_photo_service_fait,
	mail_destinataire_courriel,
	courriel_expediteur,
	date_envoi_courriel,
	id_mail_service_fait,
	executeur_service_fait,
	date_derniere_action,
	date_prevu_traitement,
	commentaire_agent_terrain
	)
	

(SELECT DISTINCT
	signalement.id_signalement, 
	signalement.fk_id_type_signalement, 
	uus.id_unit,
	signalement.fk_id_sector,
	wrw.id_state,
	signalement.fk_id_arrondissement, 
	(select idConseil from getQuartier (adr.geom)),
	concat(signalement.prefix,signalement.annee,signalement.mois,signalement.numero), 
	prio.libelle,
	getTypeSignalement(signalement.fk_id_type_signalement),
	al.alias, 
	al.alias_mobile,
	unit.label,
	(select nomQuartier from getQuartier (adr.geom)),
	adr.adresse,
	ST_X(adr.geom),
	ST_Y(adr.geom),	
	arrdt.numero_arrondissement,
	sector.name,
	to_char(signalement.date_creation,'DD/MM/YYYY'),
	to_char(signalement.date_creation,'HH24:MI'),
	ws.name,
	s.mail,
	signalement.commentaire,
	getNbPhotos (signalement.id_signalement),
	getRaisonRejet (signalement.id_signalement),
	signalement.suivi,
	signalement.felicitations,
	getDateCloture(wrw.id_state, wrw.id_resource),
	(exists (select 1 from signalement_photo where fk_id_signalement=signalement.id_signalement and vue_photo=2))::int,
	signalement.courriel_destinataire,
	signalement.courriel_expediteur,
	to_char(signalement.courriel_date,'DD/MM/YYYY'),
	getIdMailServiceFait(wrw.id_resource),
	getExecuteurServiceFait(wrw.id_resource),
	getDateDerniereAction(wrw.id_resource),
	to_char(signalement.date_prevue_traitement,'DD/MM/YYYY'),
	signalement.commentaire_agent_terrain

FROM signalement_signalement AS signalement
-- Priorité
INNER JOIN signalement_priorite  AS prio ON prio.id_priorite = signalement.fk_id_priorite
-- Alias
INNER JOIN signalement_type_signalement_alias AS al ON al.fk_id_type_signalement =  signalement.fk_id_type_signalement
-- Tables unittree pour la recherche secteur et direction
INNER JOIN unittree_unit_sector as uus on uus.id_sector = signalement.fk_id_sector 
INNER JOIN unittree_unit as unit on unit.id_unit = uus.id_unit and unit.id_parent = 0
INNER JOIN unittree_sector as sector on uus.id_sector=sector.id_sector
-- Arrondissement
INNER JOIN signalement_arrondissement as arrdt ON arrdt.id_arrondissement = signalement.fk_id_arrondissement
-- Mail usager
LEFT OUTER JOIN signalement_signaleur AS s ON s.fk_id_signalement = signalement.id_signalement
-- Adresse
INNER JOIN signalement_adresse AS adr ON adr.fk_id_signalement = signalement.id_signalement
-- Workflow
INNER JOIN workflow_resource_workflow AS wrw ON wrw.id_resource = signalement.id_signalement
-- Workflow state
INNER JOIN workflow_state as ws on ws.id_state=wrw.id_state
-- Nombre de photos
LEFT OUTER JOIN signalement_photo AS photo on photo.fk_id_signalement = signalement.id_signalement and photo.date_photo is not null 

WHERE date_trunc('day', signalement.date_creation) >= '2018-01-01 +02:00:00');




