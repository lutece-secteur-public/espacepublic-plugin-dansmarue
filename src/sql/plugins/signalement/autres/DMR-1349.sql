-- DMR-1349 --
-- Script de mise à jour des secteurs de la DPE
update
	signalement_signalement s
set
	fk_id_sector = (
		select
			unittree_sector.id_sector
		from
			unittree_sector
		inner join unittree_unit_sector on
			unittree_unit_sector.id_sector = unittree_sector.id_sector
		inner join unittree_unit on
			unittree_unit.id_unit = unittree_unit_sector.id_unit
		inner join signalement_signalement on
			s.id_signalement = signalement_signalement.id_signalement
		inner join signalement_adresse on
			signalement_adresse.fk_id_signalement = signalement_signalement.id_signalement
		where
			ST_Contains(
				unittree_sector.geom,
				ST_GeomFromText(
					'POINT(' || ST_X(signalement_adresse.geom) || ' ' || ST_Y(signalement_adresse.geom) || ')',
					4326
				)
			)
			and unittree_unit.id_unit = 108
	)
where
	s.id_signalement in (
		select
			id_signalement
		from
			signalement_signalement
		where
			fk_id_type_signalement in (
				select
					id_type_signalement
				from
					signalement_type_signalement
				where
					fk_id_unit = 108
			)
	);

