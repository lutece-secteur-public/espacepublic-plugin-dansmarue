--
-- Table structure for table signalement_message_creation
--
CREATE TABLE signalement_message_creation
(
  id_message INTEGER NOT NULL,
  contenu text,
  CONSTRAINT signalement_message_creation_pkey PRIMARY KEY (id_message)
);


-- Init message creation
INSERT INTO signalement_message_creation(id_message,contenu) VALUES(1, '<p>Bonjour,<br />Votre message a bien été enregistré.<br />Nous vous remercions de votre participation.<br />Vous pourrez suivre l''avancement de sa prise en compte via le lien suivant :<br />${lien_consultation}<br />Pour information, vous ne recevrez pas d''autre e-mail sur ce sujet, conservez bien cet e-mail et/ou l''adresse web ci-dessus pour suivre l''avancement de votre message.<br /><br />La Ville de Paris<br />Les services de la Mairie en charge de l''espace public</p>');
