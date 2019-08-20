Insert into page_navigator(nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (1, 'NewSubscriptionPage','next','Subscription','NewPreferencePage','NewSubscriptionPage','Y','N','SYSTEM',CURRENT_DATE,'M',1);

Insert into page_navigator(nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (2, 'NewPreferencePage','next','Subscription','StudentDetailsPage','NewSubscriptionPage','Y','N','SYSTEM',CURRENT_DATE,'M',2);

Insert into page_navigator(nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (3, 'StudentDetailsPage','next','Subscription','SchoolDetailsPage','NewPreferencePage','Y','N','SYSTEM',CURRENT_DATE,'M',3);

Insert into page_navigator(nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (4, 'SchoolDetailsPage','next','Subscription','SchoolDetailsPage','StudentDetailsPage','Y','N','SYSTEM',CURRENT_DATE,'M',4);

insert into page_navigator (nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (6, 'NewSubscriptionPage', 'next', 'PremiumSubs', 'SubscriptionPeriodPage','NewSubscriptionPage','Y', 'N','SYSTEM', CURRENT_DATE,'W',1);

insert into page_navigator (nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (7, 'SubscriptionPeriodPage', 'next', 'PremiumSubs', 'StudentDetailsPage','NewSubscriptionPage','Y', 'N','SYSTEM', CURRENT_DATE,'W',2);

insert into page_navigator (nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (8, 'StudentDetailsPage', 'next', 'PremiumSubs', 'SchoolDetailsPage','SubscriptionPeriodPage','Y', 'N','SYSTEM', CURRENT_DATE,'W',4);

insert into page_navigator (nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (9, 'SchoolDetailsPage', 'next', 'PremiumSubs', 'NewPreferencePage','StudentDetailsPage','Y', 'N','SYSTEM', CURRENT_DATE,'M',5);

insert into page_navigator (nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq )
values (10, 'NewPreferencePage', 'next', 'PremiumSubs', 'NewPreferencePage','SchoolDetailsPage','Y', 'N','SYSTEM', CURRENT_DATE,'M',6);

insert into page_navigator (nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (13, 'NewSubscriptionPage', 'next', 'SchoolSubs', 'StudentDetailsPage','NewSubscriptionPage','Y', 'N','SYSTEM', CURRENT_DATE,'W',1);

insert into page_navigator (nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (14, 'StudentDetailsPage', 'next', 'SchoolSubs', 'SchoolDetailsPage','NewSubscriptionPage','Y', 'N','SYSTEM', CURRENT_DATE,'W',2);

insert into page_navigator (nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (15, 'SchoolDetailsPage', 'next', 'SchoolSubs', 'SubscriptionPeriodPage','StudentDetailsPage','Y', 'N','SYSTEM', CURRENT_DATE,'W',3);

insert into page_navigator (nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (16, 'SubscriptionPeriodPage', 'next', 'SchoolSubs', 'PaymentPage','SchoolDetailsPage','Y', 'N','SYSTEM', CURRENT_DATE,'W',4);

insert into page_navigator (nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (17, 'PaymentPage', 'next', 'SchoolSubs', 'StudentDetailsPage','SubscriptionPeriodPage','Y', 'N','SYSTEM', CURRENT_DATE,'M',5);

insert into page_navigator (nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (18, 'NewPreferencesPage', 'next', 'SchoolSubs', 'VerifyEmailPage','PaymentPage','Y', 'N','SYSTEM', CURRENT_DATE,'M',6);

insert into page_navigator (nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (19, 'VerifyEmailPage', 'next', 'PremiumSubs', 'VerifyEmailPage','NewPreferencesPage','Y', 'N','SYSTEM', CURRENT_DATE,'M',7);

insert into page_navigator (nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (20, 'NewSubscriptionPage', 'next', 'GoPremiumSubs/MPremiumSubs', 'SubscriptionPeriodPage','NewSubscriptionPage','Y', 'N','SYSTEM', CURRENT_DATE,'W',1);

insert into page_navigator (nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (21, 'SubscriptionPeriodPage', 'next', 'GoPremiumSubs/MPremiumSubs', 'PaymentPage','NewSubscriptionPage','Y', 'N','SYSTEM', CURRENT_DATE,'W',2);

insert into page_navigator (nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (22, 'PaymentPage', 'next', 'GoPremiumSubs/MPremiumSubs', 'StudentDetailsPage','SubscriptionPeriodPage','Y', 'N','SYSTEM', CURRENT_DATE,'M',3);

insert into page_navigator (nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (23, 'StudentDetailsPage', 'next', 'GoPremiumSubs/MPremiumSubs', 'SchoolDetailsPage','PaymentPage','Y', 'N','SYSTEM', CURRENT_DATE,'W',4);

insert into page_navigator (nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (24, 'SchoolDetailsPage', 'next', 'GoPremiumSubs', 'NewPreferencesPage','StudentDetailsPage','Y', 'N','SYSTEM', CURRENT_DATE,'M',5);

insert into page_navigator (nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (25, 'NewPreferencesPage', 'next', 'GoPremiumSubs', 'VerifyEmailPage','SchoolDetailsPage','Y', 'N','SYSTEM', CURRENT_DATE,'M',6);

insert into page_navigator (nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (26, 'VerifyEmailPage', 'next', 'GoPremiumSubs', 'VerifyEmailPage','NewPreferencesPage','Y', 'N','SYSTEM', CURRENT_DATE,'M',7);

insert into page_navigator (nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (27, 'NewSubscriptionPage', 'next', 'GoSchoolSubs/MSchoolSubs', 'StudentDetailsPage','NewSubscriptionPage','Y', 'N','SYSTEM', CURRENT_DATE,'W',1);

insert into page_navigator (nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (28, 'StudentDetailsPage', 'next', 'GoSchoolSubs/MSchoolSubs', 'SchoolDetailsPage','NewSubscriptionPage','Y', 'N','SYSTEM', CURRENT_DATE,'W',2);

insert into page_navigator (nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (29, 'SchoolDetailsPage', 'next', 'GoSchoolSubs/MSchoolSubs', 'SubscriptionPeriodPage','StudentDetailsPage','Y', 'N','SYSTEM', CURRENT_DATE,'M',3);

insert into page_navigator (nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (30, 'SubscriptionPeriodPage', 'next', 'GoSchoolSubs/MSchoolSubs', 'PaymentPage','SchoolDetailsPage','Y', 'N','SYSTEM', CURRENT_DATE,'W',4);

insert into page_navigator (nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (31, 'PaymentPage', 'next', 'GoSchoolSubs/MSchoolSubs', 'NewPreferencesPage','SubscriptionPeriodPage','Y', 'N','SYSTEM', CURRENT_DATE,'M',5);

insert into page_navigator (nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (32, 'NewPreferencesPage', 'next', 'GoSchoolSubs/MSchoolSubs', 'VerifyEmailPage','PaymentPage','Y', 'N','SYSTEM', CURRENT_DATE,'M',6);

insert into page_navigator (nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (33, 'VerifyEmailPage', 'next', 'GoSchoolSubs/MSchoolSubs', 'VerifyEmailPage','NewPreferencesPage','Y', 'N','SYSTEM', CURRENT_DATE,'M',7);

insert into page_navigator (nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (34, 'NewSubscriptionPage', 'next', 'ToSchoolSubs', 'SubscriptionPeriodPage','NewSubscriptionPage','Y', 'N','SYSTEM', CURRENT_DATE,'W',1);

insert into page_navigator (nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (35, 'SchoolDetailsPage', 'next', 'ToSchoolSubs', 'NewPreferencesPage','NewSubscriptionPage','Y', 'N','SYSTEM', CURRENT_DATE,'M',2);

insert into page_navigator (nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (36, 'SubscriptionPeriodPage', 'next', 'ToSchoolSubs', 'PaymentPage','SchoolDetailsPage','Y', 'N','SYSTEM', CURRENT_DATE,'W',3);

insert into page_navigator (nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (37, 'PaymentPage', 'next', 'ToSchoolSubs', 'NewPreferencesPage','SubscriptionPeriodPage','Y', 'N','SYSTEM', CURRENT_DATE,'M',4);

insert into page_navigator (nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (38, 'NewPreferencesPage', 'next', 'ToSchoolSubs', 'VerifyEmailPage','PaymentPage','Y', 'N','SYSTEM', CURRENT_DATE,'M',5);

insert into page_navigator (nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq )
values (39, 'VerifyEmailPage', 'next', 'ToSchoolSubs', 'VerifyEmailPage','NewPreferencesPage','Y', 'N','SYSTEM', CURRENT_DATE,'M',6);


insert into page_navigator (nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (40, 'NewSubscriptionPage', 'next', 'ToPremiumSubs', 'SubscriptionPeriodPage','NewSubscriptionPage','Y', 'N','SYSTEM', CURRENT_DATE,'W',1);

insert into page_navigator (nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (41, 'SubscriptionPeriodPage', 'next', 'ToPremiumSubs', 'PaymentPage','SubscriptionPeriodPage','Y', 'N','SYSTEM', CURRENT_DATE,'W',2);

insert into page_navigator (nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq )
values (42, 'PaymentPage', 'next', 'ToPremiumSubs', 'SchoolDetailsPage','SubscriptionPeriodPage','Y', 'N','SYSTEM', CURRENT_DATE,'M',3);

insert into page_navigator (nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (43, 'SchoolDetailsPage', 'next', 'ToPremiumSubs', 'NewPreferencesPage','PaymentPage','Y', 'N','SYSTEM', CURRENT_DATE,'M',4);

insert into page_navigator (nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (44, 'NewPreferencesPage', 'next', 'ToPremiumSubs', 'VerifyEmailPage','SchoolDetailsPage','Y', 'N','SYSTEM', CURRENT_DATE,'M',5);

insert into page_navigator (nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (45, 'VerifyEmailPage', 'next', 'ToPremiumSubs', 'VerifyEmailPage','NewPreferencesPage','Y', 'N','SYSTEM', CURRENT_DATE,'M',6);

Insert into page_navigator(nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (46, 'NewSubscriptionPage','previous','Subscription','NewSubscriptionPage','NewSubscriptionPage','Y','N','SYSTEM',CURRENT_DATE,'W',1);

Insert into page_navigator(nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (47, 'NewPreferencePage','previous','Subscription','StudentDetailsPage','NewSubscriptionPage','Y','N','SYSTEM',CURRENT_DATE,'M',2);

Insert into page_navigator(nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (48, 'StudentDetailsPage','previous','Subscription','SchoolDetailsPage','NewPreferencePage','Y','N','SYSTEM',CURRENT_DATE,'W',3);

Insert into page_navigator(nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (49, 'SchoolDetailsPage','previous','Subscription','VerifyEmailPage','StudentDetailsPage','Y','N','SYSTEM',CURRENT_DATE,'M',4);

Insert into page_navigator(nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (50, 'VerifyEmailPage','previous','Subscription','VerifyEmailPage','SchoolDetailsPage','Y','N','SYSTEM',CURRENT_DATE,'M',5);

Insert into page_navigator(nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (51, 'MyPicture','save','ModifyStudentDetails','MyPicture','MyPicture','Y','N','SYSTEM',CURRENT_DATE,'M',1);

Insert into page_navigator(nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (52, 'ShareAchievements','save','ShareAchievements','ShareAchievements','ShareAchievements','Y','N','SYSTEM',CURRENT_DATE,'M',1);

Insert into page_navigator(nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (53, 'ShareAchievements','back','ShareAchievements','ShareAchievements','ShareAchievements','Y','N','SYSTEM',CURRENT_DATE,'M',1);

Insert into page_navigator(nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (54, 'MyProfile','get','MyProfile','MyProfile','MyProfile','Y','N','SYSTEM',CURRENT_DATE,'M',1);

Insert into page_navigator(nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (55, 'Quiz','save','SavedNewsArticle','Quiz','Quiz','Y','N','SYSTEM',CURRENT_DATE,'M',1);

Insert into page_navigator(nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (56, 'NewsArticleQuizCompletion','ok','SavedNewsArticle','NewsArticleQuizCompletion','NewsArticleQuizCompletion','Y','N','SYSTEM',CURRENT_DATE,'M',1);

Insert into page_navigator(nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (57, 'NewsArticle','like','NewsArticle','appnewsarticle','appnewsarticle','Y','N','SYSTEM',CURRENT_DATE,'M',1);

Insert into page_navigator(nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (58, 'NewsArticle','opinion','AppNewsArticle','appnewsarticle','appnewsarticle','Y','N','SYSTEM',CURRENT_DATE,'M',1);
Insert into page_navigator(nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (59, 'NewsArticle','savearticle','SavedNewsArticle','appnewsarticle','appnewsarticle','Y','N','SYSTEM',CURRENT_DATE,'M',1);

Insert into page_navigator(nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (60, 'NewsArticle','quiz','NewsArticle','Quiz','appnewsarticle','Y','N','SYSTEM',CURRENT_DATE,'M',1);

Insert into page_navigator(nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (61, 'Menu','myprofile','Menu','MyProfile','Menu','Y','N','SYSTEM',CURRENT_DATE,'M',1);

Insert into page_navigator(nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (62, 'Menu','mypicture','Menu','MyPicture','Menu','Y','N','SYSTEM',CURRENT_DATE,'M',1);

Insert into page_navigator(nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (63, 'Menu','studentdetails','Menu','StudentDetailsPage','Menu','Y','N','SYSTEM',CURRENT_DATE,'M',1);

Insert into page_navigator(nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (64, 'Menu','schooldetails','Menu','SchoolDetailsPage','Menu','Y','N','SYSTEM',CURRENT_DATE,'M',1);

Insert into page_navigator(nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (65, 'Menu','preferences','Menu','NewPreferencePage','Menu','Y','N','SYSTEM',CURRENT_DATE,'M',1);

Insert into page_navigator(nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (66, 'Menu','shareachievements','Menu','ShareAchievements','Menu','Y','N','SYSTEM',CURRENT_DATE,'M',1);

Insert into page_navigator(nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (68, 'WelcomeUser','load','WelcomeUser','publication','publication','Y','N','SYSTEM',CURRENT_DATE,'M',1);

Insert into page_navigator(nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (69, 'Publication','UnSwipe','IncompleteRegistration','publication','publication','Y','N','SYSTEM',CURRENT_DATE,'M',1);

Insert into page_navigator(nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (70, 'Menu','Home','Menu','publication','publication','Y','N','SYSTEM',CURRENT_DATE,'M',1);
Insert into page_navigator(nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (71, 'Helpdesk','Home','Helpdesk','publication','publication','Y','N','SYSTEM',CURRENT_DATE,'M',1);
Insert into page_navigator(nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (72, 'Notifications','Home','ListNotifications','publication','publication','Y','N','SYSTEM',CURRENT_DATE,'M',1);
Insert into page_navigator(nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (73, 'Opinions','Home','ListOpinions','publication','publication','Y','N','SYSTEM',CURRENT_DATE,'M',1);
Insert into page_navigator(nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (74, 'Trends','Home','ListMonthlyTrends','publication','publication','Y','N','SYSTEM',CURRENT_DATE,'M',1);
Insert into page_navigator(nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (75, 'Scores','Home','ListMonthlyScores','publication','publication','Y','N','SYSTEM',CURRENT_DATE,'M',1);
Insert into page_navigator(nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (76, 'NewsEvents','Home','ListNewsEvents','publication','publication','Y','N','SYSTEM',CURRENT_DATE,'M',1);
Insert into page_navigator(nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (77, 'Recognitions','UpSwipe','ListRecognitions','recognitions','recognitions','Y','N','SYSTEM',CURRENT_DATE,'M',1);
Insert into page_navigator(nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (78, 'Publication','ClickArticleImage','Publication','appnewsarticle','appnewsarticle','Y','N','SYSTEM',CURRENT_DATE,'M',1);
Insert into page_navigator(nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (79, 'Menu','SavedArticles','Menu','appnewsarticle','appnewsarticle','Y','N','SYSTEM',CURRENT_DATE,'M',1);

Insert into page_navigator(nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (80, 'NewsArticle','Next','SavedNewsArticle','appnewsarticle','appnewsarticle','Y','N','SYSTEM',CURRENT_DATE,'M',1);

Insert into page_navigator(nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (81, 'Publication','GoPremium','Publication','NewSubscriptionPage','Publication','Y','N','SYSTEM',CURRENT_DATE,'M',1);

Insert into page_navigator(nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (82, 'Menu','PicImage','Menu','NewSubscriptionPage','Publication','Y','N','SYSTEM',CURRENT_DATE,'M',1);
Insert into page_navigator(nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (83, 'Menu','Opinions','Menu','opinions','opinions','Y','N','SYSTEM',CURRENT_DATE,'M',1);

Insert into page_navigator(nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (84, 'Menu','Subscription','Menu','NewSubscriptionPage','NewSubscriptionPage','Y','N','SYSTEM',CURRENT_DATE,'M',1);

Insert into page_navigator(nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (85, 'NewsArticle','GoPremium','NewsArticle','NewSubscriptionPage','NewsArticle','Y','N','SYSTEM',CURRENT_DATE,'M',1);
Insert into page_navigator(nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (86, 'Quiz','Back','SavedNewsArticle','appnewsarticle','appnewsarticle','Y','N','SYSTEM',CURRENT_DATE,'M',1);
Insert into page_navigator(nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (87, 'Helpdesk','GoPremium','Helpdesk','NewSubscriptionPage','NewSubscriptionPage','Y','N','SYSTEM',CURRENT_DATE,'M',1);
Insert into page_navigator(nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (88, 'Recognitions','Recognitions','ListRecognitions','recognitions','recognitions','Y','N','SYSTEM',CURRENT_DATE,'M',1);
Insert into page_navigator(nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (89, 'Recognitions','SavedArticles','ListRecognitions','appnewsarticle','appnewsarticle','Y','N','SYSTEM',CURRENT_DATE,'M',1);
Insert into page_navigator(nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (90, 'Opinions','FilterOpinons','ListOpinions','opinions','opinions','Y','N','SYSTEM',CURRENT_DATE,'M',1);

Insert into page_navigator(nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (91, 'Opinions','ClearFilterOpinions','ListOpinions','opinions','opinions','Y','N','SYSTEM',CURRENT_DATE,'M',1);

Insert into page_navigator(nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (92, 'Champs','Level1','ListChamps','champs','champs','Y','N','SYSTEM',CURRENT_DATE,'M',1);
Insert into page_navigator(nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (93, 'Champs','Level2','ListChamps','champs','champs','Y','N','SYSTEM',CURRENT_DATE,'M',1);
Insert into page_navigator(nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (94, 'Champs','Level3','ListChamps','champs','champs','Y','N','SYSTEM',CURRENT_DATE,'M',1);
Insert into page_navigator(nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (95, 'Champs','Level4','ListChamps','champs','champs','Y','N','SYSTEM',CURRENT_DATE,'M',1);
Insert into page_navigator(nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (96, 'Champs','RightSwipe','ListChamps','champs','champs','Y','N','SYSTEM',CURRENT_DATE,'M',1);
Insert into page_navigator(nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (96, 'Champs','LeftSwipe','ListChamps','champs','champs','Y','N','SYSTEM',CURRENT_DATE,'M',1);
Insert into page_navigator(nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (97, 'Champs','GoPremium','ListChamps','NewSubscriptionPage','Champs','Y','N','SYSTEM',CURRENT_DATE,'M',1);
Insert into page_navigator(nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (98, 'Champs','Home','ListChamps','publication','Champs','Y','N','SYSTEM',CURRENT_DATE,'M',1);

Insert into page_navigator(nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (99, 'Trends','Month','ListMonthlyTrends','trends','trends','Y','N','SYSTEM',CURRENT_DATE,'M',1);

Insert into page_navigator(nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (100, 'Trends','Next','ListMonthlyTrends','trends','trends','Y','N','SYSTEM',CURRENT_DATE,'M',1);
Insert into page_navigator(nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (101, 'Trends','Previous','ListMonthlyTrends','trends','trends','Y','N','SYSTEM',CURRENT_DATE,'M',1);
Insert into page_navigator(nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (102, 'Trends','Year','ListYearlyTrends','trends','trends','Y','N','SYSTEM',CURRENT_DATE,'M',1);
Insert into page_navigator(nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (103, 'Trends','Next','ListYearlyTrends','trends','trends','Y','N','SYSTEM',CURRENT_DATE,'M',1);
Insert into page_navigator(nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (104, 'Trends','Previous','ListYearlyTrends','trends','trends','Y','N','SYSTEM',CURRENT_DATE,'M',1);

Insert into page_navigator(nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (106, 'Trends','Home','ListYearlyTrends','publication','trends','Y','N','SYSTEM',CURRENT_DATE,'M',1);


Insert into page_navigator(nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (107, 'Publication','MyActivity','Publication','recognitions','recognitions','Y','N','SYSTEM',CURRENT_DATE,'M',1);

Insert into page_navigator(nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (108, 'Publication','MyActivity','Publication','champs','champs','Y','N','SYSTEM',CURRENT_DATE,'M',1);

Insert into page_navigator(nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (109, 'Publication','Menu','Publication','Menu','Menu','Y','N','SYSTEM',CURRENT_DATE,'M',1);

Insert into page_navigator(nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (110, 'Menu','MyActivity','Menu','recognitions','recognitions','Y','N','SYSTEM',CURRENT_DATE,'M',1);

Insert into page_navigator(nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (111, 'Menu','Recognitions','Menu','recognitions','recognitions','Y','N','SYSTEM',CURRENT_DATE,'M',1);

Insert into page_navigator_rules (nav_id,rule_id , exec_seq,rule, nextpage, plugin_class, record_in_use , operator_id, operation_date_time )
values(1, 1, 1, '''#{operation}'' == ''Subscription''', 'StudentDetailsPage','SubscripitionRulePlugin','Y','SYSTEM',CURRENT_DATE);

Insert into page_navigator_rules (nav_id,rule_id , exec_seq,rule, nextpage, plugin_class, record_in_use , operator_id, operation_date_time )
values(4, 2, 1, '''Y'' == ''#{schoolPayment}'' &&  ''#{operation}'' == ''Subscription''', 'NewPreferencePage','SubscripitionSchoolDetailsRulePlugin','Y','SYSTEM',CURRENT_DATE);

commit;

CREATE or replace VIEW champs_vw AS
    SELECT 
        a.studentId as student_Id,a.photo as student_photo, a.name AS student_name, a.surname AS surname, b.grade, c.name as school_name,d.name_id as city_name, e.quiz_correct as Monthly_score, e.trendyear_month as trendyear_month, f.reading_level
    FROM
        student_details a, 
        student_school b, 
        school c, 
        city d, 
        trends_monthly_total e,
        student_preference f
        where a.studentid=b.studentid 
        and b.schoolid=c.school_id 
        and c.city_id=d.city_id 
        and e.student_id=a.studentid
        and a.studentid = f.studentid
        and e.quiz_correct <= (select ee.quiz_correct from trends_monthly_total ee where ee.student_id=e.student_id order by ee.quiz_correct desc limit 10);
 
create or replace view daily_article_vw as
select 
count(b.news_articleid) as article_published,
count((select student_id from trends_daily a where a.quiz_date = b.publish_date)) as articlesRead,
b.publish_date as publish_date
from news_article b ,
publication c,
publication_article_linkage d
where 
 d.news_articleid = b.news_articleid
and d.publicationid=c.publicationid
and c.status='Published' and c.record_in_use='Y'
group by publish_date;


create or replace view daily_quiz_scores_vw as
select 
count(a.news_articleid) as quizpublished , 
count((select sum(quiz_attempted) from trends_daily where quiz_date = a.operation_date_time)) as quiz_attempted,
count((select sum(quiz_correct) from trends_daily where quiz_date = a.operation_date_time)) as quizcorrect,
a.operation_date_time as publish_date
 from 
news_article_quiz a,
news_article b,
publication c,
publication_article_linkage d
where 
a.news_articleid = b.news_articleid
and d.news_articleid = b.news_articleid
and d.publicationid=c.publicationid
and c.status='Published' and c.record_in_use='Y'
group by a.operation_date_time;

create or replace view monthly_articles_genre_vw as 
select e.genreid,
count(b.news_articleid) as article_published,
count((select student_id from trends_daily a where a.quiz_date = b.publish_date)) as articles_read,
b.publish_date as publish_date
from  
news_article b,
publication c,
publication_article_linkage d,
news_article_group e
where 
 d.news_articleid = b.news_articleid
and d.publicationid=c.publicationid
and e.news_article_groupid=b.news_article_groupid
and c.status='Published' and c.record_in_use='Y'
group by e.genreid,publish_date;


create or replace view monthly_quiz_genre_vw as
select 
e.genreid,
count(a.news_articleid) as quizpublished , 
count((select sum(quiz_attempted) from trends_daily where quiz_date = a.operation_date_time)) as quiz_attempted,
count((select sum(quiz_correct) from trends_daily where quiz_date = a.operation_date_time)) as quizcorrect,
a.operation_date_time as publish_date
 from 
news_article_quiz a,
news_article b,
publication c,
publication_article_linkage d,
news_article_group e
where 
a.news_articleid = b.news_articleid
and d.news_articleid = b.news_articleid
and d.publicationid=c.publicationid
and e.news_article_groupid=b.news_article_groupid
and c.status='Published' and c.record_in_use='Y'
group by e.genreid,a.operation_date_time;


create or replace view daily_article_yearly_vw as
select 
count(b.news_articleid) as article_published,
count((select student_id from trends_daily a where a.quiz_date = b.publish_date)) as articles_read,
b.publish_date as publish_date
from news_article b ,
publication c,
publication_article_linkage d
where 
 d.news_articleid = b.news_articleid
and d.publicationid=c.publicationid
and c.status='Published' and c.record_in_use='Y'
group by b.publish_date;


----------Scores---------
create or replace view published_acrticles_vw as
select 
count(b.news_articleid) articles_published,
b.publish_date,
count((select distinct news_articleid from news_article_quiz aa where aa.news_articleid= b.news_articleid and aa.operation_date_time=b.publish_date )) as quiz_published
from  
news_article b,
publication c,
publication_article_linkage d,
news_article_group e
where 
 d.news_articleid = b.news_articleid
and d.publicationid=c.publicationid
and e.news_article_groupid=b.news_article_groupid
and c.status='Published' and c.record_in_use='Y'
group by b.publish_date;



select 
articles_published,
((select article_read from trends_daily aa where aa.quiz_date = a.publish_date and aa.student_id=1)) as articles_read,
a.quiz_published,
((select quiz_attempted from trends_daily aa where aa.quiz_date = a.publish_date and aa.student_id=1)) as quiz_attempted,
((select quiz_correct from trends_daily aa where aa.quiz_date = a.publish_date and aa.student_id=1)) as quizcorrect,
a.publish_date
from published_acrticles_vw a where a.publish_date>='2019-01-01' and a.publish_date<='2019-12-31'
group by a.publish_date,articles_read,quiz_published,quiz_attempted,quizcorrect;



----group by genre
create or replace view published_acrticles_genre_vw as
select 
count(b.news_articleid) articles_published,
b.publish_date,
e.genreid,
count((select distinct news_articleid from news_article_quiz aa where aa.news_articleid= b.news_articleid and aa.operation_date_time=b.publish_date )) as quiz_published
from  
news_article b,
publication c,
publication_article_linkage d,
news_article_group e
where 
 d.news_articleid = b.news_articleid
and d.publicationid=c.publicationid
and e.news_article_groupid=b.news_article_groupid
and c.status='Published' and c.record_in_use='Y'
group by b.publish_date,e.genreid
------------
select 
 articles_published,
((select article_read from trends_daily aa where aa.quiz_date = a.publish_date and aa.student_id=1)) as articles_read,
quiz_published,
((select quiz_attempted from trends_daily aa where aa.quiz_date = a.publish_date and aa.student_id=1)) as quiz_attempted,
((select quiz_correct from trends_daily aa where aa.quiz_date = a.publish_date and aa.student_id=1)) as quizcorrect,
a.genreid
from published_acrticles_genre_vw a where a.publish_date>='2019-01-01' and a.publish_date<='2019-12-31';


-------
select 
sum(a.articles_published) as articles_published,
sum((select (articles_read) from trends_monthly_total aa where year(a.publish_date)= SUBSTRING(aa.trendyear_month, 1,4) and month(a.publish_date)=SUBSTRING(aa.trendyear_month, 5,6) and aa.student_id=1)) as articles_read,
sum(a.quiz_published) as quiz_published,
sum((select (quiz_attempted) from trends_monthly_total aa where  year(a.publish_date)= SUBSTRING(aa.trendyear_month, 1,4) and month(a.publish_date)=SUBSTRING(aa.trendyear_month, 5,6) and aa.student_id=1)) as quiz_attempted,
sum((select (quiz_correct) from trends_monthly_total aa where  year(a.publish_date)= SUBSTRING(aa.trendyear_month, 1,4) and month(a.publish_date)=SUBSTRING(aa.trendyear_month, 5,6) and aa.student_id=1)) as quizcorrect,
month(a.publish_date) publish_date
from published_acrticles_vw a where a.publish_date>='2019-01-01' and a.publish_date<='2019-12-31'
group by (a.publish_date);

---------yearly genre
select 
sum(a.articles_published) as articles_published,
sum((select (articles_read) from trends_monthly_total aa where year(a.publish_date)= SUBSTRING(aa.trendyear_month, 1,4) and month(a.publish_date)=SUBSTRING(aa.trendyear_month, 5,6) and aa.student_id=1)) as articles_read,
sum(a.quiz_published) as quiz_published,
sum((select (quiz_attempted) from trends_monthly_total aa where  year(a.publish_date)= SUBSTRING(aa.trendyear_month, 1,4) and month(a.publish_date)=SUBSTRING(aa.trendyear_month, 5,6) and aa.student_id=1)) as quiz_attempted,
sum((select (quiz_correct) from trends_monthly_total aa where  year(a.publish_date)= SUBSTRING(aa.trendyear_month, 1,4) and month(a.publish_date)=SUBSTRING(aa.trendyear_month, 5,6) and aa.student_id=1)) as quizcorrect,
a.genreid
from published_acrticles_genre_vw a 
where a.publish_date>='2020-01-01' and a.publish_date<='2020-12-31'
group by genreid,articles_published,quiz_published;

