-- DMR-1322 --
-- Reprise du nom prenom des utilsateurs à partir du mail
WITH badusers AS
( 
   select id_user,substring(email from 0 for position ('.' in email) ) as prenom, 
   substring(email from position ('.' in email)+1  for (position ('@' in email) - position ('.' in email)) -1 ) as nom
   from core_admin_user where last_name = 'last_name' 
   and position ('.' in email) > 0
   order by id_user
)
update core_admin_user
set first_name = prenom, last_name = nom
from badusers
where badusers.id_user = core_admin_user.id_user;
   
-- Update user FORMA17_DVD_CODIR et user auto --
update core_admin_user set first_name = 'FORMA17_DVD_CODIR', last_name = 'FORMA17_DVD_CODIR' where access_code = 'FORMA17_DVD_CODIR';
update core_admin_user set first_name = 'DansMaRue', last_name = 'DansMaRue', email = 'SG-DansMaRue@paris.fr' where access_code = 'auto';