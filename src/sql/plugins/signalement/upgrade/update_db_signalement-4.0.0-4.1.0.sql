--- CREATION ROLE POUR SUPPRESSION EN MASSE ET SERVICE FAIT EN MASSE PM 2015 ---
INSERT INTO core_admin_role (role_key, role_description) VALUES ('SUPPRIMER_SIGNALEMENT_MASSE', 'Suppression en masse des anomalies');
INSERT INTO core_admin_role (role_key, role_description) VALUES ('DECLARER_SERVICE_FAIT_MASSE', 'service fait en masse');

--- AJOUT PERMISSION POUR SUPPRESSION EN MASSE ET SERVICE FAIT EN MASSE PM 2015 ---
INSERT INTO core_admin_role_resource (rbac_id, role_key, resource_type, resource_id, permission) VALUES (1505, 'SUPPRIMER_SIGNALEMENT_MASSE', 'GESTION_DES_SIGNALEMENTS', '*', 'SUPPRIMER_SIGNALEMENT_MASSE');
INSERT INTO core_admin_role_resource (rbac_id, role_key, resource_type, resource_id, permission) VALUES (1506, 'DECLARER_SERVICE_FAIT_MASSE', 'GESTION_DES_SIGNALEMENTS', '*', 'DECLARER_SERVICE_FAIT_MASSE');

-- fix the group SIGNALEMENT for SIGNALEMENT rights
update core_admin_right set id_feature_group ='SIGNALEMENT'
where id_right in (
'SIGNALEMENT_MANAGEMENT',
'REFERENTIEL_MANAGEMENT_SIGNALEMENT');

ALTER TABLE signalement_signalement
                ADD COLUMN service_fait_date_passage timestamp without time zone;
                
                
--- UPDATE MAIL SIGNALEUR TO ANONYMOUS MAIL lutece@lutece ---
UPDATE signalement_signaleur SET mail='lutece@lutece.fr'
WHERE fk_id_signalement 
IN (SELECT id_signalement
FROM signalement_signalement as SG
  
INNER JOIN workflow_resource_workflow as WS
 
ON SG.id_signalement=WS.id_resource AND WS.id_state in (7,8,18)

WHERE SG.date_creation::timestamp < '2016-01-01' ) ;

--- UPDATE  ETAT TO SERVICE FAIT ---
UPDATE workflow_resource_workflow SET id_state=1 
WHERE id_resource 
IN (SELECT id_signalement
FROM signalement_signalement as SG
  
INNER JOIN workflow_resource_workflow as WS
 
ON SG.id_signalement=WS.id_resource AND WS.id_state in (7,8,18)

WHERE SG.date_creation::timestamp < '2016-01-01' ) ;                

