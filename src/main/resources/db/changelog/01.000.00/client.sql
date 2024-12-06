--liquibase formatted sql
--changeset K.Kormilcev:task1 logicalFilePath:01.000.00/client.sql
CREATE TABLE IF NOT EXISTS client
(
    client_id  BIGSERIAL NOT NULL,
    surname    VARCHAR   NOT NULL,
    name       VARCHAR   NOT NULL,
    patronymic VARCHAR   NOT NULL,
    phone      VARCHAR   NOT NULL,
    inn        VARCHAR   NOT NULL,
    address    VARCHAR   NOT NULL,
    passport_scan bytea,
    CONSTRAINT pk_client PRIMARY KEY (client_id),
    CONSTRAINT uc_client_phone UNIQUE (phone)
);