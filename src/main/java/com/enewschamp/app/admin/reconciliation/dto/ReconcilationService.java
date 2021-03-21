package com.enewschamp.app.admin.reconciliation.dto;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enewschamp.app.student.entity.StudentActivity;
import com.enewschamp.app.student.quiz.entity.QuizScore;
import com.enewschamp.app.student.quiz.repository.QuizScoreRepository;
import com.enewschamp.app.student.registration.entity.StudentRegistration;
import com.enewschamp.app.student.registration.repository.StudentRegistrationRepository;
import com.enewschamp.app.student.repository.StudentActivityRepository;
import com.enewschamp.app.student.scores.entity.ScoresDaily;
import com.enewschamp.app.student.scores.repository.ScoresDailyRepository;
import com.enewschamp.article.domain.common.ArticleStatusType;
import com.enewschamp.article.domain.entity.NewsArticle;
import com.enewschamp.article.domain.entity.NewsArticleQuiz;
import com.enewschamp.article.domain.service.NewsArticleQuizRepository;
import com.enewschamp.article.domain.service.NewsArticleRepository;
import com.enewschamp.problem.BusinessException;

@Service
public class ReconcilationService {

	@Autowired
	private NewsArticleRepository newsArticleRepository;
	@Autowired
	private NewsArticleQuizRepository newsArticleQuizRepository;
	@Autowired
	private StudentRegistrationRepository studentRegistrationRepository;
	@Autowired
	private QuizScoreRepository quizScoreRepository;
	@Autowired
	private StudentActivityRepository studentActivityRepository;
	@Autowired
	private ScoresDailyRepository scoreDailyRepository;

	public List<NewsArticle> getNewsArticleByStatusAndPublicationDate(String status, LocalDate publicationDate) {
		Optional<List<NewsArticle>> newsArticles = newsArticleRepository
				.findByStatusAndPublicationDate(ArticleStatusType.Published, publicationDate);
		if (newsArticles.isPresent())
			return newsArticles.get();
		else
			throw new BusinessException("NewsArticleNotFound");
	}

	public List<NewsArticle> getNewsArticleByStatusAndPublicationDateAndReadingLevel(String status,
			LocalDate publicationDate, Integer readingLevel) {
		Optional<List<NewsArticle>> newsArticles = newsArticleRepository.findByStatusAndPublicationDateAndReadingLevel(
				ArticleStatusType.Published, publicationDate, readingLevel);
		if (newsArticles.isPresent())
			return newsArticles.get();
		else
			throw new BusinessException("NewsArticleNotFound");
	}

	public List<NewsArticleQuiz> getNewsArticleQuizByArticleId(long newsArticleId) {
		return newsArticleQuizRepository.findByNewsArticleId(newsArticleId);
	}

	public List<StudentRegistration> getStudentRegistrationList() {
		return studentRegistrationRepository.findAll();
	}

	public QuizScore getQuizScore(Long studentId, Long newsArticleQuizId) {
		Optional<QuizScore> quizScore = quizScoreRepository.getQuizScore(studentId, newsArticleQuizId);
		if (quizScore.isPresent()) {
			return quizScore.get();
		} else {
			return null;
		}
	}

	public StudentActivity getStudentActivity(Long studentId, Long newsArticleId) {
		Optional<StudentActivity> studentActivity = studentActivityRepository.getStudentActivity(studentId,
				newsArticleId);
		if (studentActivity.isPresent()) {
			return studentActivity.get();
		} else {
			return null;
		}
	}

	public ScoresDaily getScoreDaily(Long studentId, int readingLevel, LocalDate publicationDate) {
		Optional<ScoresDaily> scoresDaily = scoreDailyRepository
				.findByStudentIdAndReadingLevelAndPublicationDate(studentId, readingLevel, publicationDate);
		if (scoresDaily.isPresent()) {
			return scoresDaily.get();
		} else {
			return null;
		}
	}

}
