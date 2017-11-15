
-- Suppression des TABLE RAMEN
DROP TABLE IF EXISTS ramen_adresse CASCADE;
DROP TABLE IF EXISTS ramen_arrondissement_service CASCADE;
DROP TABLE IF EXISTS ramen_arrondissement_service_seuil CASCADE;
DROP TABLE IF EXISTS ramen_arrondissement CASCADE;
DROP TABLE IF EXISTS ramen_commentaire_jour_nt CASCADE;
DROP TABLE IF EXISTS ramen_jour_marche CASCADE;
DROP TABLE IF EXISTS ramen_jour_non_travaille CASCADE;
DROP TABLE IF EXISTS ramen_marche CASCADE;
DROP TABLE IF EXISTS ramen_message_erreur CASCADE;
DROP TABLE IF EXISTS ramen_nature_objet CASCADE;
DROP TABLE IF EXISTS ramen_rue_inaccessible CASCADE;
DROP TABLE IF EXISTS ramen_encombrant CASCADE;
DROP TABLE IF EXISTS ramen_demande CASCADE;
DROP TABLE IF EXISTS ramen_dossier CASCADE;
DROP TABLE IF EXISTS ramen_service CASCADE;
DROP TABLE IF EXISTS ramen_observation CASCADE;
DROP TABLE IF EXISTS ramen_type_dossier CASCADE;
DROP TABLE IF EXISTS ramen_type_objet CASCADE;
DROP TABLE IF EXISTS ramen_type_observation CASCADE;
DROP TABLE IF EXISTS ramen_unit_jour_non_travaille CASCADE;
DROP TABLE IF EXISTS ramen_workflow CASCADE;
DROP TABLE IF EXISTS ramen_workflow_creation_config CASCADE;
DROP TABLE IF EXISTS ramen_workflow_notification_config CASCADE;



-- Suppression des INDEX Ramen
DROP INDEX IF EXISTS ramen_adresse_dossier;
DROP INDEX IF EXISTS ramen_arrondissement_geom_gist;
DROP INDEX IF EXISTS ramen_demande_date;
DROP INDEX IF EXISTS ramen_demande_dossier;
DROP INDEX IF EXISTS ramen_demande_perf_01;
DROP INDEX IF EXISTS ramen_demande_perf_02;
DROP INDEX IF EXISTS ramen_demande_service;
DROP INDEX IF EXISTS ramen_dossier_fk_id_sector_idx;
DROP INDEX IF EXISTS ramen_encombrant_dossier;
DROP INDEX IF EXISTS ramen_encombrant_dossier_fk;
DROP INDEX IF EXISTS ramen_encombrant_type;
DROP INDEX IF EXISTS ramen_marche_geom_gist;


-- Suppression des SEQUENCE Ramen
DROP SEQUENCE IF EXISTS seq_ramen_adresse_id_adresse;
DROP SEQUENCE IF EXISTS seq_ramen_arrondissement_id_arrondissement;
DROP SEQUENCE IF EXISTS seq_ramen_demande_id_demande;
DROP SEQUENCE IF EXISTS seq_ramen_dossier_id_dossier;
DROP SEQUENCE IF EXISTS seq_ramen_encombrant_id_objet;
DROP SEQUENCE IF EXISTS seq_ramen_jour_marche_id_jour_marche;
DROP SEQUENCE IF EXISTS seq_ramen_jour_non_travaille_id_jour_non_travaille;
DROP SEQUENCE IF EXISTS seq_ramen_marche_id_marche;
DROP SEQUENCE IF EXISTS seq_ramen_message_erreur_id_messages;
DROP SEQUENCE IF EXISTS seq_ramen_nature_objet_id_nature_objet;
DROP SEQUENCE IF EXISTS seq_ramen_observation_id_observation;
DROP SEQUENCE IF EXISTS seq_ramen_rue_inaccessible_id_rue_inaccessible;
DROP SEQUENCE IF EXISTS seq_ramen_service_id_service;
DROP SEQUENCE IF EXISTS seq_ramen_type_objet_id_type_objet;
DROP SEQUENCE IF EXISTS seq_ramen_type_observation_id_type_observation;



-- INSERT Site labels
DELETE FROM public.core_datastore WHERE entity_key = 'sitelabels.site_property.app.content';
INSERT INTO public.core_datastore (entity_key, entity_value) VALUES
	('sitelabels.site_property.app.content', 'Utilisez l''application mobile DansMaRue depuis votre smartphone pour transmettre directement vos anomalies depuis les rues ou les parcs de Paris.');

DELETE FROM public.core_datastore WHERE entity_key = 'sitelabels.site_property.app.title';
INSERT INTO public.core_datastore (entity_key, entity_value) VALUES
	('sitelabels.site_property.app.title', 'DANSMARUE SUR VOTRE MOBILE');

DELETE FROM public.core_datastore WHERE entity_key = 'sitelabels.site_property.finalisation.photo.detaillee.label';
INSERT INTO public.core_datastore (entity_key, entity_value) VALUES
	('sitelabels.site_property.finalisation.photo.detaillee.label', 'Photo détaillée');

DELETE FROM public.core_datastore WHERE entity_key = 'sitelabels.site_property.finalisation.photo.ensemble.label';
INSERT INTO public.core_datastore (entity_key, entity_value) VALUES
	('sitelabels.site_property.finalisation.photo.ensemble.label', 'Photo d''ensemble');

