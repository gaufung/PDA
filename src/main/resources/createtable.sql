use industry;
drop table co2;
create table co2(
    id int(4) not null primary key auto_increment,
    province varchar(10) not null,
    year char(4) not null,
    coal double(20,8) default 0.00,
    refine_coal double(20, 8) default 0.00,
    other_coal double(20, 8) default 0.00,
    briquette double(20, 8) default 0.00,
    coke double(20, 8) default 0.00,
    coke_gas double(20, 8) default 0.00,
    other_gas double(20, 8) default 0.00,
    crude double(20, 8) default 0.00,
    petrol double(20, 8) default 0.00,
    kerosene double(20, 8) default 0.00,
    diesel double(20, 8) default 0.00,
    fuel double(20, 8) default 0.00,
    liquefied double(20, 8) default 0.00,
    refine_gas double(20, 8) default 0.00,
    gas double(20, 8) default 0.00,
    heat double(20, 8) default 0.00,
    electricity double(20,8) default 0.00
) default charset=utf8;

drop table energy;
create table energy(
    id int(4) not null primary key auto_increment,
    province varchar(10) not null,
    year char(4) not null,
    coal double(20,8) default 0.00,
    refine_coal double(20, 8) default 0.00,
    other_coal double(20, 8) default 0.00,
    briquette double(20, 8) default 0.00,
    coke double(20, 8) default 0.00,
    coke_gas double(20, 8) default 0.00,
    other_gas double(20, 8) default 0.00,
    crude double(20, 8) default 0.00,
    petrol double(20, 8) default 0.00,
    kerosene double(20, 8) default 0.00,
    diesel double(20, 8) default 0.00,
    fuel double(20, 8) default 0.00,
    liquefied double(20, 8) default 0.00,
    refine_gas double(20, 8) default 0.00,
    gas double(20, 8) default 0.00,
    heat double(20, 8) default 0.00,
    electricity double(20, 8) default 0.00
) default charset=utf8;

drop table cef;
create table cef(
    id int(4) not null primary key auto_increment,
    province varchar(10) not null,
    year char(4) not null,
    coal double(20,8) default 0.00,
    refine_coal double(20, 8) default 0.00,
    other_coal double(20, 8) default 0.00,
    briquette double(20, 8) default 0.00,
    coke double(20, 8) default 0.00,
    coke_gas double(20, 8) default 0.00,
    other_gas double(20, 8) default 0.00,
    crude double(20, 8) default 0.00,
    petrol double(20, 8) default 0.00,
    kerosene double(20, 8) default 0.00,
    diesel double(20, 8) default 0.00,
    fuel double(20, 8) default 0.00,
    liquefied double(20, 8) default 0.00,
    refine_gas double(20, 8) default 0.00,
    gas double(20, 8) default 0.00,
    heat double(20, 8) default 0.00,
    electricity double(20, 8) default 0.00
) default charset=utf8;

drop table production;

create table production(
    id int(4) not null primary key auto_increment,
    province varchar(10) not null,
    year char(4) not null,
    prod double(20,8) default 0.00
)default charset=utf8;