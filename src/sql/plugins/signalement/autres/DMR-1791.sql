-- DMR-1791
update signalement_signalement 
set service_fait_date_passage = dateHistory.newDate
from  
 (select rh.creation_date as newDate, rh.id_resource as idSignalement
 from workflow_task_comment_value tcv, workflow_resource_history rh, workflow_resource_workflow rw  
 where tcv.id_history = rh.id_history
 and rw.id_resource = rh.id_resource
 and tcv.comment_value = 'Clôture via script' 
 and rw.id_state = '10') as dateHistory  
where id_signalement =  dateHistory.idSignalement;

update signalement_export 
set date_cloture = to_char(dateHistory.newDate, 'DD/MM/YYYY')
from  
 (select rh.creation_date as newDate, rh.id_resource as idSignalement
 from workflow_task_comment_value tcv, workflow_resource_history rh, workflow_resource_workflow rw  
 where tcv.id_history = rh.id_history
 and rw.id_resource = rh.id_resource
 and tcv.comment_value = 'Clôture via script' 
 and rw.id_state = '10') as dateHistory  
where id_signalement =  dateHistory.idSignalement;