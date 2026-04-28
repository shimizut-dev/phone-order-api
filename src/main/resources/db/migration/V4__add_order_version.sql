-- 既存データなし前提: orders テーブルを version 列付きで再作成
drop table orders;

create table orders
(
    id           uuid                                  not null,
    order_code   varchar(20)                           not null,
    ordered_at   timestamptz                           not null,
    order_status varchar(10)                           not null,
    version      bigint      default 0                 not null,
    created_at   timestamptz default CURRENT_TIMESTAMP not null,
    created_by   varchar(50)                           not null,
    updated_at   timestamptz default CURRENT_TIMESTAMP not null,
    updated_by   varchar(50)                           not null,
    deleted_at   timestamptz,
    deleted_by   varchar(50),
    constraint pk_orders primary key (id)
);

alter table orders
    add constraint uk_orders_order_code unique (order_code);

comment on column orders.version is 'バージョン';
