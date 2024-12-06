--liquibase formatted sql
--changeset K.Kormilcev:task1 logicalFilePath:01.000.00/currency.sql
CREATE TABLE IF NOT EXISTS currency
(
    currency_id     BIGSERIAL NOT NULL,
    currency        VARCHAR   NOT NULL,
    label           VARCHAR   NOT NULL,
    disabled        BOOLEAN,
    CONSTRAINT pk_currency PRIMARY KEY (currency_id),
    CONSTRAINT uc_currency UNIQUE (currency)
);
