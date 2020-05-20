-- Suppression des photos des anomalies « stationnements gênants » de plus de 48 heures
delete from signalement_photo where id_photo in (SELECT id_photo FROM signalement_signalement signalement INNER JOIN signalement_type_signalement typeSignalement ON signalement.fk_id_type_signalement = typeSignalement.id_type_signalement 
INNER JOIN signalement_photo photo on photo.fk_id_signalement = signalement.id_signalement
inner join workflow_resource_workflow workflow on workflow.id_resource = signalement.id_signalement 
WHERE typeSignalement.id_type_signalement IN ( 12002 ) AND now() - '2 days'::interval > signalement.date_creation);
