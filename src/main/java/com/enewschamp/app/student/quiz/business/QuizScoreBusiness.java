package com.enewschamp.app.student.quiz.business;

import java.time.LocalDate;
import java.util.List;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enewschamp.app.article.page.dto.ArticleQuizCompletionDTO;
import com.enewschamp.app.student.badges.business.StudentBadgesBusiness;
import com.enewschamp.app.student.badges.entity.StudentBadges;
import com.enewschamp.app.student.business.StudentActivityBusiness;
import com.enewschamp.app.student.quiz.dto.QuizScoreDTO;
import com.enewschamp.app.student.quiz.entity.QuizScore;
import com.enewschamp.app.student.quiz.service.QuizScoreService;
import com.enewschamp.app.student.scores.business.ScoresDailyBusiness;
import com.enewschamp.app.student.scores.business.ScoresMonthlyGenreBusiness;
import com.enewschamp.app.student.scores.business.ScoresMonthlyTotalBusiness;
import com.enewschamp.app.student.scores.dto.ScoresMonthlyGenreDTO;
import com.enewschamp.app.student.scores.dto.ScoresMonthlyTotalDTO;
import com.enewschamp.app.welcome.page.data.BadgeDetailsDTO;
import com.enewschamp.article.domain.entity.NewsArticle;
import com.enewschamp.article.domain.entity.NewsArticleQuiz;
import com.enewschamp.article.domain.service.NewsArticleQuizService;
import com.enewschamp.article.domain.service.NewsArticleService;
import com.enewschamp.domain.common.RecordInUseType;
import com.enewschamp.master.badge.service.BadgeService;
import com.enewschamp.publication.domain.entity.Badge;

@Service
public class QuizScoreBusiness {

	@Autowired
	QuizScoreService quizScoreService;

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	NewsArticleQuizService newsArticleQuizService;

	@Autowired
	ScoresDailyBusiness scoresDailyBusiness;

	@Autowired
	ScoresMonthlyGenreBusiness scoresMonthlyGenreBusiness;

	@Autowired
	ScoresMonthlyTotalBusiness scoresMonthlyTotalBusiness;

	@Autowired
	StudentActivityBusiness studentActivityBusiness;

	@Autowired
	StudentBadgesBusiness studentBadgesBusiness;

	@Autowired
	NewsArticleService newsArticleService;

	@Autowired
	BadgeService badgeService;

	@Transactional
	public ArticleQuizCompletionDTO saveQuizScore(Long newsArticleId, List<QuizScoreDTO> quizScoreDTOList,
			Long studentId, String editionId, int readingLevel, String emailId) {
		Long quizQAttempted = 0L;
		Long quizQCorrect = 0L;
		for (QuizScoreDTO quizscore : quizScoreDTOList) {
			quizQAttempted++;
			String isCorrectAns = getCorrectAns(quizscore.getNewsArticleQuizId(), quizscore.getResponse());
			if ("Y".equalsIgnoreCase(isCorrectAns)) {
				quizQCorrect++;
			}
			quizscore.setResponseCorrect(isCorrectAns);
			QuizScore quizScore = modelMapper.map(quizscore, QuizScore.class);
			quizScore.setRecordInUse(RecordInUseType.Y);
			quizScore.setOperatorId(""+studentId);
			quizScore = quizScoreService.create(quizScore);
		}

		// update the daily score entity..
		scoresDailyBusiness.saveQuizData(newsArticleId, studentId, emailId, editionId, readingLevel, quizQAttempted,
				quizQCorrect);

		// update the Monthly Score Genre Entity
		ScoresMonthlyGenreDTO scoresMonthlyGenreDTO = scoresMonthlyGenreBusiness.saveQuizData(newsArticleId, studentId,
				editionId, readingLevel, quizQAttempted, quizQCorrect);

		// update Monthly Score Total Entity
		ScoresMonthlyTotalDTO scoresMonthlyTotalDTO = scoresMonthlyTotalBusiness.saveQuizData(newsArticleId, studentId,
				editionId, readingLevel, quizQAttempted, quizQCorrect);

		// update Student Activity

		studentActivityBusiness.saveQuizData(studentId, emailId, editionId, readingLevel, newsArticleId, quizQCorrect);

		// grant badge if monthly points exceeds..
		NewsArticle newsArticle = newsArticleService.get(newsArticleId);
		LocalDate publicationDate = newsArticle.getPublicationDate();
		int year = publicationDate.getYear();
		int month = publicationDate.getMonthValue();
		String yearMonth = year + "" + (month > 9 ? month : "0" + month);
		String genreId = scoresMonthlyGenreBusiness.getGenreId(newsArticleId);
		StudentBadges studGenreBadge = studentBadgesBusiness.grantGenreBadge(studentId, emailId, editionId,
				readingLevel, Long.valueOf(yearMonth), genreId, scoresMonthlyGenreDTO);
		StudentBadges studBadge = studentBadgesBusiness.grantBadge(studentId, emailId, editionId, readingLevel,
				Long.valueOf(yearMonth), "MONTHLY", scoresMonthlyTotalDTO);
		ArticleQuizCompletionDTO quizCompletiondto = new ArticleQuizCompletionDTO();
		quizCompletiondto.setArticleId(newsArticleId);
		String message = "" + quizQCorrect;
		if (studGenreBadge != null) {
			Badge badge = badgeService.get(studGenreBadge.getBadgeId());
			BadgeDetailsDTO genreBadgeDetailsDTO = new BadgeDetailsDTO();
			genreBadgeDetailsDTO.setBadgeId(badge.getBadgeId());
			genreBadgeDetailsDTO.setBadgeName(badge.getNameId());
			genreBadgeDetailsDTO.setImageName(badge.getImageName());
			genreBadgeDetailsDTO.setBadgeGenre(badge.getGenreId());
			quizCompletiondto.setGenreBadge(genreBadgeDetailsDTO);
		}
		if (studBadge != null) {
			Badge badge = badgeService.get(studBadge.getBadgeId());
			BadgeDetailsDTO monthlyBadgeDetailsDTO = new BadgeDetailsDTO();
			monthlyBadgeDetailsDTO.setBadgeId(badge.getBadgeId());
			monthlyBadgeDetailsDTO.setBadgeName(badge.getNameId());
			monthlyBadgeDetailsDTO.setImageName(badge.getImageName());
			monthlyBadgeDetailsDTO.setBadgeGenre(badge.getGenreId());
			quizCompletiondto.setMonthlyBadge(monthlyBadgeDetailsDTO);
		}
		quizCompletiondto.setQuizCompletionMessage(message);
		return quizCompletiondto;
	}

	public QuizScoreDTO getQuizScore(Long studentId, Long newsArticleId) {
		QuizScore quizScore = null;
		try {
			quizScore = quizScoreService.getQuizScore(studentId, newsArticleId);
		} catch (Exception e) {
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
		}
		return isCorrect;
	}

}
