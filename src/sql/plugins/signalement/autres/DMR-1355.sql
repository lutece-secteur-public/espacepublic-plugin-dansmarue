-- DMR 1355 --
-- MAJ adresses --
update signalement_adresse 
set adresse = regexp_replace(adresse, SUBSTRING(adresse, ' 75[0-9]{3}'), ', '||SUBSTRING(adresse, '75[0-9]{3}'))
where adresse not similar to '%, 75[0-9]{3}%'
and adresse not similar to '%,75[0-9]{3}%'
and adresse similar to '%75[0-9]{3}%' ;