DROP TABLE IF EXISTS signalement_unit;
CREATE TABLE signalement_unit(
	fk_id_unit integer,
	visible integer,
	CONSTRAINT pk_signalement_id_unit PRIMARY KEY(fk_id_unit),
	CONSTRAINT fk_signalement_id_unit FOREIGN KEY(fk_id_unit) REFERENCES unittree_unit(id_unit)
);