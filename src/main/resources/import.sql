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
INSERT INTO book VALUES(1, 'The Tartar Steppe', 'tts@abc.com', NOW());
INSERT INTO book VALUES(2, 'Poem Strip', 'ps@abc.com', NOW());
INSERT INTO book VALUES(3, 'Restless Nights: Selected Stories of Dino Buzzati', 'rnssodb@abc.com', NOW());
INSERT INTO book VALUES(4, 'Alice''s Adventures in Wonderland', 'aaw@abc.com', NOW());

ALTER SEQUENCE hibernate_sequence RESTART WITH 5;

INSERT INTO user (id, username, password) VALUES (1, 'user', 'e10adc3949ba59abbe56e057f20f883e');
INSERT INTO user (id, username , password) VALUES (2,'admin','e10adc3949ba59abbe56e057f20f883e');

INSERT INTO role (id, name) VALUES (1, 'USER');
INSERT INTO role (id, name) VALUES (2, 'ADMIN');

-- INSERT INTO user_role (user_id, role_id) VALUES (1, 1);
-- INSERT INTO user_role (user_id, role_id) VALUES (2, 1);
-- INSERT INTO user_role (user_id, role_id) VALUES (2, 2);

