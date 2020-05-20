-- mise à jour date prevue traitement
update signalement_signalement  
set date_prevue_traitement = newdate.dateTraitement
from 
	(
	select max(creation_date) as dateTraitement, id_signalement from 
	(select * from signalement_signalement sign, workflow_resource_workflow rw, workflow_resource_history rh 
	where sign.id_signalement = rw.id_resource
	and rw.id_resource = rh.id_resource
	and rw.id_state = 21
	and rh.id_action in (68,72)
	and sign.date_prevue_traitement is null) as subquerry
	group by id_signalement
	) as newdate
where signalement_signalement.id_signalement = newdate.id_signalement;


-- mise à jour commentaire
update workflow_task_comment_value  
set comment_value = workflow_task_comment_value.comment_value || ' - ' || 'Date de programmation mise à jour via script'  
from 
	(select rh.id_history from signalement_signalement sign, workflow_resource_workflow rw, workflow_resource_history rh,  workflow_task_comment_value tcv
	where sign.id_signalement = rw.id_resource
	and rw.id_resource = rh.id_resource
	and rh.id_history = tcv.id_history
	and rw.id_state = 21
	and rh.id_action in (68,72)
	and sign.date_prevue_traitement is not null
	and sign.date_prevue_traitement = rh.creation_date) as subquerry
where workflow_task_comment_value.id_history = subquerry.id_history;