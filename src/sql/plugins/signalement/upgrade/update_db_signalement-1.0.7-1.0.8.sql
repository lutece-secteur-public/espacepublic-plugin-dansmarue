--
-- Add sequences
--

-- Add sequence on signalement_adresse table
CREATE SEQUENCE seq_signalement_adresse_id_adresse; 
-- Set the sequence with the max value of id_adresse
SELECT setval( 'seq_signalement_adresse_id_adresse', ( SELECT MAX( signalement_adresse.id_adresse )+1 FROM signalement_adresse ) );

-- Add sequence on signalement_observation_rejet table
CREATE SEQUENCE seq_signalement_observation_rejet_id_observation_rejet; 
-- Set the sequence with the max value of id_observation_rejet
SELECT setval( 'seq_signalement_observation_rejet_id_observation_rejet', ( SELECT MAX( signalement_observation_rejet.id_observation_rejet )+1 FROM signalement_observation_rejet ) );

-- Add sequence on signalement_photo table
CREATE SEQUENCE seq_signalement_photo_id_photo; 
-- Set the sequence with the max value of id_photo
SELECT setval( 'seq_signalement_photo_id_photo', ( SELECT MAX( signalement_photo.id_photo )+1 FROM signalement_photo ) );

-- Add sequence on signalement_priorite table
CREATE SEQUENCE seq_signalement_priorite_id_priorite; 
-- Set the sequence with the max value of id_priorite
SELECT setval( 'seq_signalement_priorite_id_priorite', ( SELECT MAX( signalement_priorite.id_priorite )+1 FROM signalement_priorite ) );

-- Add sequence on signalement_signalement table
CREATE SEQUENCE seq_signalement_signalement_id_signalement; 
-- Set the sequence with the max value of id_signalement
SELECT setval( 'seq_signalement_signalement_id_signalement', ( SELECT MAX( signalement_signalement.id_signalement )+1 FROM signalement_signalement ) );

-- Add sequence on signalement_signaleur table
CREATE SEQUENCE seq_signalement_signaleur_id_signaleur; 
-- Set the sequence with the max value of id_signaleur
SELECT setval( 'seq_signalement_signaleur_id_signaleur', ( SELECT MAX( signalement_signaleur.id_signaleur )+1 FROM signalement_signaleur ) );

-- Add sequence on signalement_type_signalement table
CREATE SEQUENCE seq_signalement_type_signalement_id_type_signalement; 
-- Set the sequence with the max value of id_type_signalement
SELECT setval( 'seq_signalement_type_signalement_id_type_signalement', ( SELECT MAX( signalement_type_signalement.id_type_signalement )+1 FROM signalement_type_signalement ) );

--
-- Add columns to signalement_type_signalement table
--

-- Add column image_url on signalement_type_signalement table
ALTER TABLE signalement_type_signalement
ADD image_url character varying(255);

-- Add column image_content on signalement_type_signalement table
ALTER TABLE signalement_type_signalement
ADD image_content bytea;

-- Add column image_mime_type on signalement_type_signalement table
ALTER TABLE signalement_type_signalement
ADD image_mime_type character varying(255);

-- Add column token on signalement_signalement table
ALTER TABLE signalement_signalement ADD COLUMN token character varying(32);

--
-- Table structure for table signalement_arrondissement
--
CREATE TABLE signalement_type_signalement_version (
    version real NOT NULL,
	PRIMARY KEY (version)
);

-- Init version signalement_type_signalement to 1.00
INSERT INTO signalement_type_signalement_version (version) VALUES (1.00);
