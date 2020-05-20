-- Ajout des delete cascade sur les tables liées aux signalement
ALTER TABLE signalement_signaleur DROP CONSTRAINT fk_id_signalement_fkey;
ALTER TABLE signalement_signaleur ADD CONSTRAINT fk_id_signalement_fkey FOREIGN KEY (fk_id_signalement) REFERENCES signalement_signalement(id_signalement) ON DELETE CASCADE;

ALTER TABLE signalement_adresse DROP CONSTRAINT fk_id_signalement_fkey;
ALTER TABLE signalement_adresse ADD CONSTRAINT fk_id_signalement_fkey FOREIGN KEY (fk_id_signalement) REFERENCES signalement_signalement(id_signalement) ON DELETE CASCADE;
