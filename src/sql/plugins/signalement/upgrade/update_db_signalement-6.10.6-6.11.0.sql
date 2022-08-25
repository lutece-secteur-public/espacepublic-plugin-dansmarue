CREATE OR REPLACE FUNCTION maj_export_after_insert_photo()
 RETURNS trigger
 LANGUAGE plpgsql
AS $function$
begin
	if new.vue_photo=2
		THEN UPDATE signalement_export
			set is_photo_service_fait=1 where id_signalement = new.fk_id_signalement;
	end if;
    RETURN NEW;
END;
$function$
;

ALTER FUNCTION maj_export_after_insert_photo() OWNER TO postgres_app;
GRANT ALL ON FUNCTION maj_export_after_insert_photo() TO postgres_app;

--Fonction et trigger d'ajout de photos de service fait
DROP TRIGGER IF EXISTS maj_export_after_insert_photo on signalement_photo;
create trigger maj_export_after_insert_photo after
insert
    on
    signalement_photo for each row execute procedure maj_export_after_insert_photo();


--Fonction et trigger de suppression de photos de service fait
CREATE OR REPLACE FUNCTION maj_export_after_delete_photo()
 RETURNS trigger
 LANGUAGE plpgsql
AS $function$
begin
	if old.vue_photo=2
		THEN UPDATE signalement_export
			set is_photo_service_fait=0 where id_signalement = old.fk_id_signalement;
	end if;
    RETURN OLD;
END;
$function$
;

ALTER FUNCTION maj_export_after_delete_photo() OWNER TO postgres_app;
GRANT ALL ON FUNCTION maj_export_after_delete_photo() TO postgres_app;

DROP TRIGGER IF EXISTS maj_export_after_delete_photo on signalement_photo;
create trigger maj_export_after_delete_photo after
delete
    on
    signalement_photo for each row execute procedure maj_export_after_delete_photo();

-- Ajout site label telechargement nomenclature
INSERT INTO core_datastore(entity_key,entity_value) VALUES ('sitelabels.site_property.nomanclature.pdf', 'https://intraparis.mdp/intraparis/servlet/plugins/document/resource?id=43789&nocache&id_attribute=127');

-- Refonte mon espace
create sequence seq_signalement_actualite_id_actualite;
DROP TABLE IF EXISTS signalement_actualite;
create table signalement_actualite (
	id_actualite int8 not null,
	libelle varchar(255) not null,
	url_hypertexte varchar(255) not null,
	image_content bytea not null,
	image_mime_type varchar(255) NULL,
	actif int4 null,
	ordre int4 null,
	constraint signalement_actualite_pkey primary key (id_actualite)
)
with (
	oids = false
);

DROP TABLE IF EXISTS signalement_versions;
create table signalement_versions (
	"table" varchar(255) not null,
	"version" real NOT NULL,
	constraint signalement_versions_pkey primary key ("table")
)
with (
	oids = false
);

INSERT INTO signalement_versions
("table", "version")
VALUES('actualites', 0);

INSERT INTO signalement_versions
("table", "version")
VALUES('aides', 0);


create sequence seq_signalement_aide_id_aide;
DROP TABLE IF EXISTS signalement_aide;
CREATE TABLE signalement_aide (
id_aide int8 not null,
libelle varchar(255) not null,
texte varchar(255) not null,
actif int4 null,
ordre int4 null,
constraint signalement_aide_pkey primary key (id_aide)
)
with (
	oids = false
);
