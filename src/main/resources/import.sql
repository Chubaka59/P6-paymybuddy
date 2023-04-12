insert into user_account(ID, FIRSTNAME, LASTNAME, MAIL, PASSWORD, BANK_USER_ID) values (1, 'Joffrey', 'Lefebvre', 'joffrey.lefebvre@gmail.com', 'azerty123', 1);
insert into user_account(ID, FIRSTNAME, LASTNAME, MAIL, PASSWORD) values (2, 'spider', 'man', 'spiderman@marvel.com', 'azerty123');

insert into transaction(ID, DATE, AMOUNT, DESCRIPTION, CREDITOR, DEBTOR) VALUES (1, '2023-04-07', 99.99, 'test', 'spiderman', 'Joffrey');

insert into bank(USER_ID, BANK_ACCOUNT, BALANCE) VALUES (1, 'banktest', 200);

insert into user_account_contact_list(USER_ACCOUNT_ID, CONTACT_LIST_ID) VALUES (1, 2);