delete
from
	signalement_workflow_notification_config_unit
where
	id_task = 35
	and id_type_signalement is null
	and id_unit in (
	select
		id_unit
	from
		signalement_workflow_notification_config_unit
	where
		id_task = 35
		and id_type_signalement is null
		and id_unit not in ( select	id_unit	from unittree_unit ) );