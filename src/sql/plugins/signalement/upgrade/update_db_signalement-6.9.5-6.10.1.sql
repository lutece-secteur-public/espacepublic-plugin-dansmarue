-- DMR-2313
INSERT INTO core_datastore (entity_key, entity_value) VALUES('sitelabels.site_property.taches.notification.mail.exclues', 115);

-- DMR-2322
ALTER TABLE signalement_feuille_de_tournee ADD COLUMN id_direction int8 NULL;
ALTER TABLE signalement_feuille_de_tournee ADD COLUMN id_entite int8 NULL;
update signalement_feuille_de_tournee set id_direction = fk_id_unit where id_direction is null;

-- DMR-2330
update signalement_adresse set adresse = REPLACE(adresse, 'All. ', 'allée ' ) where adresse like '%All. %';
update signalement_adresse set adresse = REPLACE(adresse, 'Av. ', 'avenue ' ) where adresse like '%Av. %';
update signalement_adresse set adresse = REPLACE(adresse, 'Ave ', 'avenue ' ) where adresse like '%Ave %';
update signalement_adresse set adresse = REPLACE(adresse, 'Bd ', 'boulevard ' ) where adresse like '%Bd %';
update signalement_adresse set adresse = REPLACE(adresse, 'Blvd ', 'boulevard ' ) where adresse like '%Blvd %';
update signalement_adresse set adresse = REPLACE(adresse, 'Cr ', 'cours ' ) where adresse like '%Cr %';
update signalement_adresse set adresse = REPLACE(adresse, 'Ch. ', 'Charles ' ) where adresse like '%Ch. %';
update signalement_adresse set adresse = REPLACE(adresse, 'Chem. ', 'Chemin ' ) where adresse like '%Chem. %';
update signalement_adresse set adresse = REPLACE(adresse, 'Dr ', 'docteur ' ) where adresse like '%Dr %';
update signalement_adresse set adresse = REPLACE(adresse, 'Espl. ', 'esplanade ' ) where adresse like '%Espl. %';
update signalement_adresse set adresse = REPLACE(adresse, 'Frme ', 'Ferme ' ) where adresse like '%Frme %';
update signalement_adresse set adresse = REPLACE(adresse, 'Imp. ', 'impasse ' ) where adresse like '%Imp. %';
update signalement_adresse set adresse = REPLACE(adresse, 'Pass. ', 'passage ' ) where adresse like '%Pass. %';
update signalement_adresse set adresse = REPLACE(adresse, 'Pl. ', 'place ' ) where adresse like '%Pl. %';
update signalement_adresse set adresse = REPLACE(adresse, 'Prom. ', 'promenade ' ) where adresse like '%Prom. %';
update signalement_adresse set adresse = REPLACE(adresse, 'Prte ', 'porte ' ) where adresse like '%Prte %';
update signalement_adresse set adresse = REPLACE(adresse, 'R. ', 'rue ' ) where adresse like '%R. %';
update signalement_adresse set adresse = REPLACE(adresse, 'Rdpt ', 'rond-point ' ) where adresse like '%Rdpt %';
update signalement_adresse set adresse = REPLACE(adresse, 'Rte ', 'route ' ) where adresse like '%Rte %';
update signalement_adresse set adresse = REPLACE(adresse, 'Sent. ', 'sentier ' ) where adresse like '%Sent. %';
update signalement_adresse set adresse = REPLACE(adresse, 'Sq. ', 'square ' ) where adresse like '%Sq. %';
update signalement_adresse set adresse = REPLACE(adresse, 'St ', 'Saint ' ) where adresse like '%St %';
update signalement_adresse set adresse = REPLACE(adresse, 'Ste ', 'Sainte ' ) where adresse like '%Ste %';
update signalement_adresse set adresse = REPLACE(adresse, 'Vla ', 'villa ' ) where adresse like '%Vla %';
