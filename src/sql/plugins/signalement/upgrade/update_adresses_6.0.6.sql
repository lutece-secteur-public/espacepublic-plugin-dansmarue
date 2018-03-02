/* DMR 307 */
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
					lower( adresse ) similar to lower( '((%[0-9]{5}%) PARIS%){2}' )
			)
	);
	
/* DMR 119 i et retours chariots */

/* retours chariots */
update
	signalement_adresse
set
	adresse = regexp_replace(adresse, E'[\\n\\r]+', ',' )
where
	lower( adresse ) similar to lower( '%\n%' );
	
/* i accentués */
	
update
	signalement_adresse
set
	adresse = regexp_replace(adresse, 'í', 'i' )
where
	lower( adresse ) similar to lower( '%í%' )
	
/* PARIS en majuscule partout */
update
	signalement_adresse
set
	adresse = replace(lower(adresse), 'paris', 'PARIS' )
where
	lower( adresse ) similar to lower( '%((%[0-9]{5}%) PARIS%)%' )
	
