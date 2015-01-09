drop table if exists CAMEL_MESSAGEPROCESSED;
drop table if exists HouseInfo;
drop table if exists CustInfo;

-- required by Camel JdbcMessageIdRepository
create table CAMEL_MESSAGEPROCESSED (
    processorName varchar(255),
    messageId varchar(100),
    createdAt timestamp
);

create table HouseInfo (
	nationalID varchar(100) NOT NULL,
	address varchar(200),
	bedroom integer,
	bathroom integer,
	landSize integer,
	appraisedValue integer
);

create table CustInfo (
    nationalID varchar(100) NOT NULL,
    firstName varchar(200),
    lastName varchar(200),
    age integer,
    occupation varchar(255)
);

commit;
