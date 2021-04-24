package com.enewschamp.app.article.page.handler;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import com.enewschamp.app.article.page.dto.ArticleCompletionQuizPageData;
import com.enewschamp.app.article.page.dto.ArticleQuizAnswers;
import com.enewschamp.app.article.page.dto.ArticleQuizCompletionDTO;
import com.enewschamp.app.article.page.dto.ArticleQuizDetailsPageData;
import com.enewschamp.app.article.page.dto.ArticleQuizPageData;
import com.enewschamp.app.article.page.dto.ArticleQuizQuestionsPageData;
import com.enewschamp.app.common.CommonService;
import com.enewschamp.app.common.ErrorCodeConstants;
import com.enewschamp.app.common.PageDTO;
import com.enewschamp.app.common.PageRequestDTO;
import com.enewschamp.app.fw.page.navigation.dto.PageNavigatorDTO;
import com.enewschamp.app.student.business.StudentActivityBusiness;
import com.enewschamp.app.student.dto.StudentActivityDTO;
import com.enewschamp.app.student.quiz.business.QuizScoreBusiness;
import com.enewschamp.app.student.quiz.dto.QuizScoreDTO;
import com.enewschamp.app.student.registration.entity.StudentRegistration;
import com.enewschamp.app.student.registration.service.StudentRegistrationService;
import com.enewschamp.article.app.dto.NewsArticleSummaryDTO;
import com.enewschamp.article.domain.entity.NewsArticleQuiz;
import com.enewschamp.article.domain.service.NewsArticleQuizService;
import com.enewschamp.article.domain.service.NewsArticleService;
import com.enewschamp.article.page.data.NewsArticleSearchRequest;
import com.enewschamp.domain.common.IPageHandler;
import com.enewschamp.domain.common.PageNavigationContext;
import com.enewschamp.master.badge.service.BadgeService;
import com.enewschamp.problem.BusinessException;
import com.enewschamp.subscription.app.dto.StudentPreferencesDTO;
import com.enewschamp.subscription.domain.business.PreferenceBusiness;
import com.enewschamp.subscription.domain.business.StudentControlBusiness;
import com.enewschamp.subscription.domain.business.SubscriptionBusiness;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component(value = "QuizPageHandler")
public class QuizPageHandler implements IPageHandler {

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	CommonService commonService;

	@Autowired
	SubscriptionBusiness subscriptionBusiness;

	@Autowired
	StudentControlBusiness studentControlBusiness;

	@Autowired
	ObjectMapper objectMapper;

	@Autowired
	QuizScoreBusiness quizScoreBusiness;

	@Autowired
	NewsArticleQuizService newsArticleQuizService;

	@Autowired
	BadgeService badgeService;

	@Autowired
	private NewsArticleService newsArticleService;

	@Autowired
	private StudentActivityBusiness studentActivityBusiness;

	@Autowired
	PreferenceBusiness preferenceBusiness;

	@Autowired
	StudentRegistrationService regService;

	@Override
	public PageDTO handleAction(PageRequestDTO pageRequest) {
		return null;
	}

	@Override
	public PageDTO loadPage(PageNavigationContext pageNavigationContext) {
		String methodName = pageNavigationContext.getLoadMethod();
		if (methodName != null && !"".equals(methodName)) {
			Class[] params = new Class[1];
			params[0] = PageNavigationContext.class;
			Method m = null;
			try {
				m = this.getClass().getDeclaredMethod(methodName, params);
				return (PageDTO) m.invoke(this, pageNavigationContext);
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				if (e.getCause() instanceof BusinessException) {
					throw ((BusinessException) e.getCause());
				} else {
					throw new BusinessException(ErrorCodeConstants.RUNTIME_EXCEPTION, ExceptionUtils.getStackTrace(e));
				}
			} catch (NoSuchMethodException nsmEx) {
				nsmEx.printStackTrace();
			} catch (SecurityException seEx) {
				seEx.printStackTrace();
			}
		}
		PageDTO pageDTO = new PageDTO();
		pageDTO.setHeader(pageNavigationContext.getPageRequest().getHeader());
		return pageDTO;
	}

