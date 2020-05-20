-- DMR-1479
UPDATE signalement_photo SET date_photo = '1970-01-01 00:00:00' WHERE date_photo is null;