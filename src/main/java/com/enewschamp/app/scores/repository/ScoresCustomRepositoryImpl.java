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
import com.enewschamp.app.trends.dto.TrendsSearchData;


@Repository
public class ScoresCustomRepositoryImpl implements ScoresCustomRepository {
	@PersistenceContext
	private EntityManager entityManager;
	
	public List<DailyScoreDTO> findDailyScores(ScoresSearchData searchRequest)
	{
		Long studentId = searchRequest.getStudentId();
		String monthYear = searchRequest.getMonth();
		LocalDate startDate = null;
		LocalDate endDate = null;
		Query query = entityManager.createNativeQuery(
				"select \n" + 
				"articles_published,\n" + 
				"((select article_read from trends_daily aa where aa.quiz_date = a.publish_date and aa.student_id=?1)) as articles_read,\n" + 
				"a.quiz_published,\n" + 
				"((select quiz_attempted from trends_daily aa where aa.quiz_date = a.publish_date and aa.student_id=?2)) as quiz_attempted,\n" + 
				"((select quiz_correct from trends_daily aa where aa.quiz_date = a.publish_date and aa.student_id=?3)) as quiz_correct,\n" + 
				"a.publish_date\n" + 
				"from published_acrticles_vw a where a.publish_date>= ?4 and a.publish_date<= ?5 \n" + 
				"group by a.publish_date,articles_read,quiz_published,quiz_attempted,quiz_correct\n" , DailyScore.class );

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
		query.setParameter(2, studentId);
		query.setParameter(3, studentId);
		query.setParameter(4, startDate);
		query.setParameter(5, endDate);
		
		List<DailyScoreDTO> list = query.getResultList();
		System.out.println("Daily article: " + list);
		return list;

	}

	@Override
	public List<MonthlyScoreGenreDTO> findMonthlyScoresByGenre(ScoresSearchData searchRequest) {
		
		Long studentId = searchRequest.getStudentId();
		String monthYear = searchRequest.getMonth();
		LocalDate startDate = null;
		LocalDate endDate = null;
		Query query = entityManager.createNativeQuery(
				"select \n" + 
				" articles_published,\n" + 
				"((select article_read from trends_daily aa where aa.quiz_date = a.publish_date and aa.student_id=?1)) as articles_read,\n" + 
				"quiz_published,\n" + 
				"((select quiz_attempted from trends_daily aa where aa.quiz_date = a.publish_date and aa.student_id=?2)) as quiz_attempted,\n" + 
				"((select quiz_correct from trends_daily aa where aa.quiz_date = a.publish_date and aa.student_id=?3)) as quiz_correct,\n" + 
				"a.genreid\n" + 
				"from published_acrticles_genre_vw a where a.publish_date>=?4 and a.publish_date<=?5\n" + 
				"\n" + 
				"", MonthlyScoreGenre.class);

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
		query.setParameter(2, studentId);
		query.setParameter(3, studentId);
		query.setParameter(4, startDate);
		query.setParameter(5, endDate);
		
		List<MonthlyScoreGenreDTO> list = query.getResultList();
		System.out.println("Daily article: " + list);
		return list;
	}

