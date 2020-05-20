-----------------------------------------------------------------------------------------------------------------------------------------
------------------------------------------------------ FIRST PART - Non DMR tables ------------------------------------------------------
-----------------------------------------------------------------------------------------------------------------------------------------

--
-- Table structure for sira user
--
CREATE SEQUENCE seq_sira_user_id;
CREATE TABLE sira_user (
	id_sira_user int8 NOT NULL,
	user_guid varchar(255) NULL,
	user_udid varchar(255) NULL,
	user_device varchar(255) NULL,
	user_email varchar(255) NULL,
	user_token varchar(255) NULL,
	CONSTRAINT user_guid_pk PRIMARY KEY (id_sira_user)
)
WITH (
	OIDS=FALSE
) ;


--
-- Table structure for formengine tables
--
CREATE TABLE formengine_notice (
	id_notice int4 NOT NULL DEFAULT 0,
	title varchar(255) NULL DEFAULT NULL::character varying,
	message text NULL,
	workgroup_key varchar(255) NULL DEFAULT NULL::character varying,
	date_debut timestamp NULL,
	date_fin timestamp NULL,
	id_diffusion int4 NOT NULL DEFAULT (-1),
	is_enabled int4 NULL DEFAULT 0,
	id_notification int4 NULL DEFAULT 0,
	notice_order int4 NULL DEFAULT 0,
	id_notice_group int4 NOT NULL DEFAULT (-1),
	CONSTRAINT formengine_notice_pkey PRIMARY KEY (id_notice)
)
WITH (
	OIDS=FALSE
);

CREATE TABLE formengine_id_generator (
	id_form varchar(255) NOT NULL,
	current_transaction_id varchar(255) NULL DEFAULT ''::character varying,
	CONSTRAINT formengine_id_generator_pkey PRIMARY KEY (id_form)
)
WITH (
	OIDS=FALSE
);

CREATE TABLE formengine_formengine_parameter (
	parameter_key varchar(100) NOT NULL,
	parameter_value varchar(100) NOT NULL,
	CONSTRAINT formengine_formengine_parameter_pkey PRIMARY KEY (parameter_key)
)
WITH (
	OIDS=FALSE
);

CREATE TABLE formengine_group_notice (
	id_group_notice int4 NOT NULL DEFAULT 0,
	title varchar(255) NOT NULL,
	id_form varchar(255) NULL DEFAULT NULL::character varying,
	is_enabled int4 NULL DEFAULT 0,
	workgroup_key varchar(255) NOT NULL,
	CONSTRAINT formengine_group_notice_pkey PRIMARY KEY (id_group_notice)
)
WITH (
	OIDS=FALSE
);

CREATE TABLE formengine_signalement_right_column (
	id_right_column int8 NOT NULL,
	content varchar(6550) NULL,
	CONSTRAINT formengine_signalement_right_column_pkey PRIMARY KEY (id_right_column)
)
WITH (
	OIDS=FALSE
);

CREATE TABLE formengine_stylesheet (
	id_stylesheet int4 NOT NULL DEFAULT 0,
	description varchar(255) NOT NULL,
	file_name varchar(255) NULL DEFAULT NULL::character varying,
	form varchar(255) NULL DEFAULT NULL::character varying,
	xsl_mode varchar(255) NOT NULL,
	CONSTRAINT formengine_stylesheet_pkey PRIMARY KEY (id_stylesheet)
)
WITH (
	OIDS=FALSE
);

--
-- Table structure for export tables
--
CREATE TABLE export_unittree_unit (
	id_unit int4 NOT NULL DEFAULT 0,
	id_parent int4 NOT NULL DEFAULT 0,
	label varchar(255) NOT NULL DEFAULT ''::character varying,
	description varchar(255) NOT NULL DEFAULT ''::character varying,
	profondeur int4 NULL DEFAULT 0,
	CONSTRAINT export_unittree_unit_pkey PRIMARY KEY (id_unit)
)
WITH (
	OIDS=FALSE
);

CREATE TABLE export_signalement_type_signalement (
	id_type_signalement int8 NOT NULL,
	libelle varchar(255) NULL,
	actif int4 NULL,
	fk_id_type_signalement int8 NULL,
	fk_id_unit int8 NULL,
	ordre int4 NULL,
	image_url varchar(255) NULL,
	image_content bytea NULL,
	image_mime_type varchar(255) NULL,
	profondeur int4 NULL DEFAULT 0,
	CONSTRAINT export_signalement_type_signalement_pkey PRIMARY KEY (id_type_signalement),
	CONSTRAINT fk_export_id_type_signalement_fkey FOREIGN KEY (fk_id_type_signalement) REFERENCES export_signalement_type_signalement(id_type_signalement)
)
WITH (
	OIDS=FALSE
);

