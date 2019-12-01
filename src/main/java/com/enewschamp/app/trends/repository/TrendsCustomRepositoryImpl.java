package com.enewschamp.app.trends.repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;

import com.enewschamp.app.trends.dto.DailyArticleDTO;
import com.enewschamp.app.trends.dto.DailyQuizScoresDTO;
import com.enewschamp.app.trends.dto.MonthlyArticleDTO;
import com.enewschamp.app.trends.dto.MonthlyArticlesGenreDTO;
import com.enewschamp.app.trends.dto.MonthlyQuizGenreDTO;
import com.enewschamp.app.trends.dto.MonthlyQuizScoresDTO;
import com.enewschamp.app.trends.dto.TrendsSearchData;
import com.enewschamp.app.trends.dto.YearlyArticlesGenreDTO;
import com.enewschamp.app.trends.dto.YearlyQuizGenreDTO;
import com.enewschamp.app.trends.entity.DailyArticle;
import com.enewschamp.app.trends.entity.DailyQuizScores;
import com.enewschamp.app.trends.entity.MonthlyArticlesGenre;
import com.enewschamp.app.trends.entity.MonthlyQuizGenre;

@Repository
public class TrendsCustomRepositoryImpl implements TrendsCustomRepository {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public List<DailyArticleDTO> findDailyNewsArticlesTrend(TrendsSearchData searchRequest) {

		Long studentId = searchRequest.getStudentId();
		String monthYear = searchRequest.getMonthYear();
		LocalDate startDate = null;
		LocalDate endDate = null;

		Query query = entityManager.createNativeQuery(
				"select  count(b.news_article_id) as article_published, ((select article_read from trends_daily a where a.quiz_date = b.publish_date and a.student_id= ?1)) as articles_read, b.publish_date as publish_date from news_article b , publication c, publication_articles d where  d.news_article_id = b.news_article_id and d.publication_id=c.publication_id and c.status='Published' and c.record_in_use='Y' and b.publish_date>=?2 and b.publish_date<= ?3 group by publish_date",
				DailyArticle.class);

		if (monthYear != null && !"".equals(monthYear)) {
			String year = monthYear.substring(0, 4);
			String month = monthYear.substring(monthYear.length() - 2, monthYear.length());
			String day1 = "01";
			// form date..
			startDate = LocalDate.of(Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(day1));
			endDate = LocalDate.of(startDate.getYear(), startDate.getMonthValue(), 1).plusMonths(1).minusDays(1);

		} else {
			startDate = LocalDate.now();
			startDate = LocalDate.of(startDate.getYear(), startDate.getMonthValue(), 1);
			endDate = LocalDate.of(startDate.getYear(), startDate.getMonthValue(), 1).plusMonths(1).minusDays(1);

		}

		query.setParameter(1, studentId);
		query.setParameter(2, startDate);
		query.setParameter(3, endDate);
		List<DailyArticleDTO> list = query.getResultList();
		System.out.println("Daily article: " + list);
		return list;
	}

	@Override
	public List<DailyQuizScoresDTO> findDailyQuizScoreTrend(TrendsSearchData searchRequest) {
		System.out.println("Year month : " + searchRequest.getMonthYear());
		Query query = entityManager.createNativeQuery(
				"select  count(a.news_article_id) as quizpublished ,  ((select sum(quiz_attempted) from trends_daily where quiz_date = a.operation_date_time and student_id=?1)) as quiz_attempted, ((select sum(quiz_correct) from trends_daily where quiz_date = a.operation_date_time and student_id=?2)) as quizcorrect, a.operation_date_time as publish_date from  news_article_quiz a, news_article b, publication c, publication_articles d where  a.news_article_id = b.news_article_id and d.news_article_id = b.news_article_id and d.publication_id=c.publication_id and c.status='Published' and c.record_in_use='Y' and b.publish_date>= ?3 and b.publish_date<= ?4 group by a.operation_date_time",
				DailyQuizScores.class);
		Long studentId = searchRequest.getStudentId();
		String monthYear = searchRequest.getMonthYear();
		LocalDate startDate = null;
		LocalDate endDate = null;
		if (monthYear != null && !"".equals(monthYear)) {

			String year = monthYear.substring(0, 4);
			System.out.println("year : " + year);

			String month = monthYear.substring(monthYear.length() - 2, monthYear.length());
			String day1 = "01";
			// form date..
			String dateStr = year + "-" + month + "-" + day1;
			startDate = LocalDate.of(Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(day1));
			endDate = LocalDate.of(startDate.getYear(), startDate.getMonthValue(), 1).plusMonths(1).minusDays(1);
		} else {
			startDate = LocalDate.now();
			startDate = LocalDate.of(startDate.getYear(), startDate.getMonthValue(), 1);
			endDate = LocalDate.of(startDate.getYear(), startDate.getMonthValue(), 1).plusMonths(1).minusDays(1);
		}
		query.setParameter(1, studentId);
		query.setParameter(2, studentId);
		query.setParameter(3, startDate);
		query.setParameter(4, endDate);

		List<DailyQuizScoresDTO> list = query.getResultList();

		return list;
	}

