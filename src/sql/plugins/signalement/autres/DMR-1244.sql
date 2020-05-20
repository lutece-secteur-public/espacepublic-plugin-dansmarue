-- DMR-1244 (Déjà exécuté)
INSERT INTO unittree_unit_sector
(id_unit, id_sector)
VALUES(1522, 99031);

UPDATE signalement_type_signalement
SET fk_id_unitt=1522
WHERE id_type_signalement=7210;

update
	signalement_signalement
set
	fk_id_sector = 99031
where fk_id_type_signalement = 7210;

