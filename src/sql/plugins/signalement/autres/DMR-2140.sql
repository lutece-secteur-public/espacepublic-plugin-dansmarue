update
	signalement_signalement sig
set
	service_fait_date_passage =(
		select
			histo.creation_date
		from
			workflow_resource_workflow wkf join workflow_resource_history histo 
			on histo.id_resource = wkf.id_resource
		where
			wkf.id_state = 10 --service fait
			and wkf.id_resource = sig.id_signalement
			and histo.id_action = 70 --action service fait en masse
		order by
			wkf.id_resource desc limit 1 --récupération de la derniere action
	)
where
	service_fait_date_passage is null
	and (select id_state from workflow_resource_workflow workflow where workflow.id_resource = id_signalement)= 10;