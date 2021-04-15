--roles
INSERT INTO roles(name) VALUES('ROLE_USER') on conflict do nothing ;
INSERT INTO roles(name) VALUES('ROLE_ADMIN') on conflict  do nothing ;

--regTypes
INSERT INTO regtypes(name) VALUES('DEFAULT') on conflict  do nothing ;
INSERT INTO regtypes(name) VALUES('VK') on conflict do nothing ;
INSERT INTO regtypes(name) VALUES('GOOGLE') on conflict  do nothing ;

--messageStatus
INSERT INTO messageStatus(name) VALUES('RECEIVED') on conflict do nothing ;
INSERT INTO messageStatus(name) VALUES('DELIVERED') on conflict  do nothing ;

--messageContent
INSERT INTO messagecontenttype(type) VALUES('TEXT') on conflict do nothing ;
INSERT INTO messagecontenttype(type) VALUES('IMAGE') on conflict  do nothing ;

--ChatRoomType
INSERT INTO chatroomtypes(type) VALUES('DIALOG') on conflict do nothing ;
INSERT INTO chatroomtypes(type) VALUES('CHAT') on conflict  do nothing ;

--RiskType
INSERT INTO risktypes(type) VALUES('ONE') on conflict do nothing ;
INSERT INTO risktypes(type) VALUES('TWO') on conflict do nothing ;
INSERT INTO risktypes(type) VALUES('THREE') on conflict do nothing ;
INSERT INTO risktypes(type) VALUES('FOUR') on conflict do nothing ;
INSERT INTO risktypes(type) VALUES('FIVE') on conflict do nothing ;

--RiskState
INSERT INTO riskstates(state) VALUES('BEGIN') on conflict do nothing ;
INSERT INTO riskstates(state) VALUES('END') on conflict do nothing ;
INSERT INTO riskstates(state) VALUES('IN_PROCESS') on conflict do nothing ;

--Risk
INSERT INTO riskdb(description, name) VALUES('description','risk1') on conflict do nothing ;
INSERT INTO riskdb(description, name) VALUES('description','risk2') on conflict do nothing ;
INSERT INTO riskdb(description, name) VALUES('description','risk3') on conflict do nothing ;

--risk-risktypes
INSERT INTO types_risks(risk_id, type_id) VALUES(1,1) on conflict do nothing ;
INSERT INTO types_risks(risk_id, type_id) VALUES(1,2) on conflict do nothing ;
INSERT INTO types_risks(risk_id, type_id) VALUES(1,3) on conflict do nothing ;
INSERT INTO types_risks(risk_id, type_id) VALUES(1,4) on conflict do nothing ;
INSERT INTO types_risks(risk_id, type_id) VALUES(1,5) on conflict do nothing ;
INSERT INTO types_risks(risk_id, type_id) VALUES(2,1) on conflict do nothing ;
INSERT INTO types_risks(risk_id, type_id) VALUES(3,2) on conflict do nothing ;
INSERT INTO types_risks(risk_id, type_id) VALUES(5,3) on conflict do nothing ;
INSERT INTO types_risks(risk_id, type_id) VALUES(6,4) on conflict do nothing ;
INSERT INTO types_risks(risk_id, type_id) VALUES(3,5) on conflict do nothing ;


