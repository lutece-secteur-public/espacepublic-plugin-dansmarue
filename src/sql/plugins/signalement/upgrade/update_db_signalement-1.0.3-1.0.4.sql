-- photo refactoring and thumbnail implementation.
ALTER TABLE signalement_photo ADD COLUMN image_thumbnail bytea;
ALTER TABLE signalement_photo DROP COLUMN image_url