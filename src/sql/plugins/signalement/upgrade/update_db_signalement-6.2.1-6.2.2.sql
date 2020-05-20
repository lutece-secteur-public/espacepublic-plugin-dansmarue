-- DMR-1503 --
-- Add column commentaitre_agent_terrain
ALTER TABLE signalement_requalification ADD COLUMN commentaire_agent_terrain varchar NULL;

-- DMR-1500 -- 
-- Add entity key for checkVersion WS
INSERT INTO core_datastore (entity_key, entity_value) VALUES ('sitelabels.site_property.android.version.store', '2.2.0');
INSERT INTO core_datastore (entity_key, entity_value) VALUES ('sitelabels.site_property.android.maj.obligatoire', true);
INSERT INTO core_datastore (entity_key, entity_value) VALUES ('sitelabels.site_property.android.derniere.version.obligatoire', '2.2.0');
INSERT INTO core_datastore (entity_key, entity_value) VALUES ('sitelabels.site_property.ios.maj.obligatoire', true);
INSERT INTO core_datastore (entity_key, entity_value) VALUES ('sitelabels.site_property.ios.derniere.version.obligatoire', '2.2.2');

-- DMR--1532 --
-- Add column isAdresseRattrapee
ALTER TABLE signalement_adresse ADD COLUMN is_adresse_rattrapee boolean NULL;