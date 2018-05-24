-- Ajout des secteurs au prestataire Ramen créé précédemment
update unittree_sector
set name = 'Ramen'
where id_sector = 50013;

insert into unittree_unit_sector(id_unit,id_sector) values(1521,50013);
insert into unittree_unit_sector(id_unit,id_sector) values(0,50013);