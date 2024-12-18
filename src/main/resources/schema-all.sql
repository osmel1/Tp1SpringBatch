drop table products if exists;

create table products (
    id bigint identity primary key,
    name varchar(100),
    price double
 );