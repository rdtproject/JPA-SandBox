insert into course(id, name, created, updated) values (10001, 'JPA in 50 steps', sysdate(), sysdate());
insert into course(id, name, created, updated) values (10002, 'Dupa in 12 steps', sysdate(), sysdate());
insert into course(id, name, created, updated) values (10003, 'Spring in 19 steps', sysdate(), sysdate());

insert into passport(id, number) values (4001, 'E123654');
insert into passport(id, number) values (4002, 'J764293');
insert into passport(id, number) values (4003, 'Z323643');

insert into student(id, name, passport_id) values (2001, 'Ranga', 4002);
insert into student(id, name, passport_id) values (2002, 'Janusz', 4001);
insert into student(id, name, passport_id) values (2003, 'Zenon', 4003);

insert into review(id, rating, description) values (5001, '5', 'Great course');
insert into review(id, rating, description) values (5002, '4', 'Average');
insert into review(id, rating, description) values (5003, '2', 'Boring at all');
