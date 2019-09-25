package com.enewschamp.app.student.quiz.business;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enewschamp.app.article.page.dto.ArticleQuizCompletionDTO;
import com.enewschamp.app.student.badges.business.StudentBadgesBusiness;
import com.enewschamp.app.student.badges.entity.StudentBadges;
import com.enewschamp.app.student.business.StudentActivityBusiness;
import com.enewschamp.app.student.dailytrends.business.TrendsDailyBusiness;
import com.enewschamp.app.student.monthlytrends.business.TrendsMonthlyByGenreBusiness;
import com.enewschamp.app.student.monthlytrends.business.TrendsMonthlyTotalBusiness;
import com.enewschamp.app.student.monthlytrends.dto.TrendsMonthlyTotalDTO;
import com.enewschamp.app.student.quiz.dto.QuizScoreDTO;
import com.enewschamp.app.student.quiz.entity.QuizScore;
import com.enewschamp.app.student.quiz.service.QuizScoreService;
import com.enewschamp.article.domain.entity.NewsArticleQuiz;
import com.enewschamp.article.domain.service.NewsArticleQuizService;
import com.enewschamp.domain.common.RecordInUseType;
import com.enewschamp.master.badge.entity.Badge;
import com.enewschamp.master.badge.service.BadgeService;

@Service
public class QuizScoreBusiness {

	@Autowired
	QuizScoreService quizScoreService;

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	NewsArticleQuizService newsArticleQuizService;

	@Autowired
	TrendsDailyBusiness trendsDailyBusiness;

	@Autowired
	TrendsMonthlyByGenreBusiness trendsMonthlyByGenreBusiness;

	@Autowired
	TrendsMonthlyTotalBusiness trendsMonthlyTotalBusiness;
	
	@Autowired
	StudentActivityBusiness studentActivityBusiness;
	
	@Autowired
	StudentBadgesBusiness studentBadgesBusiness;
	@Autowired
	BadgeService badgeService;
	
	private Long quizQAttempted = 0L;
	private Long quizQCorrect = 0L;

	@Transactional
	public ArticleQuizCompletionDTO saveQuizScore(Long newsArticleId, List<QuizScoreDTO> quizScoreDTOList, Long studentId,
			String editionId) {

		for (QuizScoreDTO quizscore : quizScoreDTOList) {
			quizQAttempted++;

			quizscore.setResponseCorrect(getCorrectAns(quizscore.getNewsArticleQuizId(), quizscore.getResponse()));
			QuizScore quizScore = modelMapper.map(quizscore, QuizScore.class);
			// TO TO to be corrected
			quizScore.setRecordInUse(RecordInUseType.Y);
			quizScore.setOperatorId("SYSTEM");
			
			quizScore = quizScoreService.create(quizScore);
		}

		// update the daily trend entity..
		trendsDailyBusiness.saveQuizData(studentId, editionId, quizQAttempted, quizQCorrect);
		
		// update the Monthly Trend Genre Entity
		trendsMonthlyByGenreBusiness.saveQuizData(newsArticleId, studentId, editionId, quizQAttempted, quizQCorrect);
		
		//update Monthly Total Entity
		TrendsMonthlyTotalDTO trendsMonthlyTotalDTO = trendsMonthlyTotalBusiness.saveQuizData(studentId, editionId, quizQAttempted, quizQCorrect);
		
		//update Student Activity
		
		studentActivityBusiness.saveQuizData(studentId, newsArticleId, quizQCorrect);
		
		//grant badge if monthly points exceeds..
		Date date = new Date();
		LocalDate currDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

		int year = currDate.getYear();
		int month = currDate.getMonthValue();
		String monthStr = (month<10) ? "0"+month : ""+month;
		String monthYear = year + "" + monthStr;
		String genreId = trendsMonthlyByGenreBusiness.getGenreId(newsArticleId);
		StudentBadges studbadge = studentBadgesBusiness.grantBadge(studentId,editionId,Long.valueOf(monthYear),genreId,trendsMonthlyTotalDTO);
		ArticleQuizCompletionDTO quizCompletiondto = new ArticleQuizCompletionDTO();
		quizCompletiondto.setArticleId(newsArticleId);
		String message="You got "+quizQCorrect+ "correct answers...";
		
		if(studbadge!=null)
		{
			Badge badge = badgeService.get(studbadge.getBadgeId());
			message = message+"You also have the following badges ! Congratulations !!!";
			quizCompletiondto.setBadgeName(badge.getName());
			quizCompletiondto.setNewBadge(badge.getImage());
			quizCompletiondto.setNewGenreBadge(badge.getGenreId());
			
		}
	
		quizCompletiondto.setQuizCompletionMessage(message);
		
		return quizCompletiondto;
	}

	public QuizScoreDTO getQuizScore(Long studentId, Long newsArticleId) {
		QuizScore quizScore =null;

		try {
			quizScore = quizScoreService.getQuizScore(studentId, newsArticleId);
		}catch(Exception e)
		{
			return null;
		}
		QuizScoreDTO QuizScoreDTO = modelMapper.map(quizScore, QuizScoreDTO.class);
		return QuizScoreDTO;
	}

	public String getCorrectAns(Long newsArticleQuizId, Long ansSelected) {
		String isCorrect = "N";
		NewsArticleQuiz newsArticleQuiz = newsArticleQuizService.get(newsArticleQuizId);
		if (ansSelected == newsArticleQuiz.getCorrectOpt()) {
			isCorrect = "Y";
			quizQCorrect++;
		}
		return isCorrect;
	}

}
