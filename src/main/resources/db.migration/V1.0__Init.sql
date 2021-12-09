create table goods
(
    id          bigserial
        constraint goods_pk
            primary key,
    name        varchar(50)    not null,
    category    varchar(50)    not null,
    date        timestamp      not null,
);
