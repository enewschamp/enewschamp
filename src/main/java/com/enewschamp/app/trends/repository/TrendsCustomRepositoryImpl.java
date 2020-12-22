package com.enewschamp.app.trends.repository;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

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
import com.enewschamp.app.trends.entity.MonthlyArticles;
import com.enewschamp.app.trends.entity.MonthlyArticlesGenre;
import com.enewschamp.app.trends.entity.MonthlyQuizGenre;
import com.enewschamp.app.trends.entity.MonthlyQuizScores;

@Repository
public class TrendsCustomRepositoryImpl implements TrendsCustomRepository {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public List<DailyArticleDTO> findDailyNewsArticlesTrend(TrendsSearchData searchRequest) {
		Long studentId = searchRequest.getStudentId();
		String monthYear = searchRequest.getMonthYear();
		int readingLevel = searchRequest.getReadingLevel();
		LocalDate startDate = null;
		LocalDate endDate = null;
		Query query = entityManager.createNativeQuery("select \n" + "a.publication_date,\n" + "a.articles_published,\n"
				+ "ifnull(td.articles_read,0) AS articles_read \n"
				+ "from published_articles_vw a left outer join trends_daily td on td.student_id=?1 and td.quiz_publication_date=a.publication_date\n"
				+ "where a.reading_level=?2 and a.publication_date>= ?3 and a.publication_date<= ?4 \n"
				+ "group by publication_date", DailyArticle.class);

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
		query.setParameter(2, readingLevel);
		query.setParameter(3, startDate);
		query.setParameter(4, endDate);
		List<DailyArticleDTO> list = query.getResultList();
		return list;
	}

	@Override
	public List<DailyQuizScoresDTO> findDailyQuizScoreTrend(TrendsSearchData searchRequest) {
		Long studentId = searchRequest.getStudentId();
		String monthYear = searchRequest.getMonthYear();
		int readingLevel = searchRequest.getReadingLevel();
		LocalDate startDate = null;
		LocalDate endDate = null;
		Query query = entityManager.createNativeQuery("select\n" + "a.publication_date, \n"
				+ "ifnull(a.quiz_published,0) AS quiz_published,\n" + "ifnull(td.quiz_attempted,0) AS quiz_attempted,\n"
				+ "ifnull(td.quiz_correct,0) AS quiz_correct\n" + "from published_articles_vw a \n"
				+ "left outer join trends_daily td on td.student_id=?1 and td.quiz_publication_date=a.publication_date\n"
				+ "where a.reading_level=?2 and a.publication_date>= ?3 and a.publication_date<= ?4 \n"
				+ "group by publication_date", DailyQuizScores.class);
		if (monthYear != null && !"".equals(monthYear)) {
			String year = monthYear.substring(0, 4);
			String month = monthYear.substring(monthYear.length() - 2, monthYear.length());
			String day1 = "01";
			startDate = LocalDate.of(Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(day1));
			endDate = LocalDate.of(startDate.getYear(), startDate.getMonthValue(), 1).plusMonths(1).minusDays(1);
		} else {
			startDate = LocalDate.now();
			startDate = LocalDate.of(startDate.getYear(), startDate.getMonthValue(), 1);
			endDate = LocalDate.of(startDate.getYear(), startDate.getMonthValue(), 1).plusMonths(1).minusDays(1);
		}
		query.setParameter(1, studentId);
		query.setParameter(2, readingLevel);
		query.setParameter(3, startDate);
		query.setParameter(4, endDate);
		List<DailyQuizScoresDTO> list = query.getResultList();
		return list;
	}

	@Override
	public List<MonthlyArticlesGenreDTO> findMonthlyArticlesByGenreTrend(TrendsSearchData searchRequest) {
		Long studentId = searchRequest.getStudentId();
		String monthYear = searchRequest.getMonthYear();
		int readingLevel = searchRequest.getReadingLevel();
		LocalDate startDate = null;
		LocalDate endDate = null;
		Query query = entityManager.createNativeQuery("select\n" + "a.genre_id, \n"
				+ "ifnull(sum(a.articles_published),0) AS articles_published,\n"
				+ "ifnull(sum(td.articles_read),0) AS articles_read\n" + "from published_articles_genre_vw a \n"
				+ "left outer join trends_daily td on td.student_id=?1 and td.quiz_publication_date=a.publication_date\n"
				+ "where a.reading_level=?2 and a.publication_date>= ?3 and a.publication_date<= ?4 \n"
				+ "group by genre_id", MonthlyArticlesGenre.class);
		if (searchRequest.getMonthYear() != null && !"".equals(searchRequest.getMonthYear())) {
			String year = monthYear.substring(0, 4);
			String month = monthYear.substring(monthYear.length() - 2, monthYear.length());
			String day1 = "01";
			startDate = LocalDate.of(Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(day1));
			endDate = LocalDate.of(startDate.getYear(), startDate.getMonthValue(), 1).plusMonths(1).minusDays(1);
		} else {
			startDate = LocalDate.now();
			startDate = LocalDate.of(startDate.getYear(), startDate.getMonthValue(), 1);
			endDate = LocalDate.of(startDate.getYear(), startDate.getMonthValue(), 1).plusMonths(1).minusDays(1);
		}
		query.setParameter(1, studentId);
		query.setParameter(2, readingLevel);
		query.setParameter(3, startDate);
		query.setParameter(4, endDate);
		List<MonthlyArticlesGenreDTO> list = query.getResultList();
		return list;
	}

