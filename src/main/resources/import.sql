insert into user_account( FIRST_NAME, LAST_NAME, EMAIL, PASSWORD, BANK, BALANCE) values ( 'Joffrey', 'Lefebvre', 'joffrey.lefebvre@gmail.com', '$2a$12$aExfPLQMwuoAn/QmeDRi2uPO9abXdoouLtuDpQH9inbOEtVU7DAhO', 'bankjoffrey', 200);
insert into user_account( FIRST_NAME, LAST_NAME, EMAIL, PASSWORD, BANK, BALANCE) values ('spider', 'man', 'spiderman@marvel.com', 'azerty123', 'bankspiderman', 0);

insert into transaction(ID, DATE, AMOUNT, DESCRIPTION, CREDITOR, DEBTOR) VALUES (1, '2023-04-07', 99.99, 'test', 'spiderman', 'Joffrey');

insert into user_account_contact_list(USER_ACCOUNT_ID, CONTACT_LIST_ID) VALUES (1, 2);