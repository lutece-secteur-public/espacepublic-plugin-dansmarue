-- DMR-1782
DROP TRIGGER IF EXISTS exp_aft_ins_wkf_res_hist ON workflow_resource_history;

-- trigger after insert workflow_resource_history
--
CREATE TRIGGER exp_aft_ins_wkf_res_hist
  AFTER insert
  ON workflow_resource_history
  FOR EACH ROW
  EXECUTE PROCEDURE test_exp_ins_wkf_res_hist();
  

-- DMR-1791
update signalement_signalement 
set service_fait_date_passage = dateHistory.newDate
from  
 (select rh.creation_date as newDate, rh.id_resource as idSignalement
 from workflow_task_comment_value tcv, workflow_resource_history rh, workflow_resource_workflow rw  
 where tcv.id_history = rh.id_history
 and rw.id_resource = rh.id_resource
 and tcv.comment_value = 'Clôture via script' 
 and rw.id_state = '10') as dateHistory  
where id_signalement =  dateHistory.idSignalement;

update signalement_export 
set date_cloture = to_char(dateHistory.newDate, 'DD/MM/YYYY')
from  
 (select rh.creation_date as newDate, rh.id_resource as idSignalement
 from workflow_task_comment_value tcv, workflow_resource_history rh, workflow_resource_workflow rw  
 where tcv.id_history = rh.id_history
 and rw.id_resource = rh.id_resource
 and tcv.comment_value = 'Clôture via script' 
 and rw.id_state = '10') as dateHistory  
where id_signalement =  dateHistory.idSignalement;

--DMR-1797 Caractère spécial type signalement

--Plantation d?arbre
UPDATE signalement_type_signalement SET libelle = REPLACE(libelle,'’','''') WHERE libelle  like '%’%';
UPDATE signalement_type_signalement_alias SET alias = REPLACE(alias,'’','''') WHERE alias  like '%’%';
UPDATE signalement_type_signalement_alias SET alias_mobile = REPLACE(alias_mobile,'’','''') WHERE alias_mobile  like '%’%';
   

--Affiches, autocollants ou graffitis sur statue, monument, ?uvre d'art
UPDATE signalement_type_signalement SET libelle = REPLACE(libelle,'œ','oe') WHERE libelle  like '%œ%';
UPDATE signalement_type_signalement_alias SET alias = REPLACE(alias,'œ','oe') WHERE alias  like '%œ%';
UPDATE signalement_type_signalement_alias SET alias_mobile = REPLACE(alias_mobile,'œ','oe') WHERE alias_mobile  like '%œ%';

--Autos, motos, vélos?
UPDATE signalement_type_signalement SET libelle = REPLACE(libelle,'…','...') WHERE libelle  like '%…%';
UPDATE signalement_type_signalement_alias SET alias = REPLACE(alias,'…','...') WHERE alias  like '%…%';
UPDATE signalement_type_signalement_alias SET alias_mobile = REPLACE(alias_mobile,'…','...') WHERE alias_mobile  like '%…%';



----DMR-1796 ajout trigger suppresion signalement----

-- Suppression trigger et fonction de suppression
DROP TRIGGER IF EXISTS exp_aft_delete_sign ON signalement_signalement;
DROP FUNCTION IF EXISTS exp_delete_sign();

--Fonction de suppression de la table d'export
CREATE OR REPLACE FUNCTION exp_delete_sign()
  RETURNS trigger AS
$$
BEGIN
DELETE FROM signalement_export where id_signalement=OLD.id_signalement;
RETURN NULL;
END;

$$
LANGUAGE 'plpgsql';

--Ajout trigger
 CREATE TRIGGER exp_aft_delete_sign
  AFTER delete
  ON signalement_signalement
  FOR EACH ROW
  EXECUTE PROCEDURE exp_delete_sign();
  
--Rattrapage de données
delete from signalement_export where id_signalement not in (select id_signalement from  signalement_signalement where date_creation > '2017/12/31');










