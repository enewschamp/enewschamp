package com.enewschamp.app.scores.repository;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.enewschamp.app.scores.dto.DailyScoreDTO;
import com.enewschamp.app.scores.dto.MonthlyScoreGenreDTO;
import com.enewschamp.app.scores.dto.MonthlyScoresDTO;
import com.enewschamp.app.scores.dto.YearlyScoresGenreDTO;
import com.enewschamp.app.scores.entity.DailyScore;
import com.enewschamp.app.scores.entity.MonthlyScoreGenre;
import com.enewschamp.app.scores.entity.MonthlyScores;
import com.enewschamp.app.scores.entity.YearlyScoresGenre;
import com.enewschamp.app.scores.page.data.ScoresSearchData;

@Repository
public class ScoresCustomRepositoryImpl implements ScoresCustomRepository {
	@PersistenceContext
	private EntityManager entityManager;

	public List<DailyScoreDTO> findDailyScores(ScoresSearchData searchRequest) {
		Long studentId = searchRequest.getStudentId();
		int readingLevel = searchRequest.getReadingLevel();
		String monthYear = searchRequest.getYearMonth();
		LocalDate startDate = null;
		LocalDate endDate = null;
		Query query = entityManager.createNativeQuery("select\n" + "a.publication_date, \n" + "a.articles_published,\n"
				+ "ifnull(td.articles_read,0) AS articles_read,\n" + "ifnull(a.quiz_published,0) AS quiz_published,\n"
				+ "ifnull(td.quiz_attempted,0) AS quiz_attempted,\n" + "ifnull(td.quiz_correct,0) AS quiz_correct\n"
				+ "from published_articles_vw a \n"
				+ "left outer join trends_daily td on td.student_id=?1 and td.quiz_publication_date=a.publication_date \n"
				+ "where a.reading_level=?2 and  a.publication_date>= ?3 and a.publication_date<= ?4 \n"
				+ "group by publication_date", DailyScore.class);
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
		List<DailyScoreDTO> list = query.getResultList();
		return list;
	}

	@Override
	public List<MonthlyScoreGenreDTO> findMonthlyScoresByGenre(ScoresSearchData searchRequest) {
		Long studentId = searchRequest.getStudentId();
		int readingLevel = searchRequest.getReadingLevel();
		String monthYear = searchRequest.getYearMonth();
		LocalDate startDate = null;
		LocalDate endDate = null;
		Query query = entityManager.createNativeQuery("select\n" + "a.genre_id, \n"
				+ "ifnull(sum(a.articles_published),0) AS articles_published,\n"
				+ "ifnull(sum(td.articles_read),0) AS articles_read,\n"
				+ "ifnull(sum(a.quiz_published),0) AS quiz_published,\n"
				+ "ifnull(sum(td.quiz_attempted),0) AS quiz_attempted,\n"
				+ "ifnull(sum(td.quiz_correct),0) AS quiz_correct\n" + "from published_articles_genre_vw a \n"
				+ "left outer join trends_daily td on td.student_id=?1 and td.quiz_publication_date=a.publication_date\n"
				+ "where a.reading_level=?2 and  a.publication_date>= ?3 and a.publication_date<= ?4 \n"
				+ "group by genre_id", MonthlyScoreGenre.class);
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
		List<MonthlyScoreGenreDTO> list = query.getResultList();
		return list;
	}

	@Override
	public List<YearlyScoresGenreDTO> findYearlyScoresByGenre(ScoresSearchData searchRequest) {
		Long studentId = searchRequest.getStudentId();
		String monthYear = searchRequest.getYearMonth();
		int readingLevel = searchRequest.getReadingLevel();
		LocalDate startDate = null;
		LocalDate endDate = null;
		Query query = entityManager.createNativeQuery("select \n" + "a.genre_id,\n"
				+ "ifnull(sum(a.articles_published),0) as articles_published,\n"
				+ "ifnull(sum(td.articles_read),0) as articles_read,\n"
				+ "ifnull(sum(a.quiz_published),0) as quiz_published,\n"
				+ "ifnull(sum(td.quiz_attempted),0) as quiz_attempted,\n"
				+ "ifnull(sum(td.quiz_correct),0) as quiz_correct\n" + "from monthly_published_articles_genre_vw a \n"
				+ "left outer join trends_monthly_by_genre td on td.student_id=?1 and td.trendyear_month=a.month_year\n"
				+ "where a.reading_level=?2 and a.month_year>= ?3 and a.month_year<= ?4 \n" + "group by genre_id",
				YearlyScoresGenre.class);
		endDate = LocalDate.of(Integer.parseInt(monthYear.substring(0, 4)), Integer.parseInt(monthYear.substring(4, 6)),
				LocalDate.now().getDayOfMonth());
		startDate = LocalDate.of(endDate.getYear(), endDate.getMonthValue(), 1).minusMonths(11);
		String startYearMonth = startDate.getYear() + ""
				+ (startDate.getMonthValue() > 9 ? startDate.getMonthValue() : "0" + startDate.getMonthValue());
		query.setParameter(1, studentId);
		query.setParameter(2, readingLevel);
		query.setParameter(3, startYearMonth);
		query.setParameter(4, monthYear);
		List<YearlyScoresGenreDTO> list = query.getResultList();
		return list;
	}

	@Override
	public List<MonthlyScoresDTO> findYMonthlyScores(ScoresSearchData searchRequest) {
		Long studentId = searchRequest.getStudentId();
		String monthYear = searchRequest.getYearMonth();
		int readingLevel = searchRequest.getReadingLevel();
		LocalDate startDate = null;
		LocalDate endDate = null;
		Query query = entityManager.createNativeQuery("select \n" + "a.month_year as month,\n"
				+ "ifnull(sum(a.articles_published),0) as articles_published,\n"
				+ "ifnull(sum(td.articles_read),0) as articles_read,\n"
				+ "ifnull(sum(a.quiz_published),0) as quiz_published,\n"
				+ "ifnull(sum(td.quiz_attempted),0) as quiz_attempted,\n"
				+ "ifnull(sum(td.quiz_correct),0) as quiz_correct\n" + "from monthly_published_articles_genre_vw a \n"
				+ "left outer join trends_monthly_by_genre td on td.student_id=?1 and td.trendyear_month=a.month_year\n"
				+ "where a.reading_level=?2 and a.month_year>= ?3 and a.month_year<= ?4 \n" + "group by month",
				MonthlyScores.class);
		endDate = LocalDate.of(Integer.parseInt(monthYear.substring(0, 4)), Integer.parseInt(monthYear.substring(4, 6)),
				LocalDate.now().getDayOfMonth());
		startDate = LocalDate.of(endDate.getYear(), endDate.getMonthValue(), 1).minusMonths(11);
		String startYearMonth = startDate.getYear() + ""
				+ (startDate.getMonthValue() > 9 ? startDate.getMonthValue() : "0" + startDate.getMonthValue());
		query.setParameter(1, studentId);
		query.setParameter(2, readingLevel);
		query.setParameter(3, startYearMonth);
		query.setParameter(4, monthYear);
		List<MonthlyScoresDTO> list = query.getResultList();
		return list;
	}
}
