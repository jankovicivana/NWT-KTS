insert into role (name) values ('ROLE_client');
insert into role (name) values ('ROLE_admin');
insert into role (name) values ('ROLE_driver');

INSERT INTO system_info (token_price) VALUES (5.0);

insert into car_types (person_num,type,price,photo) values (4,'Comfort ride',100,'c4.png');
insert into car_types (person_num,type,price,photo) values (6,'Van XL',150,'c5.png');
insert into car_types (person_num,type,price,photo) values (8,'Minibus',50,'c6.png');

insert  into cars (type,pet_friendly,babies_allowed,driver) values (2,false,true,null );

INSERT INTO clients (id, email, password, name, surname, deleted, phone_number, city,is_social_login, tokens, photo, blocked, enabled)
          VALUES (nextval('user_seq'), 'ivanaj0610@gmail.com', '$2a$10$iWm70CXU267iEgtasI.gGOYswU4qcaGKxa7rw/ZJtHnhdmukWqwWW', 'Ivana', 'Jankovic', false, '0654079380', 'Trebinje',false, 10, 'unknown.jpg', false, true);

INSERT INTO clients (id, email, password, name, surname, deleted, phone_number, city,is_social_login, tokens, photo, blocked, enabled)
          VALUES (nextval('user_seq'), 'i@gmail.com', '$2a$10$iWm70CXU267iEgtasI.gGOYswU4qcaGKxa7rw/ZJtHnhdmukWqwWW', 'Ivana', 'Kasikovic', false, '0654079380', 'Trebinje',false, 10, 'unknown.jpg', false, true);

INSERT INTO admins (id, email, password, name, surname, deleted, phone_number, city,is_social_login, enabled,blocked)
VALUES (nextval('user_seq'), 'admin@gmail.com', '$2a$10$iWm70CXU267iEgtasI.gGOYswU4qcaGKxa7rw/ZJtHnhdmukWqwWW', 'Maki', 'Milosevic', false, '0654079380', 'Trebinje', false, true, false);


INSERT INTO clients (id, email, password, name, surname, deleted, phone_number, city,is_social_login, tokens, photo, blocked, enabled)
VALUES (nextval('user_seq'), 'ivana@gmail.com', '$2a$10$iWm70CXU267iEgtasI.gGOYswU4qcaGKxa7rw/ZJtHnhdmukWqwWW', 'Ivana', 'Prezimenic', false, '0654079380', 'Trebinje',false, 10, 'unknown.jpg', false, true);

INSERT INTO clients (id, email, password, name, surname, deleted, phone_number, city,is_social_login, tokens, photo, blocked, enabled)
VALUES (nextval('user_seq'), 'marija@gmail.com', '$2a$10$iWm70CXU267iEgtasI.gGOYswU4qcaGKxa7rw/ZJtHnhdmukWqwWW', 'Marija', 'Milosevic', false, '0654079380', 'Trebinje',false, 10, 'unknown.jpg', false, true);

INSERT INTO clients (id, email, password, name, surname, deleted, phone_number, city,is_social_login, tokens, photo, blocked, enabled)
VALUES (nextval('user_seq'), 'mako@gmail.com', '$2a$10$iWm70CXU267iEgtasI.gGOYswU4qcaGKxa7rw/ZJtHnhdmukWqwWW', 'Marko', 'Kasikovic', false, '0654079380', 'Trebinje',false, 10, 'unknown.jpg', false, true);

INSERT INTO drivers (id, email, password, name, surname, deleted, phone_number, city,is_social_login, photo, blocked, enabled,available,active,car)
VALUES (nextval('user_seq'), 'driver@gmail.com', '$2a$10$iWm70CXU267iEgtasI.gGOYswU4qcaGKxa7rw/ZJtHnhdmukWqwWW', 'Vozac', 'Vozic', false, '0654079380', 'Trebinje',false, 'unknown.jpg', false, true,false ,false,1 );

