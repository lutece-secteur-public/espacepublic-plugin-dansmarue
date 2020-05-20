-- delete photo service fait pour etat nouveau, rejete, a requalifier
delete from signalement_photo where vue_photo = 2
and fk_id_signalement in (
 select sig.id_signalement
 from workflow_resource_workflow rw, signalement_signalement sig
 where rw.id_resource = sig.id_signalement
 and rw.id_state in ('7','11','15')
);

update signalement_export set is_photo_service_fait = 0, nb_photos = 0
where id_signalement in (
 select sig.id_signalement
 from workflow_resource_workflow rw, signalement_signalement sig
 where rw.id_resource = sig.id_signalement
 and rw.id_state in ('7','11','15')
)
and is_photo_service_fait = 1;

--delete photo service fait pour etat service fait
delete from signalement_photo where id_photo in (
	select id_photo from 
		(select  ph.fk_id_signalement , ph.id_photo 
		from signalement_photo ph, workflow_resource_workflow rw
		where ph.fk_id_signalement = rw.id_resource
		and  rw.id_state = '10'
		and ph.vue_photo = 2
		order by ph.fk_id_signalement,ph.id_photo) as subquery
	where id_photo not in 
	    (select  distinct on (fk_id_signalement, ph.vue_photo) ph.id_photo
		from signalement_photo ph, workflow_resource_workflow rw
		where ph.fk_id_signalement = rw.id_resource
		and  rw.id_state = '10'
		and ph.vue_photo = 2)
);

--mise a jour du nombre de photos dans signalement export
update signalement_export set nb_photos  = nbphotos 
from 
 (
  select count(id_photo) as nbphotos, fk_id_signalement from signalement_photo
  group by fk_id_signalement
 ) as subquerry
 where signalement_export.id_signalement = subquerry.fk_id_signalement;