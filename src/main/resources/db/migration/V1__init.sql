-- Project Name : phone-order-api
-- Date/Time    : 2026/04/04 14:45:02
-- Author       : shimizut-dev
-- RDBMS Type   : PostgreSQL
-- Application  : A5:SQL Mk-2

-- 配送履歴
create table delivery_histories (
                                    id uuid default gen_random_uuid() not null
    , delivery_id uuid not null
    , delivery_status varchar(10) not null
    , created_at timestamptz default CURRENT_TIMESTAMP not null
    , created_by varchar(50) not null
    , updated_at timestamptz default CURRENT_TIMESTAMP not null
    , updated_by varchar(50) not null
    , deleted_at timestamptz
    , deleted_by varchar(50)
    , constraint pk_delivery_histories primary key (id)
) ;

-- 注文履歴
create table order_histories (
                                 id uuid default gen_random_uuid() not null
    , order_id uuid not null
    , order_status varchar(10) not null
    , created_at timestamptz default CURRENT_TIMESTAMP not null
    , created_by varchar(50) not null
    , updated_at timestamptz default CURRENT_TIMESTAMP not null
    , updated_by varchar(50) not null
    , deleted_at timestamptz
    , deleted_by varchar(50)
    , constraint pk_order_histories primary key (id)
) ;

-- 注文関係者
create table order_parties (
                               id uuid default gen_random_uuid() not null
    , order_id uuid not null
    , order_party_code varchar(20) not null
    , order_party_role_division varchar(10) not null
    , order_party_last_name varchar(50) not null
    , order_party_first_name varchar(50) not null
    , order_party_last_name_kana varchar(50) not null
    , order_party_first_name_kana varchar(50) not null
    , order_party_gender_division varchar(10) not null
    , order_party_birth_date date not null
    , order_party_postal_code varchar(7) not null
    , order_party_prefecture varchar(20) not null
    , order_party_city varchar(100) not null
    , order_party_town varchar(100)
    , order_party_street_address varchar(100) not null
    , order_party_building_name varchar(100) not null
    , created_at timestamptz default CURRENT_TIMESTAMP not null
    , created_by varchar(50) not null
    , updated_at timestamptz default CURRENT_TIMESTAMP not null
    , updated_by varchar(50) not null
    , deleted_at timestamptz
    , deleted_by varchar(50)
    , constraint pk_order_parties primary key (id)
) ;

-- 配送明細
create table delivery_lines (
                                id uuid default gen_random_uuid() not null
    , delivery_id uuid not null
    , delivery_line_code varchar(20) not null
    , order_line_id uuid not null
    , delivery_quantity integer not null
    , created_at timestamptz default CURRENT_TIMESTAMP not null
    , created_by varchar(50) not null
    , updated_at timestamptz default CURRENT_TIMESTAMP not null
    , updated_by varchar(50) not null
    , deleted_at timestamptz
    , deleted_by varchar(50)
    , constraint pk_delivery_lines primary key (id)
) ;

-- 配送
create table deliveries (
                            id uuid default gen_random_uuid() not null
    , order_id uuid not null
    , delivery_code varchar(20) not null
    , delivery_status varchar(10) not null
    , recipient_last_name varchar(50) not null
    , recipient_first_name varchar(50) not null
    , recipient_last_name_kana varchar(50) not null
    , recipient_first_name_kana varchar(50)
    , delivery_postal_code varchar(7) not null
    , delivery_prefecture varchar(20) not null
    , delivery_city varchar(100) not null
    , delivery_town varchar(100) not null
    , delivery_street_address varchar(100) not null
    , delivery_building_name varchar(100)
    , delivery_time_slot_division varchar(10) not null
    , leave_place_division varchar(10) not null
    , leave_at_place_division varchar(10)
    , created_at timestamptz default CURRENT_TIMESTAMP not null
    , created_by varchar(50) not null
    , updated_at timestamptz default CURRENT_TIMESTAMP not null
    , updated_by varchar(50) not null
    , deleted_at timestamptz
    , deleted_by varchar(50)
    , constraint pk_deliveries primary key (id)
) ;

