-- Rattrapage
update signalement_export set direction='DPE-STPP-DT' where direction ='DPE';


--Ajout du trigger
CREATE OR REPLACE FUNCTION exp_aft_upd_unit()
 RETURNS trigger
 LANGUAGE plpgsql
AS $function$
declare
 newLabelDirection varchar (255) :='';
begin
	select label into newLabelDirection from unittree_unit where id_unit = new.id_unit;
	UPDATE signalement_export set direction=newLabelDirection where id_direction = new.id_unit;
    RETURN NEW;
END;
$function$
;



create trigger exp_aft_upd_unit after
update
    on
    unittree_unit for each row execute procedure exp_aft_upd_unit();