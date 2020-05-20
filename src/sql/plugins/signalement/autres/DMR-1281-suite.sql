-- DMR 1281 --
-- Insertion d'une ligne historique workflow --
-- pour les anomalies de 2018 à l'etat Service programmé sans historique --
with insWorkflowHistory as ( 
	insert into workflow_resource_history (id_history, id_resource, resource_type, id_workflow, id_action, creation_date, user_access_code)
	select nextval('seq_workflow_resource_history'), signalement_without_history.id_signalement, 'SIGNALEMENT_SIGNALEMENT', 2, 68, date_creation, 'admin'
	from (
      (select id_signalement from signalement_signalement) except ( select id_resource from workflow_resource_history )
    ) signalement_without_history 
    join signalement_signalement on signalement_without_history.id_signalement = signalement_signalement.id_signalement
    join workflow_resource_workflow on signalement_without_history.id_signalement = workflow_resource_workflow.id_resource
    where signalement_signalement.annee = 2018 and workflow_resource_workflow.id_state = 9
	RETURNING id_history
)
insert into workflow_task_comment_value (id_history, id_task, comment_value)
select id_history, 158, 'Historique ajouté via script'
from insWorkflowHistory;


-- DMR 1281 --
-- Insertion d'une ligne historique workflow --
-- pour les anomalies de 2018 à l'etat A faire bureau sans historique --
with insWorkflowHistory as ( 
	insert into workflow_resource_history (id_history, id_resource, resource_type, id_workflow, id_action, creation_date, user_access_code)
	select nextval('seq_workflow_resource_history'), signalement_without_history.id_signalement, 'SIGNALEMENT_SIGNALEMENT', 2, 47, date_creation, 'admin'
	from (
      (select id_signalement from signalement_signalement) except ( select id_resource from workflow_resource_history )
    ) signalement_without_history 
    join signalement_signalement on signalement_without_history.id_signalement = signalement_signalement.id_signalement
    join workflow_resource_workflow on signalement_without_history.id_signalement = workflow_resource_workflow.id_resource
    where signalement_signalement.annee = 2018 and workflow_resource_workflow.id_state = 17 
	RETURNING id_history
)
insert into workflow_task_comment_value (id_history, id_task, comment_value)
select id_history, 50, 'Historique ajouté via script'
from insWorkflowHistory;


-- DMR 1281 --
-- Insertion d'une ligne historique workflow --
-- pour les anomalies de 2018 à l'etat A faire terrain sans historique --
with insWorkflowHistory as ( 
	insert into workflow_resource_history (id_history, id_resource, resource_type, id_workflow, id_action, creation_date, user_access_code)
	select nextval('seq_workflow_resource_history'), signalement_without_history.id_signalement, 'SIGNALEMENT_SIGNALEMENT', 2, 46, date_creation, 'admin'
	from (
      (select id_signalement from signalement_signalement) except ( select id_resource from workflow_resource_history )
    ) signalement_without_history 
    join signalement_signalement on signalement_without_history.id_signalement = signalement_signalement.id_signalement
    join workflow_resource_workflow on signalement_without_history.id_signalement = workflow_resource_workflow.id_resource
    where signalement_signalement.annee = 2018 and workflow_resource_workflow.id_state = 16
	RETURNING id_history
)
insert into workflow_task_comment_value (id_history, id_task, comment_value)
select id_history, 49, 'Historique ajouté via script'
from insWorkflowHistory;


-- DMR 1281 --
-- Insertion d'une ligne historique workflow --
-- pour les anomalies de 2018 à l'etat A requalifier sans historique --
with insWorkflowHistory as ( 
	insert into workflow_resource_history (id_history, id_resource, resource_type, id_workflow, id_action, creation_date, user_access_code)
	select nextval('seq_workflow_resource_history'), signalement_without_history.id_signalement, 'SIGNALEMENT_SIGNALEMENT', 2, 42, date_creation, 'admin'
	from (
      (select id_signalement from signalement_signalement) except ( select id_resource from workflow_resource_history )
    ) signalement_without_history 
    join signalement_signalement on signalement_without_history.id_signalement = signalement_signalement.id_signalement
    join workflow_resource_workflow on signalement_without_history.id_signalement = workflow_resource_workflow.id_resource
    where signalement_signalement.annee = 2018 and workflow_resource_workflow.id_state = 15 
	RETURNING id_history
)
insert into workflow_task_comment_value (id_history, id_task, comment_value)
select id_history, 53, 'Historique ajouté via script'
from insWorkflowHistory;


-- DMR 1281 --
-- Insertion d'une ligne historique workflow --
-- pour les anomalies de 2018 à l'etat Nouveau sans historique --
-- Pas d'ajout de commentaire car pas de tache -- 
insert into workflow_resource_history (id_history, id_resource, resource_type, id_workflow, id_action, creation_date, user_access_code)
select nextval('seq_workflow_resource_history'), signalement_without_history.id_signalement, 'SIGNALEMENT_SIGNALEMENT', 2, 27, date_creation, 'admin'
from (
  (select id_signalement from signalement_signalement) except ( select id_resource from workflow_resource_history )
) signalement_without_history 
join signalement_signalement on signalement_without_history.id_signalement = signalement_signalement.id_signalement
join workflow_resource_workflow on signalement_without_history.id_signalement = workflow_resource_workflow.id_resource
where signalement_signalement.annee = 2018 and workflow_resource_workflow.id_state = 7 ; 


-- DMR 1281 --
-- Insertion d'une ligne historique workflow --
-- pour les anomalies de 2018 à l'etat A traiter sans historique --
with insWorkflowHistory as ( 
	insert into workflow_resource_history (id_history, id_resource, resource_type, id_workflow, id_action, creation_date, user_access_code)
	select nextval('seq_workflow_resource_history'), signalement_without_history.id_signalement, 'SIGNALEMENT_SIGNALEMENT', 2, 19, date_creation, 'admin'
	from (
      (select id_signalement from signalement_signalement) except ( select id_resource from workflow_resource_history )
    ) signalement_without_history 
    join signalement_signalement on signalement_without_history.id_signalement = signalement_signalement.id_signalement
    join workflow_resource_workflow on signalement_without_history.id_signalement = workflow_resource_workflow.id_resource
    where signalement_signalement.annee = 2018 and workflow_resource_workflow.id_state = 8 
	RETURNING id_history
)
insert into workflow_task_comment_value (id_history, id_task, comment_value)
select id_history, 37, 'Historique ajouté via script'
from insWorkflowHistory;

-- DMR 1281 --
-- Mise à jour de la date de service fait via l'historique -- 
update signalement_signalement signalement
set service_fait_date_passage=(select max(creation_date) from workflow_resource_history h where h.id_resource=signalement.id_signalement)
where (select w.id_state from workflow_resource_workflow w where w.id_resource=signalement.id_signalement)=10
and service_fait_date_passage is null;