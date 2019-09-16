package com.enewschamp.app.savedarticle.handler;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import com.enewschamp.EnewschampApplicationProperties;
import com.enewschamp.app.article.page.dto.ArticlePageData;
import com.enewschamp.app.article.page.dto.PublicationData;
import com.enewschamp.app.common.ErrorCodes;
import com.enewschamp.app.common.PageDTO;
import com.enewschamp.app.common.PageRequestDTO;
import com.enewschamp.app.fw.page.navigation.common.PageAction;
import com.enewschamp.app.fw.page.navigation.dto.PageNavigatorDTO;
import com.enewschamp.app.student.business.StudentActivityBusiness;
import com.enewschamp.app.student.dto.StudentActivityDTO;
import com.enewschamp.article.app.dto.NewsArticleSummaryDTO;
import com.enewschamp.article.domain.service.NewsArticleService;
import com.enewschamp.domain.common.IPageHandler;
import com.enewschamp.domain.common.PageNavigationContext;
import com.enewschamp.problem.BusinessException;
import com.enewschamp.subscription.domain.business.StudentControlBusiness;
import com.enewschamp.subscription.domain.business.SubscriptionBusiness;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component(value = "SavedNewsArticlePageHandler")
public class SavedNewsArticlePageHandler implements IPageHandler {

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
	private StudentActivityBusiness studentActivityBusiness;
	@Autowired
	private NewsArticleService newsArticleService;
	
	@Override
	public PageDTO handleAction(String actionName, PageRequestDTO pageRequest) {
		return null;
	}

	@Override
	public PageDTO loadPage(PageNavigationContext pageNavigationContext) {
		PageDTO pageDto = new PageDTO();
		pageDto.setHeader(pageNavigationContext.getPageRequest().getHeader());
		String eMailId = pageNavigationContext.getPageRequest().getHeader().getEmailID();
		if(eMailId==null || "".equals(eMailId))
		{
			throw new BusinessException(ErrorCodes.INVALID_NOT_PRESENT);
			
		}
		Long studentId = studentControlBusiness.getStudentId(eMailId);
		
		String action = pageNavigationContext.getActionName();
		String editionId = pageNavigationContext.getPageRequest().getHeader().getEditionID();
		LocalDate publicationDate = pageNavigationContext.getPageRequest().getHeader().getPublicationdate();
		
		if(PageAction.savedarticles.toString().equalsIgnoreCase(action))
		{
			 studentId = studentControlBusiness.getStudentId(eMailId);
			List<StudentActivityDTO> savedArticles = studentActivityBusiness.getSavedArticles(studentId);
			for(StudentActivityDTO studentArticle:savedArticles )
			{
				//fetch the 
			}
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
		
		Long studentId = studentControlBusiness.getStudentId(eMailId);
		
		if (PageAction.like.toString().equals(actionName)) {
			ArticlePageData articlePageData = new ArticlePageData();
			articlePageData = mapPageData(articlePageData, pageRequest);
			String likeFlag = articlePageData.getLikeFlag();
			Long newsArticleId = articlePageData.getNewsArticleId();
			
			studentActivityBusiness.likeArticle(studentId, newsArticleId, likeFlag);
			
		} 
		else if(PageAction.opinion.toString().equals(actionName))
		{
			ArticlePageData articlePageData = new ArticlePageData();
			articlePageData = mapPageData(articlePageData, pageRequest);
			String opinion = articlePageData.getOpinion();
			Long newsArticleId = articlePageData.getNewsArticleId();

			studentActivityBusiness.saveOpinion(studentId, newsArticleId, opinion);
		}
		else if(PageAction.savearticle.toString().equals(actionName))
		{
			ArticlePageData articlePageData = new ArticlePageData();
			articlePageData = mapPageData(articlePageData, pageRequest);
			String saveFlag = articlePageData.getSaveFlag();
			Long newsArticleId = articlePageData.getNewsArticleId();

			studentActivityBusiness.saveArticle(studentId, newsArticleId, saveFlag);
			
		}
		pageDTO.setHeader(pageRequest.getHeader());
		return pageDTO;
	}
	private ArticlePageData mapPageData(ArticlePageData pageData, PageRequestDTO pageRequest)
	{
		try {
			pageData = objectMapper.readValue(pageRequest.getData().toString(), ArticlePageData.class);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return pageData;
	}
	
	private List<PublicationData>    updateQuizIndic(Page<NewsArticleSummaryDTO>  page, Long studentId)
	{
		List<NewsArticleSummaryDTO> pageData = page.getContent();
		List<PublicationData> publicationPageDataList = new ArrayList<PublicationData>();
		for(NewsArticleSummaryDTO article: pageData) {
			//get quiz score
			StudentActivityDTO stdactivity = studentActivityBusiness.getActivity(studentId, article.getNewsArticleId());
			PublicationData publicationata = new PublicationData();
			publicationata.setNewsArticleId(article.getNewsArticleId());
			publicationata.setImagePathMobile(article.getImagePathMobile());
			
			if(stdactivity!=null)
			{
				Long quizScore = stdactivity.getQuizScore();
				if(quizScore >0)
				{
					publicationata.setQuizCompletedIndicator(true);
				}
				else
				{
					publicationata.setQuizCompletedIndicator(false);

				}
			}
			publicationPageDataList.add(publicationata);
		}
		
		return publicationPageDataList;
	}
	
	private List<PublicationData> mapData(Page<NewsArticleSummaryDTO>  page)
	{
		
		List<PublicationData> publicationPageDataList = new ArrayList<PublicationData>();
		List<NewsArticleSummaryDTO> pageDataList = page.getContent();

		for(NewsArticleSummaryDTO article: pageDataList) {
			PublicationData pPageData = new PublicationData();
			pPageData.setNewsArticleId(article.getNewsArticleId());
			pPageData.setImagePathMobile(article.getImagePathMobile());
			pPageData.setQuizCompletedIndicator(false);
			publicationPageDataList.add(pPageData);
		}
		return publicationPageDataList;
	}
}