	public PageDTO loadQuizPage(PageNavigationContext pageNavigationContext) {
		PageDTO pageDto = new PageDTO();
		pageDto.setHeader(pageNavigationContext.getPageRequest().getHeader());
		String emailId = pageNavigationContext.getPageRequest().getHeader().getEmailId();
		String isTestUser = "";
		StudentRegistration studReg = regService.getStudentReg(emailId);
		if (studReg != null) {
			isTestUser = studReg.getIsTestUser();
		}
		Long studentId = pageNavigationContext.getPageRequest().getHeader().getStudentId();
		ArticleQuizDetailsPageData pageData = (ArticleQuizDetailsPageData) commonService
				.mapPageData(ArticleQuizDetailsPageData.class, pageNavigationContext.getPageRequest());
		Long newsArticleId = pageData.getNewsArticleId();
		StudentActivityDTO stdactivity = studentActivityBusiness.getActivity(studentId, newsArticleId);
		if (stdactivity != null) {
			if (stdactivity.getQuizScore() == null) {
				pageData.setQuizCompleted("N");
			} else {
				pageData.setQuizCompleted("Y");
			}
		} else {
			pageData.setQuizCompleted("N");
		}
		NewsArticleSearchRequest searchRequestData = new NewsArticleSearchRequest();
		searchRequestData.setArticleId(newsArticleId);
		searchRequestData.setIsTestUser(isTestUser);
		Page<NewsArticleSummaryDTO> pageResult = newsArticleService.findArticles(searchRequestData, studentId, 1, 10,
				pageNavigationContext.getPageRequest().getHeader());
		List<NewsArticleSummaryDTO> articleDtoList = pageResult.getContent();
		if (articleDtoList != null && !articleDtoList.isEmpty()) {
			pageData.setHeadline(articleDtoList.get(0).getHeadline());
		}
		List<NewsArticleQuiz> newsArticleQuizList = newsArticleQuizService.getByNewsArticleId(newsArticleId);
		List<ArticleQuizQuestionsPageData> qList = mapQuizDataWithAnswers(newsArticleQuizList, studentId);
		pageData.setQuizQuestions(qList);
		pageDto.setData(pageData);
		return pageDto;
	}

	public PageDTO loadQuizCompletionPage(PageNavigationContext pageNavigationContext) {
		PageDTO pageDto = new PageDTO();
		pageDto.setHeader(pageNavigationContext.getPageRequest().getHeader());
		pageDto.setData(pageNavigationContext.getPreviousPageResponse().getData());
		return pageDto;
	}

	@Override
	public PageDTO saveAsMaster(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();

		return pageDto;
	}

	@Override
	public PageDTO handleAppAction(PageRequestDTO pageRequest, PageNavigatorDTO pageNavigatorDTO) {
		String methodName = pageNavigatorDTO.getSubmissionMethod();
		if (methodName != null && !"".equals(methodName)) {
			Class[] params = new Class[2];
			params[0] = PageRequestDTO.class;
			params[1] = PageNavigatorDTO.class;
			Method m = null;
			try {
				m = this.getClass().getDeclaredMethod(methodName, params);
				return (PageDTO) m.invoke(this, pageRequest, pageNavigatorDTO);
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				if (e.getCause() instanceof BusinessException) {
					throw ((BusinessException) e.getCause());
				} else {
					throw new BusinessException(ErrorCodeConstants.RUNTIME_EXCEPTION, ExceptionUtils.getStackTrace(e));
				}
			} catch (NoSuchMethodException nsmEx) {
				nsmEx.printStackTrace();
			} catch (SecurityException seEx) {
				seEx.printStackTrace();
			}
		}
		PageDTO pageDTO = new PageDTO();
		pageDTO.setHeader(pageRequest.getHeader());
		return pageDTO;
	}

