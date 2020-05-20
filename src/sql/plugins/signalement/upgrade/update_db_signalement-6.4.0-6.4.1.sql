--DMR-1892: Ajout description et commentaire agent terrain dans la source
ALTER TABLE signalement_source ADD COLUMN description varchar(255);
ALTER TABLE signalement_source ADD COLUMN commentaire varchar(255);

INSERT INTO core_datastore (entity_key, entity_value) VALUES ('sitelabels.site_property.mobile.limitationNbAnoMonEspace', '100');

-- DMR-1892
ALTER TABLE signalement_source ALTER COLUMN description SET DEFAULT '';
ALTER TABLE signalement_source ALTER COLUMN commentaire SET DEFAULT '';
update signalement_source set description='', commentaire='' where description is null;