-- liquibase formatted sql

-- changeset fsommer:1723033313807-1
CREATE TABLE skima_user
(
    id        BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    version   BIGINT                                  NOT NULL,
    username  VARCHAR(255),
    firstname VARCHAR(255),
    lastname  VARCHAR(255),
    email     VARCHAR(255),
    CONSTRAINT pk_skimauser PRIMARY KEY (id)
);