	@Override
	public List<MonthlyArticlesGenreDTO> findMonthlyArticlesByGenreTrend(TrendsSearchData searchRequest) {

		Query query = entityManager.createNativeQuery("select e.genre_id,\n"
				+ "count(b.news_article_id) as article_published,\n"
				+ "((select student_id from trends_daily a where a.quiz_date = b.publish_date and student_id=?1)) as articles_read,\n"
				+ "b.publish_date as publish_date\n" + "from  \n" + "news_article b,\n" + "publication c,\n"
				+ "publication_articles d,\n" + "news_article_group e\n" + "where \n"
				+ " d.news_article_id = b.news_article_id \n" + "and d.publication_id=c.publication_id \n"
				+ "and e.news_article_group_id=b.news_article_group_id \n"
				+ "and c.status='Published' and c.record_in_use='Y' \n"
				+ "and b.publish_date>= ?2 and b.publish_date<= ?3 \n" + "group by e.genre_id,publish_date",
				MonthlyArticlesGenre.class);

		Long studentId = searchRequest.getStudentId();

		String monthYear = searchRequest.getMonthYear();
		LocalDate startDate = null;
		LocalDate endDate = null;

		if (searchRequest.getMonthYear() != null && !"".equals(searchRequest.getMonthYear())) {
			String year = monthYear.substring(0, 4);
			System.out.println("year : " + year);

			String month = monthYear.substring(monthYear.length() - 2, monthYear.length());
			String day1 = "01";
			// form date..
			String dateStr = year + "-" + month + "-" + day1;
			startDate = LocalDate.of(Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(day1));
			endDate = LocalDate.of(startDate.getYear(), startDate.getMonthValue(), 1).plusMonths(1).minusDays(1);
		} else {
			startDate = LocalDate.now();
			startDate = LocalDate.of(startDate.getYear(), startDate.getMonthValue(), 1);

			// endDate = LocalDate.now();
			endDate = LocalDate.of(startDate.getYear(), startDate.getMonthValue(), 1).plusMonths(1).minusDays(1);
		}
		System.out.println("Year month : " + searchRequest.getMonthYear());
		query.setParameter(1, studentId);
		query.setParameter(2, startDate);
		query.setParameter(3, endDate);

		List<MonthlyArticlesGenreDTO> list = query.getResultList();

		return list;

	}

