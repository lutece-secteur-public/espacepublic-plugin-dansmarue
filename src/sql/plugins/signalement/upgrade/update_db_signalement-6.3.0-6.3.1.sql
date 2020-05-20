-- Type Signalement - ajout de colonnes pour les types sans anomalies
ALTER TABLE signalement_type_signalement 
ADD COLUMN horsdmr integer default 0,
ADD COLUMN messagehorsdmr text;