-- DMR-1253 Adresse avec Parigi
-- Reecriture de l'adresse au bon format --

update signalement_adresse sa
set adresse = subquerry.newadresse
from (
 select sa1.id_adresse, newadresse
 from signalement_adresse sa1
 left join (
     select *, adresse, position('Parigi' in adresse), replace(substring(adresse from 0 for position('Parigi' in adresse) +6), 'Parigi','PARIS') as newadresse
	 from signalement_adresse 
	 where adresse ~* 'Parigi' and position('Parigi' in adresse) > 6
  ) as sa2
 on sa1.id_adresse = sa2.id_adresse
 where sa2.newadresse is not null
) as subquerry
where sa.id_adresse = subquerry.id_adresse
and adresse ~* 'Parigi' and position('Parigi' in adresse) > 6