	@Override
	public List<MonthlyQuizGenreDTO> findMonthlyQuizScoreByGenre(TrendsSearchData searchRequest) {
		System.out.println("Year month : " + searchRequest.getMonthYear());

		Query query = entityManager.createNativeQuery("select \n" + "e.genre_id,\n"
				+ "count(a.news_article_id) as quizpublished , \n"
				+ "((select sum(quiz_attempted) from trends_daily where quiz_date = a.operation_date_time and student_id = ?1)) as quiz_attempted,\n"
				+ "((select sum(quiz_correct) from trends_daily where quiz_date = a.operation_date_time and student_id = ?2)) as quizcorrect,\n"
				+ "a.operation_date_time as publish_date\n" + " from \n" + "news_article_quiz a,\n"
				+ "news_article b, \n" + "publication c, \n" + "publication_articles d, \n" + "news_article_group e \n"
				+ "where \n" + "a.news_article_id = b.news_article_id \n"
				+ "and d.news_article_id = b.news_article_id \n" + "and d.publication_id=c.publication_id \n"
				+ "and e.news_article_group_id=b.news_article_group_id \n"
				+ "and c.status='Published' and c.record_in_use='Y' \n"
				+ "and b.publish_date>=?3 and b.publish_date <=?4 \n" + "group by e.genre_id,a.operation_date_time",
				MonthlyQuizGenre.class);

		Long studentId = searchRequest.getStudentId();

		String monthYear = searchRequest.getMonthYear();
		LocalDate startDate = null;
		LocalDate endDate = null;

		if (searchRequest.getMonthYear() != null && !"".equals(searchRequest.getMonthYear())) {
			String year = monthYear.substring(0, 4);
			String month = monthYear.substring(monthYear.length() - 2, monthYear.length());
			String day1 = "01";
			// form date..
			startDate = LocalDate.of(Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(day1));
			endDate = LocalDate.of(startDate.getYear(), startDate.getMonthValue(), 1).plusMonths(1).minusDays(1);
		} else {
			startDate = LocalDate.now();
			startDate = LocalDate.of(startDate.getYear(), startDate.getMonthValue(), 1);
			endDate = LocalDate.of(startDate.getYear(), startDate.getMonthValue(), 1).plusMonths(1).minusDays(1);
		}
		query.setParameter(1, studentId);
		query.setParameter(2, studentId);
		query.setParameter(3, startDate);
		query.setParameter(4, endDate);

		List<MonthlyQuizGenreDTO> list = query.getResultList();
		return list;
	}

	@Override
	public List<MonthlyArticleDTO> findMonthlyNewsArticlesTrend(TrendsSearchData searchRequest) {

		Query query = entityManager.createNativeQuery(
				"select sum(article_published) as article_published, sum(article_read) as articles_read,publish_date as publish_date\n"
						+ "from(\n" + "select \n" + "count(b.news_article_id) as article_published,\n"
						+ "((select distinct count(articles_read) from trends_monthly_total where year(b.publish_date)= SUBSTRING(trendyear_month, 1,4) and month(b.publish_date)=SUBSTRING(trendyear_month, 5,6) and student_id= ?1)) as article_read,\n"
						+ "month(b.publish_date) as publish_date\n" + "from news_article b ,\n" + "publication c,\n"
						+ "publication_articles d\n" + "where \n" + " d.news_article_id = b.news_article_id\n"
						+ "and d.publication_id=c.publication_id\n"
						+ "and c.status='Published' and c.record_in_use='Y'\n"
						+ "and b.publish_date >=?2 and b.publish_date<=?3\n"
						+ "group by month(publish_date),article_read) A\n" + "group by publish_date",
				DailyArticle.class);

		Long studentId = searchRequest.getStudentId();

		String monthYear = searchRequest.getMonthYear();
		LocalDate startDate = null;
		LocalDate endDate = null;
		if (searchRequest.getMonthYear() != null && !"".equals(searchRequest.getMonthYear())) {
			String year = monthYear.substring(0, 4);
			String month = "01";
			String day1 = "01";
			startDate = LocalDate.of(Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(day1));
			endDate = LocalDate.of(startDate.getYear(), startDate.getMonthValue(), 1).plusMonths(12).minusDays(1);
		} else {
			startDate = LocalDate.now();
			String year = "" + startDate.getYear();
			String month = "01";
			String date = "01";
			startDate = LocalDate.of(Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(date));
			endDate = LocalDate.of(startDate.getYear(), startDate.getMonthValue(), 1).plusMonths(1).minusDays(1);
		}
		query.setParameter(1, studentId);
		query.setParameter(2, startDate);
		query.setParameter(3, endDate);
		// Build query
		List<MonthlyArticleDTO> list = query.getResultList();
		return list;
	}

