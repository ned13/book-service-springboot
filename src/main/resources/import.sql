-- Book domain

-- tried to create db in import.sql with spring.jpa.hibernate.ddl-auto=validate.
-- But it seems this script is executed after hibernate using spring.jpa.hibernate.ddl-auto=validate
-- So I can't create table here.
-- hibernate checks DB before
--CREATE TABLE book (
--    id bigint(11) NOT NULL AUTO_INCREMENT,
--    name varchar(100) NOT NULL,
--    publish_date timestamp,
--    contact_email varchar(255),
--);

-- hibernate auto created column doesn't follow the order in definition of Book entity.
-- INSERT INTO table_name (column1, column2, column3, ...)
-- VALUES (value1, value2, value3, ...);


INSERT INTO book (id, name, publish_date, contact_email) VALUES(1, 'The Tartar Steppe', NOW(),'tts@abc.com');
INSERT INTO book (id, name, publish_date, contact_email) VALUES(2, 'Poem Strip', NOW(), 'ps@abc.com');
INSERT INTO book (id, name, publish_date, contact_email) VALUES(3, 'Restless Nights: Selected Stories of Dino Buzzati', NOW(), 'rnssodb@abc.com');
INSERT INTO book (id, name, publish_date, contact_email) VALUES(4, 'Alice''s Adventures in Wonderland', NOW(), 'aaw@abc.com');

ALTER SEQUENCE hibernate_sequence RESTART WITH 5;

INSERT INTO user (id, username, password) VALUES (1, 'user', '9a1996efc97181f0aee18321aa3b3b12');
INSERT INTO user (id, username , password) VALUES (2,'admin','9a1996efc97181f0aee18321aa3b3b12');

INSERT INTO role (id, name) VALUES (1, 'USER');
INSERT INTO role (id, name) VALUES (2, 'ADMIN');

-- Following statement is initialized by code.
-- INSERT INTO user_role (user_id, role_id) VALUES (1, 1);
-- INSERT INTO user_role (user_id, role_id) VALUES (2, 1);
-- INSERT INTO user_role (user_id, role_id) VALUES (2, 2);

