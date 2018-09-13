CREATE OR REPLACE FUNCTION signalementgettypesignalementhierarchy(OUT id_type_signalement bigint, OUT id_parent bigint, OUT actif integer, OUT is_parent_a_category integer)
RETURNS SETOF record
LANGUAGE plpgsql
AS $function$
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
END;
$function$;

CREATE MATERIALIZED VIEW v_signalement_type_signalement_with_parents_links AS
 SELECT liens_signalement.id_type_signalement,
    liens_signalement.id_parent,
    liens_signalement.is_parent_a_category,
    liens_signalement.actif
   FROM signalementgettypesignalementhierarchy() liens_signalement(id_type_signalement, id_parent, actif, is_parent_a_category);
   
INSERT INTO core_datastore (entity_key,entity_value) VALUES 
('core.advanced_parameters.email_pattern','^[\w_.\-!\#\$\%\&\''\*\+\/\=\?\^\`\{\}\|\~]+@[\w_.\-]+\.[\w]+$')