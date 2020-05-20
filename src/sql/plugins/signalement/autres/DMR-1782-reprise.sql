--Mobil user
update  signalement_export set executeur_service_fait = 'Mobil user'
where id_signalement in (
  select sgneur.fk_id_signalement from signalement_signaleur sgneur, workflow_resource_workflow  rw, workflow_resource_history rh,signalement_export expo
  where sgneur.fk_id_signalement = rw.id_resource
  and rw.id_resource = rh.id_resource
  and rh.id_resource = expo.id_signalement
  and sgneur.id_telephone is not null
  and rw.id_state = 10
  and rh.id_action in (62,70,22,18,49,53,41)
  and rh.user_access_code='auto'
  and (expo.executeur_service_fait is null or expo.executeur_service_fait='')
);


 --Mail signaleur
 update signalement_export set executeur_service_fait = email.mail
  from (
	  select sgneur.mail, expo.id_signalement 
	  from signalement_signaleur sgneur, workflow_resource_workflow  rw, workflow_resource_history rh,signalement_export expo
	  where sgneur.fk_id_signalement = rw.id_resource
	  and rw.id_resource = rh.id_resource
	  and rh.id_resource = expo.id_signalement
	  and (sgneur.mail is not null and sgneur.mail <>'')
	  and sgneur.id_telephone is  null
	  and rw.id_state = 10
	  and rh.id_action in (62,70,22,18,49,53,41)
	  and rh.user_access_code='auto'
	  and (expo.executeur_service_fait is null or expo.executeur_service_fait='')
  ) as email
  where signalement_export.id_signalement = email.id_signalement;
  
--core_admin_user_mail
  update signalement_export set executeur_service_fait = email.email
  from (
	  select auser.email, expo.id_signalement
	  from workflow_resource_workflow  rw, workflow_resource_history rh,signalement_export expo, core_admin_user auser
	  where rw.id_resource = rh.id_resource
	  and rh.user_access_code = auser.access_code
	  and rh.id_resource = expo.id_signalement
	  and rw.id_state = 10
	  and rh.id_action in (62,70,22,18,49,53,41)
	  and rh.user_access_code<>'auto' and rh.user_access_code not like '%@%'
	  and (expo.executeur_service_fait is null or expo.executeur_service_fait='')
  ) as email
  where signalement_export.id_signalement = email.id_signalement;
 
 --user_access_code
  update signalement_export set executeur_service_fait = email.email
  from (
	  select auser.email, expo.id_signalement
	  from workflow_resource_workflow  rw, workflow_resource_history rh,signalement_export expo, core_admin_user auser
	  where rw.id_resource = rh.id_resource
	  and rh.user_access_code = auser.access_code
	  and rh.id_resource = expo.id_signalement
	  and rw.id_state = 10
	  and rh.id_action in (62,70,22,18,49,53,41)
	  and rh.user_access_code<>'auto' and rh.user_access_code like '%@%'
	  and (expo.executeur_service_fait is null or expo.executeur_service_fait='')
  ) as email
  where signalement_export.id_signalement = email.id_signalement;
  
 -- special case
  update signalement_export set executeur_service_fait = 'vincent.corbel@accenture.com' where numero = 'G2019G26';
  update signalement_export set executeur_service_fait = 'vincent.corbel@accenture.com' where numero = 'G2019G30';
  update signalement_export set executeur_service_fait = 'vincent.corbel@accenture.com' where numero = 'G2019G31';
  update signalement_export set executeur_service_fait = 'vincent.corbel@accenture.com' where numero = 'G2019H4';
  update signalement_export set executeur_service_fait = 'SG-DansMaRue@paris.fr' where numero = 'S2019L70';
  update signalement_export set executeur_service_fait = 'SG-DansMaRue@paris.fr' where numero = 'S2020A14';
  update signalement_export set executeur_service_fait = 'SG-DansMaRue@paris.fr' where numero = 'S2020A15';
  update signalement_export set executeur_service_fait = 'SG-DansMaRue@paris.fr' where numero = 'S2020A16';
  