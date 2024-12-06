--liquibase formatted sql
--changeset K.Kormilcev:task1 logicalFilePath:01.000.00/currency-insert.sql
INSERT INTO currency (currency_id, currency, label, disabled)
VALUES (1, 'RUB', 'Рубли', null),
       (2, 'USD', 'Доллары', null),
       (3, 'BTC', 'Биткоины', true),
       (4, 'EUR', 'Евро', null);
