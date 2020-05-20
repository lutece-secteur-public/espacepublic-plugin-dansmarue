-- DMR-1637
-- Mise a jour de signalement_export suite à modification de unittree_sector
DROP TRIGGER IF EXISTS exp_aft_upd_unittree_sector ON unittree_sector;
DROP FUNCTION IF EXISTS exp_upd_sector();

--
--Mise a jour du secteur sur signalement_export.
--
 CREATE OR REPLACE FUNCTION exp_upd_sector()
  RETURNS trigger AS
$$
BEGIN
  UPDATE signalement_export SET secteur = new.name WHERE id_sector = new.id_sector;
  RETURN NEW;
END;
$$
LANGUAGE 'plpgsql';

--
-- trigger after update unittree_sector
--
CREATE TRIGGER exp_aft_upd_unittree_sector
  AFTER update
  ON unittree_sector
  FOR EACH ROW
  EXECUTE PROCEDURE exp_upd_sector();



-- Mise a jour signalement_export suite à modification signalement_type_signalement
DROP TRIGGER IF EXISTS exp_aft_upd_signalement_type_signalement ON signalement_type_signalement;
DROP FUNCTION IF EXISTS exp_upd_type_signalement();

--
--Mise a jour du secteur sur signalement_export.
--
 CREATE OR REPLACE FUNCTION exp_upd_type_signalement()
  RETURNS trigger AS
$$
declare
 TypeAno text;
 Fk_type_parent integer;
 TypeAnoTemp varchar(255);
BEGIN
  -- Recherche du type d'anomalie (concaténation jusqu'au dernier parent)
	select libelle, fk_id_type_signalement into TypeAno, Fk_type_parent from signalement_type_signalement where id_type_signalement = new.id_type_signalement;
	WHILE Fk_type_parent IS NOT NULL LOOP
		select libelle into TypeAnoTemp from signalement_type_signalement where id_type_signalement = Fk_type_parent;
		TypeAno := concat(TypeAnoTemp, ' > ',  TypeAno);
		select fk_id_type_signalement into Fk_type_parent from signalement_type_signalement where id_type_signalement = Fk_type_parent;
	END LOOP;

  UPDATE signalement_export SET type_signalement = TypeAno WHERE id_type_signalement = new.id_type_signalement;
  RETURN NEW;
END;
$$
LANGUAGE 'plpgsql';

--
-- trigger after update signalement_type_signalement
--
CREATE TRIGGER exp_aft_upd_signalement_type_signalement
  AFTER update
  ON signalement_type_signalement
  FOR EACH ROW
  EXECUTE PROCEDURE exp_upd_type_signalement();
 
 
 
-- Mise a jour de signalement_export suite à modification de signalement_type_signalement_alias
DROP TRIGGER IF EXISTS exp_aft_upd_type_signalement_alias ON signalement_type_signalement_alias;
DROP FUNCTION IF EXISTS exp_upd_alias();

--
--Mise a jour du secteur sur signalement_export.
--
 CREATE OR REPLACE FUNCTION exp_upd_alias()
  RETURNS trigger AS
$$
BEGIN
  UPDATE signalement_export SET alias = new.alias, alias_mobile = new.alias_mobile WHERE id_type_signalement = new.fk_id_type_signalement;
  RETURN NEW;
END;
$$
LANGUAGE 'plpgsql';

--
-- trigger after insert type_signalement_alias
--
CREATE TRIGGER exp_aft_upd_type_signalement_alias
  AFTER insert
  ON signalement_type_signalement_alias
  FOR EACH ROW
  EXECUTE PROCEDURE exp_upd_alias();



-- Mise a jour de signalement_export suite à modification de signalement_conseil_quartier
DROP TRIGGER IF EXISTS exp_aft_upd_conseil_quartier ON signalement_conseil_quartier;
DROP FUNCTION IF EXISTS exp_upd_conseil_quartier();

--
--Mise a jour du secteur sur signalement_export.
--
 CREATE OR REPLACE FUNCTION exp_upd_conseil_quartier()
  RETURNS trigger AS
$$
BEGIN
  UPDATE signalement_export SET quartier = new.nom_consqrt WHERE id_quartier = new.id_consqrt;
  RETURN NEW;
END;
$$
LANGUAGE 'plpgsql';

--
-- trigger after update type_signalement_alias
--
CREATE TRIGGER exp_aft_upd_conseil_quartier
  AFTER update
  ON signalement_conseil_quartier
  FOR EACH ROW
  EXECUTE PROCEDURE exp_upd_conseil_quartier();

  
--
-- Création table signalement_type_signalement_source
--
CREATE TABLE public.signalement_type_signalement_source (
	fk_id_type_signalement int8 NOT NULL,
	id_source int8 NOT NULL,
	CONSTRAINT signalement_type_signalement_source_pk PRIMARY KEY (fk_id_type_signalement, id_source),
	CONSTRAINT fk_signalement_type_source FOREIGN KEY (fk_id_type_signalement) REFERENCES public.signalement_type_signalement(id_type_signalement)
)
WITH (
	OIDS=FALSE
) ;
  
  
  
  
  