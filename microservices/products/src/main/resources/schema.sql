drop table t_product if exists;

create table t_product (ID bigint identity primary key, NUMBER varchar(8),
                        NAME varchar(50) not null, PRICE decimal(8,2), unique(NUMBER));