-- DMR 1929 decrease nb_requalifications after delete
DROP TRIGGER IF EXISTS exp_aft_del_sign_requalification ON signalement_requalification;
--
--Alimente colonne Nombre de requalification
--
 CREATE OR REPLACE FUNCTION test_exp_del_sign_requalification()
  RETURNS trigger AS
$$
begin

	if exists (select 1 from signalement_export where id_signalement=old.id_signalement) 
		then
			update signalement_export set nb_requalifications = nb_requalifications -1 where id_signalement=old.id_signalement;
	end if;
	
	RETURN OLD;
END;
$$
LANGUAGE 'plpgsql';
--
-- trigger after delete signalement_requalification
--
CREATE TRIGGER exp_aft_del_sign_requalification
  AFTER delete
  ON signalement_requalification
  FOR EACH ROW
  EXECUTE PROCEDURE test_exp_del_sign_requalification();
