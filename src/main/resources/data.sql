insert into course(id, name, created, updated, is_deleted) values (10001, 'JPA in 50 steps', sysdate(), sysdate(), false);
insert into course(id, name, created, updated, is_deleted) values (10002, 'Dupa in 12 steps', sysdate(), sysdate(), false);
insert into course(id, name, created, updated, is_deleted) values (10003, 'Spring in 19 steps', sysdate(), sysdate(), false);

insert into passport(id, number) values (4001, 'E123654');
insert into passport(id, number) values (4002, 'J764293');
insert into passport(id, number) values (4003, 'Z323643');

insert into student(id, name, passport_id, city, line1, line2) values (2001, 'Ranga', 4002, 'Gdynia', 'Badennerstrasse', '8');
insert into student(id, name, passport_id, city, line1, line2) values (2002, 'Janusz', 4001, 'Zurich', 'Altestrasse', '4');
insert into student(id, name, passport_id, city, line1, line2) values (2003, 'Zenon', 4003, 'Kopenhaga', 'Shonestrasse', '57');

insert into review(id, review_rating, description, course_id) values (5001, 'ONE', 'Great course', 10001);
insert into review(id, review_rating, description, course_id) values (5002, 'TWO', 'Average', 10001);
insert into review(id, review_rating, description, course_id) values (5003, 'THREE', 'Boring at all', 10003);

insert into student_course(student_id, course_id) values(2001, 10001);
insert into student_course(student_id, course_id) values(2001, 10002);
insert into student_course(student_id, course_id) values(2001, 10002);
insert into student_course(student_id, course_id) values(2002, 10001);
insert into student_course(student_id, course_id) values(2002, 10001);
insert into student_course(student_id, course_id) values(2003, 10001);