-----------------------------------------------------------------------------------------------------------------------------------------
------------------------------------------------------- SECONDE PART - DMR tables -------------------------------------------------------
-----------------------------------------------------------------------------------------------------------------------------------------

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
-- Table structure for table signalement_priorite
--
CREATE SEQUENCE seq_signalement_priorite_id_priorite;
CREATE TABLE signalement_priorite (
	id_priorite int8 NOT NULL,
	libelle varchar(255) NULL,
	ordre_priorite int4 NULL,
	CONSTRAINT signalement_priorite_pkey PRIMARY KEY (id_priorite)
)
WITH (
	OIDS=FALSE
);

--
-- Table structure for table signalement_type_signalement
--
CREATE SEQUENCE seq_signalement_type_signalement_id_type_signalement;
CREATE TABLE signalement_type_signalement (
	id_type_signalement int8 NOT NULL,
	libelle varchar(255) NULL,
	actif int4 NULL,
	fk_id_type_signalement int8 NULL,
	fk_id_unit int8 NULL,
	ordre int4 NULL,
	image_url varchar(255) NULL,
	image_content bytea NULL,
	image_mime_type varchar(255) NULL,
	CONSTRAINT signalement_type_signalement_pkey PRIMARY KEY (id_type_signalement),
	CONSTRAINT fk_id_type_signalement_fkey FOREIGN KEY (fk_id_type_signalement) REFERENCES signalement_type_signalement(id_type_signalement)
)
WITH (
	OIDS=FALSE
);

--
-- Table structure for table signalement_observation_rejet
--
CREATE SEQUENCE seq_signalement_observation_rejet_id_observation_rejet;
CREATE TABLE signalement_observation_rejet (
	id_observation_rejet int8 NOT NULL,
	libelle text NULL,
	actif int4 NULL,
	ordre int4 NULL,
	CONSTRAINT signalement_observation_rejet_pkey PRIMARY KEY (id_observation_rejet)
)
WITH (
	OIDS=FALSE
);

--
-- Table structure for table signalement_signalement
--
CREATE SEQUENCE seq_signalement_signalement_id_signalement;
CREATE TABLE signalement_signalement (
	id_signalement int8 NOT NULL,
	suivi int4 NULL,
	date_creation timestamp NULL,
	date_prevue_traitement timestamp NULL,
	commentaire varchar(255) NULL,
	annee int4 NULL,
	mois varchar(2) NULL,
	numero int4 NULL,
	prefix varchar(2) NULL,
	fk_id_priorite int8 NULL,
	fk_id_arrondissement int8 NULL,
	fk_id_type_signalement int8 NULL,
	fk_id_sector int8 NULL,
	heure_creation timestamp NULL,
	is_doublon int2 NOT NULL DEFAULT 0,
	token varchar(32) NULL,
	service_fait_date_passage timestamp NULL,
	felicitations int4 NULL DEFAULT 0,
	date_mise_surveillance timestamp NULL,
	date_rejet timestamp NULL,
	courriel_destinataire varchar(255) NULL,
	courriel_expediteur varchar(255) NULL,
	courriel_date timestamp NULL,
	is_send_ws int2 NOT NULL DEFAULT 0,
	CONSTRAINT signalement_signalement_pkey PRIMARY KEY (id_signalement),
	CONSTRAINT fk_id_arrondissement_fkey FOREIGN KEY (fk_id_arrondissement) REFERENCES signalement_arrondissement(id_arrondissement),
	CONSTRAINT fk_id_priorite_fkey FOREIGN KEY (fk_id_priorite) REFERENCES signalement_priorite(id_priorite),
	CONSTRAINT fk_id_type_signalement_fkey FOREIGN KEY (fk_id_type_signalement) REFERENCES signalement_type_signalement(id_type_signalement)
)
WITH (
	OIDS=FALSE
);

--
-- Table structure for table signalement_observation_rejet_signalement
--
CREATE SEQUENCE seq_observation_rejet_signalement;
CREATE TABLE signalement_observation_rejet_signalement (
	id_observation_rejet_signalement int8 NOT NULL,
	fk_id_signalement int8 NULL,
	fk_id_observation_rejet int8 NULL,
	observation_rejet_comment text NULL,
	CONSTRAINT observation_rejet_signalement_pk PRIMARY KEY (id_observation_rejet_signalement),
	CONSTRAINT observation_rejet_rejet_fk FOREIGN KEY (fk_id_observation_rejet) REFERENCES signalement_observation_rejet(id_observation_rejet),
	CONSTRAINT observation_rejet_signalement_fk FOREIGN KEY (fk_id_signalement) REFERENCES signalement_signalement(id_signalement) ON DELETE CASCADE
)
WITH (
	OIDS=FALSE
);