	@Override
	public List<MonthlyQuizGenreDTO> findMonthlyQuizScoreByGenre(TrendsSearchData searchRequest) {
		Long studentId = searchRequest.getStudentId();
		String monthYear = searchRequest.getMonthYear();
		int readingLevel = searchRequest.getReadingLevel();
		LocalDate startDate = null;
		LocalDate endDate = null;
		Query query = entityManager.createNativeQuery("select\n" + "a.genre_id, \n"
				+ "ifnull(sum(a.quiz_published),0) AS quiz_published,\n"
				+ "ifnull(sum(td.quiz_attempted),0) AS quiz_attempted,\n"
				+ "ifnull(sum(td.quiz_correct),0) AS quiz_correct\n" + "from published_articles_genre_vw a \n"
				+ "left outer join trends_daily td on td.student_id=?1 and td.quiz_publication_date=a.publication_date\n"
				+ "where a.reading_level=?2 and a.publication_date>= ?3 and a.publication_date<= ?4 \n"
				+ "group by genre_id", MonthlyQuizGenre.class);
		if (searchRequest.getMonthYear() != null && !"".equals(searchRequest.getMonthYear())) {
			String year = monthYear.substring(0, 4);
			String month = monthYear.substring(monthYear.length() - 2, monthYear.length());
			String day1 = "01";
			startDate = LocalDate.of(Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(day1));
			endDate = LocalDate.of(startDate.getYear(), startDate.getMonthValue(), 1).plusMonths(1).minusDays(1);
		} else {
			startDate = LocalDate.now();
			startDate = LocalDate.of(startDate.getYear(), startDate.getMonthValue(), 1);
			endDate = LocalDate.of(startDate.getYear(), startDate.getMonthValue(), 1).plusMonths(1).minusDays(1);
		}
		query.setParameter(1, studentId);
		query.setParameter(2, readingLevel);
		query.setParameter(3, startDate);
		query.setParameter(4, endDate);
		List<MonthlyQuizGenreDTO> list = query.getResultList();
		return list;
	}

	@Override
	public List<MonthlyArticleDTO> findMonthlyNewsArticlesTrend(TrendsSearchData searchRequest) {
		Long studentId = searchRequest.getStudentId();
		String monthYear = searchRequest.getMonthYear();
		int readingLevel = searchRequest.getReadingLevel();
		LocalDate startDate = null;
		LocalDate endDate = null;
		Query query = entityManager.createNativeQuery("select \n" + "a.month_year as  month,\n"
				+ "ifnull(sum(a.articles_published),0) as articles_published,\n"
				+ "ifnull(sum(td.articles_read),0) as articles_read\n" + "from monthly_published_articles_genre_vw a \n"
				+ "left outer join trends_monthly_by_genre td on td.student_id=?1 and td.trendyear_month=a.month_year\n"
				+ "where a.reading_level=?2 and a.month_year>= ?3 and a.month_year<= ?4 \n" + "group by month",
				MonthlyArticles.class);
		endDate = LocalDate.of(Integer.parseInt(monthYear.substring(0, 4)), Integer.parseInt(monthYear.substring(4, 6)),
				LocalDate.now().getDayOfMonth());
		startDate = LocalDate.of(endDate.getYear(), endDate.getMonthValue(), 1).minusMonths(11);
		String startYearMonth = startDate.getYear() + ""
				+ (startDate.getMonthValue() > 9 ? startDate.getMonthValue() : "0" + startDate.getMonthValue());
		query.setParameter(1, studentId);
		query.setParameter(2, readingLevel);
		query.setParameter(3, startYearMonth);
		query.setParameter(4, monthYear);
		List<MonthlyArticleDTO> list = query.getResultList();
		return list;
	}

