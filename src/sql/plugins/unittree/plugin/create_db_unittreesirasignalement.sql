ALTER TABLE signalement_signalement
    ADD CONSTRAINT fk_id_sector_fkey FOREIGN KEY (fk_id_sector) REFERENCES unittree_sector(id_sector);