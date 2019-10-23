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
Insert into page_navigator(nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (112, 'Menu','HelpDesk','Menu','helpdesk','helpdesk','Y','N','SYSTEM',CURRENT_DATE,'M',1);
Insert into page_navigator(nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (113, 'Helpdesk','Save','Helpdesk','helpdesk','helpdesk','Y','N','SYSTEM',CURRENT_DATE,'M',1);

Insert into page_navigator(nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (120, 'Notifications','Filter','ListNotifications','notifications','notifications','Y','N','SYSTEM',CURRENT_DATE,'M',1);
Insert into page_navigator(nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (121, 'Notifications','ClearFilter','ListNotifications','notifications','notifications','Y','N','SYSTEM',CURRENT_DATE,'M',1);
Insert into page_navigator(nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (122, 'Notifications','UpSwipe','ListNotifications','notifications','notifications','Y','N','SYSTEM',CURRENT_DATE,'M',1);
Insert into page_navigator(nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (123, 'Publication','Next','UnknownUser','publication','publication','Y','N','SYSTEM',CURRENT_DATE,'M',1);
Insert into page_navigator(nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (124, 'Publication','Previous','UnknownUser','publication','publication','Y','N','SYSTEM',CURRENT_DATE,'M',1);
Insert into page_navigator(nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (125, 'Publication','Next','IncompleteRegistration','publication','publication','Y','N','SYSTEM',CURRENT_DATE,'M',1);
Insert into page_navigator(nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (126, 'Publication','Next','Publication','publication','publication','Y','N','SYSTEM',CURRENT_DATE,'M',1);
Insert into page_navigator(nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (127, 'Publication','LeftSwipe','UnkownUser','publication','publication','Y','N','SYSTEM',CURRENT_DATE,'M',1);
Insert into page_navigator(nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (128, 'Publication','LeftSwipe','IncompleteRegistration','publication','publication','Y','N','SYSTEM',CURRENT_DATE,'M',1);
Insert into page_navigator(nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (129, 'Publication','Previous','IncompleteRegistration','publication','publication','Y','N','SYSTEM',CURRENT_DATE,'M',1);
Insert into page_navigator(nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (130, 'Publication','Previous','Publication','publication','publication','Y','N','SYSTEM',CURRENT_DATE,'M',1);
Insert into page_navigator(nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (131, 'Publication','RightSwipe','Publication','publication','publication','Y','N','SYSTEM',CURRENT_DATE,'M',1);
Insert into page_navigator(nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (132, 'Publication','RightSwipe','IncompleteRegistration','publication','publication','Y','N','SYSTEM',CURRENT_DATE,'M',1);
Insert into page_navigator(nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (133, 'NewsArticle','Back','NewsArticle','publication','publication','Y','N','SYSTEM',CURRENT_DATE,'M',1);
update page_navigator set nextpage='publication' where nav_id=113;

Insert into page_navigator(nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (134, 'SavedArticles','Home','ListSavedArticles','publication','publication','Y','N','SYSTEM',CURRENT_DATE,'M',1);
Insert into page_navigator(nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (135, 'AboutUs','Home','AboutUs','publication','publication','Y','N','SYSTEM',CURRENT_DATE,'M',1);
Insert into page_navigator(nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (136, 'Signin','Cancel','Login','publication','publication','Y','N','SYSTEM',CURRENT_DATE,'M',1);
Insert into page_navigator(nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (137, 'NewsArticle','Back','OpinionNewsArticle','opinions','opinions','Y','N','SYSTEM',CURRENT_DATE,'M',1);
Insert into page_navigator(nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (138, 'SavedArticles','Opinions','ListSavedArticles','opinions','opinions','Y','N','SYSTEM',CURRENT_DATE,'M',1);
Insert into page_navigator(nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (139, 'NewsArticle','Back','SavedNewsArticle','appnewsarticle','appnewsarticle','Y','N','SYSTEM',CURRENT_DATE,'M',1);
Insert into page_navigator(nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (140, 'SavedArticles','SavedArticles','ListSavedArticles','appnewsarticle','appnewsarticle','Y','N','SYSTEM',CURRENT_DATE,'M',1);
Insert into page_navigator(nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (141, 'SavedArticles','FilterSavedArticles','ListSavedArticles','appnewsarticle','appnewsarticle','Y','N','SYSTEM',CURRENT_DATE,'M',1);
Insert into page_navigator(nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (142, 'SavedArticles','ClearFilterSavedArticles','ListSavedArticles','appnewsarticle','appnewsarticle','Y','N','SYSTEM',CURRENT_DATE,'M',1);
Insert into page_navigator(nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (143, 'SavedArticles','UpSwipe','ListSavedArticles','appnewsarticle','appnewsarticle','Y','N','SYSTEM',CURRENT_DATE,'M',1);
Insert into page_navigator(nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (144, 'Opinions','SavedArticles','ListOpinions','appnewsarticle','appnewsarticle','Y','N','SYSTEM',CURRENT_DATE,'M',1);
Insert into page_navigator(nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (145, 'Trends','SavedArticles','ListMonthlyTrends','appnewsarticle','appnewsarticle','Y','N','SYSTEM',CURRENT_DATE,'M',1);
Insert into page_navigator(nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (146, 'Scores','SavedArticles','ListMonthlyScores','appnewsarticle','appnewsarticle','Y','N','SYSTEM',CURRENT_DATE,'M',1);
Insert into page_navigator(nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (147, 'Menu','Scores','Menu','scores','scores','Y','N','SYSTEM',CURRENT_DATE,'M',1);
Insert into page_navigator(nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (148, 'Menu','Trends','Menu','trends','trends','Y','N','SYSTEM',CURRENT_DATE,'M',1);
Insert into page_navigator(nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (149, 'Recognitions','Trends','ListRecognitions','trends','trends','Y','N','SYSTEM',CURRENT_DATE,'M',1);
Insert into page_navigator(nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (150, 'SavedArticles','Trends','ListSavedArticles','trends','trends','Y','N','SYSTEM',CURRENT_DATE,'M',1);
Insert into page_navigator(nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (151, 'Opinions','Trends','ListOpinions','trends','trends','Y','N','SYSTEM',CURRENT_DATE,'M',1);
Insert into page_navigator(nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (152, 'Trends','Year','ListMonthlyTrends','trends','trends','Y','N','SYSTEM',CURRENT_DATE,'M',1);
Insert into page_navigator(nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (153, 'Trends','Month','ListYearlyTrends','trends','trends','Y','N','SYSTEM',CURRENT_DATE,'M',1);
Insert into page_navigator(nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (154, 'Trends','LeftSwipe','ListMonthlyTrends','trends','trends','Y','N','SYSTEM',CURRENT_DATE,'M',1);
Insert into page_navigator(nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (155, 'Trends','RightSwipe','ListMonthlyTrends','trends','trends','Y','N','SYSTEM',CURRENT_DATE,'M',1);
Insert into page_navigator(nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (156, 'Scores','Trends','ListMonthlyScores','trends','trends','Y','N','SYSTEM',CURRENT_DATE,'M',1);
Insert into page_navigator(nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (157, 'Scores','Trends','ListYearlyScores','trends','trends','Y','N','SYSTEM',CURRENT_DATE,'M',1);
Insert into page_navigator(nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (158, 'Recognitions','Scores','ListRecognitions','scores','scores','Y','N','SYSTEM',CURRENT_DATE,'M',1);
Insert into page_navigator(nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (159, 'SavedArticles','Scores','ListSavedArticles','scores','scores','Y','N','SYSTEM',CURRENT_DATE,'M',1);
Insert into page_navigator(nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (160, 'Opinions','Scores','ListOpinions','scores','scores','Y','N','SYSTEM',CURRENT_DATE,'M',1);
Insert into page_navigator(nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (161, 'Trends','Scores','ListMonthlyTrends','scores','scores','Y','N','SYSTEM',CURRENT_DATE,'M',1);
Insert into page_navigator(nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (162, 'Trends','Scores','ListYearlyTrends','scores','scores','Y','N','SYSTEM',CURRENT_DATE,'M',1);
Insert into page_navigator(nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (163, 'Scores','Month','ListYearlyScores','scores','scores','Y','N','SYSTEM',CURRENT_DATE,'M',1);
Insert into page_navigator(nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (164, 'Scores','LeftSwipe','ListMonthlyScores','scores','scores','Y','N','SYSTEM',CURRENT_DATE,'M',1);
Insert into page_navigator(nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (165, 'Scores','RightSwipe','ListMonthlyScores','scores','scores','Y','N','SYSTEM',CURRENT_DATE,'M',1);
Insert into page_navigator(nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (166, 'Scores','Year','ListMonthlyScores','scores','scores','Y','N','SYSTEM',CURRENT_DATE,'M',1);
Insert into page_navigator(nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (167, 'Menu','ShareMyAchievements','Menu','scores','scores','Y','N','SYSTEM',CURRENT_DATE,'M',1);
Insert into page_navigator(nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (168, 'SavedArticles','Recognitions','ListSavedArticles','recognitions','recognitions','Y','N','SYSTEM',CURRENT_DATE,'M',1);
Insert into page_navigator(nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (169, 'Opinions','Recognitions','ListOpinions','recognitions','recognitions','Y','N','SYSTEM',CURRENT_DATE,'M',1);
Insert into page_navigator(nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (170, 'Trends','Recognitions','ListMonthlyTrends','recognitions','recognitions','Y','N','SYSTEM',CURRENT_DATE,'M',1);
Insert into page_navigator(nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (171, 'Scores','Recognitions','ListMonthlyScores','recognitions','recognitions','Y','N','SYSTEM',CURRENT_DATE,'M',1);
Insert into page_navigator(nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (172, 'Champs','LeftSwipe','ListChamps','champs','champs','Y','N','SYSTEM',CURRENT_DATE,'M',1);
Insert into page_navigator(nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (173, 'Menu','GoPremium','Menu','appnewsarticle','appnewsarticle','Y','N','SYSTEM',CURRENT_DATE,'M',1);
Insert into page_navigator(nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (174, 'AboutUs','GoPremium','AboutUs','newsubscriptionpage','newsubscriptionpage','Y','N','SYSTEM',CURRENT_DATE,'M',1);
Insert into page_navigator(nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (175, 'NewsEvents','GoPremium','ListNewsEvents','newsubscriptionpage','newsubscriptionpage','Y','N','SYSTEM',CURRENT_DATE,'M',1);
Insert into page_navigator(nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (176, 'MyProfile','GoPremium','MyProfile','newsubscriptionpage','newsubscriptionpage','Y','N','SYSTEM',CURRENT_DATE,'M',1);
Insert into page_navigator(nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (177, 'SignIn','CreateAccount','LogIn','newsubscriptionpage','newsubscriptionpage','Y','N','SYSTEM',CURRENT_DATE,'M',1);
Insert into page_navigator(nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (178, 'MyProfile','Back','MyProfile','menu','menu','Y','N','SYSTEM',CURRENT_DATE,'M',1);
update page_navigator set nextpage='menu',previous_page='menu' where nav_id=53;

Insert into page_navigator(nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (179, 'NewsArticle','Next','NewsArticle','appnewsarticle','appnewsarticle','Y','N','SYSTEM',CURRENT_DATE,'M',1);
Insert into page_navigator(nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (180, 'NewsArticle','Next','OpinionNewsArticle','opinions','opinions','Y','N','SYSTEM',CURRENT_DATE,'M',1);
Insert into page_navigator(nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (181, 'NewsArticle','LeftSwipe','NewsArticle','appnewsarticle','appnewsarticle','Y','N','SYSTEM',CURRENT_DATE,'M',1);
Insert into page_navigator(nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (182, 'NewsArticle','LeftSwipe','OpinionNewsArticle','opinions','opinions','Y','N','SYSTEM',CURRENT_DATE,'M',1);
Insert into page_navigator(nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (183, 'NewsArticle','LeftSwipe','SavedNewsArticle','appnewsarticle','appnewsarticle','Y','N','SYSTEM',CURRENT_DATE,'M',1);
Insert into page_navigator(nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (184, 'NewsArticle','Previous','NewsArticle','appnewsarticle','appnewsarticle','Y','N','SYSTEM',CURRENT_DATE,'M',1);
Insert into page_navigator(nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (185, 'NewsArticle','LeftSwipe','NewsEvent','appnewsarticle','appnewsarticle','Y','N','SYSTEM',CURRENT_DATE,'M',1);
Insert into page_navigator(nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (186, 'Opinions','Opinions','ListOpinions','opinions','opinions','Y','N','SYSTEM',CURRENT_DATE,'M',1);
Insert into page_navigator(nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (187, 'Opinions','UpSwipe','ListOpinions','opinions','opinions','Y','N','SYSTEM',CURRENT_DATE,'M',1);
Insert into page_navigator(nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (188, 'Trends','Opinions','ListMonthlyTrends','opinions','opinions','Y','N','SYSTEM',CURRENT_DATE,'M',1);
Insert into page_navigator(nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (189, 'Scores','Opinions','ListMonthlyScores','opinions','opinions','Y','N','SYSTEM',CURRENT_DATE,'M',1);
Insert into page_navigator(nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (190, 'Recognitions','Opinions','ListRecognitions','opinions','opinions','Y','N','SYSTEM',CURRENT_DATE,'M',1);
Insert into page_navigator(nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (192, 'signin','SecurityCode','SignUp','signin','signin','Y','N','SYSTEM',CURRENT_DATE,'M',1);

Insert into page_navigator(nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (193, 'signin','Register','SignUp','signin','signin','Y','N','SYSTEM',CURRENT_DATE,'M',1);
Insert into page_navigator(nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (194, 'signin','ResetPassword','SignUp','signin','signin','Y','N','SYSTEM',CURRENT_DATE,'M',1);
Insert into page_navigator(nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (195, 'signin','ResendSecurityCode','SignUp','signin','signin','Y','N','SYSTEM',CURRENT_DATE,'M',1);
Insert into page_navigator(nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (197, 'signin','DeleteAccount','SignUp','signin','signin','Y','N','SYSTEM',CURRENT_DATE,'M',1);
Insert into page_navigator(nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (198, 'Login','Login','Login','login','login','Y','N','SYSTEM',CURRENT_DATE,'M',1);
Insert into page_navigator(nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (199, 'Login','Logout','Login','login','login','Y','N','SYSTEM',CURRENT_DATE,'M',1);
Insert into page_navigator(nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (200, 'WelcomeUser','WelcomeUser','WelcomeUser','welcome','welcome','Y','N','SYSTEM',CURRENT_DATE,'M',1);
Insert into page_navigator(nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (201, 'NewsEvents','NewsEvents','Publication','newsevents','newsevents','Y','N','SYSTEM',CURRENT_DATE,'M',1);
Insert into page_navigator(nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (202, 'NewsArticle','Back','NewsEvent','newsevents','newsevents','Y','N','SYSTEM',CURRENT_DATE,'M',1);
Insert into page_navigator(nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (203, 'NewsEvents','FilterNewsEvents','ListNewsEvents','newsevents','newsevents','Y','N','SYSTEM',CURRENT_DATE,'M',1);
Insert into page_navigator(nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (204, 'NewsEvents','ClearFilterNewsEvents','ListNewsEvents','newsevents','newsevents','Y','N','SYSTEM',CURRENT_DATE,'M',1);
Insert into page_navigator(nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (205, 'NewsEvents','UpSwipe','ListNewsEvents','newsevents','newsevents','Y','N','SYSTEM',CURRENT_DATE,'M',1);
Insert into page_navigator(nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (206, 'NewsArticle','Previous','OpinionNewsArticle','opinions','opinions','Y','N','SYSTEM',CURRENT_DATE,'M',1);
Insert into page_navigator(nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (207, 'NewsArticle','RightSwipe','NewsArticle','appnewsarticle','appnewsarticle','Y','N','SYSTEM',CURRENT_DATE,'M',1);
Insert into page_navigator(nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (208, 'NewsArticle','RightSwipe','OpinionNewsArticle','opinions','opinions','Y','N','SYSTEM',CURRENT_DATE,'M',1);
Insert into page_navigator(nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (209, 'NewsArticle','RightSwipe','NewsEvent','newsevents','newsevents','Y','N','SYSTEM',CURRENT_DATE,'M',1);
Insert into page_navigator(nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (210, 'SavedArticles','ClickSavedArticle','ListSavedArticles','appnewsarticle','appnewsarticle','Y','N','SYSTEM',CURRENT_DATE,'M',1);
Insert into page_navigator(nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (211, 'Opinions','ClickOpinionArticle','ListOpinionArticles','appnewsarticle','appnewsarticle','Y','N','SYSTEM',CURRENT_DATE,'M',1);
Insert into page_navigator(nav_Id ,current_page ,action ,operation ,nextpage , previous_Page, record_In_Use ,commit_Master_Data ,operator_Id ,operation_Date_Time ,updation_Table,process_Seq)
values (212, 'NewsEvents','ClickNewsEvent','ListNewsEvents','appnewsarticle','appnewsarticle','Y','N','SYSTEM',CURRENT_DATE,'M',1);


Insert into page_navigator_rules (nav_id,rule_id , exec_seq,rule, nextpage, plugin_class, record_in_use , operator_id, operation_date_time )
values(1, 1, 1, '''#{operation}'' == ''Subscription''', 'StudentDetailsPage','SubscripitionRulePlugin','Y','SYSTEM',CURRENT_DATE);

Insert into page_navigator_rules (nav_id,rule_id , exec_seq,rule, nextpage, plugin_class, record_in_use , operator_id, operation_date_time )
values(4, 2, 1, '''Y'' == ''#{schoolPayment}'' &&  ''#{operation}'' == ''Subscription''', 'NewPreferencePage','SubscripitionSchoolDetailsRulePlugin','Y','SYSTEM',CURRENT_DATE);

commit;


insert into uicontrols values (1, CURRENT_DATE,'SYSTEM','Y','Next','Button',CURRENT_DATE,'','',true,'Publication',true);
insert into uicontrols values (2, CURRENT_DATE,'SYSTEM','Y','Previous','Button',CURRENT_DATE,'','',false,'Publication',false);
insert into uicontrols values (3, CURRENT_DATE,'SYSTEM','Y','LeftSwipe','Gesture',CURRENT_DATE,'','',true,'Publication',true);
insert into uicontrols values (4, CURRENT_DATE,'SYSTEM','Y','RightSwipe','Gesture',CURRENT_DATE,'','',true,'Publication',false);
insert into uicontrols values (5, CURRENT_DATE,'SYSTEM','Y','UpSwipe','Button',CURRENT_DATE,'','',true,'Publication',true);
insert into uicontrols values (6, CURRENT_DATE,'SYSTEM','Y','DownSwipe','Button',CURRENT_DATE,'','',true,'Publication',false);
insert into uicontrols values (7, CURRENT_DATE,'SYSTEM','Y','GoPremium','Button',CURRENT_DATE,'','',true,'Publication',true);
insert into uicontrols values (8, CURRENT_DATE,'SYSTEM','Y','Champs','Button',CURRENT_DATE,'','',true,'Publication',true);
insert into uicontrols values (9, CURRENT_DATE,'SYSTEM','Y','NewsEvents','Button',CURRENT_DATE,'','',true,'Publication',true);
insert into uicontrols values (10, CURRENT_DATE,'SYSTEM','Y','MyActivity','Button',CURRENT_DATE,'','',true,'Publication',true);
insert into uicontrols values (11, CURRENT_DATE,'SYSTEM','Y','Notifications','Button',CURRENT_DATE,'','',true,'Publication',true);
insert into uicontrols values (12, CURRENT_DATE,'SYSTEM','Y','Menu','Button',CURRENT_DATE,'','',true,'Publication',true);
insert into uicontrols values (13, CURRENT_DATE,'SYSTEM','Y','GoPremium','Button',CURRENT_DATE,'','',true,'Menu',true);
insert into uicontrols values (14, CURRENT_DATE,'SYSTEM','Y','picImage','Button',CURRENT_DATE,'','',false,'Menu',false);
insert into uicontrols values (15, CURRENT_DATE,'SYSTEM','Y','MyProfile','Button',CURRENT_DATE,'','',true,'Menu',true);
insert into uicontrols values (16, CURRENT_DATE,'SYSTEM','Y','MyPicture','Button',CURRENT_DATE,'','',true,'Menu',false);
insert into uicontrols values (17, CURRENT_DATE,'SYSTEM','Y','StudentDetails','Button',CURRENT_DATE,'','',true,'Menu',true);
insert into uicontrols values (18, CURRENT_DATE,'SYSTEM','Y','SchoolDetails','Button',CURRENT_DATE,'','',true,'Menu',false);
insert into uicontrols values (19, CURRENT_DATE,'SYSTEM','Y','Preferences','Button',CURRENT_DATE,'','',true,'Menu',true);
insert into uicontrols values (20, CURRENT_DATE,'SYSTEM','Y','Password','Button',CURRENT_DATE,'','',true,'Menu',true);
insert into uicontrols values (21, CURRENT_DATE,'SYSTEM','Y','DeleteAccount','Button',CURRENT_DATE,'','',true,'Menu',true);
insert into uicontrols values (22, CURRENT_DATE,'SYSTEM','Y','MyActvity','Button',CURRENT_DATE,'','',true,'Menu',true);
insert into uicontrols values (23, CURRENT_DATE,'SYSTEM','Y','ShareAchievements','Button',CURRENT_DATE,'','',true,'Menu',false);
insert into uicontrols values (24, CURRENT_DATE,'SYSTEM','Y','SavedArticles','Button',CURRENT_DATE,'','',true,'Menu',false);
insert into uicontrols values (25, CURRENT_DATE,'SYSTEM','Y','Opinions','Button',CURRENT_DATE,'','',false,'Menu',false);
insert into uicontrols values (26, CURRENT_DATE,'SYSTEM','Y','Trends','Gesture',CURRENT_DATE,'','',true,'Menu',true);
insert into uicontrols values (27, CURRENT_DATE,'SYSTEM','Y','Scores','Gesture',CURRENT_DATE,'','',true,'Menu',false);
insert into uicontrols values (28, CURRENT_DATE,'SYSTEM','Y','Subscription','Button',CURRENT_DATE,'','',true,'Menu',true);
insert into uicontrols values (29, CURRENT_DATE,'SYSTEM','Y','Info','Button',CURRENT_DATE,'','',true,'Menu',true);
insert into uicontrols values (30, CURRENT_DATE,'SYSTEM','Y','Helpdesk','Button',CURRENT_DATE,'','',true,'Menu',true);
insert into uicontrols values (31, CURRENT_DATE,'SYSTEM','Y','Home','Button',CURRENT_DATE,'','',true,'Menu',true);
insert into uicontrols values (32, CURRENT_DATE,'SYSTEM','Y','Recognitions','Button',CURRENT_DATE,'','',true,'Menu',false);
insert into uicontrols values (45, CURRENT_DATE,'SYSTEM','Y','GoPremium','Button',CURRENT_DATE,'','',true,'Quiz',true);
insert into uicontrols values (46, CURRENT_DATE,'SYSTEM','Y','OptCheckbox','Button',CURRENT_DATE,'','',false,'Quiz',false);
insert into uicontrols values (47, CURRENT_DATE,'SYSTEM','Y','Save','Gesture',CURRENT_DATE,'','',true,'Quiz',true);
insert into uicontrols values (48, CURRENT_DATE,'SYSTEM','Y','RightSwipe','Gesture',CURRENT_DATE,'','',true,'Quiz',false);
insert into uicontrols values (49, CURRENT_DATE,'SYSTEM','Y','Back','Button',CURRENT_DATE,'','',true,'Quiz',true);
insert into uicontrols values (50, CURRENT_DATE,'SYSTEM','Y','GoPremium','Button',CURRENT_DATE,'','',true,'NewsArticleQuizCompletion',true);
insert into uicontrols values (51, CURRENT_DATE,'SYSTEM','Y','OK','Button',CURRENT_DATE,'','',false,'NewsArticleQuizCompletion',false);
insert into uicontrols values (52, CURRENT_DATE,'SYSTEM','Y','GoPremium','Button',CURRENT_DATE,'','',true,'Helpdesk',true);
insert into uicontrols values (53, CURRENT_DATE,'SYSTEM','Y','Category','Dropdown',CURRENT_DATE,'','',false,'Helpdesk',false);
insert into uicontrols values (54, CURRENT_DATE,'SYSTEM','Y','Details','TextArea',CURRENT_DATE,'','',true,'Helpdesk',true);
insert into uicontrols values (55, CURRENT_DATE,'SYSTEM','Y','requestCallback','Checkbox',CURRENT_DATE,'','',true,'Helpdesk',false);
insert into uicontrols values (56, CURRENT_DATE,'SYSTEM','Y','phoneNumber','Textbox',CURRENT_DATE,'','',true,'Helpdesk',true);
insert into uicontrols values (57, CURRENT_DATE,'SYSTEM','Y','preferredDate','Dropdown',CURRENT_DATE,'','',true,'Helpdesk',false);
insert into uicontrols values (58, CURRENT_DATE,'SYSTEM','Y','preferredTime','Dropdown',CURRENT_DATE,'','',true,'Helpdesk',true);
insert into uicontrols values (59, CURRENT_DATE,'SYSTEM','Y','Save','Button',CURRENT_DATE,'','',true,'Helpdesk',true);
insert into uicontrols values (60, CURRENT_DATE,'SYSTEM','Y','Clear','Button',CURRENT_DATE,'','',true,'Helpdesk',true);
insert into uicontrols values (61, CURRENT_DATE,'SYSTEM','Y','Home','Button',CURRENT_DATE,'','',true,'Helpdesk',true);
insert into uicontrols values (62, CURRENT_DATE,'SYSTEM','Y','GoPremium','Button',CURRENT_DATE,'','',true,'Recognitions',true);
insert into uicontrols values (63, CURRENT_DATE,'SYSTEM','Y','Recognitions','Button',CURRENT_DATE,'','',false,'Recognitions',false);
insert into uicontrols values (64, CURRENT_DATE,'SYSTEM','Y','SavedArticles','Button',CURRENT_DATE,'','',true,'Recognitions',true);
insert into uicontrols values (65, CURRENT_DATE,'SYSTEM','Y','Opinions','Button',CURRENT_DATE,'','',true,'Recognitions',false);
insert into uicontrols values (66, CURRENT_DATE,'SYSTEM','Y','Trends','Button',CURRENT_DATE,'','',true,'Recognitions',true);
insert into uicontrols values (67, CURRENT_DATE,'SYSTEM','Y','Scores','Button',CURRENT_DATE,'','',true,'Recognitions',false);
insert into uicontrols values (68, CURRENT_DATE,'SYSTEM','Y','Home','Button',CURRENT_DATE,'','',true,'Recognitions',true);
insert into uicontrols values (69, CURRENT_DATE,'SYSTEM','Y','DownSwipe','Gesture',CURRENT_DATE,'','',true,'Recognitions',true);
insert into uicontrols values (70, CURRENT_DATE,'SYSTEM','Y','UpSwipe','Gesture',CURRENT_DATE,'','',true,'Recognitions',true);
insert into uicontrols values (71, CURRENT_DATE,'SYSTEM','Y','GoPremium','Button',CURRENT_DATE,'','',true,'SavedArticles',true);
insert into uicontrols values (72, CURRENT_DATE,'SYSTEM','Y','Recognitions','Button',CURRENT_DATE,'','',false,'SavedArticles',false);
insert into uicontrols values (74, CURRENT_DATE,'SYSTEM','Y','SavedArticles','Button',CURRENT_DATE,'','',true,'SavedArticles',true);
insert into uicontrols values (75, CURRENT_DATE,'SYSTEM','Y','Opinions','Button',CURRENT_DATE,'','',true,'SavedArticles',false);
insert into uicontrols values (76, CURRENT_DATE,'SYSTEM','Y','Trends','Button',CURRENT_DATE,'','',true,'SavedArticles',true);
insert into uicontrols values (77, CURRENT_DATE,'SYSTEM','Y','Genre','Dropdown',CURRENT_DATE,'','',true,'SavedArticles',false);
insert into uicontrols values (78, CURRENT_DATE,'SYSTEM','Y','Month','Dropdown',CURRENT_DATE,'','',true,'SavedArticles',true);
insert into uicontrols values (79, CURRENT_DATE,'SYSTEM','Y','Headline','Textbox',CURRENT_DATE,'','',true,'SavedArticles',true);
insert into uicontrols values (80, CURRENT_DATE,'SYSTEM','Y','FilterSavedArticles','Button',CURRENT_DATE,'','',true,'SavedArticles',true);
insert into uicontrols values (81, CURRENT_DATE,'SYSTEM','Y','ClearFilterSavedArticles','Button',CURRENT_DATE,'','',true,'SavedArticles',false);
insert into uicontrols values (82, CURRENT_DATE,'SYSTEM','Y','Home','Button',CURRENT_DATE,'','',true,'SavedArticles',true);
insert into uicontrols values (83, CURRENT_DATE,'SYSTEM','Y','DownSwipe','Gesture',CURRENT_DATE,'','',true,'SavedArticles',true);
insert into uicontrols values (84, CURRENT_DATE,'SYSTEM','Y','UpSwipe','Gesture',CURRENT_DATE,'','',true,'SavedArticles',true);
insert into uicontrols values (99, CURRENT_DATE,'SYSTEM','Y','GoPremium','Button',CURRENT_DATE,'','',true,'Trends',true);
insert into uicontrols values (100, CURRENT_DATE,'SYSTEM','Y','Recognitions','Button',CURRENT_DATE,'','',false,'Trends',false);
insert into uicontrols values (101, CURRENT_DATE,'SYSTEM','Y','SavedArticles','Button',CURRENT_DATE,'','',true,'Trends',true);
insert into uicontrols values (102, CURRENT_DATE,'SYSTEM','Y','Opinions','Button',CURRENT_DATE,'','',true,'Trends',false);
insert into uicontrols values (103, CURRENT_DATE,'SYSTEM','Y','Trends','Button',CURRENT_DATE,'','',true,'Trends',true);
insert into uicontrols values (104, CURRENT_DATE,'SYSTEM','Y','Scores','Button',CURRENT_DATE,'','',true,'Trends',true);
insert into uicontrols values (105, CURRENT_DATE,'SYSTEM','Y','Month','Button',CURRENT_DATE,'','',true,'Trends',false);
insert into uicontrols values (106, CURRENT_DATE,'SYSTEM','Y','Year','Button',CURRENT_DATE,'','',true,'Trends',true);
insert into uicontrols values (107, CURRENT_DATE,'SYSTEM','Y','Home','Button',CURRENT_DATE,'','',true,'Trends',true);
insert into uicontrols values (108, CURRENT_DATE,'SYSTEM','Y','Next','Button',CURRENT_DATE,'','',true,'Trends',false);
insert into uicontrols values (109, CURRENT_DATE,'SYSTEM','Y','Previous','Button',CURRENT_DATE,'','',true,'Trends',true);
insert into uicontrols values (110, CURRENT_DATE,'SYSTEM','Y','RightSwipe','Gesture',CURRENT_DATE,'','',true,'Trends',false);
insert into uicontrols values (111, CURRENT_DATE,'SYSTEM','Y','LeftSwipe','Gesture',CURRENT_DATE,'','',true,'Trends',true);
insert into uicontrols values (112, CURRENT_DATE,'SYSTEM','Y','GoPremium','Button',CURRENT_DATE,'','',true,'Scores',true);
insert into uicontrols values (113, CURRENT_DATE,'SYSTEM','Y','Recognitions','Button',CURRENT_DATE,'','',false,'Scores',false);
insert into uicontrols values (114, CURRENT_DATE,'SYSTEM','Y','SavedArticles','Button',CURRENT_DATE,'','',true,'Scores',true);
insert into uicontrols values (115, CURRENT_DATE,'SYSTEM','Y','Opinions','Button',CURRENT_DATE,'','',true,'Scores',false);
insert into uicontrols values (116, CURRENT_DATE,'SYSTEM','Y','Trends','Button',CURRENT_DATE,'','',true,'Scores',true);
insert into uicontrols values (117, CURRENT_DATE,'SYSTEM','Y','Scores','Button',CURRENT_DATE,'','',true,'Scores',true);
insert into uicontrols values (118, CURRENT_DATE,'SYSTEM','Y','Month','Button',CURRENT_DATE,'','',true,'Scores',false);
insert into uicontrols values (119, CURRENT_DATE,'SYSTEM','Y','Year','Button',CURRENT_DATE,'','',true,'Scores',true);
insert into uicontrols values (120, CURRENT_DATE,'SYSTEM','Y','Home','Button',CURRENT_DATE,'','',true,'Scores',true);
insert into uicontrols values (121, CURRENT_DATE,'SYSTEM','Y','Next','Button',CURRENT_DATE,'','',true,'Scores',false);
insert into uicontrols values (122, CURRENT_DATE,'SYSTEM','Y','Previous','Button',CURRENT_DATE,'','',true,'Scores',true);
insert into uicontrols values (123, CURRENT_DATE,'SYSTEM','Y','RightSwipe','Gesture',CURRENT_DATE,'','',true,'Scores',false);
insert into uicontrols values (124, CURRENT_DATE,'SYSTEM','Y','LeftSwipe','Gesture',CURRENT_DATE,'','',true,'Scores',true);
insert into uicontrols values (125, CURRENT_DATE,'SYSTEM','Y','GoPremium','Button',CURRENT_DATE,'','',true,'Champs',true);
insert into uicontrols values (126, CURRENT_DATE,'SYSTEM','Y','Level1','Button',CURRENT_DATE,'','',false,'Champs',false);
insert into uicontrols values (127, CURRENT_DATE,'SYSTEM','Y','Level2','Button',CURRENT_DATE,'','',true,'Champs',true);
insert into uicontrols values (128, CURRENT_DATE,'SYSTEM','Y','Level3','Button',CURRENT_DATE,'','',true,'Champs',false);
insert into uicontrols values (129, CURRENT_DATE,'SYSTEM','Y','Level4','Button',CURRENT_DATE,'','',true,'Champs',true);
insert into uicontrols values (130, CURRENT_DATE,'SYSTEM','Y','Home','Button',CURRENT_DATE,'','',true,'Champs',true);
insert into uicontrols values (131, CURRENT_DATE,'SYSTEM','Y','RightSwipe','Gesture',CURRENT_DATE,'','',true,'Champs',false);
insert into uicontrols values (132, CURRENT_DATE,'SYSTEM','Y','LeftSwipe','Gesture',CURRENT_DATE,'','',true,'Champs',true);
insert into uicontrols values (133, CURRENT_DATE,'SYSTEM','Y','Next','Button',CURRENT_DATE,'','',true,'Champs',true);
insert into uicontrols values (134, CURRENT_DATE,'SYSTEM','Y','Previous','Button',CURRENT_DATE,'','',true,'Champs',false);
insert into uicontrols values (135, CURRENT_DATE,'SYSTEM','Y','Previous','Button',CURRENT_DATE,'','',true,'Champs',true);
insert into uicontrols values (136, CURRENT_DATE,'SYSTEM','Y','RightSwipe','Gesture',CURRENT_DATE,'','',true,'Champs',false);
insert into uicontrols values (137, CURRENT_DATE,'SYSTEM','Y','LeftSwipe','Gesture',CURRENT_DATE,'','',true,'Champs',true);
insert into uicontrols values (138, CURRENT_DATE,'SYSTEM','Y','GoPremium','Button',CURRENT_DATE,'','',true,'AboutUs',true);
insert into uicontrols values (139, CURRENT_DATE,'SYSTEM','Y','Home','Button',CURRENT_DATE,'','',false,'AboutUs',true);
insert into uicontrols values (140, CURRENT_DATE,'SYSTEM','Y','GoPremium','Button',CURRENT_DATE,'','',true,'NewsEvents',true);
insert into uicontrols values (141, CURRENT_DATE,'SYSTEM','Y','City','Textbox',CURRENT_DATE,'','',false,'NewsEvents',true);
insert into uicontrols values (142, CURRENT_DATE,'SYSTEM','Y','Month','Dropdown',CURRENT_DATE,'','',true,'NewsEvents',true);
insert into uicontrols values (143, CURRENT_DATE,'SYSTEM','Y','Headline','Textbox',CURRENT_DATE,'','',true,'NewsEvents',false);
insert into uicontrols values (144, CURRENT_DATE,'SYSTEM','Y','FilterNewsEvents','Button',CURRENT_DATE,'','',true,'NewsEvents',true);
insert into uicontrols values (145, CURRENT_DATE,'SYSTEM','Y','ClearFilterNewsEvents','Button',CURRENT_DATE,'','',true,'NewsEvents',true);
insert into uicontrols values (146, CURRENT_DATE,'SYSTEM','Y','Home','Button',CURRENT_DATE,'','',true,'NewsEvents',false);
insert into uicontrols values (147, CURRENT_DATE,'SYSTEM','Y','UpSwipe','Button',CURRENT_DATE,'','',true,'NewsEvents',true);
insert into uicontrols values (148, CURRENT_DATE,'SYSTEM','Y','DownSwipe','Button',CURRENT_DATE,'','',true,'NewsEvents',true);
insert into uicontrols values (149, CURRENT_DATE,'SYSTEM','Y','GoPremium','Button',CURRENT_DATE,'','',true,'SignIn',true);
insert into uicontrols values (150, CURRENT_DATE,'SYSTEM','Y','eMailID','Textbox',CURRENT_DATE,'','',false,'SignIn',true);
insert into uicontrols values (151, CURRENT_DATE,'SYSTEM','Y','Password','Textbox',CURRENT_DATE,'','',true,'SignIn',true);
insert into uicontrols values (152, CURRENT_DATE,'SYSTEM','Y','ForgotAccount','Button',CURRENT_DATE,'','',true,'SignIn',false);
insert into uicontrols values (153, CURRENT_DATE,'SYSTEM','Y','CreateAccount','Button',CURRENT_DATE,'','',true,'SignIn',true);
insert into uicontrols values (154, CURRENT_DATE,'SYSTEM','Y','DeleteAccount','Button',CURRENT_DATE,'','',true,'SignIn',true);
insert into uicontrols values (155, CURRENT_DATE,'SYSTEM','Y','SignIn','Button',CURRENT_DATE,'','',true,'SignIn',false);
insert into uicontrols values (156, CURRENT_DATE,'SYSTEM','Y','UpSwipe','Button',CURRENT_DATE,'','',true,'SignIn',true);
insert into uicontrols values (157, CURRENT_DATE,'SYSTEM','Y','DownSwipe','Button',CURRENT_DATE,'','',true,'SignIn',true);

insert into uicontrols values (158, CURRENT_DATE,'SYSTEM','Y','GoPremium','Button',CURRENT_DATE,'','',true,'ResetPassword',true);
insert into uicontrols values (159, CURRENT_DATE,'SYSTEM','Y','eMailID','Textbox',CURRENT_DATE,'','',false,'ResetPassword',true);
insert into uicontrols values (160, CURRENT_DATE,'SYSTEM','Y','SecurityCode','Textbox',CURRENT_DATE,'','',true,'ResetPassword',true);
insert into uicontrols values (161, CURRENT_DATE,'SYSTEM','Y','NewPassword','Textbox',CURRENT_DATE,'','',true,'ResetPassword',false);
insert into uicontrols values (162, CURRENT_DATE,'SYSTEM','Y','VerifyPassword','Textbox',CURRENT_DATE,'','',true,'ResetPassword',true);
insert into uicontrols values (163, CURRENT_DATE,'SYSTEM','Y','ResendSecurityCode','Button',CURRENT_DATE,'','',true,'ResetPassword',true);
insert into uicontrols values (164, CURRENT_DATE,'SYSTEM','Y','Cancel','Button',CURRENT_DATE,'','',true,'ResetPassword',false);
insert into uicontrols values (165, CURRENT_DATE,'SYSTEM','Y','SetPassword','Button',CURRENT_DATE,'','',true,'ResetPassword',true);
insert into uicontrols values (166, CURRENT_DATE,'SYSTEM','Y','GoPremium','Button',CURRENT_DATE,'','',true,'Subscription',true);
insert into uicontrols values (167, CURRENT_DATE,'SYSTEM','Y','eMailID','Textbox',CURRENT_DATE,'','',false,'Subscription',true);
insert into uicontrols values (168, CURRENT_DATE,'SYSTEM','Y','StandardSubscription','Radio',CURRENT_DATE,'','',true,'Subscription',true);
insert into uicontrols values (169, CURRENT_DATE,'SYSTEM','Y','PremiumSubscription','Radio',CURRENT_DATE,'','',true,'Subscription',false);
insert into uicontrols values (170, CURRENT_DATE,'SYSTEM','Y','SchoolSubscription','Radio',CURRENT_DATE,'','',true,'Subscription',true);
insert into uicontrols values (171, CURRENT_DATE,'SYSTEM','Y','TermsOfUse','Button',CURRENT_DATE,'','',true,'Subscription',true);
insert into uicontrols values (172, CURRENT_DATE,'SYSTEM','Y','PrivacyPolicy','Button',CURRENT_DATE,'','',true,'Subscription',false);
insert into uicontrols values (173, CURRENT_DATE,'SYSTEM','Y','Next','Button',CURRENT_DATE,'','',true,'Subscription',true);
insert into uicontrols values (174, CURRENT_DATE,'SYSTEM','Y','Previous','Button',CURRENT_DATE,'','',true,'Subscription',true);
insert into uicontrols values (175, CURRENT_DATE,'SYSTEM','Y','GoPremium','Button',CURRENT_DATE,'','',true,'SubscriptionPeriod',true);
insert into uicontrols values (176, CURRENT_DATE,'SYSTEM','Y','1Month','Radio',CURRENT_DATE,'','',false,'SubscriptionPeriod',true);
insert into uicontrols values (177, CURRENT_DATE,'SYSTEM','Y','3Months','Radio',CURRENT_DATE,'','',true,'SubscriptionPeriod',true);
insert into uicontrols values (178, CURRENT_DATE,'SYSTEM','Y','6Months','Radio',CURRENT_DATE,'','',true,'SubscriptionPeriod',false);
insert into uicontrols values (179, CURRENT_DATE,'SYSTEM','Y','12Months','Radio',CURRENT_DATE,'','',true,'SubscriptionPeriod',true);
insert into uicontrols values (180, CURRENT_DATE,'SYSTEM','Y','Autorenew','CheckBox',CURRENT_DATE,'','',true,'SubscriptionPeriod',true);
insert into uicontrols values (181, CURRENT_DATE,'SYSTEM','Y','Next','Button',CURRENT_DATE,'','',true,'SubscriptionPeriod',true);
insert into uicontrols values (182, CURRENT_DATE,'SYSTEM','Y','Previous','Button',CURRENT_DATE,'','',true,'SubscriptionPeriod',true);
insert into uicontrols values (183, CURRENT_DATE,'SYSTEM','Y','GoPremium','Button',CURRENT_DATE,'','',true,'StudentDetails',true);
insert into uicontrols values (184, CURRENT_DATE,'SYSTEM','Y','DoBText','Button',CURRENT_DATE,'','',true,'StudentDetails',true);
insert into uicontrols values (185, CURRENT_DATE,'SYSTEM','Y','MyPicture','Button',CURRENT_DATE,'','',true,'StudentDetails',true);
insert into uicontrols values (186, CURRENT_DATE,'SYSTEM','Y','Name','TextBox',CURRENT_DATE,'','',true,'StudentDetails',true);
insert into uicontrols values (187, CURRENT_DATE,'SYSTEM','Y','Surname','TextBox',CURRENT_DATE,'','',true,'StudentDetails',true);
insert into uicontrols values (188, CURRENT_DATE,'SYSTEM','Y','OtherNames','TextBox',CURRENT_DATE,'','',true,'StudentDetails',true);
insert into uicontrols values (189, CURRENT_DATE,'SYSTEM','Y','Gender','Dropdown',CURRENT_DATE,'','',true,'StudentDetails',true);
insert into uicontrols values (190, CURRENT_DATE,'SYSTEM','Y','DoB','TextBox',CURRENT_DATE,'','',true,'StudentDetails',true);
insert into uicontrols values (200, CURRENT_DATE,'SYSTEM','Y','Mobile','Button',CURRENT_DATE,'','',true,'StudentDetails',true);
insert into uicontrols values (201, CURRENT_DATE,'SYSTEM','Y','Next','Button',CURRENT_DATE,'','',true,'StudentDetails',false);
insert into uicontrols values (202, CURRENT_DATE,'SYSTEM','Y','Previous','Button',CURRENT_DATE,'','',true,'StudentDetails',true);
insert into uicontrols values (203, CURRENT_DATE,'SYSTEM','Y','Avtar','Button',CURRENT_DATE,'','',true,'MyPicture',true);
insert into uicontrols values (204, CURRENT_DATE,'SYSTEM','Y','Photo','Button',CURRENT_DATE,'','',true,'MyPicture',true);
insert into uicontrols values (205, CURRENT_DATE,'SYSTEM','Y','Save','Button',CURRENT_DATE,'','',true,'MyPicture',true);
insert into uicontrols values (206, CURRENT_DATE,'SYSTEM','Y','Back','Button',CURRENT_DATE,'','',true,'MyPicture',true);
insert into uicontrols values (207, CURRENT_DATE,'SYSTEM','Y','GoPremium','Button',CURRENT_DATE,'','',true,'SchoolDetails',true);
insert into uicontrols values (208, CURRENT_DATE,'SYSTEM','Y','Country','Dropdown',CURRENT_DATE,'','',true,'SchoolDetails',true);
insert into uicontrols values (209, CURRENT_DATE,'SYSTEM','Y','State','Dropdown',CURRENT_DATE,'','',true,'SchoolDetails',true);
insert into uicontrols values (210, CURRENT_DATE,'SYSTEM','Y','City','Dropdown',CURRENT_DATE,'','',true,'SchoolDetails',true);
insert into uicontrols values (211, CURRENT_DATE,'SYSTEM','Y','School','TextBox',CURRENT_DATE,'','',true,'SchoolDetails',true);
insert into uicontrols values (212, CURRENT_DATE,'SYSTEM','Y','Grade','TextBox',CURRENT_DATE,'','',true,'SchoolDetails',true);
insert into uicontrols values (213, CURRENT_DATE,'SYSTEM','Y','Section','TextBox',CURRENT_DATE,'','',true,'SchoolDetails',true);
insert into uicontrols values (214, CURRENT_DATE,'SYSTEM','Y','Next','Button',CURRENT_DATE,'','',true,'SchoolDetails',true);
insert into uicontrols values (215, CURRENT_DATE,'SYSTEM','Y','Previous','Button',CURRENT_DATE,'','',true,'SchoolDetails',true);
insert into uicontrols values (216, CURRENT_DATE,'SYSTEM','Y','GoPremium','Button',CURRENT_DATE,'','',true,'Preferences',true);
insert into uicontrols values (217, CURRENT_DATE,'SYSTEM','Y','ReadingLevel','RadioButton',CURRENT_DATE,'','',true,'Preferences',true);
insert into uicontrols values (218, CURRENT_DATE,'SYSTEM','Y','DailyNewsOnEmail','CheckBox',CURRENT_DATE,'','',true,'Preferences',true);
insert into uicontrols values (219, CURRENT_DATE,'SYSTEM','Y','ScoresOnEmail','CheckBox',CURRENT_DATE,'','',true,'Preferences',true);
insert into uicontrols values (220, CURRENT_DATE,'SYSTEM','Y','NotificationsOnEmail','CheckBox',CURRENT_DATE,'','',true,'Preferences',true);
insert into uicontrols values (221, CURRENT_DATE,'SYSTEM','Y','Save','Button',CURRENT_DATE,'','',true,'Preferences',true);
insert into uicontrols values (222, CURRENT_DATE,'SYSTEM','Y','Back','Button',CURRENT_DATE,'','',true,'Preferences',true);
insert into uicontrols values (223, CURRENT_DATE,'SYSTEM','Y','Next','Button',CURRENT_DATE,'','',true,'Preferences',true);
insert into uicontrols values (224, CURRENT_DATE,'SYSTEM','Y','Previous','Button',CURRENT_DATE,'','',true,'Preferences',true);
insert into uicontrols values (225, CURRENT_DATE,'SYSTEM','Y','GoPremium','Button',CURRENT_DATE,'','',true,'MyProfile',true);
insert into uicontrols values (226, CURRENT_DATE,'SYSTEM','Y','Back','Button',CURRENT_DATE,'','',true,'MyProfile',true);
insert into uicontrols values (227, CURRENT_DATE,'SYSTEM','Y','GoPremium','Button',CURRENT_DATE,'','',true,'ShareAchievements',true);
insert into uicontrols values (228, CURRENT_DATE,'SYSTEM','Y','personalisedMsg','TextArea',CURRENT_DATE,'','',true,'ShareAchievements',true);
insert into uicontrols values (229, CURRENT_DATE,'SYSTEM','Y','eMailID','Textbox',CURRENT_DATE,'','',true,'ShareAchievements',true);
insert into uicontrols values (230, CURRENT_DATE,'SYSTEM','Y','More','Button',CURRENT_DATE,'','',true,'ShareAchievements',true);
insert into uicontrols values (231, CURRENT_DATE,'SYSTEM','Y','Save','Button',CURRENT_DATE,'','',true,'ShareAchievements',true);
insert into uicontrols values (232, CURRENT_DATE,'SYSTEM','Y','Back','Button',CURRENT_DATE,'','',true,'ShareAchievements',true);


insert into lov values(1,CURRENT_TIME, 'SYSTEM','Y','Application Performance','AP','HelpDeskCategory');
insert into lov values(2,CURRENT_TIME, 'SYSTEM','Y','Technnical Support','TS','HelpDeskCategory');
insert into lov values(3,CURRENT_TIME, 'SYSTEM','Y','Payment Query','PY','HelpDeskCategory');
insert into lov values(4,CURRENT_TIME, 'SYSTEM','Y','Product Details','PR','HelpDeskCategory');
insert into lov values(5,CURRENT_TIME, 'SYSTEM','Y','"Others','OT','HelpDeskCategory');

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

INSERT INTO app_security(app_sec_id,app_name, app_key) values (1,'ABNGYHUKLOIHGTY','ENEWSCHAMPAPP')
