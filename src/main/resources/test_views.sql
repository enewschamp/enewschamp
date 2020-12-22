select 
articles_published,
((select article_read from trends_daily aa where aa.quiz_date = a.publication_date and aa.student_id=1)) as articles_read,
a.quiz_published,
((select quiz_attempted from trends_daily aa where aa.quiz_date = a.publication_date and aa.student_id=1)) as quiz_attempted,
((select quiz_correct from trends_daily aa where aa.quiz_date = a.publication_date and aa.student_id=1)) as quizcorrect,
a.publication_date
from published_acrticles_vw a where a.publication_date>='2019-01-01' and a.publication_date<='2019-12-31'
group by a.publication_date,articles_read,quiz_published,quiz_attempted,quizcorrect;

------------
SELECT 
 articles_published,
((SELECT article_read FROM trends_daily aa WHERE aa.quiz_date = a.publication_date AND aa.student_id=1)) AS articles_read,
quiz_published,
((SELECT quiz_attempted FROM trends_daily aa WHERE aa.quiz_date = a.publication_date AND aa.student_id=1)) AS quiz_attempted,
((SELECT quiz_correct FROM trends_daily aa WHERE aa.quiz_date = a.publication_date AND aa.student_id=1)) AS quizcorrect,
a.genre_id
FROM published_acrticles_genre_vw a WHERE a.publication_date>='2019-01-01' AND a.publication_date<='2019-12-31';


-------
select 
sum(a.articles_published) as articles_published,
sum((select (articles_read) from trends_monthly_total aa where year(a.publication_date)= SUBSTRING(aa.trendyear_month, 1,4) and month(a.publication_date)=SUBSTRING(aa.trendyear_month, 5,6) and aa.student_id=1)) as articles_read,
sum(a.quiz_published) as quiz_published,
sum((select (quiz_attempted) from trends_monthly_total aa where  year(a.publication_date)= SUBSTRING(aa.trendyear_month, 1,4) and month(a.publication_date)=SUBSTRING(aa.trendyear_month, 5,6) and aa.student_id=1)) as quiz_attempted,
sum((select (quiz_correct) from trends_monthly_total aa where  year(a.publication_date)= SUBSTRING(aa.trendyear_month, 1,4) and month(a.publication_date)=SUBSTRING(aa.trendyear_month, 5,6) and aa.student_id=1)) as quizcorrect,
month(a.publication_date) publication_date
from published_acrticles_vw a where a.publication_date>='2019-01-01' and a.publication_date<='2019-12-31'
group by (a.publication_date);

---------yearly genre
SELECT 
SUM(a.articles_published) AS articles_published,
SUM((SELECT (articles_read) FROM trends_monthly_total aa WHERE YEAR(a.publication_date)= SUBSTRING(aa.trendyear_month, 1,4) AND MONTH(a.publication_date)=SUBSTRING(aa.trendyear_month, 5,6) AND aa.student_id=1)) AS articles_read,
SUM(a.quiz_published) AS quiz_published,
SUM((SELECT (quiz_attempted) FROM trends_monthly_total aa WHERE  YEAR(a.publication_date)= SUBSTRING(aa.trendyear_month, 1,4) AND MONTH(a.publication_date)=SUBSTRING(aa.trendyear_month, 5,6) AND aa.student_id=1)) AS quiz_attempted,
SUM((SELECT (quiz_correct) FROM trends_monthly_total aa WHERE  YEAR(a.publication_date)= SUBSTRING(aa.trendyear_month, 1,4) AND MONTH(a.publication_date)=SUBSTRING(aa.trendyear_month, 5,6) AND aa.student_id=1)) AS quizcorrect,
a.genre_id
FROM published_acrticles_genre_vw a 
WHERE a.publication_date>='2020-01-01' AND a.publication_date<='2020-12-31'
GROUP BY genre_id,articles_published,quiz_published;