	public PageDTO handleQuizSaveAction(PageRequestDTO pageRequest, PageNavigatorDTO pageNavigatorDTO) {
		PageDTO pageDTO = new PageDTO();
		String emailId = pageRequest.getHeader().getEmailId();
		String editionId = pageRequest.getHeader().getEditionId();
		Long studentId = pageRequest.getHeader().getStudentId();
		List<QuizScoreDTO> quizScoreDTOList = null;
		ArticleCompletionQuizPageData articleCompletionQuizPageData = new ArticleCompletionQuizPageData();
		int readingLevel = 3;
		if (studentId > 0) {
			StudentPreferencesDTO preferenceDto = preferenceBusiness.getPreferenceFromMaster(studentId);
			if (preferenceDto != null) {
				readingLevel = Integer.parseInt(preferenceDto.getReadingLevel());
			}
		}
		ArticleQuizPageData pageData = (ArticleQuizPageData) commonService.mapPageData(ArticleQuizPageData.class,
				pageRequest);
		quizScoreDTOList = populateQuizDto(pageData, studentId);
		Long newsArticleId = pageData.getNewsArticleId();
		StudentActivityDTO stdactivity = studentActivityBusiness.getActivity(studentId, newsArticleId);
		if (stdactivity == null || stdactivity.getQuizScore() == null) {
			ArticleQuizCompletionDTO articleQuizCompletionDTO = quizScoreBusiness.saveQuizScore(newsArticleId,
					quizScoreDTOList, studentId, editionId, readingLevel, emailId);
			articleCompletionQuizPageData.setNewsArticleId(articleQuizCompletionDTO.getArticleId());
			articleCompletionQuizPageData.setGenreBadge(articleQuizCompletionDTO.getGenreBadge());
			articleCompletionQuizPageData.setMonthlyBadge(articleQuizCompletionDTO.getMonthlyBadge());
			articleCompletionQuizPageData.setQuizCompletionMessage(articleQuizCompletionDTO.getQuizCompletionMessage());
		} else {
			articleCompletionQuizPageData.setQuizCompletionMessage("You have already attempted this quiz!!");
		}
		pageDTO.setData(articleCompletionQuizPageData);
		pageDTO.setHeader(pageRequest.getHeader());
		return pageDTO;
	}

	private List<QuizScoreDTO> populateQuizDto(ArticleQuizPageData pageData, Long studentId) {
		List<QuizScoreDTO> quizScoreDTOList = new ArrayList<QuizScoreDTO>();
		for (ArticleQuizAnswers data : pageData.getQuizAnswers()) {
			QuizScoreDTO dto = new QuizScoreDTO();
			dto.setStudentId(studentId);
			dto.setNewsArticleQuizId(data.getNewsArticleQuizId());
			dto.setResponse(data.getChosenOpt());
			quizScoreDTOList.add(dto);
		}
		return quizScoreDTOList;
	}

	private List<ArticleQuizQuestionsPageData> mapQuizDataWithAnswers(List<NewsArticleQuiz> newsArticleQuizList,
			Long studentId) {
		List<ArticleQuizQuestionsPageData> questions = new ArrayList<ArticleQuizQuestionsPageData>();
		for (NewsArticleQuiz quiz : newsArticleQuizList) {
			QuizScoreDTO quizScoreDTO = quizScoreBusiness.getQuizScore(studentId, quiz.getNewsArticleQuizId());
			ArticleQuizQuestionsPageData articleQuizQuestionsPageData = new ArticleQuizQuestionsPageData();
			articleQuizQuestionsPageData.setNewsArticleQuizId(quiz.getNewsArticleQuizId());
			articleQuizQuestionsPageData.setQuestion(quiz.getQuestion());
			articleQuizQuestionsPageData.setOpt1(quiz.getOpt1());
			articleQuizQuestionsPageData.setOpt2(quiz.getOpt2());
			articleQuizQuestionsPageData.setOpt3(quiz.getOpt3());
			articleQuizQuestionsPageData.setOpt4(quiz.getOpt4());
			articleQuizQuestionsPageData.setCorrectOpt(quiz.getCorrectOpt());
			if (quizScoreDTO != null) {
				articleQuizQuestionsPageData.setChosenOpt(quizScoreDTO.getResponse());
			}
			questions.add(articleQuizQuestionsPageData);
		}
		return questions;
	}
}