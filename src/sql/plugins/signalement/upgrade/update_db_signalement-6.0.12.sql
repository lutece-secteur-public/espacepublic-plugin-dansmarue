-- Ajout des secteurs au prestataire Ramen créé précédemment
insert
	into
		unittree_unit_sector(
			id_unit,
			id_sector
		) select
			unittree_unit.id_unit as id_unit,
			unittree_unit_sector.id_sector as id_sector
		from
			unittree_unit, unittree_unit_sector
		where
			LOWER(unittree_unit.label) like LOWER('%RAMEN%')
			and unittree_unit_sector.id_unit = 108;