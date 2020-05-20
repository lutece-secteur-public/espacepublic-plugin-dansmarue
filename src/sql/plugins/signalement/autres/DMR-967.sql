-- DMR-967 --
UPDATE core_admin_user_field set id_field=4 WHERE id_field=57;
UPDATE core_admin_user_field set id_field=6 WHERE id_field=58;

DELETE FROM core_attribute_field WHERE id_field=57;
DELETE FROM core_attribute_field WHERE id_field=58;