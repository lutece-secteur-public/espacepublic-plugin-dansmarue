-- DMR-2194 Refonte mon espace
-- mise a jour table signalement_aide et signalement_actualite
DROP TABLE IF EXISTS signalement_aide;
CREATE TABLE signalement_aide (
id_aide int8 not null,
libelle varchar(255) not null,
url_hypertexte varchar(255) not null,
image_content bytea not null,
image_mime_type varchar(255) NULL,
actif int4 null,
ordre int4 null,
constraint signalement_aide_pkey primary key (id_aide)
)with (
	oids = false
);

DROP TABLE IF EXISTS signalement_actualite;
create table signalement_actualite (
id_actualite int8 not null,
libelle varchar(255) not null,
texte varchar(500) not null,
image_content bytea not null,
image_mime_type varchar(255) NULL,
actif int4 null,
ordre int4 null,
constraint signalement_actualite_pkey primary key (id_actualite)
)with (
	oids = false
);

update signalement_versions set version = 0 
where "table" = 'actualites' 
or "table" = 'aides';