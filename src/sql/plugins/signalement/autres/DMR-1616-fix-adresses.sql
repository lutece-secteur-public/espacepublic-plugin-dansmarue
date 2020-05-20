--MAJ flag adresses
ALTER TABLE signalement_adresse ALTER COLUMN is_adresse_rattrapee SET DEFAULT false;
update signalement_adresse set is_adresse_rattrapee = false where is_adresse_rattrapee is null;