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
INSERT INTO risktypes(type) VALUES('Analysis') on conflict do nothing ;
INSERT INTO risktypes(type) VALUES('Requirements') on conflict do nothing ;
INSERT INTO risktypes(type) VALUES('Software') on conflict do nothing ;
INSERT INTO risktypes(type) VALUES('Support') on conflict do nothing ;
INSERT INTO risktypes(type) VALUES('Project team') on conflict do nothing ;
INSERT INTO risktypes(type) VALUES('Technical') on conflict do nothing ;
INSERT INTO risktypes(type) VALUES('Customer') on conflict do nothing ;
INSERT INTO risktypes(type) VALUES('Internal') on conflict do nothing ;
INSERT INTO risktypes(type) VALUES('Functional') on conflict do nothing ;
INSERT INTO risktypes(type) VALUES('Managerial') on conflict do nothing ;


--Risk
INSERT INTO riskdb(description, name) VALUES('Wrong conclusions after analysis	Analysis	Bad analyst skills, not much time for analysis, bad skills.	May result in wrong requirements, wrong schedules, wrong product.	While working with the client, not only analytical data shall be analyzed, but also a lot of interpersonal communication shall occur in order to identify the real need.','Wrong conclusions after analysis') on conflict do nothing ;
INSERT INTO riskdb(description, name) VALUES('Words like “acceptable performance” or “adequate timing” shall be avoided, because each understands them differently.','Unmeasurable requirements') on conflict do nothing ;
INSERT INTO riskdb(description, name) VALUES('If software is too slow, end users will not like to use it at all.','Bad performance') on conflict do nothing ;
INSERT INTO riskdb(description, name) VALUES('Cost of maintenance depends much on development stage because each decision took during development influence maintenance.','Lack of specialists') on conflict do nothing ;
INSERT INTO riskdb(description, name) VALUES('Time expectations shall not be lowered.','Inadequate scheduling') on conflict do nothing ;
INSERT INTO riskdb(description, name) VALUES('Software shall meet todays’ quality standards.','Insufficient testing') on conflict do nothing ;
INSERT INTO riskdb(description, name) VALUES('Customer is not satisfied with the product. There are more modern ones.','Obsolete product developed') on conflict do nothing ;
INSERT INTO riskdb(description, name) VALUES('Product testing doesn’t show real product state','Incorrect testing on product') on conflict do nothing ;
INSERT INTO riskdb(description, name) VALUES('System works inefficient, too slow.','Low performance of the system') on conflict do nothing ;

--risk-risktypes
INSERT INTO types_risks(risk_id, type_id) VALUES(1,1) on conflict do nothing ;
INSERT INTO types_risks(risk_id, type_id) VALUES(2,2) on conflict do nothing ;
INSERT INTO types_risks(risk_id, type_id) VALUES(3,3) on conflict do nothing ;
INSERT INTO types_risks(risk_id, type_id) VALUES(4,4) on conflict do nothing ;
INSERT INTO types_risks(risk_id, type_id) VALUES(5,2) on conflict do nothing ;
INSERT INTO types_risks(risk_id, type_id) VALUES(5,1) on conflict do nothing ;
INSERT INTO types_risks(risk_id, type_id) VALUES(6,3) on conflict do nothing ;
INSERT INTO types_risks(risk_id, type_id) VALUES(7,7) on conflict do nothing ;
INSERT INTO types_risks(risk_id, type_id) VALUES(8,7) on conflict do nothing ;
INSERT INTO types_risks(risk_id, type_id) VALUES(9,7) on conflict do nothing ;


