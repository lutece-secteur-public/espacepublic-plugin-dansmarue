-- DMR-989 (Déjà exécuté) 

--update
--	signalement_signalement ss
--set
--	fk_id_sector = (
--		select
--			unittree_sector.id_sector
--		from
--			unittree_sector
--		inner join unittree_unit_sector on
--			unittree_unit_sector.id_sector = unittree_sector.id_sector
--		inner join unittree_unit on
--			unittree_unit.id_unit = unittree_unit_sector.id_unit
--		inner join signalement_signalement on
--			ss.fk_id_sector = 50007
--			and ss.id_signalement = signalement_signalement.id_signalement
--		inner join signalement_adresse on
--			signalement_adresse.fk_id_signalement = signalement_signalement.id_signalement
--		where
--			ST_Contains(
--				unittree_sector.geom,
--				ST_GeomFromText(
--					'POINT(' || ST_X(signalement_adresse.geom) || ' ' || ST_Y(signalement_adresse.geom) || ')',
--					4326
--				)
--			)
--			and unittree_unit.id_unit = 1505
--			and unittree_sector.id_sector != 50007
--	)
--where ss.fk_id_sector = 50007;

--delete from unittree_unit_sector
--where id_unit = 1505 and id_sector not in(100000,100001,100002);

---------------------------------------------------------------------------------------------------------------------

-- DMR-1217 (Déjà exécuté) 

--UPDATE unittree_sector
--SET "name"='SMEP - Tout Paris'
--WHERE "name"='Autolib';

---------------------------------------------------------------------------------------------------------------------

-- DMR-1215 (Déjà exécuté)

--UPDATE signalement_type_signalement
--SET fk_id_type_signalement=7000
--WHERE id_type_signalement=7209;

--update
--	signalement_type_signalement
--set
--	fk_id_type_signalement = null,
--	ordre = 11
--where
--	id_type_signalement = 12002;

---------------------------------------------------------------------------------------------------------------------