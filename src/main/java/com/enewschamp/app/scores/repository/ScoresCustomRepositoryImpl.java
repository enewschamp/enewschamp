package com.enewschamp.app.scores.repository;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.enewschamp.app.scores.dto.StudentScoresYearlyGenreDTO;
import com.enewschamp.app.scores.page.data.ScoresSearchData;
import com.enewschamp.app.student.scores.dto.StudentScoresDailyDTO;
import com.enewschamp.app.student.scores.dto.StudentScoresMonthlyGenreDTO;
import com.enewschamp.app.student.scores.dto.StudentScoresMonthlyTotalDTO;

@Repository
public class ScoresCustomRepositoryImpl implements ScoresCustomRepository {
	@PersistenceContext
	private EntityManager entityManager;

	public List<StudentScoresDailyDTO> findScoresDaily(ScoresSearchData searchRequest) {
		Long studentId = searchRequest.getStudentId();
		int readingLevel = searchRequest.getReadingLevel();
		String yearMonth = searchRequest.getYearMonth();
		LocalDate startDate = null;
		LocalDate endDate = null;
		Query query = entityManager.createNativeQuery("select\n" + "a.publication_date, \n" + "a.articles_published,\n"
				+ "ifnull(td.articles_read,0) AS articles_read,\n" + "ifnull(a.quiz_published,0) AS quiz_published,\n"
				+ "ifnull(td.quiz_attempted,0) AS quiz_attempted,\n" + "ifnull(td.quiz_correct,0) AS quiz_correct\n"
				+ "from daily_published_articles_vw a \n"
				+ "left outer join scores_daily td on (td.student_id=?1 and td.publication_date=a.publication_date)\n"
				+ "where a.reading_level=?2 and  a.publication_date>= ?3 and a.publication_date<= ?4 \n"
				+ "group by publication_date", StudentScoresDailyDTO.class);
		if (yearMonth != null && !"".equals(yearMonth)) {
			String year = yearMonth.substring(0, 4);
			String month = yearMonth.substring(yearMonth.length() - 2, yearMonth.length());
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
		List<StudentScoresDailyDTO> list = query.getResultList();
		return list;
	}

	@Override
	public List<StudentScoresMonthlyGenreDTO> findScoresMonthlyGenre(ScoresSearchData searchRequest) {
		Long studentId = searchRequest.getStudentId();
		int readingLevel = searchRequest.getReadingLevel();
		String yearMonth = searchRequest.getYearMonth();
		Query query = entityManager.createNativeQuery("select\n" + "a.genre_id as genre, \n"
				+ "ifnull(sum(a.articles_published),0) AS articles_published,\n"
				+ "ifnull(sum(td.articles_read),0) AS articles_read,\n"
				+ "ifnull(sum(a.quiz_published),0) AS quiz_published,\n"
				+ "ifnull(sum(td.quiz_attempted),0) AS quiz_attempted,\n"
				+ "ifnull(sum(td.quiz_correct),0) AS quiz_correct\n" + "from monthly_published_articles_genre_vw a \n"
				+ "left outer join scores_monthly_genre td on (td.student_id=?1 and td.genre_id=a.genre_id and td.score_year_month=a.year_month)\n"
				+ "where a.reading_level=?2 and a.year_month= ?3\n" + "group by genre",
				StudentScoresMonthlyGenreDTO.class);
		query.setParameter(1, studentId);
		query.setParameter(2, readingLevel);
		query.setParameter(3, yearMonth);
		List<StudentScoresMonthlyGenreDTO> list = query.getResultList();
		return list;
	}

	@Override
	public List<StudentScoresMonthlyTotalDTO> findYScoresMonthly(ScoresSearchData searchRequest) {
		Long studentId = searchRequest.getStudentId();
		String yearMonth = searchRequest.getYearMonth();
		int readingLevel = searchRequest.getReadingLevel();
		LocalDate startDate = null;
		LocalDate endDate = null;
		Query query = entityManager.createNativeQuery("select \n" + "a.year_month as month,\n"
				+ "ifnull(sum(a.articles_published),0) as articles_published,\n"
				+ "ifnull(sum(td.articles_read),0) as articles_read,\n"
				+ "ifnull(sum(a.quiz_published),0) as quiz_published,\n"
				+ "ifnull(sum(td.quiz_attempted),0) as quiz_attempted,\n"
				+ "ifnull(sum(td.quiz_correct),0) as quiz_correct\n" + "from monthly_published_articles_total_vw a \n"
				+ "left outer join scores_monthly_total td on (td.student_id=?1 and td.score_year_month=a.year_month)\n"
				+ "where a.reading_level=?2 and a.year_month>= ?3 and a.year_month<= ?4 \n" + "group by month",
				StudentScoresMonthlyTotalDTO.class);
		endDate = LocalDate.of(Integer.parseInt(yearMonth.substring(0, 4)), Integer.parseInt(yearMonth.substring(4, 6)),
				LocalDate.now().getDayOfMonth());
		startDate = LocalDate.of(endDate.getYear(), endDate.getMonthValue(), 1).minusMonths(11);
		String startYearMonth = startDate.getYear() + ""
				+ (startDate.getMonthValue() > 9 ? startDate.getMonthValue() : "0" + startDate.getMonthValue());
		query.setParameter(1, studentId);
		query.setParameter(2, readingLevel);
		query.setParameter(3, startYearMonth);
		query.setParameter(4, yearMonth);
		List<StudentScoresMonthlyTotalDTO> list = query.getResultList();
		return list;
	}

	@Override
	public List<StudentScoresYearlyGenreDTO> findScoresYearlyGenre(ScoresSearchData searchRequest) {
		Long studentId = searchRequest.getStudentId();
		String yearMonth = searchRequest.getYearMonth();
		int readingLevel = searchRequest.getReadingLevel();
		LocalDate startDate = null;
		LocalDate endDate = null;
		Query query = entityManager.createNativeQuery("select \n" + "a.genre_id as genre,\n"
				+ "ifnull(sum(a.articles_published),0) as articles_published,\n"
				+ "ifnull(sum(td.articles_read),0) as articles_read,\n"
				+ "ifnull(sum(a.quiz_published),0) as quiz_published,\n"
				+ "ifnull(sum(td.quiz_attempted),0) as quiz_attempted,\n"
				+ "ifnull(sum(td.quiz_correct),0) as quiz_correct\n" + "from monthly_published_articles_genre_vw a \n"
				+ "left outer join scores_monthly_genre td on (td.student_id=?1 and td.genre_id=a.genre_id and td.score_year_month=a.year_month)\n"
				+ "where a.reading_level=?2 and a.year_month>= ?3 and a.year_month<= ?4 \n" + "group by genre",
				StudentScoresYearlyGenreDTO.class);
		endDate = LocalDate.of(Integer.parseInt(yearMonth.substring(0, 4)), Integer.parseInt(yearMonth.substring(4, 6)),
				LocalDate.now().getDayOfMonth());
		startDate = LocalDate.of(endDate.getYear(), endDate.getMonthValue(), 1).minusMonths(11);
		String startYearMonth = startDate.getYear() + ""
				+ (startDate.getMonthValue() > 9 ? startDate.getMonthValue() : "0" + startDate.getMonthValue());
		query.setParameter(1, studentId);
		query.setParameter(2, readingLevel);
		query.setParameter(3, startYearMonth);
		query.setParameter(4, yearMonth);
		List<StudentScoresYearlyGenreDTO> list = query.getResultList();
		return list;
	}

	@Override
	public List<StudentScoresMonthlyTotalDTO> findMonthWiseTotalScores(ScoresSearchData searchRequest) {
		Long studentId = searchRequest.getStudentId();
		String yearMonth = searchRequest.getYearMonth();
		int readingLevel = searchRequest.getReadingLevel();
		Query query = entityManager.createNativeQuery("select td.score_year_month as month,\n"
				+ "ifnull(sum(td.quiz_correct),0) as quiz_correct,'0' as articles_published,'0' as articles_read,'0' as quiz_attempted,'0' as quiz_published\n"
				+ "from scores_monthly_total td where td.student_id=?1 and td.reading_level=?2 and td.score_year_month= ?3 group by month\n",
				StudentScoresMonthlyTotalDTO.class);
		query.setParameter(1, studentId);
		query.setParameter(2, readingLevel);
		query.setParameter(3, yearMonth);
		List<StudentScoresMonthlyTotalDTO> list = query.getResultList();
		return list;
	}

	@Override
	public List<StudentScoresMonthlyGenreDTO> findMonthWiseGenreScores(ScoresSearchData searchRequest) {
		Long studentId = searchRequest.getStudentId();
		String yearMonth = searchRequest.getYearMonth();
		int readingLevel = searchRequest.getReadingLevel();
		Query query = entityManager.createNativeQuery("select td.genre_id as genre,\n"
				+ "ifnull(sum(td.quiz_correct),0) as quiz_correct,'0' as articles_published,'0' as articles_read,'0' as quiz_attempted,'0' as quiz_published\n"
				+ "from scores_monthly_genre td where td.student_id=?1 and td.reading_level=?2 and td.score_year_month= ?3 group by genre\n",
				StudentScoresMonthlyGenreDTO.class);
		query.setParameter(1, studentId);
		query.setParameter(2, readingLevel);
		query.setParameter(3, yearMonth);
		List<StudentScoresMonthlyGenreDTO> list = query.getResultList();
		return list;
	}

}
