create table products
(
    id          bigserial
        constraint goods_pk
            primary key,
    name        varchar(50)    not null,
    category    varchar(50)    not null,
    amount      INTEGER not null,
    price      NUMERIC (18,2) not null,
    date        timestamp      not null
);
