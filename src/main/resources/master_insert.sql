INSERT INTO app_security(app_security_id,app_name, app_key,module) VALUES (1,'ENEWSCHAMPAPP','ABNGYHUKLOIHGTY','StudentApp');
INSERT INTO app_security(app_security_id,app_name, app_key,module) VALUES (2,'ENEWSCHAMPPUBLISHER','GODISALMIGHTY','Publisher');
INSERT INTO app_security(app_security_id,app_name, app_key,module) VALUES (3,'ENEWSCHAMPADMIN','GODISALMIGHTY','Admin');

INSERT INTO country (country_id,name_Id,description,isd,currency_id, language_Id,operator_Id,operation_Date_Time,record_In_Use) VALUES (1,'IN','India','11','INR','EN','SYSTEM', CURRENT_DATE, 'Y');
INSERT INTO country (country_id,name_Id,description,isd,currency_id, language_Id,Operator_Id,operation_Date_Time,record_In_Use) VALUES (2,'US','United States','12','USD','EN','SYSTEM', CURRENT_DATE, 'Y');

INSERT INTO state (state_id,country_id,name_id, description,Operator_Id,operation_Date_Time,record_In_Use) VALUES (1, 'IN','MAH','Maharashtra','SYSTEM', CURRENT_DATE, 'Y');
INSERT INTO state (state_id,country_id,name_id, description,Operator_Id,operation_Date_Time,record_In_Use) VALUES (2, 'IN','DEL','Delhi','SYSTEM', CURRENT_DATE, 'Y');
INSERT INTO state (state_id,country_id,name_id, description,Operator_Id,operation_Date_Time,record_In_Use) VALUES (3, 'IN','BIH','Bihar','SYSTEM', CURRENT_DATE, 'Y');
INSERT INTO state (state_id,country_id,name_id, description,Operator_Id,operation_Date_Time,record_In_Use) VALUES (4, 'IN','KAR','Karnataka','SYSTEM', CURRENT_DATE, 'Y');
INSERT INTO state (state_id,country_id,name_id, description,Operator_Id,operation_Date_Time,record_In_Use) VALUES (5, 'IN','TNA','Tamilnadu','SYSTEM', CURRENT_DATE, 'Y');
INSERT INTO state (state_id,country_id,name_id, description,Operator_Id,operation_Date_Time,record_In_Use) VALUES (6, 'IN','BEN','West Bengal','SYSTEM', CURRENT_DATE, 'Y');
INSERT INTO state (state_id,country_id,name_id, description,Operator_Id,operation_Date_Time,record_In_Use) VALUES (7, 'IN','UPA','Uttar Pradesh','SYSTEM', CURRENT_DATE, 'Y');
INSERT INTO state (state_id,country_id,name_id, description,Operator_Id,operation_Date_Time,record_In_Use) VALUES (8, 'IN','TEL','Telangana','SYSTEM', CURRENT_DATE, 'Y');

INSERT INTO city (city_Id, is_applicable_for_news_events ,name_Id,description,state_Id,country_Id,Operator_Id,operation_Date_Time,record_In_Use) VALUES (1, 'Y', 'Mumbai', 'Mumbai','MAH','IN','SYSTEM', CURRENT_DATE, 'Y');
INSERT INTO city (city_Id, is_applicable_for_news_events ,name_Id,description,state_Id,country_Id,Operator_Id,operation_Date_Time,record_In_Use) VALUES (2, 'Y', 'Delhi', 'Delhi','DEL','IN','SYSTEM', CURRENT_DATE, 'Y');
INSERT INTO city (city_Id, is_applicable_for_news_events ,name_Id,description,state_Id,country_Id,Operator_Id,operation_Date_Time,record_In_Use) VALUES (3, 'Y', 'Pune', 'Pune','BIH','IN','SYSTEM', CURRENT_DATE, 'Y');
INSERT INTO city (city_Id, is_applicable_for_news_events ,name_Id,description,state_Id,country_Id,Operator_Id,operation_Date_Time,record_In_Use) VALUES (4, 'Y', 'Kolkota', 'Kolkota','BEN','IN','SYSTEM', CURRENT_DATE, 'Y');
INSERT INTO city (city_Id, is_applicable_for_news_events ,name_Id,description,state_Id,country_Id,Operator_Id,operation_Date_Time,record_In_Use) VALUES (5, 'Y', 'Chennai', 'Chennai','TNA','IN','SYSTEM', CURRENT_DATE, 'Y');
INSERT INTO city (city_Id, is_applicable_for_news_events ,name_Id,description,state_Id,country_Id,Operator_Id,operation_Date_Time,record_In_Use) VALUES (6, 'Y', 'Bengaluru', 'Bengaluru','KAR','IN','SYSTEM', CURRENT_DATE, 'Y');
INSERT INTO city (city_Id, is_applicable_for_news_events ,name_Id,description,state_Id,country_Id,Operator_Id,operation_Date_Time,record_In_Use) VALUES (7, 'N', 'Patna', 'Patna','BIH','IN','SYSTEM', CURRENT_DATE, 'Y');
INSERT INTO city (city_Id, is_applicable_for_news_events ,name_Id,description,state_Id,country_Id,Operator_Id,operation_Date_Time,record_In_Use) VALUES (8, 'Y', 'Hyderabad', 'Hyderabad','TEL','IN','SYSTEM', CURRENT_DATE, 'Y');
INSERT INTO city (city_Id, is_applicable_for_news_events ,name_Id,description,state_Id,country_Id,Operator_Id,operation_Date_Time,record_In_Use) VALUES (9, 'N', 'Nagpur', 'Nagpur','MAH','IN','SYSTEM', CURRENT_DATE, 'Y');
INSERT INTO city (city_Id, is_applicable_for_news_events ,name_Id,description,state_Id,country_Id,Operator_Id,operation_Date_Time,record_In_Use) VALUES (10, 'N', 'Lucknow', 'Lucknow','UPA','IN','SYSTEM', CURRENT_DATE, 'Y');
INSERT INTO city (city_Id, is_applicable_for_news_events ,name_Id,description,state_Id,country_Id,Operator_Id,operation_Date_Time,record_In_Use) VALUES (11, 'N', 'Kanpur', 'Kanpur','UPA','IN','SYSTEM', CURRENT_DATE, 'Y');

INSERT INTO individual_pricing (individual_pricing_Id,edition_Id,effective_Date,pricing_Details,Operator_Id,operation_Date_Time,record_In_Use) VALUES (1,'INDIAN',CURRENT_DATE,'{1M:10,3M:30,6M:60,1Y:120}','SYSTEM', CURRENT_DATE, 'Y');

INSERT INTO school (school_id, operation_date_time, operator_id, record_in_use, city_id, comments, country_id, edu_board, edu_medium, fee_structure, gender_diversity, NAME, ownership, school_chain_id, school_program, shift_details, state_id, student_residences, website) VALUES('1',CURRENT_DATE,'SYSTEM','Y','Mumbai',NULL,'IN','PUN','ENG',NULL,'CO','NESB',NULL,'1',NULL,NULL,'MH','Y',NULL);
INSERT INTO school_pricing(school_pricing_Id,institution_Id,institution_Type,edition_Id,effective_Date,pricing_Details,Operator_Id,operation_Date_Time,record_In_Use) VALUES ( 1, 1,'S','INDIAN',CURRENT_DATE,'{1M:20,3M:60,6M:120,1Y:240}','SYSTEM', CURRENT_DATE, 'Y');
COMMIT;