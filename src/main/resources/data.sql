
delete FROM enewschamp.jv_snapshot where 1 = 1;
delete FROM enewschamp.jv_commit where 1 = 1;
delete FROM enewschamp.jv_global_id where 1 = 1;
delete FROM enewschamp.jv_commit_property where 1 = 1;

INSERT INTO Country (countryId,name_Id,description,isd,currency_id, language,operator_Id,operation_Date_Time,record_In_Use) values (1,'IN','India','11','INR','EN','SYSTEM', CURRENT_DATE, 'Y');
INSERT INTO Country (countryId,name_Id,description,isd,currency_id, language,Operator_Id,operation_Date_Time,record_In_Use) values (2,'US','United States','12','USD','EN','SYSTEM', CURRENT_DATE, 'Y');

Insert into City (city_Id,name_Id,description,state_Id,country_Id,Operator_Id,operation_Date_Time,record_In_Use) values (1, 'Mumbai', 'Mumbai','MH','IN','SYSTEM', CURRENT_DATE, 'Y');
Insert into City (city_Id,name_Id,description,state_Id,country_Id,Operator_Id,operation_Date_Time,record_In_Use) values (2, 'Delhi', 'Mumbai','DEL','IN','SYSTEM', CURRENT_DATE, 'Y');

Insert into State (state_id,country_id,name_id, description,Operator_Id,operation_Date_Time,record_In_Use) values (1, 'IN','MH','Maharashtra','SYSTEM', CURRENT_DATE, 'Y');
Insert into State (state_id,country_id,name_id, description,Operator_Id,operation_Date_Time,record_In_Use) values (2, 'IN','DEL','Delhi','SYSTEM', CURRENT_DATE, 'Y');


Insert into Individual_Pricing (individual_Pricing_Id,edition_Id,effective_Date,fee_Currency,fee_Monthly,fee_Quarterly,fee_Half_Yearly,fee_Yearly,Operator_Id,operation_Date_Time,record_In_Use) 
values (1,'Indian',CURRENT_DATE,'INR',10,30,60,120,'SYSTEM', CURRENT_DATE, 'Y');

Insert into School_Pricing(school_Pricing_Id,institution_Id,institution_Type,edition_Id,start_Date,end_Date,fee_Currency,fee_Monthly,fee_Quarterly,fee_Half_Yearly,fee_Yearly,Operator_Id,operation_Date_Time,record_In_Use) 
values ( 1, 1,'S','Indian',CURRENT_DATE,CURRENT_DATE+10,'INR',20,60,120,240,'SYSTEM', CURRENT_DATE, 'Y');

