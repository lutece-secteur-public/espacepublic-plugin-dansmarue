--DMR-1694
DROP TRIGGER IF EXISTS exp_aft_upd_wkf_res_hist ON workflow_resource_history;
DROP FUNCTION IF EXISTS test_exp_ins_wkf_res_hist ();

DROP TRIGGER IF EXISTS exp_aft_upd_wkf ON workflow_resource_workflow;
DROP TRIGGER IF EXISTS exp_aft_ins_wkf ON workflow_resource_workflow;
DROP FUNCTION IF EXISTS test_exp_ins_wkf ();

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
					if (select length(id_telephone) from  signalement_signaleur where fk_id_signalement = new.id_resource) > 0
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


--
-- trigger after insert workflow_resource_history
--
CREATE TRIGGER exp_aft_upd_wkf_res_hist
  AFTER update
  ON workflow_resource_history
  FOR EACH ROW
  EXECUTE PROCEDURE test_exp_ins_wkf_res_hist();
  
  
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
 IdLastHistory integer;
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
	
	if exists (select 1 from signalement_export where id_signalement=new.id_resource)
		then 
			UPDATE signalement_export
			set id_wkf_state= new.id_state,etat=NewEtat, id_mail_service_fait=NewIdMailServiceFait, date_cloture=NewDateCloture, date_derniere_action=NewDerniereDateMAJ
			where id_signalement = new.id_resource;
	end if;
			
    RETURN NEW;
END;
$$
LANGUAGE 'plpgsql';

--
-- trigger after insert workflow_resource_workflow
--
CREATE TRIGGER exp_aft_ins_wkf
  AFTER insert
  ON workflow_resource_workflow
  FOR EACH ROW
  EXECUTE PROCEDURE test_exp_ins_wkf();

--
-- trigger after update workflow_resource_workflow
--
CREATE TRIGGER exp_aft_upd_wkf
  AFTER update
  ON workflow_resource_workflow
  FOR EACH ROW
  EXECUTE PROCEDURE test_exp_ins_wkf();