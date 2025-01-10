-- liquibase formatted sql

-- changeset fsommer:4711-1
insert into skima_role (version, name, permissions) VALUES (0, 'TECH_ADMIN', '["demopoc:*"]')
;
insert into skima_role (version, name, permissions) VALUES (0, 'BASIC', '["my-skill-topics:*"]')
;
