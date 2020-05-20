-- DMR 1281 --
-- Insertion d'une ligne historique workflow --
-- pour les anomalies de 2018 à l'etat Transféré à un tiers sans historique --
with insWorkflowHistory as ( 
	insert into workflow_resource_history (id_history, id_resource, resource_type, id_workflow, id_action, creation_date, user_access_code)
	select nextval('seq_workflow_resource_history'), signalement_without_history.id_signalement, 'SIGNALEMENT_SIGNALEMENT', 2, 72, date_creation, 'admin'
	from (
      (select id_signalement from signalement_signalement) except ( select id_resource from workflow_resource_history )
    ) signalement_without_history 
    join signalement_signalement on signalement_without_history.id_signalement = signalement_signalement.id_signalement
    join workflow_resource_workflow on signalement_without_history.id_signalement = workflow_resource_workflow.id_resource
    where signalement_signalement.annee = 2018 and workflow_resource_workflow.id_state = 18 
	RETURNING id_history
)
insert into workflow_task_comment_value (id_history, id_task, comment_value)
select id_history, 154, 'Historique ajouté via script'
from insWorkflowHistory;

-- DMR 1281 --
-- Insertion d'une ligne historique workflow --
-- pour les anomalies de 2018 à l'etat A traiter sans historique --
with insWorkflowHistory as ( 
	insert into workflow_resource_history (id_history, id_resource, resource_type, id_workflow, id_action, creation_date, user_access_code)
	select nextval('seq_workflow_resource_history'), signalement_without_history.id_signalement, 'SIGNALEMENT_SIGNALEMENT', 2, 13, date_creation, 'admin'
	from (
      (select id_signalement from signalement_signalement) except ( select id_resource from workflow_resource_history )
    ) signalement_without_history 
    join signalement_signalement on signalement_without_history.id_signalement = signalement_signalement.id_signalement
    join workflow_resource_workflow on signalement_without_history.id_signalement = workflow_resource_workflow.id_resource
    where signalement_signalement.annee = 2018 and workflow_resource_workflow.id_state = 8 
	RETURNING id_history
)
insert into workflow_task_comment_value (id_history, id_task, comment_value)
select id_history, 56, 'Historique ajouté via script'
from insWorkflowHistory;