-- アクセサリ
create table accessories (
                             id uuid default gen_random_uuid() not null
    , order_line_id uuid not null
    , accessory_code varchar(20) not null
    , created_at timestamptz default CURRENT_TIMESTAMP not null
    , created_by varchar(50) not null
    , updated_at timestamptz default CURRENT_TIMESTAMP not null
    , updated_by varchar(50) not null
    , deleted_at timestamptz
    , deleted_by varchar(50)
    , constraint pk_accessories primary key (id)
) ;

-- 移動機
create table phones (
                        id uuid default gen_random_uuid() not null
    , order_line_id uuid not null
    , phone_code varchar(20) not null
    , imei varchar(15) not null
    , created_at timestamptz default CURRENT_TIMESTAMP not null
    , created_by varchar(50) not null
    , updated_at timestamptz default CURRENT_TIMESTAMP not null
    , updated_by varchar(50) not null
    , deleted_at timestamptz
    , deleted_by varchar(50)
    , constraint pk_phones primary key (id)
) ;

-- 回線
create table lines (
                       id uuid default gen_random_uuid() not null
    , order_line_id uuid not null
    , line_code varchar(20) not null
    , line_contract_division varchar(10) not null
    , msisdn varchar(15) not null
    , created_at timestamptz default CURRENT_TIMESTAMP not null
    , created_by varchar(50) not null
    , updated_at timestamptz default CURRENT_TIMESTAMP not null
    , updated_by varchar(50) not null
    , deleted_at timestamptz
    , deleted_by varchar(50)
    , constraint pk_lines primary key (id)
) ;

-- 注文明細
create table order_lines (
                             id uuid default gen_random_uuid() not null
    , order_id uuid not null
    , order_line_code varchar(20) not null
    , order_line_division varchar(10) not null
    , order_quantity integer not null
    , created_at timestamptz default CURRENT_TIMESTAMP not null
    , created_by varchar(50) not null
    , updated_at timestamptz default CURRENT_TIMESTAMP not null
    , updated_by varchar(50) not null
    , deleted_at timestamptz
    , deleted_by varchar(50)
    , constraint pk_order_lines primary key (id)
) ;

-- 注文
create table orders (
                        id uuid default gen_random_uuid() not null
    , order_code varchar(20) not null
    , ordered_at timestamptz not null
    , order_status varchar(10) not null
    , created_at timestamptz default CURRENT_TIMESTAMP not null
    , created_by varchar(50) not null
    , updated_at timestamptz default CURRENT_TIMESTAMP not null
    , updated_by varchar(50) not null
    , deleted_at timestamptz
    , deleted_by varchar(50)
    , constraint pk_orders primary key (id)
) ;

alter table delivery_histories
    add constraint fk_delivery_histories_1 foreign key (delivery_id) references deliveries(id);

alter table order_histories
    add constraint fk_order_histories_1 foreign key (order_id) references orders(id);

alter table order_parties
    add constraint fk_order_parties_1 foreign key (order_id) references orders(id);

alter table delivery_lines
    add constraint fk_delivery_lines_1 foreign key (delivery_id) references deliveries(id);

alter table deliveries
    add constraint fk_deliveries_1 foreign key (order_id) references orders(id);

alter table accessories
    add constraint fk_accessories_1 foreign key (order_line_id) references order_lines(id);

alter table phones
    add constraint fk_phones_1 foreign key (order_line_id) references order_lines(id);

alter table lines
    add constraint fk_lines_1 foreign key (order_line_id) references order_lines(id);

alter table order_lines
    add constraint fk_order_lines_1 foreign key (order_id) references orders(id);

comment on table delivery_histories is '配送履歴:配送履歴を管理するテーブル';
comment on column delivery_histories.id is 'ID';
comment on column delivery_histories.delivery_id is '配送ID';
comment on column delivery_histories.delivery_status is '配送ステータス:配送ステータス
001:未出荷
002:出荷準備中
003:出荷済
004:配送中
005:配送完了
006:配送失敗
007:キャンセル';
comment on column delivery_histories.created_at is '作成日時';
comment on column delivery_histories.created_by is '作成者';
comment on column delivery_histories.updated_at is '更新日時';
comment on column delivery_histories.updated_by is '更新者';
comment on column delivery_histories.deleted_at is '削除日時';
comment on column delivery_histories.deleted_by is '削除者';

