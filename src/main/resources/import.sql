insert into user_account( FIRST_NAME, LAST_NAME, EMAIL, PASSWORD, BANK, BALANCE) values ( 'Joffrey', 'Lefebvre', 'joffrey.lefebvre@gmail.com', '$2a$12$aExfPLQMwuoAn/QmeDRi2uPO9abXdoouLtuDpQH9inbOEtVU7DAhO', 'bankjoffrey', 200);
insert into user_account( FIRST_NAME, LAST_NAME, EMAIL, PASSWORD, BANK, BALANCE) values ('spider', 'man', 'spiderman@marvel.com', 'azerty123', 'bankspiderman', 0);

insert into transaction(ID, DATE, AMOUNT, DESCRIPTION, CREDITOR, DEBTOR) VALUES (1, '2023-04-07', 99.99, 'test', 'spider man', 'Joffrey Lefebvre');
insert into transaction(ID, DATE, AMOUNT, DESCRIPTION, CREDITOR, DEBTOR) VALUES (2, '2023-04-07', 99.99, 'test', 'Joffrey Lefebvre', 'Spider Man');
insert into transaction(ID, DATE, AMOUNT, DESCRIPTION, CREDITOR, DEBTOR) VALUES (3, '2023-04-05', 99.99, 'test', 'Joffrey Lefebvre', 'Iron Man');
insert into transaction(ID, DATE, AMOUNT, DESCRIPTION, CREDITOR, DEBTOR) VALUES (4, '2023-04-10', 200, 'test', 'Joffrey Lefebvre', 'Thor');
insert into transaction(ID, DATE, AMOUNT, DESCRIPTION, CREDITOR, DEBTOR) VALUES (5, '2023-04-01', 99.99, 'test', 'Captain America', 'Joffrey Lefebvre');

insert into user_account_contact_list(USER_ACCOUNT_ID, CONTACT_LIST_ID) VALUES (1, 2);