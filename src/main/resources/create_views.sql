drop table champs_vw;
drop table published_articles_vw;
drop table published_articles_genre_vw;
drop table monthly_published_articles_genre_vw;

CREATE or replace VIEW champs_vw AS
    SELECT 
        a.student_id as student_id,r.avatar_name, r.photo_name,a.name AS student_name, a.surname AS surname, b.grade, c.name as school_name,
d.name_id as city_name, e.quiz_correct as monthly_score, e.trendyear_month as trendyear_month, f.reading_level,
RANK() OVER (PARTITION BY f.reading_level,e.trendyear_month ORDER BY e.quiz_correct desc) AS student_rank
    FROM
	trends_monthly_total e
	left outer join student_preferences f on f.student_id=e.student_id
	left outer join student_registration r on r.student_id=e.student_id
	left outer join student_details a on a.student_id=e.student_id
	left outer join student_school b on b.student_id=e.student_id
	left outer join school c on b.school=c.school_id
	left outer join city d on d.city_id=c.city_id;
	
CREATE OR REPLACE VIEW published_articles_vw AS
SELECT
b.publication_date,
b.reading_level, 
COUNT(distinct b.news_article_id) articles_published,
COUNT(aa.news_article_quiz_id) AS quiz_published
FROM news_article b left outer join news_article_quiz aa on aa.news_article_id= b.news_article_id 
WHERE b.status='Published' AND b.record_in_use='Y' GROUP BY publication_date,reading_level;

CREATE OR REPLACE VIEW published_articles_genre_vw AS
SELECT
b.publication_date,
b.reading_level,
g.genre_id,  
COUNT(distinct b.news_article_id) articles_published,
COUNT(aa.news_article_quiz_id) AS quiz_published
FROM  news_article_group g,news_article b
left outer join news_article_quiz aa on aa.news_article_id= b.news_article_id 
WHERE b.status='Published' AND b.record_in_use='Y' AND g.news_article_group_id=b.news_article_group_id 
GROUP BY publication_date,reading_level,genre_id;

CREATE OR REPLACE VIEW monthly_published_articles_genre_vw AS 
SELECT g.genre_id,
b.reading_level,
concat(year(publication_date),lpad(month(publication_date),2,'0')) as month_year,
COUNT(distinct b.news_article_id) AS articles_published,
COUNT(aa.news_article_quiz_id) AS quiz_published
FROM  news_article_group g,news_article b
left outer join news_article_quiz aa on aa.news_article_id= b.news_article_id
WHERE b.status='Published' AND b.record_in_use='Y' 
AND g.news_article_group_id=b.news_article_group_id
GROUP BY genre_id,reading_level,month_year;