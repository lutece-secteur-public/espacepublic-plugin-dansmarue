CREATE INDEX signalement_export_idx ON signalement_export USING btree (id_signalement);
CREATE INDEX signalement_type_signalement_idx ON signalement_type_signalement USING btree (id_type_signalement);
CREATE INDEX signalement_message_typologie_idx ON signalement_message_typologie USING btree (id_message);