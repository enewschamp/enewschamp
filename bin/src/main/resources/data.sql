INSERT INTO country (countryId,name_Id,description,isd,currency_id, language,operator_Id,operation_Date_Time,record_In_Use) values (1,'IN','India','11','INR','EN','SYSTEM', CURRENT_DATE, 'Y');
INSERT INTO country (countryId,name_Id,description,isd,currency_id, language,Operator_Id,operation_Date_Time,record_In_Use) values (2,'US','United States','12','USD','EN','SYSTEM', CURRENT_DATE, 'Y');

Insert into city (city_Id,name_Id,description,state_Id,country_Id,Operator_Id,operation_Date_Time,record_In_Use) values (1, 'Mumbai', 'Mumbai','MH','IN','SYSTEM', CURRENT_DATE, 'Y');
Insert into city (city_Id,name_Id,description,state_Id,country_Id,Operator_Id,operation_Date_Time,record_In_Use) values (2, 'Delhi', 'Mumbai','DEL','IN','SYSTEM', CURRENT_DATE, 'Y');

Insert into state (state_id,country_id,name_id, description,Operator_Id,operation_Date_Time,record_In_Use) values (1, 'IN','MH','Maharashtra','SYSTEM', CURRENT_DATE, 'Y');
Insert into state (state_id,country_id,name_id, description,Operator_Id,operation_Date_Time,record_In_Use) values (2, 'IN','DEL','Delhi','SYSTEM', CURRENT_DATE, 'Y');


Insert into individual_pricing (individual_pricing_Id,edition_Id,effective_Date,fee_Currency,fee_Monthly,fee_Quarterly,fee_Half_Yearly,fee_Yearly,Operator_Id,operation_Date_Time,record_In_Use) 
values (1,'Indian',CURRENT_DATE,'INR',10,30,60,120,'SYSTEM', CURRENT_DATE, 'Y');

Insert into school_pricing(school_pricing_Id,institution_Id,institution_Type,edition_Id,start_Date,end_Date,fee_Currency,fee_Monthly,fee_Quarterly,fee_Half_Yearly,fee_Yearly,Operator_Id,operation_Date_Time,record_In_Use) 
values ( 1, 1,'S','Indian',CURRENT_DATE,CURRENT_DATE+10,'INR',20,60,120,240,'SYSTEM', CURRENT_DATE, 'Y');




Insert into news_article_quiz (news_article_quiz_id,news_article_id,Question,Opt1,Opt2,Opt3,Opt4,correct_Opt,Operator_Id,Operation_Date_Time,Record_In_Use) 
values (1, 3331323, 'Where is Taj Mahal ?', 'Delhi','Mumbai','Agra','Chennai', 3, 'SYSTEM', CURRENT_DATE,'Y');

Insert into news_article_quiz (news_article_quiz_id,news_article_id,Question,Opt1,Opt2,Opt3,Opt4,correct_Opt,Operator_Id,Operation_Date_Time,Record_In_Use) 
values (2, 3331323, 'Where is India Gate ?', 'Delhi','Mumbai','Kolkata','Chennai', 1, 'SYSTEM', CURRENT_DATE,'Y');

Insert into news_article_quiz (news_article_quiz_id,news_article_id,Question,Opt1,Opt2,Opt3,Opt4,correct_Opt,Operator_Id,Operation_Date_Time,Record_In_Use) 
values (3, 3331323, 'Where is Eden Garden Cricker Ground ?', 'Delhi','Mumbai','Agra','Kolkata', 4, 'SYSTEM', CURRENT_DATE,'Y');

Insert into news_article_quiz (news_article_quiz_id,news_article_id,Question,Opt1,Opt2,Opt3,Opt4,correct_Opt,Operator_Id,Operation_Date_Time,Record_In_Use) 
values (4, 3331323, 'Where is Marine Drive ?', 'Delhi','Mumbai','Agra','Chennai', 2, 'SYSTEM', CURRENT_DATE,'Y');


INSERT INTO app_security(app_sec_id,app_name, app_key) values (1,'ENEWSCHAMPAPP','ABNGYHUKLOIHGTY')


commit;




