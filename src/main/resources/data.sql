insert into role (name) values ('ROLE_client');
insert into role (name) values ('ROLE_admin');
insert into role (name) values ('ROLE_driver');

INSERT INTO system_info (token_price) VALUES (5.0);

INSERT INTO clients (id, email, password, name, surname, deleted, phone_number, city,is_social_login, tokens, photo, blocked, enabled)
          VALUES (nextval('user_seq'), 'ivanaj0610@gmail.com', '$2a$10$iWm70CXU267iEgtasI.gGOYswU4qcaGKxa7rw/ZJtHnhdmukWqwWW', 'Ivana', 'Jankovic', false, '0654079380', 'Trebinje',false, 10, 'unknown.jpg', false, true);

insert into user_role (user_id, role_id) values (1, 1);

INSERT INTO admins (id, email, password, name, surname, deleted, phone_number, city,is_social_login, enabled)
VALUES (nextval('user_seq'), 'admin@gmail.com', '$2a$10$iWm70CXU267iEgtasI.gGOYswU4qcaGKxa7rw/ZJtHnhdmukWqwWW', 'Admin', 'Admin', false, '0654079380', 'Trebinje',false, true);

insert into user_role (user_id, role_id) values (2, 2);

INSERT INTO clients (id, email, password, name, surname, deleted, phone_number, city,is_social_login, tokens, photo, blocked, enabled)
VALUES (nextval('user_seq'), 'maki@gmail.com', '$2a$10$iWm70CXU267iEgtasI.gGOYswU4qcaGKxa7rw/ZJtHnhdmukWqwWW', 'Marija', 'Milosevic', false, '0654079380', 'Cuprija',false, 10, 'coka.jpg', false, true);

insert into user_role (user_id, role_id) values (3, 1);

insert into messages (sender, recipient, text) values (1,2,'sta sta');
insert into messages (sender, recipient, text) values (2,1,'sta vam treba?');
insert into messages (sender, recipient, text) values (1,2,'nista hehe');
insert into messages (sender, recipient, text) values (3,2,'sta ima?');
