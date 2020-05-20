-- DMR-1796 rattrapage table export suite à la non création du trigger de delete
delete
from
	signalement_export
where
	id_signalement in (
		select
			export.id_signalement
		from
			signalement_export export 
			left join signalement_signalement signalement on signalement.id_signalement = export.id_signalement
		where
			signalement.id_signalement is null
	);
