-- 注文
create table orders
(
    id           uuid        default gen_random_uuid() not null,
    order_code   varchar(20)                           not null,
    ordered_at   timestamptz                           not null,
    order_status varchar(10)                           not null,
    created_at   timestamptz default CURRENT_TIMESTAMP not null,
    created_by   varchar(50)                           not null,
    updated_at   timestamptz default CURRENT_TIMESTAMP not null,
    updated_by   varchar(50)                           not null,
    deleted_at   timestamptz,
    deleted_by   varchar(50),
    constraint pk_orders primary key (id)
);

comment on table orders is '注文:注文を管理するテーブル';
comment on column orders.id is 'ID';
comment on column orders.order_code is '注文コード:業務向けID（例：ORD-000001）';
comment on column orders.ordered_at is '注文日時';
comment on column orders.order_status is '注文ステータス:001:受付
002:審査中
003:手配中
004:出荷待ち
005:完了
006:キャンセル';
comment on column orders.created_at is '作成日時';
comment on column orders.created_by is '作成者';
comment on column orders.updated_at is '更新日時';
comment on column orders.updated_by is '更新者';
comment on column orders.deleted_at is '削除日時';
comment on column orders.deleted_by is '削除者';
