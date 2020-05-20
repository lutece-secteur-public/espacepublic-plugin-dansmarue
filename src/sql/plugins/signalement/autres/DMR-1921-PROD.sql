update signalement_export 
set id_wkf_state = ( select id_state from workflow_resource_workflow where id_resource = 282328),
id_quartier = (select id_consqrt 
		       from signalement_conseil_quartier 
		       where ST_Intersects( ( select ST_Union(geom) from signalement_adresse where fk_id_signalement = 282328 )::geometry, signalement_conseil_quartier.geom::geometry )),
quartier = (select nom_consqrt 
		       from signalement_conseil_quartier 
		       where ST_Intersects( ( select ST_Union(geom) from signalement_adresse where fk_id_signalement = 282328 )::geometry, signalement_conseil_quartier.geom::geometry )),
adresse = (select adresse from signalement_adresse where  fk_id_signalement = 282328),
coord_x = (	select ST_X((select geom from signalement_adresse where fk_id_signalement = 282328 )::geometry)),
coord_y = (	select ST_Y((select geom from signalement_adresse where fk_id_signalement = 282328 )::geometry)),
etat = (select name from workflow_state, workflow_resource_workflow where workflow_state.id_state = workflow_resource_workflow.id_state and workflow_resource_workflow.id_resource = 282328)
where id_signalement = 282328;

update signalement_export 
set id_wkf_state = ( select id_state from workflow_resource_workflow where id_resource = 294350),
id_quartier = (select id_consqrt 
		       from signalement_conseil_quartier 
		       where ST_Intersects( ( select ST_Union(geom) from signalement_adresse where fk_id_signalement = 294350 )::geometry, signalement_conseil_quartier.geom::geometry )),
quartier = (select nom_consqrt 
		       from signalement_conseil_quartier 
		       where ST_Intersects( ( select ST_Union(geom) from signalement_adresse where fk_id_signalement = 294350 )::geometry, signalement_conseil_quartier.geom::geometry )),
adresse = (select adresse from signalement_adresse where  fk_id_signalement = 294350),
coord_x = (	select ST_X((select geom from signalement_adresse where fk_id_signalement = 294350 )::geometry)),
coord_y = (	select ST_Y((select geom from signalement_adresse where fk_id_signalement = 294350 )::geometry)),
etat = (select name from workflow_state, workflow_resource_workflow where workflow_state.id_state = workflow_resource_workflow.id_state and workflow_resource_workflow.id_resource = 294350)
where id_signalement = 294350;

update signalement_export 
set id_wkf_state = ( select id_state from workflow_resource_workflow where id_resource = 295871),
id_quartier = (select id_consqrt 
		       from signalement_conseil_quartier 
		       where ST_Intersects( ( select ST_Union(geom) from signalement_adresse where fk_id_signalement = 295871 )::geometry, signalement_conseil_quartier.geom::geometry )),
quartier = (select nom_consqrt 
		       from signalement_conseil_quartier 
		       where ST_Intersects( ( select ST_Union(geom) from signalement_adresse where fk_id_signalement = 295871 )::geometry, signalement_conseil_quartier.geom::geometry )),
adresse = (select adresse from signalement_adresse where  fk_id_signalement = 295871),
coord_x = (	select ST_X((select geom from signalement_adresse where fk_id_signalement = 295871 )::geometry)),
coord_y = (	select ST_Y((select geom from signalement_adresse where fk_id_signalement = 295871 )::geometry)),
etat = (select name from workflow_state, workflow_resource_workflow where workflow_state.id_state = workflow_resource_workflow.id_state and workflow_resource_workflow.id_resource = 295871)
where id_signalement = 295871;

update signalement_export 
set id_wkf_state = ( select id_state from workflow_resource_workflow where id_resource = 298266),
id_quartier = (select id_consqrt 
		       from signalement_conseil_quartier 
		       where ST_Intersects( ( select ST_Union(geom) from signalement_adresse where fk_id_signalement = 298266 )::geometry, signalement_conseil_quartier.geom::geometry )),
