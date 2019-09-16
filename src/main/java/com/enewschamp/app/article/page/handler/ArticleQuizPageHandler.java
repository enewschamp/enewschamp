package com.enewschamp.app.article.page.handler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enewschamp.EnewschampApplicationProperties;
import com.enewschamp.app.article.page.dto.ArticleCompletionQuizPageData;
import com.enewschamp.app.article.page.dto.ArticleQuizAnswers;
import com.enewschamp.app.article.page.dto.ArticleQuizCompletionDTO;
import com.enewschamp.app.article.page.dto.ArticleQuizDetailsPageData;
import com.enewschamp.app.article.page.dto.ArticleQuizPageData;
import com.enewschamp.app.article.page.dto.ArticleQuizQuestionsPageData;
import com.enewschamp.app.common.PageDTO;
import com.enewschamp.app.common.PageRequestDTO;
import com.enewschamp.app.fw.page.navigation.common.PageAction;
import com.enewschamp.app.fw.page.navigation.dto.PageNavigatorDTO;
import com.enewschamp.app.student.quiz.business.QuizScoreBusiness;
import com.enewschamp.app.student.quiz.dto.QuizScoreDTO;
import com.enewschamp.article.domain.entity.NewsArticleQuiz;
import com.enewschamp.article.domain.service.NewsArticleQuizService;
import com.enewschamp.domain.common.IPageHandler;
import com.enewschamp.domain.common.PageNavigationContext;
import com.enewschamp.master.badge.entity.Badge;
import com.enewschamp.master.badge.service.BadgeService;
import com.enewschamp.subscription.domain.business.StudentControlBusiness;
import com.enewschamp.subscription.domain.business.SubscriptionBusiness;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component(value = "ArticleQuizPageHandler")
public class ArticleQuizPageHandler implements IPageHandler {

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	SubscriptionBusiness subscriptionBusiness;

	@Autowired
	StudentControlBusiness studentControlBusiness;

	@Autowired
	ObjectMapper objectMapper;

	@Autowired
	private EnewschampApplicationProperties appConfig;

	@Autowired
	QuizScoreBusiness quizScoreBusiness;
	
	@Autowired
	NewsArticleQuizService newsArticleQuizService;
	
	@Autowired
	BadgeService badgeService;
	
	@Override
	public PageDTO handleAction(String actionName, PageRequestDTO pageRequest) {
		return null;
	}

	@Override
	public PageDTO loadPage(PageNavigationContext pageNavigationContext) {
		PageDTO pageDto = new PageDTO();
		pageDto.setHeader(pageNavigationContext.getPageRequest().getHeader());
		String action = pageNavigationContext.getActionName();
		String eMailId = pageNavigationContext.getPageRequest().getHeader().getEmailID();

		if(PageAction.quiz.toString().equalsIgnoreCase(action) || PageAction.ok.toString().equalsIgnoreCase(action))
		{
			Long studentId = studentControlBusiness.getStudentId(eMailId);

			//load the article quiz
			ArticleQuizDetailsPageData pageData = new ArticleQuizDetailsPageData();
			pageData = mapPageDataOnLoad(pageData,pageNavigationContext.getPageRequest());
			
			Long newsArticleId= pageData.getNewsArticleId();
			
			List<NewsArticleQuiz> newsArticleQuizList= newsArticleQuizService.getByNewsArticleId(newsArticleId);
			List<ArticleQuizQuestionsPageData> qList = mapQuizDataWithAnswers(newsArticleQuizList,studentId);
			pageData.setQuizQuestions(qList);
			pageDto.setData(pageData);
		}
		else {
			pageDto.setData(pageNavigationContext.getPreviousPageResponse().getData());
		}
		return pageDto;
	}

	@Override
	public PageDTO saveAsMaster(String actionName, PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
	
		return pageDto;
	}

