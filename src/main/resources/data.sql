insert into role (name) values ('ROLE_client');
insert into role (name) values ('ROLE_admin');
insert into role (name) values ('ROLE_driver');

INSERT INTO system_info (token_price) VALUES (5.0);

INSERT INTO clients (id, email, password, name, surname, deleted, phone_number, city,is_social_login, tokens, photo, blocked, enabled)
          VALUES (nextval('user_seq'), 'ivanaj0610@gmail.com', '$2a$10$iWm70CXU267iEgtasI.gGOYswU4qcaGKxa7rw/ZJtHnhdmukWqwWW', 'Ivana', 'Jankovic', false, '0654079380', 'Trebinje',false, 10, 'unknown.jpg', false, true);

INSERT INTO clients (id, email, password, name, surname, deleted, phone_number, city,is_social_login, tokens, photo, blocked, enabled)
          VALUES (nextval('user_seq'), 'i@gmail.com', '$2a$10$iWm70CXU267iEgtasI.gGOYswU4qcaGKxa7rw/ZJtHnhdmukWqwWW', 'Ivana', 'Kasikovic', false, '0654079380', 'Trebinje',false, 10, 'unknown.jpg', false, true);

INSERT INTO admins (id, email, password, name, surname, deleted, phone_number, city,is_social_login, enabled,blocked)
VALUES (nextval('user_seq'), 'admin@gmail.com', '$2a$10$iWm70CXU267iEgtasI.gGOYswU4qcaGKxa7rw/ZJtHnhdmukWqwWW', 'Maki', 'Milosevic', false, '0654079380', 'Trebinje',false, true,false);


INSERT INTO clients (id, email, password, name, surname, deleted, phone_number, city,is_social_login, tokens, photo, blocked, enabled)
VALUES (nextval('user_seq'), 'ivana@gmail.com', '$2a$10$iWm70CXU267iEgtasI.gGOYswU4qcaGKxa7rw/ZJtHnhdmukWqwWW', 'Ivana', 'Prezimenic', false, '0654079380', 'Trebinje',false, 10, 'unknown.jpg', false, true);

INSERT INTO clients (id, email, password, name, surname, deleted, phone_number, city,is_social_login, tokens, photo, blocked, enabled)
VALUES (nextval('user_seq'), 'marija@gmail.com', '$2a$10$iWm70CXU267iEgtasI.gGOYswU4qcaGKxa7rw/ZJtHnhdmukWqwWW', 'Marija', 'Milosevic', false, '0654079380', 'Trebinje',false, 10, 'unknown.jpg', false, true);

INSERT INTO clients (id, email, password, name, surname, deleted, phone_number, city,is_social_login, tokens, photo, blocked, enabled)
VALUES (nextval('user_seq'), 'mako@gmail.com', '$2a$10$iWm70CXU267iEgtasI.gGOYswU4qcaGKxa7rw/ZJtHnhdmukWqwWW', 'Marko', 'Kasikovic', false, '0654079380', 'Trebinje',false, 10, 'unknown.jpg', false, true);

INSERT INTO drivers (id, email, password, name, surname, deleted, phone_number, city,is_social_login, photo, blocked, enabled,available,active)
VALUES (nextval('user_seq'), 'driver@gmail.com', '$2a$10$iWm70CXU267iEgtasI.gGOYswU4qcaGKxa7rw/ZJtHnhdmukWqwWW', 'Vozac', 'Vozic', false, '0654079380', 'Trebinje',false, 'unknown.jpg', false, true,false ,false );

insert into user_role (user_id, role_id) values (1, 1);
insert into user_role (user_id, role_id) values (2, 1);
insert into user_role (user_id, role_id) values (3, 2);

insert into notes (admin_id,driver_id,client_id,note) values (3,-1 ,1,'Neki pogrdan komentar');
insert into grades (car_grade,driver_grade,comment,client,driver,drive_id) values (2,2,null ,1,7,1)