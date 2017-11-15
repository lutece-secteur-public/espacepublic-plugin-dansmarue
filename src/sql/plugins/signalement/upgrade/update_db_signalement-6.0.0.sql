-- Update default page
UPDATE public.core_datastore
SET entity_value='jsp/site/Portal.jsp?page=dansmarue'
WHERE entity_key='portal.site.site_property.home_url';


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

