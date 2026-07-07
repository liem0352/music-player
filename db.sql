-- 创建数据库
drop database if exists onlinemusic;
create database if not exists onlinemusic character set utf8;

-- 使用数据库
use onlinemusic;

-- 创建 user 表
drop table if exists user;
create table if not exists user(
    uid int(11) primary key auto_increment,
    username varchar(20) not null,
    password varchar(255) not null,
    status boolean default false         -- 用户权限
);

-- 创建 music 表
drop table if exists music;
create table if not exists music(
    mid int(11) primary key auto_increment,
    mname varchar(50) not null,
    msinger varchar(30) not null,
    createtime timestamp  default now(),
    url varchar(1000) not null,
    uid int(11) not null
);

-- 创建 user 和 music 的中间表
drop table if exists lovemusic;
create table if not exists lovemusic(
    love_id int(11) primary key auto_increment,
    user_id int(11) not null,
    music_id int(11) not null
);