--
-- Table structure for table signalement_adresse
--
CREATE SEQUENCE seq_signalement_adresse_id_adresse;
CREATE TABLE signalement_adresse (
	id_adresse int8 NOT NULL,
	adresse varchar(255) NULL,
	precision_localisation varchar(255) NULL,
	fk_id_signalement int8 NULL,
	CONSTRAINT signalement_adresse_pkey PRIMARY KEY (id_adresse),
	CONSTRAINT fk_id_signalement_fkey FOREIGN KEY (fk_id_signalement) REFERENCES signalement_signalement(id_signalement) ON DELETE CASCADE
)
WITH (
	OIDS=FALSE
) ;
CREATE INDEX signalement_adresse_fk ON signalement_adresse USING btree (fk_id_signalement) ;
SELECT addgeometrycolumn('signalement_adresse','geom',4326,'POINT',2);
-- select updategeometrysrid('signalement_adresse', 'geom', 4326);

--
-- Table structure for table signalement_conseil_quartier
--
CREATE SEQUENCE signalement_conseil_quartier_gid_seq;
CREATE TABLE signalement_conseil_quartier (
	id_consqrt serial NOT NULL,
	numero_consqrt varchar(3) NULL,
	surface numeric NULL,
	nom_consqrt varchar(55) NULL,
	numero_arrondissement numeric(10) NULL,
	geom geometry NULL,
	CONSTRAINT signalement_conseil_quartier_pkey PRIMARY KEY (id_consqrt)
)
WITH (
	OIDS=FALSE
) ;
CREATE INDEX signalement_conseil_quartier_geom_idx ON signalement_conseil_quartier USING gist (geom) ;

--
-- Table structure for table signalement_photo
--
CREATE SEQUENCE seq_signalement_photo_id_photo;
CREATE TABLE signalement_photo (
	id_photo int8 NOT NULL,
	image_url varchar(255) NULL,
	image_content bytea NULL,
	image_thumbnail bytea NULL,
	image_mime_type varchar(255) NULL,
	fk_id_signalement int8 NULL,
	vue_photo int4 NULL,
	date_photo timestamp NULL,
	CONSTRAINT signalement_photo_pkey PRIMARY KEY (id_photo),
	CONSTRAINT fk_id_signalement_fkey FOREIGN KEY (fk_id_signalement) REFERENCES signalement_signalement(id_signalement) ON DELETE CASCADE
)
WITH (
	OIDS=FALSE
);
CREATE INDEX signalement_photo_fk ON signalement_photo USING btree (fk_id_signalement) ;

--
-- Table structure for table signalement_signaleur
--
CREATE SEQUENCE seq_signalement_signaleur_id_signaleur;
CREATE TABLE signalement_signaleur (
	id_signaleur int8 NOT NULL,
	mail varchar(255) NULL,
	fk_id_signalement int8 NULL,
	id_telephone varchar(41) NULL,
	guid varchar(255) NULL,
	CONSTRAINT signalement_signaleur_pkey PRIMARY KEY (id_signaleur),
	CONSTRAINT fk_id_signalement_fkey FOREIGN KEY (fk_id_signalement) REFERENCES signalement_signalement(id_signalement) ON DELETE CASCADE
)
WITH (
	OIDS=FALSE
);
CREATE INDEX signalement_signaleur_sign_fk ON signalement_signaleur USING btree (fk_id_signalement) ;

--
-- Table structure for table signalement_message_creation
--
CREATE TABLE signalement_message_creation
(
  id_message INTEGER NOT NULL,
  contenu text,
  CONSTRAINT signalement_message_creation_pkey PRIMARY KEY (id_message)
);

--
-- Table structure for table signalement_type_signalement_version
--
CREATE TABLE signalement_type_signalement_version (
    version real NOT NULL,
	PRIMARY KEY (version)
);

--
-- Table structure for table signalement_type_signalement_alias
--
CREATE TABLE signalement_type_signalement_alias (
	fk_id_type_signalement int8 NOT NULL,
	alias varchar(255) NULL,
	alias_mobile varchar(255) NULL,
	CONSTRAINT signalement_type_signalement_alias_pk PRIMARY KEY (fk_id_type_signalement),
	CONSTRAINT fk_signalement_type_alias FOREIGN KEY (fk_id_type_signalement) REFERENCES signalement_type_signalement(id_type_signalement) ON DELETE CASCADE
)
WITH (
	OIDS=FALSE
);

--
-- Table structure for table signalement_requalification
--
CREATE TABLE signalement_requalification (
	id_signalement int8 NOT NULL,
	id_type_signalement int8 NULL,
	adresse varchar(255) NULL,
	id_sector int8 NULL,
	date_requalification timestamp NULL,
	id_task int8 NULL,
	id_history int8 NULL,
	CONSTRAINT id_signalement_fk FOREIGN KEY (id_signalement) REFERENCES signalement_signalement(id_signalement) ON DELETE CASCADE
)
WITH (
	OIDS=FALSE
);