	@Override
	public List<YearlyScoresGenreDTO> findYearlyScoresByGenre(ScoresSearchData searchRequest) {
		Long studentId = searchRequest.getStudentId();
		String monthYear = searchRequest.getMonth();
		LocalDate startDate = null;
		LocalDate endDate = null;
		Query query = entityManager.createNativeQuery(
				"select \n" + 
				"sum(a.articles_published) as articles_published,\n" + 
				"sum((select (articles_read) from trends_monthly_total aa where year(a.publish_date)= SUBSTRING(aa.trendyear_month, 1,4) and month(a.publish_date)=SUBSTRING(aa.trendyear_month, 5,6) and aa.student_id= ?1)) as articles_read,\n" + 
				"sum(a.quiz_published) as quiz_published,\n" + 
				"sum((select (quiz_attempted) from trends_monthly_total aa where  year(a.publish_date)= SUBSTRING(aa.trendyear_month, 1,4) and month(a.publish_date)=SUBSTRING(aa.trendyear_month, 5,6) and aa.student_id= ?2)) as quiz_attempted,\n" + 
				"sum((select (quiz_correct) from trends_monthly_total aa where  year(a.publish_date)= SUBSTRING(aa.trendyear_month, 1,4) and month(a.publish_date)=SUBSTRING(aa.trendyear_month, 5,6) and aa.student_id=?3)) as quiz_correct,\n" + 
				"a.genreid\n" + 
				"from published_acrticles_genre_vw a \n" + 
				"where a.publish_date>= ?4 and a.publish_date<= ?5 \n" + 
				"group by genreid,articles_published,quiz_published", YearlyScoresGenre.class);

		if (monthYear != null && !"".equals(monthYear)) {
			String year = monthYear.substring(0, 4);
			//String month = monthYear.substring(monthYear.length() - 2, monthYear.length());
			String day1 = "01";
			String month="01";
			// form date..
			startDate = LocalDate.of(Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(day1));
			endDate = LocalDate.of(startDate.getYear(), startDate.getMonthValue(), 1).plusMonths(12).minusDays(1);

		} else {
			startDate = LocalDate.now();
			startDate = LocalDate.of(startDate.getYear(), startDate.getMonthValue(), 1);
			endDate = LocalDate.of(startDate.getYear(), startDate.getMonthValue(), 1).plusMonths(12).minusDays(1);

		}

		query.setParameter(1, studentId);
		query.setParameter(2, studentId);
		query.setParameter(3, studentId);
		query.setParameter(4, startDate);
		query.setParameter(5, endDate);
		
		List<YearlyScoresGenreDTO> list = query.getResultList();
		System.out.println("findYearlyScoresByGenre : " + list);
		return list;
	}

	@Override
	public List<MonthlyScoresDTO> findYMonthlyScores(ScoresSearchData searchRequest) {
		Long studentId = searchRequest.getStudentId();
		String monthYear = searchRequest.getMonth();
		LocalDate startDate = null;
		LocalDate endDate = null;
		Query query = entityManager.createNativeQuery(
				"select \n" + 
				"sum(a.articles_published) as articles_published,\n" + 
				"sum((select (articles_read) from trends_monthly_total aa where year(a.publish_date)= SUBSTRING(aa.trendyear_month, 1,4) and month(a.publish_date)=SUBSTRING(aa.trendyear_month, 5,6) and aa.student_id= ?1)) as articles_read,\n" + 
				"sum(a.quiz_published) as quiz_published,\n" + 
				"sum((select (quiz_attempted) from trends_monthly_total aa where  year(a.publish_date)= SUBSTRING(aa.trendyear_month, 1,4) and month(a.publish_date)=SUBSTRING(aa.trendyear_month, 5,6) and aa.student_id= ?2)) as quiz_attempted,\n" + 
				"sum((select (quiz_correct) from trends_monthly_total aa where  year(a.publish_date)= SUBSTRING(aa.trendyear_month, 1,4) and month(a.publish_date)=SUBSTRING(aa.trendyear_month, 5,6) and aa.student_id= ?3)) as quiz_correct,\n" + 
				"month(a.publish_date) publish_date\n" + 
				"from published_acrticles_vw a where a.publish_date>= ?4 and a.publish_date<= ?5 \n" + 
				"group by (a.publish_date)", MonthlyScores.class);

		if (monthYear != null && !"".equals(monthYear)) {
			String year = monthYear.substring(0, 4);
			//String month = monthYear.substring(monthYear.length() - 2, monthYear.length());
			String day1 = "01";
			String month="01";
			
			// form date..
			startDate = LocalDate.of(Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(day1));
			endDate = LocalDate.of(startDate.getYear(), startDate.getMonthValue(), 1).plusMonths(12).minusDays(1);

		} else {
			startDate = LocalDate.now();
			startDate = LocalDate.of(startDate.getYear(), startDate.getMonthValue(), 1);
			endDate = LocalDate.of(startDate.getYear(), startDate.getMonthValue(), 1).plusMonths(12).minusDays(1);

		}

		query.setParameter(1, studentId);
		query.setParameter(2, studentId);
		query.setParameter(3, studentId);
		query.setParameter(4, startDate);
		query.setParameter(5, endDate);
		
		List<MonthlyScoresDTO> list = query.getResultList();
		System.out.println("findYearlyScoresByGenre : " + list);
		return list;
	}
	
}
