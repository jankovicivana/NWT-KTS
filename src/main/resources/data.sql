insert into role (name) values ('ROLE_client');
insert into role (name) values ('ROLE_admin');
insert into role (name) values ('ROLE_driver');

INSERT INTO clients (id, email, password, name, surname, deleted, phone_number, city, card_number, photo, blocked)
          VALUES (nextval('user_seq'), 'ivanaj0610@gmail.com', 'pass', 'Ivana', 'Jankovic', false, '0654079380', 'Trebinje', '000', 'photo.jpg', false);

insert into user_role (user_id, role_id) values (1, 1);
