-- DMR-1204 Adrese avec E-Arrondissement
-- Reecriture de l'adresse au bon format --

update signalement_adresse sa
set adresse = subquerry.newadresse
from (
 select sa1.id_adresse, newadresse
 from signalement_adresse sa1
 left join (
     select *, adresse, position('Paris' in adresse), replace(substring(adresse from 0 for position('Paris' in adresse) +5), 'Paris','PARIS') as newadresse
	 from signalement_adresse 
	 where adresse ~* 'Arrondissement' and position('Paris' in adresse) > 5
  ) as sa2
 on sa1.id_adresse = sa2.id_adresse
 where sa2.newadresse is not null
) as subquerry
where sa.id_adresse = subquerry.id_adresse
and adresse ~* 'Arrondissement' and position('Paris' in adresse) > 5