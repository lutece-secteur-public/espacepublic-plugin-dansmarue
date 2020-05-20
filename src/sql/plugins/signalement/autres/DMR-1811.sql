--
--Alimente l'état
--
 CREATE OR REPLACE FUNCTION test_exp_ins_wkf()
  RETURNS trigger AS
$$
declare
 NewEtat varchar(255);
 NewIdMailServiceFait integer;
 NewDateCloture varchar (10) ;
 UserAccessCode varchar (255);
 IdLastHistory integer;
 NewExecuteurServiceFait varchar (255) :='';
 UserAdminEmail varchar (255);
 NewDerniereDateMAJ varchar (10);
begin

	-- état
	SELECT ws.name into NewEtat from workflow_state as ws where ws.id_state=new.id_state;

	-- date de cloture
	if new.id_state=10 
		THEN select to_char(service_fait_date_passage, 'DD/MM/YYYY') into NewDateCloture from signalement_signalement where new.id_resource=id_signalement ;
	elseif new.id_state=22 
		THEN select to_char(date_mise_surveillance, 'DD/MM/YYYY') into NewDateCloture  from signalement_signalement where new.id_resource=id_signalement;
	elseif new.id_state=11 
		THEN select to_char(date_rejet, 'DD/MM/YYYY') into NewDateCloture  from signalement_signalement where new.id_resource=id_signalement;
	else NewDateCloture  :='';       
	end if;
	
	-- id mail service fait
	select val.id_message into NewIdMailServiceFait
	from signalement_workflow_notifuser_multi_contents_value as val 
	inner join workflow_resource_history as history on history.id_history = val.id_history 
	where history.id_resource=new.id_resource order by history.id_history desc limit 1;

	-- date de dernière prise en compte
	select id_history, to_char(creation_date, 'DD/MM/YYYY') into IdLastHistory, NewDerniereDateMAJ from workflow_resource_history where id_resource = new.id_resource order by creation_date desc limit 1;
	
	
	-- Exécuteur service fait (code user)
	select user_access_code into UserAccessCode from workflow_resource_history where id_resource = new.id_resource and id_action in (62,70,22,18,49,53,41);
	
	-- Mail exécuteur service fait
	select email into UserAdminEmail from  core_admin_user where access_code=UserAccessCode;
	
	-- Nom exécuteur service fait
	if UserAccessCode ='auto'
		then if (((select length(id_telephone) from  signalement_signaleur where fk_id_signalement = new.id_resource) > 0) and ( (select length(mail) from  signalement_signaleur where fk_id_signalement = new.id_resource) = 0 ))
				then NewExecuteurServiceFait := 'Mobil user';
			 else select mail into  NewExecuteurServiceFait from  signalement_signaleur where fk_id_signalement = new.id_resource;
			 end if;
	elseif length(UserAdminEmail)>0
		then NewExecuteurServiceFait := UserAdminEmail;
		
	end if;
	
	if exists (select 1 from signalement_export where id_signalement=new.id_resource)
		then 
			UPDATE signalement_export
			set id_wkf_state= new.id_state,etat=NewEtat, id_mail_service_fait=NewIdMailServiceFait, date_cloture=NewDateCloture, executeur_service_fait=NewExecuteurServiceFait, date_derniere_action=NewDerniereDateMAJ
			where id_signalement = new.id_resource;
	end if;
			
    RETURN NEW;
END;
$$
LANGUAGE 'plpgsql';

--
--Alimente l'executeur_service_fait
--
 CREATE OR REPLACE FUNCTION test_exp_ins_wkf_res_hist()
  RETURNS trigger AS
$$
declare
 NewExecuteurServiceFait varchar (255) :='';
 UserAdminEmail varchar (255);
begin
	
	if new.id_action in (62,70,22,18,49,53,41)
		then
		    -- Mail exécuteur service fait
			select email into UserAdminEmail from  core_admin_user where access_code=new.user_access_code;
			
			if new.user_access_code='auto'
				then 
					if (((select length(id_telephone) from  signalement_signaleur where fk_id_signalement = new.id_resource) > 0) and ( (select length(mail) from  signalement_signaleur where fk_id_signalement = new.id_resource) = 0))
						then NewExecuteurServiceFait := 'Mobil user';
					else 
						select mail into  NewExecuteurServiceFait from  signalement_signaleur where fk_id_signalement = new.id_resource;
					end if;
			elseif length(UserAdminEmail)>0
				then NewExecuteurServiceFait := UserAdminEmail;
			elseif new.user_access_code LIKE '%@%'
				then NewExecuteurServiceFait := new.user_access_code;
			end if;
			
			if exists (select 1 from signalement_export where id_signalement=new.id_resource)
				then 
					UPDATE signalement_export
					set executeur_service_fait=NewExecuteurServiceFait
					where id_signalement = new.id_resource;
			end if;
	end if;

    RETURN NEW;
END;
$$
LANGUAGE 'plpgsql';

-- Reprise de donnees
update signalement_export set executeur_service_fait = mail
from
	(select expo.id_signalement, sgneur.mail from signalement_export expo, workflow_resource_history his, workflow_resource_workflow rw, signalement_signaleur sgneur
	where expo.id_signalement = his.id_resource
	and his.id_resource = rw.id_resource
	and expo.id_signalement = sgneur.fk_id_signalement
	and expo.executeur_service_fait = 'Mobil user'
	and his.user_access_code = 'auto'
	and rw.id_state = 10
	and length(sgneur.mail) > 0) as subquery
where signalement_export.id_signalement = subquery.id_signalement;
