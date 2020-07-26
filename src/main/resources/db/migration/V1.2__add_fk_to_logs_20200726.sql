ALTER TABLE logs ADD user_fk int;

ALTER TABLE logs ADD CONSTRAINT fk_logs_user FOREIGN KEY (user_fk) REFERENCES  users (id);

