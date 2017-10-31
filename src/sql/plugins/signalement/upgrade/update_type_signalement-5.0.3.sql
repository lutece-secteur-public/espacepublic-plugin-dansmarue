update signalement_type_signalement set libelle = 'Graffitis, tags, affiches et autocollants' , ordre = 1 where id_type_signalement = 3600;
update signalement_type_signalement set libelle = 'Objets abandonnés' , ordre = 2 where id_type_signalement = 1000;
update signalement_type_signalement set libelle = 'Propreté' , ordre = 3 where id_type_signalement = 3000;
update signalement_type_signalement set libelle = 'Voirie et déplacements' , ordre = 4 where id_type_signalement = 4000;
update signalement_type_signalement set libelle = 'Éclairage / Électricité' , ordre = 5 where id_type_signalement = 6000;
update signalement_type_signalement set libelle = 'Mobiliers urbains dégradés (arrachés, cassés, tordus, bancals, en panne…)' , ordre = 7 where id_type_signalement = 7000;
update signalement_type_signalement set libelle = 'Arbres, végétaux et animaux' , ordre = 8 where id_type_signalement = 9000;
update signalement_type_signalement set libelle = 'Problème sur un chantier' , ordre = 9 where id_type_signalement = 2000;

insert into signalement_type_signalement (id_type_signalement, libelle, actif, fk_id_type_signalement, fk_id_unit, ordre, image_url, image_content, image_mime_type) values (11000,'Eau et assainissement',1,null,0,6,'image?resource_type=image_type_signalement&#38;id=11000',null,'image/png');
update signalement_type_signalement set actif = 0 where id_type_signalement in (5000,8000,10000);

SELECT setval('seq_signalement_type_signalement_id_type_signalement', 12000, false);

insert into signalement_type_signalement_version values (3.0);