	@Override
	public List<MonthlyQuizScoresDTO> findMonthlyQuizScoreTrend(TrendsSearchData searchRequest) {
		System.out.println("Year month : " + searchRequest.getMonthYear());

		Query query = entityManager.createNativeQuery("select \n" + "count(a.news_article_id) as quizpublished , \n"
				+ "count((select (quiz_attempted) from trends_daily where quiz_date = a.operation_date_time and student_id= ?1)) as quiz_attempted,\n"
				+ "count((select (quiz_correct) from trends_daily where quiz_date = a.operation_date_time and student_id=?2)) as quizcorrect,\n"
				+ "month(a.operation_date_time) as publish_date\n" + " from \n" + "news_article_quiz a,\n"
				+ "news_article b,\n" + "publication c,\n" + "publication_articles d\n" + "where \n"
				+ "a.news_article_id = b.news_article_id\n" + "and d.news_article_id = b.news_article_id\n"
				+ "and d.publication_id=c.publication_id\n" + "and c.status='Published' and c.record_in_use='Y'\n"
				+ "and b.publish_date>= ?3 and b.publish_date<=?4\n" + "group by month(a.operation_date_time)",
				DailyQuizScores.class);
		Long studentId = searchRequest.getStudentId();

		String monthYear = searchRequest.getMonthYear();
		LocalDate startDate = null;
		LocalDate endDate = null;

		if (searchRequest.getMonthYear() != null && !"".equals(searchRequest.getMonthYear())) {

			String year = monthYear.substring(0, 4);

			// String month = yearMonth.substring(yearMonth.length()-2, yearMonth.length());
			String month = "01";

			String day1 = "01";
			// form date..
			startDate = LocalDate.of(Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(day1));
			endDate = LocalDate.of(startDate.getYear(), startDate.getMonthValue(), 1).plusMonths(12).minusDays(1);
		} else {

			startDate = LocalDate.now();
			// startDate = LocalDate.of(startDate.getYear(), startDate.getMonthValue(),1);
			String year = "" + startDate.getYear();
			String month = "01";
			String date = "01";
			startDate = LocalDate.of(Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(date));
			endDate = LocalDate.of(startDate.getYear(), startDate.getMonthValue(), 1).plusMonths(1).minusDays(1);
		}
		query.setParameter(1, studentId);
		query.setParameter(2, studentId);
		query.setParameter(3, startDate);
		query.setParameter(4, endDate);

		List<MonthlyQuizScoresDTO> list = query.getResultList();
		return list;
	}

	@Override
	public List<YearlyArticlesGenreDTO> findYearlyArticlesByGenreTrend(TrendsSearchData searchRequest) {

		Query query = entityManager.createNativeQuery(
				"select genre_id, sum(article_published) as article_published, sum(article_read) as articles_read,publish_date as publish_date\n"
						+ "from(\n" + "select e.genre_id as genreid,\n"
						+ "count(b.news_article_id) as article_published,\n"
						+ "((select distinct count(articles_read) from trends_monthly_total where year(b.publish_date)= SUBSTRING(trendyear_month, 1,4) and month(b.publish_date)=SUBSTRING(trendyear_month, 5,6) and student_id= ?1)) as article_read,\n"
						+ "month(b.publish_date) as publish_date\n" + "from  \n" + "news_article b,\n"
						+ "publication c,\n" + "publication_articles d,\n" + "news_article_group e\n" + "where \n"
						+ " d.news_article_id = b.news_article_id\n" + "and d.publication_id=c.publication_id\n"
						+ "and e.news_article_group_id=b.news_article_group_id\n"
						+ "and c.status='Published' and c.record_in_use='Y'\n"
						+ "and b.publish_date >=?2 and b.publish_date<=?3\n"
						+ "group by e.genre_id,month(b.publish_date),article_read) A\n"
						+ "group by genre_id,publish_date",
				MonthlyArticlesGenre.class);

		Long studentId = searchRequest.getStudentId();

		String monthYear = searchRequest.getMonthYear();
		LocalDate startDate = null;
		LocalDate endDate = null;
		if (searchRequest.getMonthYear() != null && !"".equals(searchRequest.getMonthYear())) {
			String year = monthYear.substring(0, 4);
			String month = "01";

			String day1 = "01";
			startDate = LocalDate.of(Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(day1));
			endDate = LocalDate.of(startDate.getYear(), startDate.getMonthValue(), 1).plusMonths(12).minusDays(1);
		} else {
			startDate = LocalDate.now();
			startDate = LocalDate.of(startDate.getYear(), startDate.getMonthValue(), 1);
			String year = "" + startDate.getYear();
			String month = "01";
			String date = "01";
			startDate = LocalDate.of(Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(date));
			endDate = LocalDate.of(startDate.getYear(), startDate.getMonthValue(), 1).plusMonths(12).minusDays(1);
		}
		query.setParameter(1, studentId);
		query.setParameter(2, startDate);
		query.setParameter(3, endDate);
		List<YearlyArticlesGenreDTO> list = query.getResultList();
		return list;

	}

