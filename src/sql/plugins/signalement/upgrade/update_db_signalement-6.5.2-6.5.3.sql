CREATE OR REPLACE FUNCTION test_exp_ins_wkf_res_hist()
  RETURNS trigger AS
$$
declare
  NewExecuteurServiceFait varchar (255) :='';
 UserAdminEmail varchar (255);
 NewExecuteurRejet varchar (255) :='';
 NewExecuteurMiseEnSurveillance varchar (255) :='';
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
			else 
				NewExecuteurServiceFait := new.user_access_code;
			end if;
			
			if exists (select 1 from signalement_export where id_signalement=new.id_resource)
				then 
					UPDATE signalement_export
					set executeur_service_fait=NewExecuteurServiceFait
					where id_signalement = new.id_resource;
			end if;
	elseif new.id_action in (16,21,64,71)
		then
			-- Mail exécuteur rejet
			select email into NewExecuteurRejet from  core_admin_user where access_code=new.user_access_code;
			
			if new.user_access_code='auto'
				then NewExecuteurRejet := 'auto';
			end if;
			
			if exists (select 1 from signalement_export where id_signalement=new.id_resource)
				then 
					UPDATE signalement_export
					set executeur_rejet=NewExecuteurRejet
					where id_signalement = new.id_resource;
			end if;
	elseif new.id_action in (76,80,81,85,86,87)
		then
			-- Mail exécuteur mise en surveillance
			select email into NewExecuteurMiseEnSurveillance from  core_admin_user where access_code=new.user_access_code;
			
			if new.user_access_code='auto'
				then NewExecuteurMiseEnSurveillance := 'auto';
			end if;
			
			if exists (select 1 from signalement_export where id_signalement=new.id_resource)
				then 
					UPDATE signalement_export
					set executeur_mise_surveillance=NewExecuteurMiseEnSurveillance
					where id_signalement = new.id_resource;
			end if;
	end if;

    RETURN NEW;
END;
$$
LANGUAGE 'plpgsql';