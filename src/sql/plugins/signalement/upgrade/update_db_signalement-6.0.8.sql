----DMR-389 nettoyage mail cora---

update signalement_signaleur
set mail = '',
	guid = ''
where id_signaleur in (select id_signaleur from signalement_signaleur where mail = 'cora@paris.fr');

/* DMR-831 */

delete
from
	profile_right
where
	profile_key in(
		'DPE_CHEFDESECTEUR',
		'DPE_FEUILLEDEROUTE'
	);
	
delete
from
	profile_profile
where
	profile_key in(
		'DPE_CHEFDESECTEUR',
		'DPE_FEUILLEDEROUTE'
	);
	
delete
from
	profile_role
where
	profile_key in(
		'DPE_CHEFDESECTEUR',
		'DPE_FEUILLEDEROUTE'
	);
	
delete
from
	core_admin_role
where
	role_key in(
		'DPE_CHEFDESECTEUR',
		'DPE_FEUILLEDEROUTE'
	);
	
delete
from
	core_admin_role_resource
where
	role_key in(
		'DPE_CHEFDESECTEUR',
		'DPE_FEUILLEDEROUTE'
	);
	
delete
from
	core_admin_user_field
where
	user_field_value in(
		'DPE_CHEFDESECTEUR',
		'DPE_FEUILLEDEROUTE'
	);

	
/* Correction fait 68060 */ 
	
update signalement_workflow_notifuser_3contents_config
set subject_ramen = 'DansMaRue : Suivi de l’anomalie n°${numero}'
where id_task in (133,85);
