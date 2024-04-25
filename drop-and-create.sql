
    drop table if exists beer;

    drop table if exists customer;

    create table beer (
        price decimal(38,2) not null,
        quantity_on_hand integer,
        style tinyint not null check (style between 0 and 9),
        version integer,
        created_at datetime(6),
        updated_at datetime(6),
        id varchar(36) not null,
        name varchar(50) not null,
        upc varchar(255) not null,
        primary key (id)
    ) engine=InnoDB;

    create table customer (
        version integer,
        created_at datetime(6),
        updated_at datetime(6),
        id varchar(36) not null,
        name varchar(255),
        primary key (id)
    ) engine=InnoDB;

    drop table if exists beer;

    drop table if exists customer;

    create table beer (
        price decimal(38,2) not null,
        quantity_on_hand integer,
        style tinyint not null check (style between 0 and 9),
        version integer,
        created_at datetime(6),
        updated_at datetime(6),
        id varchar(36) not null,
        name varchar(50) not null,
        upc varchar(255) not null,
        primary key (id)
    ) engine=InnoDB;

    create table customer (
        version integer,
        created_at datetime(6),
        updated_at datetime(6),
        id varchar(36) not null,
        name varchar(255),
        primary key (id)
    ) engine=InnoDB;
