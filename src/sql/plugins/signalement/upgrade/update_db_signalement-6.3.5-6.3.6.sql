--DMR-1554 Caractère spécial type signalement
UPDATE 
   signalement_type_signalement
SET 
   libelle = REPLACE(libelle,'…','...')
WHERE 
   libelle  like '%…%';