--roles
INSERT INTO roles(name)
VALUES ('ROLE_USER')
on conflict do nothing;
INSERT INTO roles(name)
VALUES ('ROLE_ADMIN')
on conflict do nothing;

--regTypes
INSERT INTO regtypes(name)
VALUES ('DEFAULT')
on conflict do nothing;
INSERT INTO regtypes(name)
VALUES ('VK')
on conflict do nothing;
INSERT INTO regtypes(name)
VALUES ('GOOGLE')
on conflict do nothing;
--RiskType
INSERT INTO risktypes(id, type)
VALUES (1, 'Analysis')
on conflict do nothing;
INSERT INTO risktypes(id, type)
VALUES (2, 'Requirements')
on conflict do nothing;
INSERT INTO risktypes(id, type)
VALUES (3, 'Software')
on conflict do nothing;
INSERT INTO risktypes(id, type)
VALUES (4, 'Support')
on conflict do nothing;
INSERT INTO risktypes(id, type)
VALUES (5, 'Technical')
on conflict do nothing;
INSERT INTO risktypes(id, type)
VALUES (6, 'Customer')
on conflict do nothing;
INSERT INTO risktypes(id, type)
VALUES (7, 'Functional')
on conflict do nothing;
INSERT INTO risktypes(id, type)
VALUES (8, 'Managerial')
on conflict do nothing;
--Risks
insert into risks(id, name, description, type_id)
values (1, 'risk_1', 'description_1', 1)
on conflict do nothing;
insert into risks(id, name, description, type_id)
values (2, 'risk_2', 'description_2', 2)
on conflict do nothing;
insert into risks(id, name, description, type_id)
values (4, 'risk_3', 'description_3', 3)
on conflict do nothing;


