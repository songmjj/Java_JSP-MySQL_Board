create database board default character set utf8;

create user 'team5'@'localhost' identified by '1234';
create user 'team5'@'%' identified by '1234';

grant all privileges on board.* to 'team5'@'localhost';
grant all privileges on board.* to 'team5'@'%';
flush privileges;


create table board.member (
    memberid varchar(50) primary key,
    name varchar(50) not null,
    password varchar(10) not null,
    regdate datetime not null
) engine=InnoDB default character set = utf8;

use board;

create table board.article(
article_no int auto_increment primary key,
writer_id varchar(50) not null,
writer_name varchar(50) not null,
title varchar(255) not null,
regdate datetime not null,
moddate datetime not null,
read_cnt int
) engine=InnoDB default character set = utf8;

create table board.article_content(
article_no int primary key,
content text
)engine=InnoDB default character set = utf8;

select * from board.member;
select * from article_content;
select * from article;


-- 참고 내용
select version(); -- MySQL DB 버전 정보
select user(); -- 계정정보

use mysql;
select user,host from user; -- MySQL DB에 있는 모든 user,host정보

select now(); -- 현재시간 
select current_date(); -- 현재 날짜
show databases; -- MySQL DB에 있는 모든 databases의 정보

use board;
show tables; -- board의 모든 table 정보
