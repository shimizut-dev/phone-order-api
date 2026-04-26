alter table orders
    add constraint uk_orders_order_code unique (order_code);
