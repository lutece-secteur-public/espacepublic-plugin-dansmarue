--
-- Dumping data for table signalement_priorite
--
INSERT INTO signalement_priorite (id_priorite, libelle) VALUES (1, 'Dangereux');
INSERT INTO signalement_priorite (id_priorite, libelle) VALUES (3, 'Peu genant');
INSERT INTO signalement_priorite (id_priorite, libelle) VALUES (2, 'Genant');

-- Set the sequence with the max value of id_priorite
SELECT setval( 'seq_signalement_priorite_id_priorite', ( SELECT MAX( signalement_priorite.id_priorite )+1 FROM signalement_priorite ) );

-- Init version signalement_type_signalement to 1.00
INSERT INTO signalement_type_signalement_version (version) VALUES (1.00);

-- Init message creation
INSERT INTO signalement_message_creation VALUES(1, '<p>Bonjour,<br />Votre message a bien été enregistré.<br />Nous vous remercions de votre participation.<br />Vous pourrez suivre l''avancement de sa prise en compte via le lien suivant :<br />${lien_consultation}<br />Pour information, vous ne recevrez pas d''autre e-mail sur ce sujet, conservez bien cet e-mail et/ou l''adresse web ci-dessus pour suivre l''avancement de votre message.<br /><br />La Ville de Paris<br />Les services de la Mairie en charge de l''espace public</p>');
