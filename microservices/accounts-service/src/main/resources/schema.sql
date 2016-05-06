drop table t_account if exists;

create table t_account (ID bigint identity primary key, NUMBER varchar(9),
                        NAME varchar(50) not null, BALANCE decimal(8,2), unique(NUMBER));
                        
ALTER TABLE t_account ALTER COLUMN BALANCE SET DEFAULT 0.0;
