-- Ajout des secteurs au prestataire Ramen créé précédemment
update unittree_sector
set name = 'Ramen'
where id_sector = 50013;

insert into unittree_unit_sector(id_unit,id_sector) values( (select id_unit from unittree_unit where label = 'Ramen') ,50013);
insert into unittree_unit_sector(id_unit,id_sector) values(0,50013);

update signalement_type_signalement
set fk_id_unit = (select id_unit from unittree_unit where label = 'Ramen')
where fk_id_type_signalement = 1000
and id_type_signalement not in ( 1006,1007,1008, 11013 );

-- DMR 973 Ajout de la colonne date de clôture dans l'export Excel
ALTER TABLE signalement_signalement
ADD date_rejet timestamp  without time zone;

-- Mise à jour date de clôture des dossiers
update
	signalement_signalement
set
	date_rejet = (
		select
			max( workflow_resource_history.creation_date )
		from
			workflow_resource_history
		where
			id_resource = signalement_signalement.id_signalement
			and signalement_signalement.id_signalement in (
				select
					id_resource
				from
					workflow_resource_workflow
				where
					id_state = 11
			)
	)
where
	signalement_signalement.id_signalement in (
		select
			id_resource
		from
			workflow_resource_workflow
		where
			id_state = 11
	);
	
-- Rajout du delete en cascade sur la table observation_rejet
 alter table
	signalement_observation_rejet_signalement 
	drop constraint observation_rejet_signalement_fk,
	add constraint observation_rejet_signalement_fk foreign key (fk_id_signalement) references signalement_signalement(id_signalement) on delete cascade;
	