DELETE FROM public.core_datastore WHERE entity_key = 'sitelabels.site_property.finalisation.precisions.label';
INSERT INTO public.core_datastore (entity_key, entity_value) VALUES
	('sitelabels.site_property.finalisation.precisions.label', 'Précisions complémentaires concernant la demande');

DELETE FROM public.core_datastore WHERE entity_key = 'sitelabels.site_property.finalisation.priotrie.label';
INSERT INTO public.core_datastore (entity_key, entity_value) VALUES
	('sitelabels.site_property.finalisation.priotrie.label', 'Priorité');

DELETE FROM public.core_datastore WHERE entity_key = 'sitelabels.site_property.finalisation.priotrie.radio.1';
INSERT INTO public.core_datastore (entity_key, entity_value) VALUES
	('sitelabels.site_property.finalisation.priotrie.radio.1', 'Dangereux');

DELETE FROM public.core_datastore WHERE entity_key = 'sitelabels.site_property.finalisation.priotrie.radio.2';
INSERT INTO public.core_datastore (entity_key, entity_value) VALUES
	('sitelabels.site_property.finalisation.priotrie.radio.2', 'Gênant');

DELETE FROM public.core_datastore WHERE entity_key = 'sitelabels.site_property.finalisation.priotrie.radio.3';
INSERT INTO public.core_datastore (entity_key, entity_value) VALUES
	('sitelabels.site_property.finalisation.priotrie.radio.3', 'Très gênant');

DELETE FROM public.core_datastore WHERE entity_key = 'sitelabels.site_property.general.facultatilf.label';
INSERT INTO public.core_datastore (entity_key, entity_value) VALUES
	('sitelabels.site_property.general.facultatilf.label', '(facultatif)');

-- Update default page
UPDATE public.core_datastore
SET entity_value='jsp/site/Portal.jsp?page=dansmarue'
WHERE entity_key='portal.site.site_property.home_url';

-- Suppression des données core_datastore Ramen
-- DELETE FROM public.core_datastore WHERE entity_key='core.plugins.status.signalement-ramen.installed';
DELETE FROM public.core_datastore WHERE entity_key='core.plugins.status.workflow-ramen.installed';
-- DELETE FROM public.core_datastore WHERE entity_key='core.plugins.status.signalement-ramen.pool';
DELETE FROM public.core_datastore WHERE entity_key='core.plugins.status.workflow-ramen.pool';
DELETE FROM public.core_datastore WHERE entity_key='core.plugins.status.ramen.installed';
-- DELETE FROM public.core_datastore WHERE entity_key='core.plugins.status.dansmarue-ramen.pool';
DELETE FROM public.core_datastore WHERE entity_key='core.plugins.status.ramen.pool';
DELETE FROM public.core_datastore WHERE entity_key='core.plugins.status.ramen-rest.installed';
-- DELETE FROM public.core_datastore WHERE entity_key='core.plugins.status.dansmarue-ramen.installed';
DELETE FROM public.core_datastore WHERE entity_key='core.plugins.status.formengine-ramen.installed';
DELETE FROM public.core_datastore WHERE entity_key='core.plugins.status.formengine-ramen.pool';
DELETE FROM public.core_datastore WHERE entity_key='sitelabels.site_property.ramen.accueil.bloc.htmlblock';

-- Modification du GEO Server
UPDATE public.core_datastore
SET entity_value='R59-GEO-APP'
WHERE entity_key='mylutece.security.public_url.mylutece.url.geoserver';

-- Modification du titre du site
UPDATE public.core_datastore
SET entity_value='DMR - Signalement'
WHERE entity_key='portal.site.site_property.name';

-- Modification de la description et des metas du site
UPDATE public.core_datastore
SET entity_value='Prise de rendez-vous pour l''enlèvement des encombrants à Paris, DansMaRue, gestion des anomalies'
WHERE entity_key='portal.site.site_property.meta.description';
UPDATE public.core_datastore
SET entity_value='encombrant, dépôt, déchet, anomalie, incivilité'
WHERE entity_key='portal.site.site_property.meta.keywords';




-- Suppression des menus Ramen
DELETE FROM public.core_feature_group WHERE id_feature_group='RAMEN';

-- Suppression des droits Ramen
DELETE FROM public.core_admin_role_resource WHERE resource_type = 'GESTION_DES_DOSSIERS';

DELETE FROM public.core_user_right WHERE id_right='DOSSIER_MANAGEMENT';
DELETE FROM public.core_user_right WHERE id_right='REFERENTIEL_MANAGEMENT';
DELETE FROM public.core_user_right WHERE id_right='ROAD_MAP_MANAGEMENT';
DELETE FROM public.core_user_right WHERE id_right='SERVICE_FAIT_MANAGEMENT';

DELETE FROM public.core_admin_right WHERE id_right='DOSSIER_MANAGEMENT';
DELETE FROM public.core_admin_right WHERE id_right='REFERENTIEL_MANAGEMENT';
DELETE FROM public.core_admin_right WHERE id_right='ROAD_MAP_MANAGEMENT';
DELETE FROM public.core_admin_right WHERE id_right='SERVICE_FAIT_MANAGEMENT';

DELETE FROM public.profile_right WHERE id_right='DOSSIER_MANAGEMENT';
DELETE FROM public.profile_right WHERE id_right='REFERENTIEL_MANAGEMENT';
DELETE FROM public.profile_right WHERE id_right='ROAD_MAP_MANAGEMENT';
DELETE FROM public.profile_right WHERE id_right='SERVICE_FAIT_MANAGEMENT';


DELETE FROM public.core_dashboard WHERE dashboard_name='RAMEN';





