drop table if exists TASKS2;
create table tasks2(
                      id int primary key  auto_increment,
                      description varchar(100) not null,
                      done bit
)