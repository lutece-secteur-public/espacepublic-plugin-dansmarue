-- DMR-1350 --
-- Rattrapage des arrondissements en se basant sur le CP de l'adresse --
update signalement_signalement signalement set fk_id_arrondissement = 
(select RIGHT(SUBSTRING(adresse, '75[0-9]{3}'),2)::int8  from signalement_adresse adresse
join signalement_arrondissement arr on arr.id_arrondissement=signalement.fk_id_arrondissement
where RIGHT(SUBSTRING(adresse, '75[0-9]{3}'),2)::int8 != id_arrondissement
and ST_Contains(arr.geom, ST_GeomFromText((ST_AsText(adresse.geom)), 4326))
and adresse.fk_id_signalement=signalement.id_signalement)
where id_signalement in (
select signalement.id_signalement from signalement_signalement signalement
join signalement_adresse adresse on adresse.fk_id_signalement=signalement.id_signalement
join signalement_arrondissement arr on arr.id_arrondissement=signalement.fk_id_arrondissement
where RIGHT(SUBSTRING(adresse, '75[0-9]{3}'),2)::int8 != id_arrondissement
and ST_Contains(arr.geom, ST_GeomFromText((ST_AsText(adresse.geom)), 4326))
and RIGHT(SUBSTRING(adresse, '75[0-9]{3}'),2)::int8>0
and RIGHT(SUBSTRING(adresse, '75[0-9]{3}'),2)::int8<21
);
