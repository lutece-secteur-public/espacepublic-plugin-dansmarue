--MAJ de la fonction
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
		where sors.fk_id_signalement=new.id_signalement) ||
		(select array_agg(observation_rejet_comment) from signalement_observation_rejet_signalement 
		where fk_id_signalement=new.id_signalement and observation_rejet_comment is not null),', ') into NewRaisonRejet ;
		 
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