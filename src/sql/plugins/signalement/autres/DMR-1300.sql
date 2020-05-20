-- DMR-1300--
--Affection de l'user admin aux actions effecutées par un utilisateur supprimé

update
	workflow_resource_history
set
	user_access_code = 'admin'
where
	id_history in(
		select
			id_history
		from
			workflow_resource_history
		where
			id_action in(
				62,
				70,
				22,
				18,
				49,
				53,
				41
			)
			and user_access_code not in(
				select
					access_code
				from
					core_admin_user
			)
	);