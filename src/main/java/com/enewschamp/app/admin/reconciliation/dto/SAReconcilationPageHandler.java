//package com.enewschamp.app.admin.reconciliation.dto;
//
//import java.time.LocalDate;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.concurrent.atomic.AtomicLong;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import com.enewschamp.app.common.PageDTO;
//import com.enewschamp.app.common.PageRequestDTO;
//import com.enewschamp.app.fw.page.navigation.dto.PageNavigatorDTO;
//import com.enewschamp.app.student.entity.StudentActivity;
//import com.enewschamp.app.student.quiz.entity.QuizScore;
//import com.enewschamp.app.student.registration.entity.StudentRegistration;
//import com.enewschamp.app.student.scores.entity.ScoresDaily;
//import com.enewschamp.article.domain.entity.NewsArticle;
//import com.enewschamp.article.domain.entity.NewsArticleQuiz;
//import com.enewschamp.domain.common.IPageHandler;
//import com.enewschamp.domain.common.PageNavigationContext;
//import com.fasterxml.jackson.databind.ObjectMapper;
//
//import lombok.SneakyThrows;
//
//@Component("SAReconcilationPageHandler")
//public class SAReconcilationPageHandler implements IPageHandler {
//
//	@Autowired
//	private ReconcilationService reconcilationService;
//	
//	@Autowired
//	private ReconcilationServiceImpl reconcilationServiceImpl;
//
//	@Autowired
//	private ObjectMapper objectMapper;
//
//	@Override
//	public PageDTO handleAction(PageRequestDTO pageRequest) {
//		PageDTO pageDto = null;
//		switch (pageRequest.getHeader().getAction()) {
//		case "Report":
//			pageDto = findScoreDailyDiscripancies(pageRequest);
//			break;
//		default:
//			break;
//		}
//		return pageDto;
//	}
//
//	@Override
//	public PageDTO loadPage(PageNavigationContext pageNavigationContext) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public PageDTO saveAsMaster(PageRequestDTO pageRequest) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public PageDTO handleAppAction(PageRequestDTO pageRequest, PageNavigatorDTO pageNavigatorDTO) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@SneakyThrows
//	public PageDTO findDiscripancies(PageRequestDTO pageRequest) {
//		PageDTO pageDto = new PageDTO();
//		SAReconcilationPageData pageData = new SAReconcilationPageData();
//		List<StudentRegistration> studentregList = reconcilationService.getStudentRegistrationList();
//		List<StudentActivityDiscrepancy> discripancies = new ArrayList<>();
//		for (StudentRegistration registration : studentregList) {
//			AtomicLong sumCounter = new AtomicLong();
//			List<NewsArticle> newsArticleList = reconcilationService
//					.getNewsArticleByStatusAndPublicationDate("Published", LocalDate.parse("2020-05-14"));
//			for (NewsArticle newsArticle : newsArticleList) {
//				List<NewsArticleInfo> newsArticleDiscripancies = new ArrayList<>();
//				List<NewsArticleQuiz> newsArticleQuizList = reconcilationService
//						.getNewsArticleQuizByArticleId(newsArticle.getNewsArticleId());
//				List<QuizInfo> quizInfoDiscripancies = new ArrayList<>();
//				for (NewsArticleQuiz articleQuiz : newsArticleQuizList) {
//					QuizScore quizScore = reconcilationService.getQuizScore(registration.getStudentId(),
//							articleQuiz.getNewsArticleQuizId());
//					if (quizScore != null) {
//						QuizInfo quizInfo = new QuizInfo();
//						quizInfo.setNewsArticleQuizId(quizScore.getNewsArticleQuizId());
//						quizInfo.setResponse(quizScore.getResponse());
//						quizInfo.setResponseCorrect(quizScore.getResponseCorrect());
//						sumCounter.addAndGet(quizScore.getResponse());
//						quizInfoDiscripancies.add(quizInfo);
//					}
//
//				}
//				StudentActivity activity = reconcilationService.getStudentActivity(registration.getStudentId(),
//						newsArticle.getNewsArticleId());
//				if (activity != null && (sumCounter.longValue() != activity.getQuizScore())) {
//					NewsArticleInfo newsArticleInfo = new NewsArticleInfo();
//					newsArticleInfo.setQuizInfoList(quizInfoDiscripancies);
//					newsArticleInfo.setQuizScoreTotal(sumCounter.longValue());
//					newsArticleInfo.setStudentActivityQuizScore(activity.getQuizScore());
//					newsArticleDiscripancies.add(newsArticleInfo);
//
//					StudentActivityDiscrepancy studentActivityDis = new StudentActivityDiscrepancy();
//					studentActivityDis.setStudentId(registration.getStudentId());
//					studentActivityDis.setNewsArticles(newsArticleDiscripancies);
//					discripancies.add(studentActivityDis);
//
//					System.out.println("Studentactivity result where student id is: " + registration.getStudentId()
//							+ " and his score is: " + activity.getQuizScore() + " for newArticle: "
//							+ newsArticle.getNewsArticleId());
//				}
//			}
//
//			System.out.println("StudentId: " + registration.getStudentId() + " quiz corrected: " + sumCounter);
//
//		}
//		System.out.println(objectMapper.writeValueAsString(discripancies));
//		pageData.setListStudentActivityDiscrepancies(discripancies);
//		pageDto.setHeader(pageRequest.getHeader());
//		pageDto.setData(pageData);
//		return pageDto;
//	}
//
//	@SneakyThrows
//	public PageDTO findScoreDailyDiscripancies(PageRequestDTO pageRequest) {
//		PageDTO pageDto = new PageDTO();
//		SAReconcilationPageData pageData = new SAReconcilationPageData();
//		reconcilationServiceImpl.getDiscripancies();
//		List<StudentRegistration> studentregList = reconcilationService.getStudentRegistrationList();
//		List<StudentDailyScoresDiscripancies> discripancies = new ArrayList<>();
//		for (StudentRegistration registration : studentregList) {
//			AtomicLong totalRespCounter = new AtomicLong();
//			AtomicLong recordCounter = new AtomicLong();
//			AtomicLong newsArticleCounter = new AtomicLong();
//
//			List<NewsArticle> newsArticleList = reconcilationService
//					.getNewsArticleByStatusAndPublicationDateAndReadingLevel("Published", LocalDate.parse("2020-05-14"),
//							4);
//
//			for (NewsArticle newsArticle : newsArticleList) {
//				List<DailyNewsArticleInfo> newsArticleDiscripancies = new ArrayList<>();
//
//				List<NewsArticleQuiz> newsArticleQuizList = reconcilationService
//						.getNewsArticleQuizByArticleId(newsArticle.getNewsArticleId());
//
//				List<QuizInfo> quizInfoDiscripancies = new ArrayList<>();
//				for (NewsArticleQuiz articleQuiz : newsArticleQuizList) {
//
//					QuizScore quizScore = reconcilationService.getQuizScore(registration.getStudentId(),
//							articleQuiz.getNewsArticleQuizId());
//
//					if (quizScore != null) {
//						if ((articleQuiz.getNewsArticleId() == newsArticle.getNewsArticleId())
//								&& !(newsArticleCounter.longValue() > 0)) {
//							newsArticleCounter.incrementAndGet();
//						}
//						QuizInfo quizInfo = new QuizInfo();
//						quizInfo.setNewsArticleQuizId(quizScore.getNewsArticleQuizId());
//						quizInfo.setResponse(quizScore.getResponse());
//						quizInfo.setResponseCorrect(quizScore.getResponseCorrect());
//						recordCounter.incrementAndGet();
//						totalRespCounter.addAndGet(quizScore.getResponse());
//						quizInfoDiscripancies.add(quizInfo);
//					}
//
//				}
//				ScoresDaily scoresDaily = reconcilationService.getScoreDaily(registration.getStudentId(), 4,
//						LocalDate.parse("2020-05-14"));
//
//				if (!quizInfoDiscripancies.isEmpty()) {
//					if (scoresDaily != null && (recordCounter.longValue() != scoresDaily.getQuizAttempted())
//							&& (totalRespCounter.longValue() != scoresDaily.getQuizCorrect())
//							&& (newsArticleCounter.longValue() != scoresDaily.getArticlesRead())) {
//						DailyNewsArticleInfo dailyArticle = new DailyNewsArticleInfo();
//						dailyArticle.setNewsArticleId(newsArticle.getNewsArticleId());
//						dailyArticle.setQuizInfoList(quizInfoDiscripancies);
//						newsArticleDiscripancies.add(dailyArticle);
//						StudentDailyScoresDiscripancies scoresDiscripancies = new StudentDailyScoresDiscripancies();
//						scoresDiscripancies.setArticleRead(scoresDaily.getArticlesRead());
//						scoresDiscripancies.setDate(LocalDate.parse("2020-05-14"));
//						scoresDiscripancies.setNewsArticleCount(newsArticleCounter.longValue());
//						scoresDiscripancies.setQuizAttempted(scoresDaily.getQuizAttempted());
//						scoresDiscripancies.setQuizCorrect(scoresDaily.getQuizCorrect());
//						scoresDiscripancies.setReadingLevel(4);
//						scoresDiscripancies.setStudentId(registration.getStudentId());
//						scoresDiscripancies.setTotalCorrectResponse(totalRespCounter.longValue());
//						scoresDiscripancies.setTotalRecords(recordCounter.longValue());
//						scoresDiscripancies.setNewsArticles(newsArticleDiscripancies);
//						discripancies.add(scoresDiscripancies);
//
//					}
//				}
//				// reconcilationService.get
////				StudentActivity activity = reconcilationService.getStudentActivity(registration.getStudentId(),
////						newsArticle.getNewsArticleId());
////				if (activity != null && (sumCounter.longValue() != activity.getQuizScore())) {
////					NewsArticleInfo newsArticleInfo = new NewsArticleInfo();
////					newsArticleInfo.setQuizInfoList(quizInfoDiscripancies);
////					newsArticleInfo.setQuizScoreTotal(sumCounter.longValue());
////					newsArticleInfo.setStudentActivityQuizScore(activity.getQuizScore());
////					newsArticleDiscripancies.add(newsArticleInfo);
////
////					StudentActivityDiscrepancy studentActivityDis = new StudentActivityDiscrepancy();
////					studentActivityDis.setStudentId(registration.getStudentId());
////					studentActivityDis.setNewsArticles(newsArticleDiscripancies);
////					discripancies.add(studentActivityDis);
////
////					System.out.println("Studentactivity result where student id is: " + registration.getStudentId()
////							+ " and his score is: " + activity.getQuizScore() + " for newArticle: "
////							+ newsArticle.getNewsArticleId());
////				}
//			}
//
//			System.out.println("StudentId: " + registration.getStudentId() + " total quiz attempted: " + recordCounter
//					+ "total correct responses: " + totalRespCounter + " and total Article is: " + newsArticleCounter);
//
//		}
//
//		System.out.println(objectMapper.writeValueAsString(discripancies));
//		pageData.setListStudentDailyScoresDiscrepancies(discripancies);
//		pageDto.setHeader(pageRequest.getHeader());
//		pageDto.setData(pageData);
//		return pageDto;
//	}
//
//}
