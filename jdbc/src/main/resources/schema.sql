drop table member if exists cascade;
create table member (
member_id varchar(10),
money integer not null default 0,
primary key (member_id)
);


--insert into member(member_id, money) values('m1', 10000);
--insert into member(member_id, money) values('m2', 20000);