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