Insert into Page_Navigator(nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (1, 'NewSubscriptionPage','next','Subscription','NewPreferencePage','NewSubscriptionPage','Y','N','SYSTEM',CURRENT_DATE,'M',1);

Insert into Page_Navigator(nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (2, 'NewPreferencePage','next','Subscription','StudentDetailsPage','NewSubscriptionPage','Y','N','SYSTEM',CURRENT_DATE,'M',2);

Insert into Page_Navigator(nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (3, 'StudentDetailsPage','next','Subscription','SchoolDetailsPage','NewPreferencePage','Y','N','SYSTEM',CURRENT_DATE,'M',3);

Insert into Page_Navigator(nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (4, 'SchoolDetailsPage','next','Subscription','SchoolDetailsPage','StudentDetailsPage','Y','N','SYSTEM',CURRENT_DATE,'M',4);


insert into Page_Navigator (nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (6, 'NewSubscriptionPage', 'next', 'PremiumSubs', 'SubscriptionPeriodPage','NewSubscriptionPage','Y', 'N','SYSTEM', CURRENT_DATE,'W',1);


insert into Page_Navigator (nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (7, 'SubscriptionPeriodPage', 'next', 'PremiumSubs', 'StudentDetailsPage','NewSubscriptionPage','Y', 'N','SYSTEM', CURRENT_DATE,'W',2);


insert into Page_Navigator (nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (8, 'StudentDetailsPage', 'next', 'PremiumSubs', 'SchoolDetailsPage','SubscriptionPeriodPage','Y', 'N','SYSTEM', CURRENT_DATE,'W',4);

insert into Page_Navigator (nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (9, 'SchoolDetailsPage', 'next', 'PremiumSubs', 'NewPreferencePage','StudentDetailsPage','Y', 'N','SYSTEM', CURRENT_DATE,'M',5);

insert into Page_Navigator (nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq )
values (10, 'NewPreferencePage', 'next', 'PremiumSubs', 'NewPreferencePage','SchoolDetailsPage','Y', 'N','SYSTEM', CURRENT_DATE,'M',6);





insert into Page_Navigator (nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (13, 'NewSubscriptionPage', 'next', 'SchoolSubs', 'StudentDetailsPage','NewSubscriptionPage','Y', 'N','SYSTEM', CURRENT_DATE,'W',1);

insert into Page_Navigator (nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (14, 'StudentDetailsPage', 'next', 'SchoolSubs', 'SchoolDetailsPage','NewSubscriptionPage','Y', 'N','SYSTEM', CURRENT_DATE,'W',2);

insert into Page_Navigator (nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (15, 'SchoolDetailsPage', 'next', 'SchoolSubs', 'SubscriptionPeriodPage','StudentDetailsPage','Y', 'N','SYSTEM', CURRENT_DATE,'W',3);

insert into Page_Navigator (nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (16, 'SubscriptionPeriodPage', 'next', 'SchoolSubs', 'PaymentPage','SchoolDetailsPage','Y', 'N','SYSTEM', CURRENT_DATE,'W',4);

insert into Page_Navigator (nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (17, 'PaymentPage', 'next', 'SchoolSubs', 'StudentDetailsPage','SubscriptionPeriodPage','Y', 'N','SYSTEM', CURRENT_DATE,'M',5);

insert into Page_Navigator (nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (18, 'NewPreferencesPage', 'next', 'SchoolSubs', 'VerifyEmailPage','PaymentPage','Y', 'N','SYSTEM', CURRENT_DATE,'M',6);

insert into Page_Navigator (nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (19, 'VerifyEmailPage', 'next', 'PremiumSubs', 'VerifyEmailPage','NewPreferencesPage','Y', 'N','SYSTEM', CURRENT_DATE,'M',7);

insert into Page_Navigator (nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (20, 'NewSubscriptionPage', 'next', 'GoPremiumSubs/MPremiumSubs', 'SubscriptionPeriodPage','NewSubscriptionPage','Y', 'N','SYSTEM', CURRENT_DATE,'W',1);

insert into Page_Navigator (nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (21, 'SubscriptionPeriodPage', 'next', 'GoPremiumSubs/MPremiumSubs', 'PaymentPage','NewSubscriptionPage','Y', 'N','SYSTEM', CURRENT_DATE,'W',2);

insert into Page_Navigator (nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (22, 'PaymentPage', 'next', 'GoPremiumSubs/MPremiumSubs', 'StudentDetailsPage','SubscriptionPeriodPage','Y', 'N','SYSTEM', CURRENT_DATE,'M',3);

insert into Page_Navigator (nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (23, 'StudentDetailsPage', 'next', 'GoPremiumSubs/MPremiumSubs', 'SchoolDetailsPage','PaymentPage','Y', 'N','SYSTEM', CURRENT_DATE,'W',4);

insert into Page_Navigator (nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (24, 'SchoolDetailsPage', 'next', 'GoPremiumSubs', 'NewPreferencesPage','StudentDetailsPage','Y', 'N','SYSTEM', CURRENT_DATE,'M',5);

insert into Page_Navigator (nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (25, 'NewPreferencesPage', 'next', 'GoPremiumSubs', 'VerifyEmailPage','SchoolDetailsPage','Y', 'N','SYSTEM', CURRENT_DATE,'M',6);

insert into Page_Navigator (nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (26, 'VerifyEmailPage', 'next', 'GoPremiumSubs', 'VerifyEmailPage','NewPreferencesPage','Y', 'N','SYSTEM', CURRENT_DATE,'M',7);

insert into Page_Navigator (nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (27, 'NewSubscriptionPage', 'next', 'GoSchoolSubs/MSchoolSubs', 'StudentDetailsPage','NewSubscriptionPage','Y', 'N','SYSTEM', CURRENT_DATE,'W',1);

insert into Page_Navigator (nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (28, 'StudentDetailsPage', 'next', 'GoSchoolSubs/MSchoolSubs', 'SchoolDetailsPage','NewSubscriptionPage','Y', 'N','SYSTEM', CURRENT_DATE,'W',2);

insert into Page_Navigator (nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (29, 'SchoolDetailsPage', 'next', 'GoSchoolSubs/MSchoolSubs', 'SubscriptionPeriodPage','StudentDetailsPage','Y', 'N','SYSTEM', CURRENT_DATE,'M',3);

insert into Page_Navigator (nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (30, 'SubscriptionPeriodPage', 'next', 'GoSchoolSubs/MSchoolSubs', 'PaymentPage','SchoolDetailsPage','Y', 'N','SYSTEM', CURRENT_DATE,'W',4);

insert into Page_Navigator (nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (31, 'PaymentPage', 'next', 'GoSchoolSubs/MSchoolSubs', 'NewPreferencesPage','SubscriptionPeriodPage','Y', 'N','SYSTEM', CURRENT_DATE,'M',5);

insert into Page_Navigator (nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (32, 'NewPreferencesPage', 'next', 'GoSchoolSubs/MSchoolSubs', 'VerifyEmailPage','PaymentPage','Y', 'N','SYSTEM', CURRENT_DATE,'M',6);

insert into Page_Navigator (nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (33, 'VerifyEmailPage', 'next', 'GoSchoolSubs/MSchoolSubs', 'VerifyEmailPage','NewPreferencesPage','Y', 'N','SYSTEM', CURRENT_DATE,'M',7);

insert into Page_Navigator (nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (34, 'NewSubscriptionPage', 'next', 'ToSchoolSubs', 'SubscriptionPeriodPage','NewSubscriptionPage','Y', 'N','SYSTEM', CURRENT_DATE,'W',1);

insert into Page_Navigator (nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (35, 'SchoolDetailsPage', 'next', 'ToSchoolSubs', 'NewPreferencesPage','NewSubscriptionPage','Y', 'N','SYSTEM', CURRENT_DATE,'M',2);

insert into Page_Navigator (nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (36, 'SubscriptionPeriodPage', 'next', 'ToSchoolSubs', 'PaymentPage','SchoolDetailsPage','Y', 'N','SYSTEM', CURRENT_DATE,'W',3);

insert into Page_Navigator (nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (37, 'PaymentPage', 'next', 'ToSchoolSubs', 'NewPreferencesPage','SubscriptionPeriodPage','Y', 'N','SYSTEM', CURRENT_DATE,'M',4);

insert into Page_Navigator (nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (38, 'NewPreferencesPage', 'next', 'ToSchoolSubs', 'VerifyEmailPage','PaymentPage','Y', 'N','SYSTEM', CURRENT_DATE,'M',5);

insert into Page_Navigator (nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq )
values (39, 'VerifyEmailPage', 'next', 'ToSchoolSubs', 'VerifyEmailPage','NewPreferencesPage','Y', 'N','SYSTEM', CURRENT_DATE,'M',6);


insert into Page_Navigator (nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (40, 'NewSubscriptionPage', 'next', 'ToPremiumSubs', 'SubscriptionPeriodPage','NewSubscriptionPage','Y', 'N','SYSTEM', CURRENT_DATE,'W',1);

insert into Page_Navigator (nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (41, 'SubscriptionPeriodPage', 'next', 'ToPremiumSubs', 'PaymentPage','SubscriptionPeriodPage','Y', 'N','SYSTEM', CURRENT_DATE,'W',2);

insert into Page_Navigator (nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq )
values (42, 'PaymentPage', 'next', 'ToPremiumSubs', 'SchoolDetailsPage','SubscriptionPeriodPage','Y', 'N','SYSTEM', CURRENT_DATE,'M',3);

insert into Page_Navigator (nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (43, 'SchoolDetailsPage', 'next', 'ToPremiumSubs', 'NewPreferencesPage','PaymentPage','Y', 'N','SYSTEM', CURRENT_DATE,'M',4);

insert into Page_Navigator (nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (44, 'NewPreferencesPage', 'next', 'ToPremiumSubs', 'VerifyEmailPage','SchoolDetailsPage','Y', 'N','SYSTEM', CURRENT_DATE,'M',5);

insert into Page_Navigator (nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (45, 'VerifyEmailPage', 'next', 'ToPremiumSubs', 'VerifyEmailPage','NewPreferencesPage','Y', 'N','SYSTEM', CURRENT_DATE,'M',6);

Insert into Page_Navigator(nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (46, 'NewSubscriptionPage','previous','Subscription','NewSubscriptionPage','NewSubscriptionPage','Y','N','SYSTEM',CURRENT_DATE,'W',1);

Insert into Page_Navigator(nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (47, 'NewPreferencePage','previous','Subscription','StudentDetailsPage','NewSubscriptionPage','Y','N','SYSTEM',CURRENT_DATE,'M',2);

Insert into Page_Navigator(nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (48, 'StudentDetailsPage','previous','Subscription','SchoolDetailsPage','NewPreferencePage','Y','N','SYSTEM',CURRENT_DATE,'W',3);

Insert into Page_Navigator(nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (49, 'SchoolDetailsPage','previous','Subscription','VerifyEmailPage','StudentDetailsPage','Y','N','SYSTEM',CURRENT_DATE,'M',4);

Insert into Page_Navigator(nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (50, 'VerifyEmailPage','previous','Subscription','VerifyEmailPage','SchoolDetailsPage','Y','N','SYSTEM',CURRENT_DATE,'M',5);

Insert into Page_Navigator(nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (51, 'MyPicture','save','ModifyStudentDetails','MyPicture','MyPicture','Y','N','SYSTEM',CURRENT_DATE,'M',1);

Insert into Page_Navigator(nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (52, 'ShareAchievements','save','ShareAchievements','ShareAchievements','ShareAchievements','Y','N','SYSTEM',CURRENT_DATE,'M',1);

Insert into Page_Navigator(nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (53, 'ShareAchievements','back','ShareAchievements','ShareAchievements','ShareAchievements','Y','N','SYSTEM',CURRENT_DATE,'M',1);

Insert into Page_Navigator(nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (54, 'MyProfile','get','MyProfile','MyProfile','MyProfile','Y','N','SYSTEM',CURRENT_DATE,'M',1);


commit;
=======
delete FROM enewschamp.jv_snapshot where 1 = 1;
delete FROM enewschamp.jv_commit where 1 = 1;
delete FROM enewschamp.jv_global_id where 1 = 1;
delete FROM enewschamp.jv_commit_property where 1 = 1;
>>>>>>> 5308d1573cf9afb51cf9aadb9da8674d80c4a668
