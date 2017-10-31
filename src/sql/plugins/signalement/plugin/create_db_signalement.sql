--
-- Drop 
--
DROP TABLE IF EXISTS signalement_adresse;
DROP TABLE IF EXISTS signalement_photo;
DROP TABLE IF EXISTS signalement_signaleur;
DROP TABLE IF EXISTS signalement_signalement;
DROP TABLE IF EXISTS signalement_priorite;
DROP TABLE IF EXISTS signalement_observation_rejet;
DROP TABLE IF EXISTS signalement_arrondissement;
DROP TABLE IF EXISTS signalement_type_signalement;
DROP TABLE IF EXISTS signalement_type_signalement_version;
DROP TABLE IF EXISTS signalement_message_creation CASCADE;

-- -------------- --
-- Drop sequences --
-- -------------- --
DROP SEQUENCE IF EXISTS seq_signalement_adresse_id_adresse;
DROP SEQUENCE IF EXISTS seq_signalement_observation_rejet_id_observation_rejet;
DROP SEQUENCE IF EXISTS seq_signalement_photo_id_photo;
DROP SEQUENCE IF EXISTS seq_signalement_priorite_id_priorite;
DROP SEQUENCE IF EXISTS seq_signalement_signalement_id_signalement;
DROP SEQUENCE IF EXISTS seq_signalement_signaleur_id_signaleur;
DROP SEQUENCE IF EXISTS seq_signalement_type_signalement_id_type_signalement;

--
-- Table structure for table signalement_adresse
--
CREATE SEQUENCE seq_signalement_adresse_id_adresse;
CREATE TABLE signalement_adresse (
    id_adresse bigint NOT NULL,
    adresse character varying(255), 
    precision_localisation character varying(255),
    fk_id_signalement bigint,
	PRIMARY KEY (id_adresse)
);
SELECT addgeometrycolumn('signalement_adresse','geom',4326,'POINT',2);
-- select updategeometrysrid('signalement_adresse', 'geom', 4326);

--
-- Table structure for table signalement_arrondissement
--
CREATE TABLE signalement_arrondissement (
    id_arrondissement bigint NOT NULL,
    numero_arrondissement character varying(255),
    actif integer, 
	PRIMARY KEY (id_arrondissement)
);
SELECT addgeometrycolumn('signalement_arrondissement','geom',-1,'MULTIPOLYGON',2);

--
-- Table structure for table signalement_observation_rejet
--
CREATE SEQUENCE seq_signalement_observation_rejet_id_observation_rejet;
CREATE TABLE signalement_observation_rejet (
    id_observation_rejet bigint NOT NULL,
    libelle character varying(255),
    actif integer,
	PRIMARY KEY (id_observation_rejet)
);

--
-- Table structure for table signalement_photo
--
CREATE SEQUENCE seq_signalement_photo_id_photo;
CREATE TABLE signalement_photo (
    id_photo bigint NOT NULL,
    image_url character varying(255),
    image_content bytea,
    image_thumbnail bytea,
    image_mime_type character varying(255),
    fk_id_signalement bigint,
    vue_photo integer,
    date_photo timestamp without time zone,
	PRIMARY KEY (id_photo)
);

--
-- Table structure for table signalement_priorite
--
CREATE SEQUENCE seq_signalement_priorite_id_priorite;
CREATE TABLE signalement_priorite (
    id_priorite bigint NOT NULL,
    libelle character varying(255),
	PRIMARY KEY (id_priorite)
);

--
-- Table structure for table signalement_signalement
--
CREATE SEQUENCE seq_signalement_signalement_id_signalement;
CREATE TABLE signalement_signalement (
    id_signalement bigint NOT NULL,
    vote integer,
    date_creation timestamp without time zone,
    date_prevue_traitement timestamp without time zone,
    commentaire character varying(255),
    annee integer,
    mois character varying(2),
    numero integer,
    prefix character varying(2),
    fk_id_observation_rejet bigint,
    fk_id_priorite bigint,
    fk_id_arrondissement bigint,
    fk_id_type_signalement bigint,
    fk_id_sector bigint,
    heure_creation timestamp without time zone,
    is_doublon SMALLINT DEFAULT 0 NOT NULL,
    token character varying(32),
	PRIMARY KEY (id_signalement)
);

--
-- Table structure for table signalement_signaleur
--
CREATE SEQUENCE seq_signalement_signaleur_id_signaleur;
CREATE TABLE signalement_signaleur (
    id_signaleur bigint NOT NULL,
    mail character varying(255),
    fk_id_signalement bigint,
    id_telephone character varying(41),
	PRIMARY KEY (id_signaleur)
);

--
-- Table structure for table signalement_type_signalement
--
CREATE SEQUENCE seq_signalement_type_signalement_id_type_signalement;
CREATE TABLE signalement_type_signalement (
    id_type_signalement bigint NOT NULL,
    libelle character varying(255),
    actif integer,
    fk_id_type_signalement bigint,
    fk_id_unit bigint,
    ordre integer,
    image_url character varying(255),
    image_content bytea,
    image_mime_type character varying(255),
	PRIMARY KEY (id_type_signalement)
);

--
-- Table structure for table signalement_message_creation
--
CREATE TABLE signalement_message_creation
(
  id_message INTEGER NOT NULL,
  contenu text,
  CONSTRAINT signalement_message_creation_pkey PRIMARY KEY (id_message)
);

-- ---------------
-- Constraints --
-- ---------------
ALTER TABLE signalement_signalement
    ADD CONSTRAINT fk_id_arrondissement_fkey FOREIGN KEY (fk_id_arrondissement) REFERENCES signalement_arrondissement(id_arrondissement);
ALTER TABLE signalement_signalement
    ADD CONSTRAINT fk_id_observation_rejet_fkey FOREIGN KEY (fk_id_observation_rejet) REFERENCES signalement_observation_rejet(id_observation_rejet);
ALTER TABLE signalement_signalement
    ADD CONSTRAINT fk_id_priorite_fkey FOREIGN KEY (fk_id_priorite) REFERENCES signalement_priorite(id_priorite);
ALTER TABLE signalement_adresse
    ADD CONSTRAINT fk_id_signalement_fkey FOREIGN KEY (fk_id_signalement) REFERENCES signalement_signalement(id_signalement);
ALTER TABLE signalement_signaleur
    ADD CONSTRAINT fk_id_signalement_fkey FOREIGN KEY (fk_id_signalement) REFERENCES signalement_signalement(id_signalement);
ALTER TABLE signalement_photo
    ADD CONSTRAINT fk_id_signalement_fkey FOREIGN KEY (fk_id_signalement) REFERENCES signalement_signalement(id_signalement);
ALTER TABLE signalement_type_signalement
    ADD CONSTRAINT fk_id_type_signalement_fkey FOREIGN KEY (fk_id_type_signalement) REFERENCES signalement_type_signalement(id_type_signalement);
ALTER TABLE signalement_signalement
    ADD CONSTRAINT fk_id_type_signalement_fkey FOREIGN KEY (fk_id_type_signalement) REFERENCES signalement_type_signalement(id_type_signalement);
    
--
-- Table structure for table signalement_arrondissement
--
CREATE TABLE signalement_type_signalement_version (
    version real NOT NULL,
	PRIMARY KEY (version)
);