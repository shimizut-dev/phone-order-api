create sequence order_code_seq
    start with 1
    increment by 1;

create function next_order_code()
    returns varchar(20)
    language sql
as
$$
select 'ORD' || lpad(nextval('order_code_seq')::text, 6, '0');
$$;
