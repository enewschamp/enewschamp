DROP TABLE scores_dailydto;
DROP TABLE scores_monthlydto;
DROP TABLE scores_monthly_genredto;
DROP TABLE yearly_scores_genredto;
DROP TABLE news_article_viewdto;
DROP TABLE student_notificationdto;
DROP TABLE recognition_data;

CREATE OR REPLACE VIEW champs_vw AS
    SELECT 
        a.student_id AS student_id,f.feature_profile_in_champs,f.champ_city,f.champ_school,f.champ_profile_pic,r.image_approval_required,r.avatar_name,r.photo_name,a.name AS NAME,a.surname AS surname,a.approval_required AS student_details_approval_required,b.grade,b.approval_required AS school_details_approval_required,ifnull(c.name,b.school) AS school,
ifnull(ct.description,b.city) AS city,ifnull(st.description,b.state) AS state,ifnull(co.description,b.country) AS country, e.quiz_correct AS score, e.score_year_month AS 'year_month', FORMAT(f.reading_level,0) AS reading_level,
DENSE_RANK() OVER (PARTITION BY FORMAT(f.reading_level,0),e.score_year_month ORDER BY e.quiz_correct DESC) AS `rank`
    FROM
	student_details a,scores_monthly_total e
	LEFT OUTER JOIN student_preferences f ON f.student_id=e.student_id
	LEFT OUTER JOIN student_registration r ON r.student_id=e.student_id
	LEFT OUTER JOIN student_school b ON b.student_id=e.student_id
	LEFT OUTER JOIN school c ON b.school=c.school_id
     LEFT OUTER JOIN city ct ON ct.name_id=b.city
    LEFT OUTER JOIN state st ON st.name_id=b.state
    LEFT OUTER JOIN country co ON co.name_id=b.country
	WHERE a.student_id=e.student_id;
	
CREATE OR REPLACE VIEW daily_published_articles_vw AS
SELECT a.publication_date AS publication_date,a.reading_level AS reading_level,COUNT(DISTINCT a.news_article_id) AS articles_published,COUNT(q.news_article_quiz_id) AS quiz_published
FROM news_article a,news_article_quiz q  
WHERE a.status='Published' AND a.record_in_use='Y' AND a.news_article_id=q.news_article_id GROUP BY publication_date,reading_level;

CREATE OR REPLACE VIEW monthly_published_articles_total_vw AS
SELECT CONCAT(YEAR(a.publication_date),LPAD(MONTH(a.publication_date),2,'0')) AS 'year_month',a.reading_level AS reading_level ,COUNT(DISTINCT a.news_article_id) AS articles_published,COUNT(q.news_article_quiz_id) AS quiz_published
FROM  news_article_group g,news_article a,news_article_quiz q 
WHERE a.status='Published' AND a.record_in_use='Y' AND g.news_article_group_id=a.news_article_group_id AND a.news_article_id=q.news_article_id
GROUP BY 'year_month',reading_level;

CREATE OR REPLACE VIEW monthly_published_articles_genre_vw AS 
SELECT CONCAT(YEAR(a.publication_date),LPAD(MONTH(a.publication_date),2,'0')) AS 'year_month',a.reading_level AS reading_level,g.genre_id AS genre_id,COUNT(DISTINCT a.news_article_id) AS articles_published,COUNT(q.news_article_quiz_id) AS quiz_published
FROM  news_article_group g,news_article a,news_article_quiz q 
WHERE a.status='Published' AND a.record_in_use='Y' AND g.news_article_group_id=a.news_article_group_id AND a.news_article_id=q.news_article_id
GROUP BY 'year_month',reading_level,genre_id;
