-- DMR-2249 MAJ email usager
DROP TRIGGER IF EXISTS exp_aft_upd_signaleur ON signalement_signaleur;
DROP FUNCTION IF EXISTS exp_update_signaleur();

CREATE OR REPLACE function exp_update_signaleur()
 RETURNS trigger
 LANGUAGE plpgsql
AS $function$
begin
	update signalement_export set mail_usager = new.mail where id_signalement = new.fk_id_signalement;
    RETURN NEW;
END;
$function$;

create
    trigger exp_aft_upd_signaleur after update
        on
        signalement_signaleur for each row execute procedure exp_update_signaleur();
        
-- Limite de signalement pour feuille de tournée
delete from core_datastore where entity_key='sitelabels.site_property.feuille.de.tournee.limite.signalement';       
INSERT INTO core_datastore (entity_key, entity_value) VALUES ('sitelabels.site_property.feuille.de.tournee.limite.signalement', '1000');        

-- DMR-2271 conversion property -> sitelabel
delete from core_datastore where entity_key='sitelabels.site_property.daemon.nbdays.trasfert.partner';
INSERT INTO core_datastore (entity_key, entity_value) VALUES ('sitelabels.site_property.daemon.nbdays.trasfert.partner', '30');