--
-- Table structure for table signalement_numero_signalement
--
CREATE SEQUENCE seq_signalement_numero_signalement;
CREATE TABLE signalement_numero_signalement (
	id_signalement_numero_signalement bigserial NOT NULL,
	mois varchar(1) NULL,
	annee int4 NULL,
	numero int8 NULL,
	CONSTRAINT signalement_numero_signalement_pkey PRIMARY KEY (id_signalement_numero_signalement)
)
WITH (
	OIDS=FALSE
);
CREATE UNIQUE INDEX idx_numero_signalement ON signalement_numero_signalement USING btree (mois, annee);

--
-- Table structure for table signalement_suivi
--
CREATE SEQUENCE seq_signalement_suivi_id;
CREATE TABLE signalement_suivi (
	id_suivi int8 NOT NULL,
	fk_id_signalement int8 NULL,
	fk_user_guid varchar(255) NULL,
	CONSTRAINT suivi_pk PRIMARY KEY (id_suivi),
	CONSTRAINT suivi_signalement_fk FOREIGN KEY (fk_id_signalement) REFERENCES signalement_signalement(id_signalement) ON DELETE CASCADE
)
WITH (
	OIDS=FALSE
) ;

--
-- Table structure for table signalement_domaine_fonctionnel
--
CREATE SEQUENCE seq_signalement_domaine_fonctionnel;
CREATE TABLE signalement_domaine_fonctionnel (
	id_domaine_fonctionnel int4 NOT NULL,
	libelle varchar(255) NOT NULL,
	actif int4 NOT NULL,
	CONSTRAINT pk_domaine_fonctionnel PRIMARY KEY (id_domaine_fonctionnel)
)
WITH (
	OIDS=FALSE
) ;


--
-- Table structure for table signalement_domaine_type_signalement
--
CREATE TABLE signalement_domaine_type_signalement (
	fk_id_domaine_fonctionnel int4 NOT NULL,
	fk_id_type_signalement int4 NOT NULL,
	CONSTRAINT pk_domaine_type_signalement PRIMARY KEY (fk_id_domaine_fonctionnel, fk_id_type_signalement),
	CONSTRAINT fk_domaine FOREIGN KEY (fk_id_domaine_fonctionnel) REFERENCES signalement_domaine_fonctionnel(id_domaine_fonctionnel) ON DELETE CASCADE,
	CONSTRAINT fk_type_signalement FOREIGN KEY (fk_id_type_signalement) REFERENCES signalement_type_signalement(id_type_signalement)
)
WITH (
	OIDS=FALSE
) ;

--
-- Table structure for table signalement_domaine_conseil_quartier
--
CREATE TABLE signalement_domaine_conseil_quartier (
	fk_id_domaine_fonctionnel int4 NOT NULL,
	fk_id_quartier int4 NOT NULL,
	CONSTRAINT pk_domaine_quartier PRIMARY KEY (fk_id_domaine_fonctionnel, fk_id_quartier),
	CONSTRAINT fk_domaine FOREIGN KEY (fk_id_domaine_fonctionnel) REFERENCES signalement_domaine_fonctionnel(id_domaine_fonctionnel),
	CONSTRAINT fk_quartier FOREIGN KEY (fk_id_quartier) REFERENCES signalement_conseil_quartier(id_consqrt)
)
WITH (
	OIDS=FALSE
) ;

--
-- Table structure for table signalement_domaine_arrondissement
--
CREATE TABLE signalement_domaine_arrondissement (
	fk_id_domaine_fonctionnel int4 NOT NULL,
	fk_id_arrondissement int4 NOT NULL,
	CONSTRAINT pk_domaine_arrondissement PRIMARY KEY (fk_id_domaine_fonctionnel, fk_id_arrondissement),
	CONSTRAINT fk_arrondissement FOREIGN KEY (fk_id_arrondissement) REFERENCES signalement_arrondissement(id_arrondissement),
	CONSTRAINT fk_domaine FOREIGN KEY (fk_id_domaine_fonctionnel) REFERENCES signalement_domaine_fonctionnel(id_domaine_fonctionnel) ON DELETE CASCADE
)
WITH (
	OIDS=FALSE
) ;

--
-- Table structure for table signalement_dashboard_period
--
CREATE SEQUENCE seq_signalement_dashboard_period_id;
CREATE TABLE signalement_dashboard_period (
	id_period int8 NOT NULL,
	libelle varchar(255) NOT NULL,
	lower_bound int4 NULL,
	higher_bound int4 NULL,
	unit varchar(50) NOT NULL,
	category varchar(100) NULL,
	ordre int4 NULL,
	CONSTRAINT signalement_dashboard_period_pk PRIMARY KEY (id_period)
)
WITH (
	OIDS=FALSE
);
