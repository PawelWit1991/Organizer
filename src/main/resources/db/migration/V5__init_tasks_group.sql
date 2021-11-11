drop table if exists tasks_groups;
create table tasks_groups(
                      id int primary key  auto_increment,
                      description varchar(100) not null,
                      done bit
);

alter table TASKS add column tasks_group_id int null;
alter table TASKS add foreign key (tasks_group_id) references tasks_groups(id);