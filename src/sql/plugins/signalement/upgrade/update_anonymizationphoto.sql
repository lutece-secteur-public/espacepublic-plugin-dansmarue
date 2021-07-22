-- add column is_anonymized
ALTER TABLE signalement_photo ADD COLUMN is_anonymized int2 DEFAULT 0 NOT NULL;

UPDATE core_datastore set entity_value = 'false' WHERE entity_key = 'core.daemon.anonymizationphotoDaemon.onStartUp';

-- renommer le statut workflow Transféré à un tiers en A transférer à un tiers et creer un nouvel etat Transféré à un tiers
-- UPDATE workflow_state SET name = 'A transférer à un tiers', description = 'En attente d''anonymisation des photos avant transfert de l''anomalie à un tiers', display_order = 12 WHERE id_state = 18;
-- INSERT INTO workflow_state
-- (id_state, "name", description, id_workflow, is_initial_state, is_required_workgroup_assigned, id_icon, display_order)
-- VALUES(23, 'Transféré à un tiers', 'Cette anomalie est en cours de traitement par un tiers externe', 2, 0, 0, 11, 10);
-- ALTER SEQUENCE seq_workflow_state RESTART WITH 24;

-- UPDATE workflow_resource_workflow set id_state = 23 WHERE id_state = 18;
-- UPDATE workflow_action set id_state_before = 23 WHERE id_state_before= 18;
-- UPDATE signalement_workflow_webservice_config set id_state_withws_success = 23 WHERE id_state_withws_success = 18;

-- ajout action 
-- INSERT INTO workflow_action
-- (id_action, "name", description, id_workflow, id_state_before, id_state_after, id_icon, is_automatic, is_mass_action, display_order, is_automatic_reflexive_action)
-- VALUES(93, 'Transférer un signalement', 'Transférer le signalement à un tiers', 2, 18, 18, NULL, 0, 0, 0, 0);
-- ALTER SEQUENCE seq_workflow_action RESTART WITH 94;

-- INSERT INTO workflow_task
-- (id_task, task_type_key, id_action, display_order)
-- VALUES(287, 'taskEnvoyerSignalement', 93, 1);
-- ALTER SEQUENCE seq_workflow_task RESTART WITH 288;
