----DMR-389 nettoyage mail cora---
update signalement_signaleur
set mail = '',
	guid = ''
where id_signaleur in (select id_signaleur from signalement_signaleur where mail = 'cora@paris.fr');