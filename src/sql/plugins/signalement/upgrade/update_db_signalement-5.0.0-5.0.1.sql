-- Fonction pour alimentation de la vue avec les hiérarchies par type de signalement
/*CREATE OR REPLACE FUNCTION signalementGetTypeSignalementHierarchy(OUT id_type_signalement bigint, OUT id_parent bigint) RETURNS SETOF record AS 
$$
DECLARE
	r signalement_type_signalement%rowtype;
BEGIN
	FOR r IN SELECT * FROM signalement_type_signalement WHERE (fk_id_unit > 0 AND fk_id_unit is not null)
	LOOP
		RETURN QUERY WITH RECURSIVE parents(id_type_signalement,fk_id_type_signalement)
		AS (
		SELECT sts.id_type_signalement, sts.fk_id_type_signalement FROM signalement_type_signalement sts WHERE sts.id_type_signalement = r.id_type_signalement
		UNION ALL
		SELECT type.id_type_signalement, type.fk_id_type_signalement FROM parents p JOIN signalement_type_signalement type ON p.fk_id_type_signalement = type.id_type_signalement
		)
		SELECT r.id_type_signalement AS id_type_sig, p.id_type_signalement as id_ancestor from parents p;
	END LOOP;
	RETURN;
END
$$
LANGUAGE 'plpgsql';*/


DROP MATERIALIZED VIEW IF EXISTS v_signalement_type_signalement_actifs_with_parents_links;

CREATE OR REPLACE FUNCTION signalementGetTypeSignalementHierarchy(OUT id_type_signalement bigint, OUT id_parent bigint, OUT is_parent_a_category integer, OUT actif integer) RETURNS SETOF record AS 
$$
DECLARE
	r signalement_type_signalement%rowtype;
BEGIN
	FOR r IN SELECT * FROM signalement_type_signalement WHERE (fk_id_unit > 0 AND fk_id_unit is not null)
	LOOP
		RETURN QUERY WITH RECURSIVE parents(id_type_signalement,fk_id_type_signalement)
		AS (
		SELECT sts.id_type_signalement, sts.fk_id_type_signalement FROM signalement_type_signalement sts WHERE sts.id_type_signalement = r.id_type_signalement
		UNION ALL
		SELECT type.id_type_signalement, type.fk_id_type_signalement FROM parents p JOIN signalement_type_signalement type ON p.fk_id_type_signalement = type.id_type_signalement
		)
		SELECT r.id_type_signalement AS id_type_sig, p.id_type_signalement as id_ancestor, r.actif,
		case when (p.fk_id_type_signalement is null) then 1 else 0 end as is_parent_a_category from parents p;
	END LOOP;
	RETURN;
END
$$
LANGUAGE 'plpgsql';

-- Création de la table des alias
DROP TABLE IF EXISTS signalement_type_signalement_alias;
CREATE TABLE signalement_type_signalement_alias(
	fk_id_type_signalement bigint NOT NULL,
	alias varchar(255),
	CONSTRAINT signalement_type_signalement_alias_pk PRIMARY KEY(fk_id_type_signalement),
	CONSTRAINT fk_signalement_type_alias FOREIGN KEY(fk_id_type_signalement)
	REFERENCES signalement_type_signalement(id_type_signalement)
);

-- Création de la vue matérialisée
/*CREATE MATERIALIZED VIEW v_signalement_type_signalement_actifs_with_parents_links AS
SELECT liens_signalement.id_type_signalement as id_type_signalement, liens_signalement.id_parent as id_parent FROM signalementGetTypeSignalementHierarchy() liens_signalement;*/

CREATE MATERIALIZED VIEW v_signalement_type_signalement_actifs_with_parents_links AS
SELECT liens_signalement.id_type_signalement as id_type_signalement, liens_signalement.id_parent as id_parent, is_parent_a_category FROM signalementGetTypeSignalementHierarchy() liens_signalement
WHERE actif=1;

-- Refresh avec REFRESH MATERIALIZED VIEW v_signalement_type_signalement_actifs_with_parents_links

ALTER TABLE signalement_type_leaf_root DROP COLUMN IF EXISTS alias;

-- Gestion des félicitations
ALTER TABLE signalement_signalement DROP COLUMN IF EXISTS felicitations;
ALTER TABLE signalement_signalement ADD COLUMN felicitations integer DEFAULT 0;


-- Gestion du suivi
DROP TABLE IF EXISTS signalement_suivi;
CREATE TABLE signalement_suivi(
	id_signalement_suivi SERIAL,
	fk_id_signalement bigint NOT NULL,
	id_telephone varchar(50),
	guid varchar(255),
	mail varchar(255),
	CONSTRAINT pk_signalement_suivi PRIMARY KEY (id_signalement_suivi),
	CONSTRAINT u_suivi_mail UNIQUE (fk_id_signalement,mail),
	CONSTRAINT u_suivi_mobile UNIQUE (fk_id_signalement,id_telephone),
	CONSTRAINT u_suivi_guid UNIQUE (fk_id_signalement,guid),
	CONSTRAINT fk_signalement_suivi FOREIGN KEY (fk_id_signalement) REFERENCES signalement_signalement(id_signalement)
);


-- Ajout du gru
ALTER TABLE signalement_signaleur DROP COLUMN IF EXISTS id_gru;
ALTER TABLE signalement_signaleur ADD COLUMN id_gru varchar(255);