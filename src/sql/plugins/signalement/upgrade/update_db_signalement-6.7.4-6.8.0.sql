CREATE TABLE signalement_feuille_de_tournee (
	id int8 NOT NULL,
	nom varchar(255) NOT  NULL,
	createur varchar(255) NOT  NULL,
	fk_id_unit int8 NOT NULL,
	date_creation timestamp not NULL,
	date_modification timestamp NULL,
	commentaire text NOT  NULL,
	id_filtre_init int8 NOT NULL,
	signalement_ids integer[],
	CONSTRAINT signalement_feuille_de_tournee_pkey PRIMARY KEY (id),
	CONSTRAINT fk_id_unit FOREIGN KEY (fk_id_unit) REFERENCES unittree_unit(id_unit)
)
WITH (
	OIDS=FALSE
) ;

CREATE TABLE signalement_feuille_de_tournee_filter (
	id int8 NOT NULL,
	nom varchar(100) NOT  NULL,
	createur varchar(255) NOT  NULL,
	id_entite int8 NOT NULL,
	commentaire varchar(255) NOT  NULL,
	date timestamp not NULL,
	valeur text NOT  NULL,
	CONSTRAINT signalement_feuille_de_tournee_filter_pkey PRIMARY KEY (id)
)
WITH (
	OIDS=FALSE
) ;

INSERT INTO core_datastore(entity_key,entity_value) VALUES ('sitelabels.site_property.feuilledetournee.nom.prefix','DMR_FDT_');
INSERT INTO core_datastore(entity_key,entity_value) VALUES ('sitelabels.site_property.feuilledetournee.mail.message','Bonjour,<br /><br /> Vous trouverez ci-joint une feuille de tourn&eacute;e<br /><br />Cordialement.');
INSERT INTO core_datastore(entity_key,entity_value) VALUES ('sitelabels.site_property.feuilledetournee.mail.objet','Feuille de tourn&eacute;e');

INSERT INTO core_admin_right (id_right,name,level_right,admin_url,description,is_updatable,plugin_name,id_feature_group,icon_url,documentation_url,id_order,is_external_feature) VALUES 
('FEUILLE_DE_TOURNEE','dansmarue.adminFeature.feuilleDeTournee.name',2,'jsp/admin/plugins/signalement/ManageFeuilleDeTournee.jsp','dansmarue.adminFeature.feuilleDeTournee.description',0,'signalement','SIGNALEMENT',NULL,NULL,5,0);