with upd as
(   select w.id_resource, comment_value from workflow_resource_workflow w 
	join signalement_export export on export.id_signalement = w.id_resource
	join workflow_resource_history history on history.id_resource=w.id_resource
	join workflow_task_comment_value comm on comm.id_history=history.id_history
	where id_state=11 --Etat rejete
	and export.raisons_rejet ='' --Sans raison de rejet 
	and id_action=64 --Action rejet
	and id_task=213 --Tache WS comment
	and comment_value<>'' --Un commentaire existe
	and TO_DATE(export.date_creation,'dd/MM/YYYY') > (SELECT TO_DATE(export.date_creation,'dd/MM/YYYY') - INTERVAL '1 years') --Ano de moins d'un an
) 
update signalement_export as t 
set raisons_rejet = upd.comment_value
from upd
where upd.id_resource = t.id_signalement;