	@Override
	public List<YearlyQuizGenreDTO> findYearlyQuizScoreByGenre(TrendsSearchData searchRequest) {
		System.out.println("Year month : " + searchRequest.getMonthYear());
		Query query = entityManager.createNativeQuery(
				"select genre_id as genreid, sum(quizpublished) as quizpublished,sum(quiz_attempted) as quiz_attempted,sum(quizcorrect) as quizcorrect,publish_date as publish_date \n"
						+ "from(\n" + "select \n" + "e.genre_id as genreid,\n"
						+ "count(distinct a.news_article_id) as quizpublished , \n"
						+ "((select (quiz_attempted) from trends_monthly_total where year(b.publish_date)= SUBSTRING(trendyear_month, 1,4) and month(b.publish_date)=SUBSTRING(trendyear_month, 5,6) and student_id= ?1)) as quiz_attempted,\n"
						+ "((select (quiz_correct) from trends_monthly_total where year(b.publish_date)= SUBSTRING(trendyear_month, 1,4) and month(b.publish_date)=SUBSTRING(trendyear_month, 5,6) and student_id= ?2)) as quizcorrect,\n"
						+ "year(b.publish_date) as publish_date\n" + " from \n" + "news_article_quiz a,\n"
						+ "news_article b,\n" + "publication c,\n" + "publication_articles d,\n"
						+ "news_article_group e\n" + "where \n" + "a.news_article_id = b.news_article_id\n"
						+ "and d.news_article_id = b.news_article_id\n" + "and d.publication_id=c.publication_id\n"
						+ "and e.news_article_group_id=b.news_article_group_id\n"
						+ "and c.status='Published' and c.record_in_use='Y'\n"
						+ "and b.publish_date >=?3 and b.publish_date<=?4\n"
						+ "group by e.genre_id,year(b.publish_date),quiz_attempted,quizcorrect) A\n"
						+ "group by genre_id,publish_date",
				MonthlyQuizGenre.class);

		Long studentId = searchRequest.getStudentId();

		String monthYear = searchRequest.getMonthYear();
		LocalDate startDate = null;
		LocalDate endDate = null;
		if (searchRequest.getMonthYear() != null && !"".equals(searchRequest.getMonthYear())) {
			String year = monthYear.substring(0, 4);
			System.out.println("year : " + year);
			String month = "01";

			String day1 = "01";
			startDate = LocalDate.of(Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(day1));
			endDate = LocalDate.of(startDate.getYear(), startDate.getMonthValue(), 1).plusMonths(12).minusDays(1);
		} else {
			startDate = LocalDate.now();
			String year = "" + startDate.getYear();
			String month = "01";
			String date = "01";
			startDate = LocalDate.of(Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(date));
			endDate = LocalDate.of(startDate.getYear(), startDate.getMonthValue(), 1).plusMonths(12).minusDays(1);
		}
		query.setParameter(1, studentId);
		query.setParameter(2, studentId);
		query.setParameter(3, startDate);
		query.setParameter(4, endDate);
		List<YearlyQuizGenreDTO> list = query.getResultList();
		return list;
	}

}
