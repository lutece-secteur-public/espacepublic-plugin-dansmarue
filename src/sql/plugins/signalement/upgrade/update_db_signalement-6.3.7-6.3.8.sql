--DMR-1692 - -Avant de mettre le compteur négatif à 1 vérifier qu'il y a un mail. S'il n'y a pas de mail, mettre 0.
update signalement_signalement signalement set suivi= 
	CASE 
		when (select mail from signalement_signaleur where fk_id_signalement=signalement.id_signalement) <>'' then 1
		else 0
	END
where signalement.suivi<0;

