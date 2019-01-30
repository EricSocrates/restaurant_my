##SQL脚本文件
drop table if exists admin;
drop table if exists tables;

create table admin(
  id int primary key auto_increment,
  username varchar(32),
  pwd varchar(32),
  email varchar(30),
  phone varchar(11),
  savepath varchar(200),
  showname varchar(200),
  createtime timestamp,
  updatetime timestamp
);
create table tables(
  id int primary key auto_increment,
  no varchar(10),
  num int,
  status int, ## 0 空闲  1 满员
  createtime timestamp,
  updatetime timestamp
);
