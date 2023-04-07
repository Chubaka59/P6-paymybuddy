create database paymybuddy;
use paymybuddy;

create table user(
    ID int primary key AUTO_INCREMENT,
    FIRSTNAME varchar(30) NOT NULL,
    LASTNAME varchar(30) NOT NULL,
    MAIL varchar(100) NOT NULL,
    PASSWORD varchar(20) NOT NULL
);

create table transaction(
    ID int primary key AUTO_INCREMENT,
    DATE DATE NOT NULL,
    AMOUNT DECIMAL(7,2) NOT NULL,
    DESCRIPTION VARCHAR(300),
    CREDITOR VARCHAR(60) NOT NULL ,
    DEBTOR VARCHAR(60) NOT NULL
);

create table bank(
    USER_ID int primary key NOT NULL,
     BANK_ACCOUNT VARCHAR(20) NOT NULL,
     BALANCE DECIMAL(7,2) NOT NULL,
     foreign key (USER_ID) references user (ID) on delete CASCADE
);

create table user_transaction(
    DEBTOR_ID int NOT NULL,
    TRANSACTION_ID int NOT NULL,
    foreign key (DEBTOR_ID) references user (ID) on delete RESTRICT on update CASCADE,
    foreign key (TRANSACTION_ID) references transaction (ID) on delete RESTRICT on update CASCADE,
    primary key (DEBTOR_ID, TRANSACTION_ID)
);

create table user_contact(
  USER_ID int NOT NULL,
  CONTACT_ID int NOT NULL,
  foreign key (USER_ID) references user (ID) on delete RESTRICT on update CASCADE,
  foreign key (CONTACT_ID) references user (ID) on delete RESTRICT on update CASCADE,
  primary key (USER_ID, CONTACT_ID)
);

insert into user(FIRSTNAME, LASTNAME, MAIL, PASSWORD) values ('Joffrey', 'Lefebvre', 'joffrey.lefebvre@gmail.com', 'azerty123');
insert into user(FIRSTNAME, LASTNAME, MAIL, PASSWORD) values ('spider', 'man', 'spiderman@marvel.com', 'azerty123');

insert into transaction(DATE, AMOUNT, DESCRIPTION, CREDITOR, DEBTOR) VALUES ('2023-04-07', 99.99, 'test', 'spiderman', 'Joffrey');

insert into bank(USER_ID, BANK_ACCOUNT, BALANCE) VALUES (1, 'banktest', 200);

insert into user_transaction(DEBTOR_ID, TRANSACTION_ID) VALUES (1, 1);

insert into user_contact(USER_ID, CONTACT_ID) VALUES (1, 2);