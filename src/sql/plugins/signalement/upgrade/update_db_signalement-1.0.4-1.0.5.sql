ALTER TABLE signalement_photo ADD COLUMN vue_photo integer;
ALTER TABLE signalement_photo ADD COLUMN date_photo timestamp without time zone;

ALTER TABLE signalement_signaleur ADD COLUMN id_telephone character varying(15);