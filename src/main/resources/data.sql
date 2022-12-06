insert into role (name) values ('ROLE_client');
insert into role (name) values ('ROLE_admin');
insert into role (name) values ('ROLE_driver');


INSERT INTO system_info (token_price) VALUES (5.0);

INSERT INTO clients (id, email, password, name, surname, deleted, phone_number, city, tokens, photo, blocked, enabled)
          VALUES (nextval('user_seq'), 'ivanaj0610@gmail.com', '$2a$10$iWm70CXU267iEgtasI.gGOYswU4qcaGKxa7rw/ZJtHnhdmukWqwWW', 'Ivana', 'Jankovic', false, '0654079380', 'Trebinje', 10, 'unknown.jpg', false, true);

insert into user_role (user_id, role_id) values (1, 1);
