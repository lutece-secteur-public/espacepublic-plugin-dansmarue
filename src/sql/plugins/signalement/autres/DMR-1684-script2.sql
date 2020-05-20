select id_signalement, prefix||annee||mois||numero as numero, fk_id_type_signalement, id_photo, vue_photo 
from signalement_signalement, signalement_photo
where id_signalement = fk_id_signalement
and date_creation > '2019-12-01 00:00:00' and date_creation < '2019-12-31 23:59:59';