quartier = (select nom_consqrt 
		       from signalement_conseil_quartier 
		       where ST_Intersects( ( select ST_Union(geom) from signalement_adresse where fk_id_signalement = 298266 )::geometry, signalement_conseil_quartier.geom::geometry )),
adresse = (select adresse from signalement_adresse where  fk_id_signalement = 298266),
coord_x = (	select ST_X((select geom from signalement_adresse where fk_id_signalement = 298266 )::geometry)),
coord_y = (	select ST_Y((select geom from signalement_adresse where fk_id_signalement = 298266 )::geometry)),
etat = (select name from workflow_state, workflow_resource_workflow where workflow_state.id_state = workflow_resource_workflow.id_state and workflow_resource_workflow.id_resource = 298266)
where id_signalement = 298266;

update signalement_export 
set id_wkf_state = ( select id_state from workflow_resource_workflow where id_resource = 301227),
id_quartier = (select id_consqrt 
		       from signalement_conseil_quartier 
		       where ST_Intersects( ( select ST_Union(geom) from signalement_adresse where fk_id_signalement = 301227 )::geometry, signalement_conseil_quartier.geom::geometry )),
quartier = (select nom_consqrt 
		       from signalement_conseil_quartier 
		       where ST_Intersects( ( select ST_Union(geom) from signalement_adresse where fk_id_signalement = 301227 )::geometry, signalement_conseil_quartier.geom::geometry )),
adresse = (select adresse from signalement_adresse where  fk_id_signalement = 301227),
coord_x = (	select ST_X((select geom from signalement_adresse where fk_id_signalement = 301227 )::geometry)),
coord_y = (	select ST_Y((select geom from signalement_adresse where fk_id_signalement = 301227 )::geometry)),
etat = (select name from workflow_state, workflow_resource_workflow where workflow_state.id_state = workflow_resource_workflow.id_state and workflow_resource_workflow.id_resource = 301227)
where id_signalement = 301227;

update signalement_export 
set id_wkf_state = ( select id_state from workflow_resource_workflow where id_resource = 301229),
id_quartier = (select id_consqrt 
		       from signalement_conseil_quartier 
		       where ST_Intersects( ( select ST_Union(geom) from signalement_adresse where fk_id_signalement = 301229 )::geometry, signalement_conseil_quartier.geom::geometry )),
quartier = (select nom_consqrt 
		       from signalement_conseil_quartier 
		       where ST_Intersects( ( select ST_Union(geom) from signalement_adresse where fk_id_signalement = 301229 )::geometry, signalement_conseil_quartier.geom::geometry )),
adresse = (select adresse from signalement_adresse where  fk_id_signalement = 301229),
coord_x = (	select ST_X((select geom from signalement_adresse where fk_id_signalement = 301229 )::geometry)),
coord_y = (	select ST_Y((select geom from signalement_adresse where fk_id_signalement = 301229 )::geometry)),
etat = (select name from workflow_state, workflow_resource_workflow where workflow_state.id_state = workflow_resource_workflow.id_state and workflow_resource_workflow.id_resource = 301229)
where id_signalement = 301229;

update signalement_export 
set id_wkf_state = ( select id_state from workflow_resource_workflow where id_resource = 323803),
id_quartier = (select id_consqrt 
		       from signalement_conseil_quartier 
		       where ST_Intersects( ( select ST_Union(geom) from signalement_adresse where fk_id_signalement = 323803 )::geometry, signalement_conseil_quartier.geom::geometry )),
quartier = (select nom_consqrt 
		       from signalement_conseil_quartier 
		       where ST_Intersects( ( select ST_Union(geom) from signalement_adresse where fk_id_signalement = 323803 )::geometry, signalement_conseil_quartier.geom::geometry )),
adresse = (select adresse from signalement_adresse where  fk_id_signalement = 323803),
coord_x = (	select ST_X((select geom from signalement_adresse where fk_id_signalement = 323803 )::geometry)),
coord_y = (	select ST_Y((select geom from signalement_adresse where fk_id_signalement = 323803 )::geometry)),
etat = (select name from workflow_state, workflow_resource_workflow where workflow_state.id_state = workflow_resource_workflow.id_state and workflow_resource_workflow.id_resource = 323803)
where id_signalement = 323803;
