drop database if exists simple_library;

CREATE DATABASE IF NOT EXISTS simple_library;

use simple_library;

drop table if exists books;
drop table if exists users;

create table if not exists users
(
    id int primary key auto_increment,
    first_name varchar(20) not null,
    last_name varchar(50) not null,
    phone_number varchar(15) not null
);

create table if not exists books
(
    id int primary key auto_increment,
    title varchar(100) not null,
    user_id int,
    isbn varchar(20) not null,
    constraint book_owner_fk
        foreign key (user_id) references users (id)
);

insert into users(first_name, last_name, phone_number) values
('Petro', 'Poroshenko', '111'),
('Leonid', 'Kuchma', '222'),
('Leonid', 'Kravchuk', '333'),
('Volodymyr', 'Zelensky', '444'),
('Viktor', 'Yanukovych', '555'),
('Viktor', 'Yushchenko', '666');

insert into books(title, user_id, isbn) values
('Konstytuciya', 1, '123'),
('Ugolovna sprava', 5, '456'),
('Diia', 4, '789'),
('Zakon', NULL, '000');