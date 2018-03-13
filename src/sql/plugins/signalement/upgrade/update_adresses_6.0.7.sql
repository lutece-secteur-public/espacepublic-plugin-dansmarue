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
