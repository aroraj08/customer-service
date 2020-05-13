

drop table if exists address CASCADE;
drop table if exists customer CASCADE ;
drop sequence if exists hibernate_sequence;
drop sequence if exists id_seq;
create sequence hibernate_sequence start with 1 increment by 1;
create sequence id_seq start with 1 increment by 50;
create table address (id bigint not null, address_line1 varchar(255), address_line2 varchar(255), city varchar(255), country varchar(255), customer_id bigint, state varchar(255), zip_code varchar(255), primary key (id));
create table customer (id bigint not null, customer_id bigint, first_name varchar(255), last_name varchar(255), primary key (id));
alter table customer add constraint UK_jt63q2suy91q2uch0ll9wcxx5 unique (customer_id);