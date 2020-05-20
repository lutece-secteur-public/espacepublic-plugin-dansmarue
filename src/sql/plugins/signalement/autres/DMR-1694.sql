-- DMR-1694
-- Mise à jour colonne executeur_service_fait avec mail de l'executeur service fait.
update signalement_export
set executeur_service_fait=uac.user_access_code
from (
	select user_access_code, id_signalement from workflow_resource_history wrh, signalement_export se
	where wrh.id_resource = se.id_signalement
	and se.executeur_service_fait = 'Mobil user'
	and wrh.user_access_code like '%@%'
	and wrh.id_action in (62,70,22,18,49,53,41)
) as uac
where signalement_export.id_signalement=uac.id_signalement;
