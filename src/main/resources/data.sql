--roles
INSERT INTO roles(name) VALUES('ROLE_USER') on conflict do nothing ;
INSERT INTO roles(name) VALUES('ROLE_ADMIN') on conflict  do nothing ;

--regTypes
INSERT INTO regtypes(name) VALUES('DEFAULT') on conflict  do nothing ;
INSERT INTO regtypes(name) VALUES('VK') on conflict do nothing ;
INSERT INTO regtypes(name) VALUES('GOOGLE') on conflict  do nothing ;
-- --RiskType
-- INSERT INTO risktypes(type) VALUES('Analysis') on conflict do nothing ;
-- INSERT INTO risktypes(type) VALUES('Requirements') on conflict do nothing ;
-- INSERT INTO risktypes(type) VALUES('Software') on conflict do nothing ;
-- INSERT INTO risktypes(type) VALUES('Support') on conflict do nothing ;
-- INSERT INTO risktypes(type) VALUES('Technical') on conflict do nothing ;
-- INSERT INTO risktypes(type) VALUES('Customer') on conflict do nothing ;
-- INSERT INTO risktypes(type) VALUES('Functional') on conflict do nothing ;
-- INSERT INTO risktypes(type) VALUES('Managerial') on conflict do nothing ;
-- --Risks
-- insert into risks(name,description,type_id) values ('risk_1','description_1',1) on conflict  do nothing ;
-- insert into risks(name,description,type_id) values ('risk_2','description_2',2) on conflict  do nothing ;
-- insert into risks(name,description,type_id) values ('risk_3','description_3',3) on conflict  do nothing ;


