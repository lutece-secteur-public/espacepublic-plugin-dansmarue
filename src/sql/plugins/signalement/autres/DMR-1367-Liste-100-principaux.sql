--100 principaux signaleurs non agent
select count(id_signaleur) nb_signalement, mail from signalement_signaleur signaleur
join signalement_signalement signalement on signaleur.fk_id_signalement = signalement.id_signalement
where mail is not null and mail <>'' and mail not like '%@paris.fr'
and signalement.annee = 2018
group by mail order by nb_signalement desc
limit 100;

--100 principaux signaleurs agent
select count(id_signaleur) nb_signalement, mail mail_agent from signalement_signaleur signaleur
join signalement_signalement signalement on signaleur.fk_id_signalement = signalement.id_signalement
where mail is not null and mail <>'' and mail like '%@paris.fr'
and signalement.annee = 2018
group by mail order by nb_signalement desc
limit 100;