	@Override
	public List<MonthlyQuizScoresDTO> findMonthlyQuizScoreTrend(TrendsSearchData searchRequest) {
		Long studentId = searchRequest.getStudentId();
		String monthYear = searchRequest.getMonthYear();
		int readingLevel = searchRequest.getReadingLevel();
		LocalDate startDate = null;
		LocalDate endDate = null;
		Query query = entityManager.createNativeQuery("select a.month_year as month,\n"
				+ "ifnull(sum(a.quiz_published),0) as quiz_published,\n"
				+ "ifnull(sum(td.quiz_attempted),0) as quiz_attempted,\n"
				+ "ifnull(sum(td.quiz_correct),0) as quiz_correct\n" + "from monthly_published_articles_genre_vw a \n"
				+ "left outer join trends_monthly_by_genre td on td.student_id=?1 and td.trendyear_month=a.month_year\n"
				+ "where a.reading_level=?2 and a.month_year>= ?3 and a.month_year<= ?4 \n" + "group by month",
				MonthlyQuizScores.class);
		endDate = LocalDate.of(Integer.parseInt(monthYear.substring(0, 4)), Integer.parseInt(monthYear.substring(4, 6)),
				LocalDate.now().getDayOfMonth());
		startDate = LocalDate.of(endDate.getYear(), endDate.getMonthValue(), 1).minusMonths(11);
		String startYearMonth = startDate.getYear() + ""
				+ (startDate.getMonthValue() > 9 ? startDate.getMonthValue() : "0" + startDate.getMonthValue());
		query.setParameter(1, studentId);
		query.setParameter(2, readingLevel);
		query.setParameter(3, startYearMonth);
		query.setParameter(4, monthYear);
		List<MonthlyQuizScoresDTO> list = query.getResultList();
		return list;
	}

	@Override
	public List<YearlyArticlesGenreDTO> findYearlyArticlesByGenreTrend(TrendsSearchData searchRequest) {
		Long studentId = searchRequest.getStudentId();
		String monthYear = searchRequest.getMonthYear();
		int readingLevel = searchRequest.getReadingLevel();
		LocalDate startDate = null;
		LocalDate endDate = null;
		Query query = entityManager.createNativeQuery("select \n" + "a.genre_id,\n"
				+ "ifnull(sum(a.articles_published),0) as articles_published,\n"
				+ "ifnull(sum(td.articles_read),0) as articles_read\n" + "from monthly_published_articles_genre_vw a \n"
				+ "left outer join trends_monthly_by_genre td on td.student_id=?1 and td.trendyear_month=a.month_year\n"
				+ "where a.reading_level=?2 and a.month_year>= ?3 and a.month_year<= ?4 \n" + "group by genre_id",
				MonthlyArticlesGenre.class);
		endDate = LocalDate.of(Integer.parseInt(monthYear.substring(0, 4)), Integer.parseInt(monthYear.substring(4, 6)),
				LocalDate.now().getDayOfMonth());
		startDate = LocalDate.of(endDate.getYear(), endDate.getMonthValue(), 1).minusMonths(11);
		String startYearMonth = startDate.getYear() + ""
				+ (startDate.getMonthValue() > 9 ? startDate.getMonthValue() : "0" + startDate.getMonthValue());
		query.setParameter(1, studentId);
		query.setParameter(2, readingLevel);
		query.setParameter(3, startYearMonth);
		query.setParameter(4, monthYear);
		List<YearlyArticlesGenreDTO> list = query.getResultList();
		return list;
	}

	@Override
	public List<YearlyQuizGenreDTO> findYearlyQuizScoreByGenre(TrendsSearchData searchRequest) {
		Long studentId = searchRequest.getStudentId();
		String monthYear = searchRequest.getMonthYear();
		int readingLevel = searchRequest.getReadingLevel();
		LocalDate startDate = null;
		LocalDate endDate = null;
		Query query = entityManager.createNativeQuery("select \n" + "a.genre_id,\n"
				+ "ifnull(sum(a.quiz_published),0) as quiz_published,\n"
				+ "ifnull(sum(td.quiz_attempted),0) as quiz_attempted,\n"
				+ "ifnull(sum(td.quiz_correct),0) as quiz_correct\n" + "from monthly_published_articles_genre_vw a \n"
				+ "left outer join trends_monthly_by_genre td on td.student_id=?1 and td.trendyear_month=a.month_year\n"
				+ "where a.reading_level=?2 and a.month_year>= ?3 and a.month_year<= ?4 \n" + "group by genre_id",
				MonthlyQuizGenre.class);
		endDate = LocalDate.of(Integer.parseInt(monthYear.substring(0, 4)), Integer.parseInt(monthYear.substring(4, 6)),
				LocalDate.now().getDayOfMonth());
		startDate = LocalDate.of(endDate.getYear(), endDate.getMonthValue(), 1).minusMonths(11);
		String startYearMonth = startDate.getYear() + ""
				+ (startDate.getMonthValue() > 9 ? startDate.getMonthValue() : "0" + startDate.getMonthValue());
		query.setParameter(1, studentId);
		query.setParameter(2, readingLevel);
		query.setParameter(3, startYearMonth);
		query.setParameter(4, monthYear);
		List<YearlyQuizGenreDTO> list = query.getResultList();
		return list;
	}

}
