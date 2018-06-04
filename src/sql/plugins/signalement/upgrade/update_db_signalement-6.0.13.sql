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
ADD date_rejet timestamp  without time zone NOT NULL;

