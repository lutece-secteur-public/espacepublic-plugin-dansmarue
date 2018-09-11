DROP MATERIALIZED VIEW IF EXISTS v_signalement_type_signalement_actifs_with_parents_links;
DROP MATERIALIZED VIEW IF EXISTS v_signalement_type_signalement_with_parents_links;

DROP FUNCTION IF EXISTS signalementGetTypeSignalementHierarchy(OUT id_type_signalement bigint, OUT id_parent bigint, OUT is_parent_a_category integer, OUT actif integer);

CREATE OR REPLACE FUNCTION signalementGetTypeSignalementHierarchy(OUT id_type_signalement bigint, OUT id_parent bigint, OUT actif integer, OUT is_parent_a_category integer) RETURNS SETOF record AS 
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

-- Création de la vue matérialisée

CREATE MATERIALIZED VIEW v_signalement_type_signalement_with_parents_links AS
SELECT liens_signalement.id_type_signalement as id_type_signalement, liens_signalement.id_parent as id_parent, is_parent_a_category, actif  FROM signalementGetTypeSignalementHierarchy() liens_signalement;




-- WORKFLOW
-- Archivé
UPDATE workflow_state SET display_order = 13 WHERE id_state = 12;
-- A traiter
UPDATE workflow_state SET display_order = 14 WHERE id_state = 19;
-- A faire bureau
UPDATE workflow_state SET display_order = 15 WHERE id_state = 20;

-- OBSERVATION DE REJET
DROP TABLE  IF EXISTS signalement_observation_rejet_signalement;
CREATE TABLE signalement_observation_rejet_signalement(
	  id_observation_rejet_signalement bigint NOT NULL,
	  fk_id_signalement bigint,
	  fk_id_observation_rejet bigint,
	  observation_rejet_comment  text,
	  CONSTRAINT observation_rejet_signalement_pk PRIMARY KEY (id_observation_rejet_signalement),
	  CONSTRAINT observation_rejet_signalement_fk FOREIGN KEY (fk_id_signalement) REFERENCES signalement_signalement(id_signalement),
	  CONSTRAINT observation_rejet_rejet_fk FOREIGN KEY (fk_id_observation_rejet) REFERENCES signalement_observation_rejet(id_observation_rejet)
);

ALTER TABLE signalement_signalement DROP COLUMN IF EXISTS fk_id_observation_rejet;

DROP SEQUENCE IF EXISTS seq_observation_rejet_signalement;
CREATE SEQUENCE seq_observation_rejet_signalement;

-- Periodes du dashboard
TRUNCATE TABLE signalement_dashboard_period;

INSERT INTO signalement_dashboard_period(id_period,libelle,lower_bound,higher_bound,unit,category,ordre) VALUES (nextval('seq_signalement_dashboard_period_id'),'Mois courant',-1,null,'MONTHS',null,0);
INSERT INTO signalement_dashboard_period(id_period,libelle,lower_bound,higher_bound,unit,category,ordre) VALUES (nextval('seq_signalement_dashboard_period_id'),'3 derniers mois',-3,null,'MONTHS',null,1);
INSERT INTO signalement_dashboard_period(id_period,libelle,lower_bound,higher_bound,unit,category,ordre) VALUES (nextval('seq_signalement_dashboard_period_id'),'6 derniers mois',-6,null,'MONTHS',null,2);

INSERT INTO signalement_dashboard_period(id_period,libelle,lower_bound,higher_bound,unit,category,ordre) VALUES (nextval('seq_signalement_dashboard_period_id'),'10 jours et +',null,-11,'DAYS','other',0);
INSERT INTO signalement_dashboard_period(id_period,libelle,lower_bound,higher_bound,unit,category,ordre) VALUES (nextval('seq_signalement_dashboard_period_id'),'- de 10 jours',-10,-3,'DAYS','other',1);
INSERT INTO signalement_dashboard_period(id_period,libelle,lower_bound,higher_bound,unit,category,ordre) VALUES (nextval('seq_signalement_dashboard_period_id'),'- de 48 h',-2,null,'DAYS','other',2);

INSERT INTO signalement_dashboard_period(id_period,libelle,lower_bound,higher_bound,unit,category,ordre) VALUES (nextval('seq_signalement_dashboard_period_id'),'- de 8 jours avant la date programmée',0,8,'DAYS','planned',0);
INSERT INTO signalement_dashboard_period(id_period,libelle,lower_bound,higher_bound,unit,category,ordre) VALUES (nextval('seq_signalement_dashboard_period_id'),'- de 10 jours de retard',-10,0,'DAYS','planned',1);
INSERT INTO signalement_dashboard_period(id_period,libelle,lower_bound,higher_bound,unit,category,ordre) VALUES (nextval('seq_signalement_dashboard_period_id'),'Retard de 10 jours et +',null,-11,'DAYS','planned',2);

-- Ajout d'une colonne de comptage du nombre de suivi
DO
$do$
BEGIN
IF EXISTS (SELECT * FROM information_schema.columns WHERE table_schema = 'public' AND table_name   = 'signalement_signalement' AND column_name='vote') THEN
	ALTER TABLE signalement_signalement RENAME COLUMN vote TO suivi;
END IF;
END
$do$