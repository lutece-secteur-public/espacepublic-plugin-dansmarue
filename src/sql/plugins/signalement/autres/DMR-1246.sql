-- DMR-1246
select
	wa.id_action,
	wt.id_task,
	wa."name" as nom_action,
	ws1."name" as etat_depart,
	ws2."name" as etat_arrive,
	notif.id_message as numero_message_sf
from
	signalement_workflow_notifuser_multi_contents_task notif
inner join workflow_task wt on
	wt.id_task = notif.id_task
inner join workflow_action wa on
	wa.id_action = wt.id_action
inner join workflow_state ws1 on
	ws1.id_state = wa.id_state_before
inner join workflow_state ws2 on
	ws2.id_state = wa.id_state_after
order by
	wa.id_action;