insert into user_role (user_id, role_id) values (1, 1);
insert into user_role (user_id, role_id) values (2, 1);
insert into user_role (user_id, role_id) values (3, 2);
insert into user_role (user_id, role_id) values (4, 1);
insert into user_role (user_id, role_id) values (5, 1);
insert into user_role (user_id, role_id) values (6, 1);
insert into user_role (user_id, role_id) values (7, 3);


insert into notes (admin_id,driver_id,client_id,note) values (3,-1 ,1,'Neki pogrdan komentar');
insert into grades (car_grade,driver_grade,comment,client,driver,drive_id) values (2,2,null ,1,7,1);


insert into messages (sender, recipient, text) values (1,3,'sta sta');
insert into messages (sender, recipient, text) values (3,1,'sta vam treba?');
insert into messages (sender, recipient, text) values (1,3,'nista hehe');
insert into messages (sender, recipient, text) values (2,3,'sta ima?');

insert into positions (lat, lon) values (45.2484513, 19.8487313);

insert into positions (lat, lon) values (45.2451945, 19.8324524);

insert into cars (type,babies_allowed, pet_friendly, driver)
values (1,true, true, null );

insert into cars (type,babies_allowed, pet_friendly, driver)
values (2,true, true, null );

INSERT INTO drivers (id, email, password,
                    name, surname, deleted, phone_number, city, is_social_login,
                    photo, blocked, enabled, active, available, position,car)

            VALUES (nextval('user_seq'), 'ivana@gmail.com', '$2a$10$iWm70CXU267iEgtasI.gGOYswU4qcaGKxa7rw/ZJtHnhdmukWqwWW',
                    'Ivana', 'Jankovic', false, '0654079380', 'Trebinje', false,
                     'unknown.jpg', false, true, true, true, 1,2);

INSERT INTO drivers (id, email, password,
                 name, surname, deleted, phone_number, city, is_social_login,
                 photo, blocked, enabled, active, available, position,car)

             VALUES (nextval('user_seq'), 'ivana1@gmail.com', '$2a$10$iWm70CXU267iEgtasI.gGOYswU4qcaGKxa7rw/ZJtHnhdmukWqwWW',
                     'Ivana', 'Jankovic', false, '0654079380', 'Trebinje', false,
                      'unknown.jpg', false, true, true, true, 2,3);

insert into user_role (user_id, role_id) values (8, 3);
insert into user_role (user_id, role_id) values (9, 3);




insert into drives (driver, start_time, end_time, price, status, distance)
values (8, '2023-01-11 23:43:00', '2023-01-11 23:59:00', 20.0, 0, 25);

insert into client_drives (client, drive, price) values (2, 1, 20.0);

insert into routes (drive, start_position, end_position, type) values (1, 1, 2, 'fastest');


insert into drives (driver, start_time, end_time, price, status, distance)
values (9, '2023-01-16 16:04:00', '2023-01-16 23:59:00', 30.0, 0, 38);

insert into client_drives (client, drive, price) values (1, 2, 20.0);
insert into client_drives (client, drive, price) values (5, 2, 20.0);
insert into client_drives (client, drive, price) values (2, 2, 20.0);


insert into routes (drive, start_position, end_position, type) values (2, 2, 1, 'shortest');

insert into grades (car_grade,driver_grade,comment,client,driver,drive_id) values (3,3,null ,2,8,1);
insert into grades (car_grade,driver_grade,comment,client,driver,drive_id) values (5,4,null ,2,8,1);
insert into grades (car_grade,driver_grade,comment,client,driver,drive_id) values (5,5,null ,2,9,2);


INSERT INTO edit_driver (id,driver_id, email, password, name, surname, status, phone_number, city, photo,type,pet_friendly,babies_allowed)
VALUES (nextval('edit_seq'),7,'driver@gmail.com', '$2a$10$iWm70CXU267iEgtasI.gGOYswU4qcaGKxa7rw/ZJtHnhdmukWqwWW', 'Vozaccc', 'Vozic', 1, '0654079380', 'Trebinjeee', 'unknown.jpg',1,true, true);

