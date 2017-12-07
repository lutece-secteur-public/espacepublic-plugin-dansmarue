-- DMR 231 Suppression des taches et actions du workflow Ramen   --
DELETE FROM workflow_resource_history WHERE id_workflow = 1;
DELETE FROM workflow_task WHERE id_action IN (SELECT id_action FROM workflow_action WHERE id_workflow = 1);
DELETE FROM workflow_action WHERE id_workflow = 1;
DELETE FROM workflow_resource_workflow WHERE id_workflow = 1;
DELETE FROM workflow_state WHERE id_workflow = 1;
DELETE FROM workflow_workflow WHERE id_workflow = 1;


-- DMR 146 Suppression de droits obsolètes
DELETE FROM profile_right WHERE id_right NOT IN (SELECT id_right FROM core_admin_right);