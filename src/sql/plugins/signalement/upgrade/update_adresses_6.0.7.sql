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
	