comment on table order_histories is '注文履歴:注文履歴を管理するテーブル';
comment on column order_histories.id is 'ID';
comment on column order_histories.order_id is '注文ID';
comment on column order_histories.order_status is '注文ステータス:001:受付
002:審査中
003:手配中
004:出荷待ち
005:完了
006:キャンセル';
comment on column order_histories.created_at is '作成日時';
comment on column order_histories.created_by is '作成者';
comment on column order_histories.updated_at is '更新日時';
comment on column order_histories.updated_by is '更新者';
comment on column order_histories.deleted_at is '削除日時';
comment on column order_histories.deleted_by is '削除者';

comment on table order_parties is '注文関係者:注文関係者を管理するテーブル';
comment on column order_parties.id is 'ID';
comment on column order_parties.order_id is '注文ID';
comment on column order_parties.order_party_code is '注文関係者コード:業務向けID（例：ORD-000001）';
comment on column order_parties.order_party_role_division is '注文関係者役割区分:001：注文者
002：契約者
003：利用者
004：請求者';
comment on column order_parties.order_party_last_name is '注文関係者性';
comment on column order_parties.order_party_first_name is '注文関係者名';
comment on column order_parties.order_party_last_name_kana is '注文関係者性（カナ）';
comment on column order_parties.order_party_first_name_kana is '注文関係者名（カナ）';
comment on column order_parties.order_party_gender_division is '注文関係者性別区分:001:男性
002:女性
003:その他
009:不明';
comment on column order_parties.order_party_birth_date is '注文関係者生年月日';
comment on column order_parties.order_party_postal_code is '注文関係者郵便番号:ハイフンなし（例：123-4567）';
comment on column order_parties.order_party_prefecture is '注文関係者都道府県';
comment on column order_parties.order_party_city is '注文関係者市区町村';
comment on column order_parties.order_party_town is '注文関係者町域・大字';
comment on column order_parties.order_party_street_address is '注文関係者番地';
comment on column order_parties.order_party_building_name is '注文関係者建物名';
comment on column order_parties.created_at is '作成日時';
comment on column order_parties.created_by is '作成者';
comment on column order_parties.updated_at is '更新日時';
comment on column order_parties.updated_by is '更新者';
comment on column order_parties.deleted_at is '削除日時';
comment on column order_parties.deleted_by is '削除者';

comment on table delivery_lines is '配送明細:配送明細を管理するテーブル';
comment on column delivery_lines.id is 'ID';
comment on column delivery_lines.delivery_id is '配送ID';
comment on column delivery_lines.delivery_line_code is '配送明細コード:業務向けID（例：DLV-000001）';
comment on column delivery_lines.order_line_id is '注文明細ID';
comment on column delivery_lines.delivery_quantity is '配送数量:1以上';
comment on column delivery_lines.created_at is '作成日時';
comment on column delivery_lines.created_by is '作成者';
comment on column delivery_lines.updated_at is '更新日時';
comment on column delivery_lines.updated_by is '更新者';
comment on column delivery_lines.deleted_at is '削除日時';
comment on column delivery_lines.deleted_by is '削除者';

