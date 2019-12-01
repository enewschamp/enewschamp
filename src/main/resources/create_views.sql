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
 
CREATE OR REPLACE VIEW daily_article_vw AS
SELECT 
COUNT(b.news_article_id) AS article_published,
COUNT((SELECT student_id FROM trends_daily a WHERE a.quiz_date = b.publish_date)) AS articlesRead,
b.publish_date AS publish_date
FROM news_article b ,
publication c,
publication_articles d
WHERE 
 d.news_article_id = b.news_article_id
AND d.publication_id=c.publication_id
AND c.status='Published' AND c.record_in_use='Y'
GROUP BY publish_date;


CREATE OR REPLACE VIEW daily_quiz_scores_vw AS
SELECT 
COUNT(a.news_article_id) AS quizpublished , 
COUNT((SELECT SUM(quiz_attempted) FROM trends_daily WHERE quiz_date = a.operation_date_time)) AS quiz_attempted,
COUNT((SELECT SUM(quiz_correct) FROM trends_daily WHERE quiz_date = a.operation_date_time)) AS quizcorrect,
a.operation_date_time AS publish_date
 FROM 
news_article_quiz a,
news_article b,
publication c,
publication_articles d
WHERE 
a.news_article_id = b.news_article_id
AND d.news_article_id = b.news_article_id
AND d.publication_id=c.publication_id
AND c.status='Published' AND c.record_in_use='Y'
GROUP BY a.operation_date_time;

CREATE OR REPLACE VIEW monthly_articles_genre_vw AS 
SELECT e.genre_id,
COUNT(b.news_article_id) AS article_published,
COUNT((SELECT student_id FROM trends_daily a WHERE a.quiz_date = b.publish_date)) AS articles_read,
b.publish_date AS publish_date
FROM  
news_article b,
publication c,
publication_articles d,
news_article_group e
WHERE 
 d.news_article_id = b.news_article_id
AND d.publication_id=c.publication_id
AND e.news_article_group_id=b.news_article_group_id
AND c.status='Published' AND c.record_in_use='Y'
GROUP BY e.genre_id,publish_date;


CREATE OR REPLACE VIEW monthly_quiz_genre_vw AS
SELECT 
e.genre_id,
COUNT(a.news_article_id) AS quizpublished , 
COUNT((SELECT SUM(quiz_attempted) FROM trends_daily WHERE quiz_date = a.operation_date_time)) AS quiz_attempted,
COUNT((SELECT SUM(quiz_correct) FROM trends_daily WHERE quiz_date = a.operation_date_time)) AS quizcorrect,
a.operation_date_time AS publish_date
 FROM 
news_article_quiz a,
news_article b,
publication c,
publication_articles d,
news_article_group e
WHERE 
a.news_article_id = b.news_article_id
AND d.news_article_id = b.news_article_id
AND d.publication_id=c.publication_id
AND e.news_article_group_id=b.news_article_group_id
AND c.status='Published' AND c.record_in_use='Y'
GROUP BY e.genre_id,a.operation_date_time;


CREATE OR REPLACE VIEW daily_article_yearly_vw AS
SELECT 
COUNT(b.news_article_id) AS article_published,
COUNT((SELECT student_id FROM trends_daily a WHERE a.quiz_date = b.publish_date)) AS articles_read,
b.publish_date AS publish_date
FROM news_article b ,
publication c,
publication_articles d
WHERE 
 d.news_article_id = b.news_article_id
AND d.publication_id=c.publication_id
AND c.status='Published' AND c.record_in_use='Y'
GROUP BY b.publish_date;


----------Scores---------
CREATE OR REPLACE VIEW published_acrticles_vw AS
SELECT 
COUNT(b.news_article_id) articles_published,
b.publish_date,
COUNT((SELECT DISTINCT news_article_id FROM news_article_quiz aa WHERE aa.news_article_id= b.news_article_id AND aa.operation_date_time=b.publish_date )) AS quiz_published
FROM  
news_article b,
publication c,
publication_articles d,
news_article_group e
WHERE 
 d.news_article_id = b.news_article_id
AND d.publication_id=c.publication_id
AND e.news_article_group_id=b.news_article_group_id
AND c.status='Published' AND c.record_in_use='Y'
GROUP BY b.publish_date;

----group by genre
CREATE OR REPLACE VIEW published_acrticles_genre_vw AS
SELECT 
COUNT(b.news_article_id) articles_published,
b.publish_date,
e.genre_id,
COUNT((SELECT DISTINCT news_article_id FROM news_article_quiz aa WHERE aa.news_article_id= b.news_article_id AND aa.operation_date_time=b.publish_date )) AS quiz_published
FROM  
news_article b,
publication c,
publication_articles d,
news_article_group e
WHERE 
 d.news_article_id = b.news_article_id
AND d.publication_id=c.publication_id
AND e.news_article_group_id=b.news_article_group_id
AND c.status='Published' AND c.record_in_use='Y'
GROUP BY b.publish_date,e.genre_id;