	@Override
	public PageDTO handleAppAction(String actionName, PageRequestDTO pageRequest, PageNavigatorDTO pageNavigatorDTO) {
		PageDTO pageDTO = new PageDTO();
		String eMailId = pageRequest.getHeader().getEmailID();
		String editionId =  pageRequest.getHeader().getEditionID();
		List<QuizScoreDTO> quizScoreDTOList = null;
		
		if (PageAction.save.toString().equals(actionName)) {
			Long studentId = studentControlBusiness.getStudentId(eMailId);
			ArticleQuizPageData pageData = new ArticleQuizPageData();
			pageData = mapPageData(pageData,pageRequest);
			quizScoreDTOList = populateQuizDto(pageData,studentId);
			Long newsArticleId= pageData.getNewsArticleId();
			
			ArticleQuizCompletionDTO articleQuizCompletionDTO = quizScoreBusiness.saveQuizScore(newsArticleId,quizScoreDTOList, studentId,editionId);
			
			ArticleCompletionQuizPageData articleCompletionQuizPageData = new ArticleCompletionQuizPageData();
			articleCompletionQuizPageData.setNewsArticleId(articleQuizCompletionDTO.getArticleId());
			articleCompletionQuizPageData.setNewBadge(articleQuizCompletionDTO.getNewBadge());
			articleCompletionQuizPageData.setNewBadgeName(articleQuizCompletionDTO.getBadgeName());
			articleCompletionQuizPageData.setQuizCompletionMessage(articleQuizCompletionDTO.getQuizCompletionMessage());
			System.out.println("articleCompletionQuizPageData "+articleCompletionQuizPageData.getNewBadgeName());
			
			pageDTO.setData(articleCompletionQuizPageData);
		} 
		else if(PageAction.previous.toString().equals(actionName))
		{
				// TO Do may want to delete the data from work table
		}
		pageDTO.setHeader(pageRequest.getHeader());
		return pageDTO;
	}
	
	private ArticleQuizPageData mapPageData(ArticleQuizPageData pageData, PageRequestDTO pageRequest)
	{
		try {
			pageData = objectMapper.readValue(pageRequest.getData().toString(), ArticleQuizPageData.class);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return pageData;
	}
	private ArticleQuizDetailsPageData mapPageDataOnLoad(ArticleQuizDetailsPageData pageData, PageRequestDTO pageRequest)
	{
		try {
			pageData = objectMapper.readValue(pageRequest.getData().toString(), ArticleQuizDetailsPageData.class);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return pageData;
	}
	private List<QuizScoreDTO> populateQuizDto(ArticleQuizPageData pageData, Long studentId)
	{
		List<QuizScoreDTO> quizScoreDTOList = new ArrayList<QuizScoreDTO>();
		Long newArticleId = pageData.getNewsArticleId();
		for(ArticleQuizAnswers data:pageData.getQuizAnswers())
		{
			QuizScoreDTO dto = new QuizScoreDTO();
			dto.setStudentId(studentId);
			dto.setNewsArticleQuizId(data.getNewsArticleQuizId());
			dto.setResponse(data.getChosenOptSeq());
			quizScoreDTOList.add(dto);
		}
		return quizScoreDTOList;
	}
	
	private List<ArticleQuizQuestionsPageData> mapQuizDataWithAnswers(List<NewsArticleQuiz> newsArticleQuizList, Long studentId)
	{
		List<ArticleQuizQuestionsPageData> questions = new ArrayList<ArticleQuizQuestionsPageData>();
		for(NewsArticleQuiz quiz: newsArticleQuizList)
		{
			//check if the question is already answered..
			QuizScoreDTO quizScoreDTO = quizScoreBusiness.getQuizScore(studentId, quiz.getNewsArticleQuizId());
			
			ArticleQuizQuestionsPageData articleQuizQuestionsPageData = new ArticleQuizQuestionsPageData();
			articleQuizQuestionsPageData.setNewsArticleQuizId( quiz.getNewsArticleQuizId());
			articleQuizQuestionsPageData.setQuestion(quiz.getQuestion());
			articleQuizQuestionsPageData.setOpt1(quiz.getOpt1());
			articleQuizQuestionsPageData.setOpt2(quiz.getOpt2());
			articleQuizQuestionsPageData.setOpt3(quiz.getOpt3());
			articleQuizQuestionsPageData.setOpt4(quiz.getOpt4());
			
			if(quizScoreDTO!=null) {
				Long selectedOpt = quizScoreDTO.getResponse();
				articleQuizQuestionsPageData.setChosenOptSeq(selectedOpt);
			}
			articleQuizQuestionsPageData.setCorrectOptSeq(quiz.getCorrectOpt());
			
			questions.add(articleQuizQuestionsPageData);

		}
		return questions;
	}
}