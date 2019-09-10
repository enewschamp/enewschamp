drop table if exists art_grp_id_seq;
drop table if exists art_id_seq;
drop table if exists art_quiz_id_seq;
drop table if exists edition;
drop table if exists genre;
drop table if exists hash_tag;
drop table if exists lang_text_id_seq;
drop table if exists multi_language_text;
drop table if exists news_article;;
drop table if exists news_article_group;
drop table if exists news_article_quiz;
drop table if exists pub_grp_id_seq;
drop table if exists pub_id_seq;
drop table if exists publication;
drop table if exists publication_article_linkage;
drop table if exists publication_group;
drop table if exists user;
drop table if exists user_role;
create table art_grp_id_seq (next_val bigint);
insert into art_grp_id_seq values ( 1 );
create table art_id_seq (next_val bigint);
insert into art_id_seq values ( 1 );
create table art_quiz_id_seq (next_val bigint);
insert into art_quiz_id_seq values ( 1 );
create table edition (edition_id varchar(10) not null, operation_date_time datetime not null, operator_id varchar(10) not null, record_in_use varchar(1) not null, edition_name varchar(255) not null, language_id varchar(3) not null, primary key (edition_id));
create table genre (genre_id varchar(12) not null, operation_date_time datetime not null, operator_id varchar(10) not null, record_in_use varchar(1) not null, image_path varchar(200) not null, name_id bigint not null, primary key (genre_id));
create table hash_tag (hash_tag varchar(25) not null, operation_date_time datetime not null, operator_id varchar(10) not null, record_in_use varchar(1) not null, genre_id varchar(12) not null, language_id varchar(3) not null, primary key (hash_tag));
create table lang_text_id_seq (next_val bigint);
insert into lang_text_id_seq values ( 1 );
create table multi_language_text (multi_language_textid bigint not null, operation_date_time datetime not null, operator_id varchar(10) not null, record_in_use varchar(1) not null, entity_column varchar(50) not null, entity_name varchar(50) not null, locale varchar(5) not null, text varchar(200) not null, primary key (multi_language_textid));
create table news_article (news_article_id bigint not null, operation_date_time datetime not null, operator_id varchar(10) not null, record_in_use varchar(1) not null, author_id varchar(10), content longtext, current_action varchar(255), editor_id varchar(10), likehcount integer, likelcount integer, likeocount integer, likescount integer, likewcount integer, news_article_group_id bigint not null, previous_status varchar(255), publication_id bigint, publish_date date, publisher_id varchar(10), rating varchar(255), reading_level integer not null, status varchar(255) not null, primary key (news_article_id));
create table news_article_group (news_article_group_id bigint not null, operation_date_time datetime not null, operator_id varchar(10) not null, record_in_use varchar(1) not null, author_id varchar(10), comments longtext, credits varchar(200) not null, edition_id varchar(10) not null, editor_id varchar(10), genre_id varchar(12) not null, hash_tags longtext, headline varchar(100) not null, image_path_desktop varchar(200), image_path_mobile varchar(200), image_path_tab varchar(200), intended_pub_day varchar(3), intended_pub_month integer, publication_date date, reading_level1 bit not null, reading_level2 bit not null, reading_level3 bit not null, reading_level4 bit not null, source_text longtext, status varchar(25) not null, target_completion_date date, url varchar(200), primary key (news_article_group_id));
create table news_article_quiz (news_article_quizid bigint not null, operation_date_time datetime not null, operator_id varchar(10) not null, record_in_use varchar(1) not null, correct_opt integer not null, news_article_id bigint not null, opt1 varchar(99) not null, opt2 varchar(99) not null, opt3 varchar(99), opt4 varchar(99), question varchar(99), article_id bigint, primary key (news_article_quizid));
create table pub_grp_id_seq (next_val bigint);
insert into pub_grp_id_seq values ( 1 );
create table pub_id_seq (next_val bigint);
insert into pub_id_seq values ( 1 );
create table publication (publicationid bigint not null, operation_date_time datetime not null, operator_id varchar(10) not null, record_in_use varchar(1) not null, comments longtext, current_action varchar(255), editionid varchar(6) not null, editor_id varchar(10) not null, previous_status varchar(255), publication_groupid bigint not null, publish_date date not null, publisher_id varchar(10) not null, reading_level integer not null, status varchar(255), primary key (publicationid));
create table publication_article_linkage (linkage_id bigint not null, operation_date_time datetime not null, operator_id varchar(10) not null, record_in_use varchar(1) not null, news_articleid bigint not null, publicationid bigint not null, sequence integer not null, publication_id bigint, primary key (linkage_id));
create table publication_group (publication_groupid bigint not null, operation_date_time datetime not null, operator_id varchar(10) not null, record_in_use varchar(1) not null, editionid varchar(6) not null, publication_date date not null, primary key (publication_groupid));
create table user (user_id varchar(8) not null, operation_date_time datetime not null, operator_id varchar(10) not null, record_in_use varchar(1) not null, comments varchar(999), contract_end_date date not null, contract_start_date date not null, email1 varchar(99) not null, email2 varchar(99), gender integer not null, land_line1 bigint, land_line2 bigint, mobile1 bigint not null, mobile2 bigint, name varchar(50) not null, other_names varchar(100), surname varchar(50) not null, title bigint not null, primary key (userid));
create table user_role (day_of_the_week varchar(255) not null, roleid varchar(12) not null, userid varchar(10) not null, operation_date_time datetime not null, operator_id varchar(10) not null, record_in_use varchar(1) not null, contribution integer not null, primary key (day_of_the_week, roleid, userid));
alter table news_article add constraint news_article_uk1 unique (news_article_group_id, reading_level);
alter table publication_group add constraint publication_group_uk1 unique (publication_date);
alter table news_article_quiz add constraint news_article_quiz_fk1 foreign key (article_id) references news_article (news_article_id);
alter table publication_article_linkage add constraint publication_article_linkage_fk1 foreign key (publication_id) references publication (publicationid);