comment on table deliveries is '配送:配送を管理するテーブル';
comment on column deliveries.id is 'ID';
comment on column deliveries.order_id is '注文ID';
comment on column deliveries.delivery_code is '配送コード:業務向けID（例：DLV-000001）';
comment on column deliveries.delivery_status is '配送ステータス:001:未出荷
002:出荷準備中
003:出荷済
004:配送中
005:配送完了
006:配送失敗
007:キャンセル';
comment on column deliveries.recipient_last_name is '受取人性';
comment on column deliveries.recipient_first_name is '受取人名';
comment on column deliveries.recipient_last_name_kana is '受取人性(カナ)';
comment on column deliveries.recipient_first_name_kana is '受取人名(カナ)';
comment on column deliveries.delivery_postal_code is '配送先郵便番号:ハイフンなし（例：123-4567）';
comment on column deliveries.delivery_prefecture is '配送先都道府県';
comment on column deliveries.delivery_city is '配送先市区町村';
comment on column deliveries.delivery_town is '配送先町域・大字';
comment on column deliveries.delivery_street_address is '配送先番地';
comment on column deliveries.delivery_building_name is '配送先建物名';
comment on column deliveries.delivery_time_slot_division is '配送希望時間帯区分:001：指定なし
002：午前中
003：12時〜14時
004：14時〜16時
005：16時〜18時
006：18時〜20時
007：19時〜21時';
comment on column deliveries.leave_place_division is '置き配区分:001:置き配不可
002:置き配可（場所指定あり）
003:置き配可（場所指定なし）';
comment on column deliveries.leave_at_place_division is '置き配場所区分:001：玄関前
002：宅配ボックス
003：ガスメーターボックス
004：車庫
005：その他';
comment on column deliveries.created_at is '作成日時';
comment on column deliveries.created_by is '作成者';
comment on column deliveries.updated_at is '更新日時';
comment on column deliveries.updated_by is '更新者';
comment on column deliveries.deleted_at is '削除日時';
comment on column deliveries.deleted_by is '削除者';

comment on table accessories is 'アクセサリ:アクセサリを管理するテーブル';
comment on column accessories.id is 'ID';
comment on column accessories.order_line_id is '注文明細ID';
comment on column accessories.accessory_code is 'アクセサリコード:業務向けID（例：ACC-000001）';
comment on column accessories.created_at is '作成日時';
comment on column accessories.created_by is '作成者';
comment on column accessories.updated_at is '更新日時';
comment on column accessories.updated_by is '更新者';
comment on column accessories.deleted_at is '削除日時';
comment on column accessories.deleted_by is '削除者';

comment on table phones is '移動機:移動機を管理するテーブル';
comment on column phones.id is 'ID';
comment on column phones.order_line_id is '注文明細ID';
comment on column phones.phone_code is '移動機コード:業務向けID（例：LIN-000001）';
comment on column phones.imei is 'IMEI:数値（例：356789012345678）';
comment on column phones.created_at is '作成日時';
comment on column phones.created_by is '作成者';
comment on column phones.updated_at is '更新日時';
comment on column phones.updated_by is '更新者';
comment on column phones.deleted_at is '削除日時';
comment on column phones.deleted_by is '削除者';

comment on table lines is '回線:回線を管理するテーブル';
comment on column lines.id is 'ID';
comment on column lines.order_line_id is '注文明細ID';
comment on column lines.line_code is '回線コード:業務向けID（例：LIN-000001）';
comment on column lines.line_contract_division is '回線契約区分:001：新規契約
002：MNP転入
003：既存回線追加（追加契約）';
comment on column lines.msisdn is 'MSISDN:数値ハイフンなし（例：09012345678）';
comment on column lines.created_at is '作成日時';
comment on column lines.created_by is '作成者';
comment on column lines.updated_at is '更新日時';
comment on column lines.updated_by is '更新者';
comment on column lines.deleted_at is '削除日時';
comment on column lines.deleted_by is '削除者';

comment on table order_lines is '注文明細:注文明細を管理するテーブル';
comment on column order_lines.id is 'ID';
comment on column order_lines.order_id is '注文ID';
comment on column order_lines.order_line_code is '注文明細コード:業務向けID（例：ODT-000001）';
comment on column order_lines.order_line_division is '注文明細区分:001：回線
002：移動機
003：アクセサリ';
comment on column order_lines.order_quantity is '注文数量:1以上';
comment on column order_lines.created_at is '作成日時';
comment on column order_lines.created_by is '作成者';
comment on column order_lines.updated_at is '更新日時';
comment on column order_lines.updated_by is '更新者';
comment on column order_lines.deleted_at is '削除日時';
comment on column order_lines.deleted_by is '削除者';

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

