alter table edition add constraint edition_oper_id_fk foreign key (operator_id) references user (user_id);
alter table edition add constraint edition_lang_id_fk foreign key (language_id) references lov (lov_id);

alter table genre add constraint genre_oper_id_fk foreign key (operator_id) references user (user_id);
alter table hash_tag add constraint hash_tag_oper_id_fk foreign key (operator_id) references user (user_id);
alter table multi_language_text add constraint multi_lang_oper_id_fk foreign key (operator_id) references user (user_id);

alter table user add constraint user_oper_id_fk foreign key (operator_id) references user (user_id);
alter table user add constraint user_title_fk foreign key (title) references lov (lov_id);

alter table user_roles add constraint role_user_id_fk foreign key (user_id) references user (user_id);


alter table news_article_group add constraint art_grp_oper_id_fk foreign key (operator_id) references user (user_id);
alter table news_article_group add constraint art_grp_author_id_fk foreign key (author_id) references user (user_id);
alter table news_article_group add constraint art_grp_editor_id_fk foreign key (editor_id) references user (user_id);
alter table news_article_group add constraint art_grp_edition_id_fk foreign key (edition_id) references edition (edition_id);

alter table news_article add constraint article_group_id_fk foreign key (news_article_group_id) references news_article_group (news_article_group_id);
alter table news_article add constraint article_oper_id_fk foreign key (operator_id) references user (user_id);
alter table news_article add constraint article_author_id_fk foreign key (author_id) references user (user_id);
alter table news_article add constraint article_editor_id_fk foreign key (editor_id) references user (user_id);
alter table news_article add constraint article_publisher_id_fk foreign key (publisher_id) references user (user_id);

alter table news_article_quiz add constraint quiz_article_id_fk foreign key (news_article_id) references news_article (news_article_id);
alter table news_article_quiz add constraint quiz_oper_id_fk foreign key (operator_id) references user (user_id);


alter table publication add constraint pub_edition_id_fk foreign key (edition_id) references edition (edition_id);
alter table publication add constraint pub_editor_id_fk foreign key (editor_id) references user (user_id);
alter table publication add constraint pub_oper_id_fk foreign key (operator_id) references user (user_id);
alter table publication add constraint pub_publisher_id_fk foreign key (publisher_id) references user (user_id);

alter table publication_articles add constraint pub_art_publication_id_fk foreign key (publication_id) references publication (publication_id);
alter table publication_articles add constraint pub_art_article_id_fk foreign key (news_article_id) references news_article (news_article_id);
