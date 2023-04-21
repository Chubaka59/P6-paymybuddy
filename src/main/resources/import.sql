insert into user_account(ID,  FIRST_NAME, LAST_NAME, EMAIL, PASSWORD, BANK, BALANCE) values (1, 'Joffrey', 'Lefebvre', 'joffrey.lefebvre@gmail.com', '$2a$12$aExfPLQMwuoAn/QmeDRi2uPO9abXdoouLtuDpQH9inbOEtVU7DAhO', 'bankjoffrey', 200);
insert into user_account(ID,  FIRST_NAME, LAST_NAME, EMAIL, PASSWORD, BANK, BALANCE) values (2, 'spider', 'man', 'spiderman@marvel.com', '$2a$12$aExfPLQMwuoAn/QmeDRi2uPO9abXdoouLtuDpQH9inbOEtVU7DAhO', 'bankspiderman', 0);
insert into user_account(ID,  FIRST_NAME, LAST_NAME, EMAIL, PASSWORD, BANK, BALANCE) values (3, 'Captain', 'America', 'captain@marvel.com', '$2a$12$aExfPLQMwuoAn/QmeDRi2uPO9abXdoouLtuDpQH9inbOEtVU7DAhO', 'bankcaptain', 0);
insert into user_account(ID,  FIRST_NAME, LAST_NAME, EMAIL, PASSWORD, BANK, BALANCE) values (4, 'Iron', 'man', 'ironman@marvel.com', '$2a$12$aExfPLQMwuoAn/QmeDRi2uPO9abXdoouLtuDpQH9inbOEtVU7DAhO', 'bankironman', 0);
insert into user_account(ID,  FIRST_NAME, LAST_NAME, EMAIL, PASSWORD, BANK, BALANCE) values (5, 'Thor', 'Thor', 'thor@marvel.com', '$2a$12$aExfPLQMwuoAn/QmeDRi2uPO9abXdoouLtuDpQH9inbOEtVU7DAhO', 'bankthor', 0);

insert into transaction(ID, DATE, AMOUNT, DESCRIPTION, CREDITOR_ID, DEBTOR_ID) VALUES (1, '2023-04-07', 99.99, 'test', 2, 1);
insert into transaction(ID, DATE, AMOUNT, DESCRIPTION, CREDITOR_ID, DEBTOR_ID) VALUES (2, '2023-04-07', 99.99, 'test', 1, 2);
insert into transaction(ID, DATE, AMOUNT, DESCRIPTION, CREDITOR_ID, DEBTOR_ID) VALUES (3, '2023-04-05', 99.99, 'test', 1, 4);
insert into transaction(ID, DATE, AMOUNT, DESCRIPTION, CREDITOR_ID, DEBTOR_ID) VALUES (4, '2023-04-10', 200, 'test', 1, 5);
insert into transaction(ID, DATE, AMOUNT, DESCRIPTION, CREDITOR_ID, DEBTOR_ID) VALUES (5, '2023-04-01', 99.99, 'test', 3, 1);
insert into transaction(ID, DATE, AMOUNT, DESCRIPTION, CREDITOR_ID, DEBTOR_ID) VALUES (6, '2023-04-01', 100.99, 'test', 3, 1);

insert into user_account_contact_list(USER_ACCOUNT_ID, CONTACT_LIST_ID) VALUES (1, 2);