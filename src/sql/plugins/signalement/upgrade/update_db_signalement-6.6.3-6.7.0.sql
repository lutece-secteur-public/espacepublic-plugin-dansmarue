--
-- Table structure for table signalement_workflow_webservice_value
--
CREATE TABLE signalement_workflow_notification_service_programme (
	id_history int4 NOT NULL,
	id_task int4 NOT NULL,
	message text NULL,
	CONSTRAINT fk_id_history FOREIGN KEY (id_history) REFERENCES workflow_resource_history(id_history) ON DELETE CASCADE
)
WITH (
	OIDS=FALSE
) ;