/* ? à la place de tiret */	
update
	signalement_adresse
set
	adresse = regexp_replace(adresse, '?', '-' )
where
	lower( adresse ) similar to lower( '%\?%' );
	
/* adresse en parigi */	
update
	signalement_adresse
set
	adresse = replace(lower(adresse), 'parigi', 'PARIS' )
where
	lower( adresse ) similar to lower( '%((%[0-9]{5}%) parigi%)%' );

	
/* DMR-800 */
update signalement_adresse as adresse
set adresse = substring( adresse from 0 for( select position( 'paris' in lower( adresse )) from signalement_adresse where id_adresse = adresse.id_adresse)+ 5 )
where
	id_adresse in(
		select
			id_adresse
		from
			signalement_adresse
		where
			id_adresse in(
				select
					id_adresse
				from
					signalement_adresse
				where
					lower( adresse ) similar to lower( '((%[0-9]{5}%) PARIS%)' )
			)
	);
	
/* DMR-119 */ 
update
	signalement_adresse
set
	adresse = concat(adresse, ', 75019 PARIS')	
where
	id_adresse = 258160;
	
	
update
	signalement_adresse
set
	adresse = concat( adresse, ' PARIS' )
where
	id_adresse in(
		249269,
		266840,
		265613,
		260419,
		260160,
		260136,
		241993,
		236893
	);
	
update
	signalement_adresse
set
	adresse = '51 rue Guy Moquet, 75017 PARIS'
where
	id_adresse = 243443;
	
/* DMR-114 */	
	
UPDATE public.signalement_adresse

SET adresse='33 rue Etienne Marcel, 75001 PARIS'

WHERE fk_id_signalement = 276563;

/* DMR-800 4 adresses à mettre à jour */
update signalement_adresse
set adresse = 'jardinet de la Mairie du Vieme, 75006 PARIS'
where id_adresse = 214886;

update signalement_adresse
set adresse = 'rue des Lombards, 75001 PARIS'
where id_adresse = 85254;

update signalement_adresse
set adresse = '18 rue de la Voûte, 75012 PARIS'
where id_adresse = 82479;

update signalement_adresse
set adresse = '11 rue Audubon, 75012 PARIS'
where id_adresse = 47630;

/* DMR-154 */

update signalement_adresse
set adresse = '205 Avenue Daumesnil, 75012 PARIS'
where id_adresse = 278227;

update signalement_adresse
set adresse = '9 Rue Berzélius, 75017 PARIS'
where id_adresse = 275282;

update signalement_adresse
set adresse = '123 Boulevard de Ménilmontant, 75011 PARIS'
where id_adresse = 276142;

update signalement_adresse
set adresse = '33 Rue Cuvier, 75005 PARIS'
where id_adresse = 276801; 

update signalement_adresse
set adresse = 'Rue Charles Monselet, 75019 PARIS'
where id_adresse = 280030;

update signalement_adresse
set adresse = '33 Avenue Reille, 75014 PARIS'
where id_adresse = 280455; 

update signalement_adresse
set adresse = '192 Rue de Crimée, 75019 PARIS'
where id_adresse = 281355;

update signalement_adresse
set adresse = '11 Rue Pierre Fontaine, 75009 PARIS'
where id_adresse = 283464;

update signalement_adresse
set adresse = '18 Rue du Temple, 75004 PARIS'
where id_adresse = 285221;

update signalement_adresse
set adresse = '10 Cours de Vincennes, 75012 PARIS'
where id_adresse = 285820;

update signalement_adresse
set adresse = 'Rue Robert Esnault Pelterie, 75007 PARIS'
where id_adresse = 285994;

update signalement_adresse
set adresse = '17 Impasse Martini, 75010 PARIS'
where id_adresse = 287310;

update signalement_adresse
set adresse = '13 Boulevard Jules Ferry, 75011 PARIS'
where id_adresse = 288501;

update signalement_adresse
set adresse = '27 Passage Charles Dallery, 75011 PARIS'
where id_adresse = 2562;

update signalement_adresse
set adresse = '1 Rue Pelleport, 75020 PARIS'
where id_adresse = 289870;

update signalement_adresse
set adresse = '110 Rue Ordener, 75018 PARIS'
where id_adresse = 291217;

update signalement_adresse
set adresse = '10 Rue Gassendi, 75014 PARIS'
where id_adresse = 293073;

update signalement_adresse
set adresse = '73 Rue d''Alésia, 75014 PARIS'
where id_adresse = 287441;

update signalement_adresse
set geom = ST_SetSRID(ST_MakePoint(2.3151021999999557, 48.8620177), 4326)
where id_adresse = 285994;
