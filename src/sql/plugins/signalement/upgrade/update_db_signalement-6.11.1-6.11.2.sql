-- DMR-2461 -- Passage a 7OO caracteres colonne texte actualite
ALTER TABLE signalement_actualite ALTER COLUMN texte TYPE varchar(700) USING texte::varchar;