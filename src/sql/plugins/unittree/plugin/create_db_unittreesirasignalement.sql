--
-- Table structure for table signalement_type_signalement_version
--
CREATE TABLE signalement_unit(
	fk_id_unit integer,
	visible integer,
	CONSTRAINT pk_signalement_id_unit PRIMARY KEY(fk_id_unit),
	CONSTRAINT fk_signalement_id_unit FOREIGN KEY(fk_id_unit) REFERENCES unittree_unit(id_unit) ON DELETE CASCADE
);

ALTER TABLE signalement_signalement
    ADD CONSTRAINT fk_id_sector_fkey FOREIGN KEY (fk_id_sector) REFERENCES unittree_sector(id_sector);

--
-- Table structure for table signalement_domaine_unit
--
CREATE TABLE signalement_domaine_unit (
	fk_id_domaine_fonctionnel int4 NOT NULL,
	fk_id_unit int4 NOT NULL,
	CONSTRAINT pk_domaine_type_unit PRIMARY KEY (fk_id_domaine_fonctionnel, fk_id_unit),
	CONSTRAINT fk_domaine FOREIGN KEY (fk_id_domaine_fonctionnel) REFERENCES signalement_domaine_fonctionnel(id_domaine_fonctionnel) ON DELETE CASCADE,
	CONSTRAINT fk_id_unit FOREIGN KEY (fk_id_unit) REFERENCES unittree_unit(id_unit)
)
WITH (
	OIDS=FALSE
) ;