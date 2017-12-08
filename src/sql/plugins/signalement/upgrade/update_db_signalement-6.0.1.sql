-- DMR 231 Suppression des taches et actions du workflow Ramen   --
DELETE FROM workflow_resource_history WHERE id_workflow = 1;
DELETE FROM workflow_task WHERE id_action IN (SELECT id_action FROM workflow_action WHERE id_workflow = 1);
DELETE FROM workflow_action WHERE id_workflow = 1;
DELETE FROM workflow_resource_workflow WHERE id_workflow = 1;
DELETE FROM workflow_state WHERE id_workflow = 1;
DELETE FROM workflow_workflow WHERE id_workflow = 1;


-- DMR 146 Suppression de droits obsolètes
DELETE FROM profile_right WHERE id_right NOT IN (SELECT id_right FROM core_admin_right);

-- DMR 146 Supression d'un utilisateur
ALTER TABLE signalement_dashboard_user_preferences 
   DROP CONSTRAINT signalement_dashboard_user_fk,
   ADD CONSTRAINT signalement_dashboard_user_fk FOREIGN KEY (fk_id_user)
        REFERENCES core_admin_user (id_user) ON DELETE CASCADE;