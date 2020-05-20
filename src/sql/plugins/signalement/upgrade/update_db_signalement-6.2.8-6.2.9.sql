--Mise à jours de l'heure de création dans la date de création
update signalement_signalement set date_creation = date_trunc('day', date_creation) + heure_creation::time;

--Suppression de la colonne d'heure
ALTER TABLE signalement_signalement 
DROP COLUMN IF EXISTS heure_creation;

--MAJ Type de signalement, suppression de caractère spécial
UPDATE signalement_type_signalement SET libelle='Autos, motos, vélos... ' WHERE id_type_signalement=12008;