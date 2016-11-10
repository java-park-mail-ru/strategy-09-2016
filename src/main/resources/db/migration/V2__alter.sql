ALTER TABLE users ALTER column email type varchar(100);
ALTER TABLE users ALTER column login type varchar(80);
ALTER TABLE users ADD CONSTRAINT email_constraint_name UNIQUE (email);
ALTER TABLE users ADD CONSTRAINT login_constraint_name UNIQUE (login);
