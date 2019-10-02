package com.enewschamp.app.article.page.handler;

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
import com.enewschamp.app.article.page.dto.PublicationPageData;
import com.enewschamp.app.common.HeaderDTO;
import com.enewschamp.app.common.PageDTO;
import com.enewschamp.app.common.PageRequestDTO;
import com.enewschamp.app.fw.page.navigation.common.PageAction;
import com.enewschamp.app.fw.page.navigation.dto.PageNavigatorDTO;
import com.enewschamp.app.savedarticle.service.SavedNewsArticleService;
import com.enewschamp.app.student.business.StudentActivityBusiness;
import com.enewschamp.app.student.dto.StudentActivityDTO;
import com.enewschamp.article.app.dto.NewsArticleSummaryDTO;
import com.enewschamp.article.domain.service.NewsArticleService;
import com.enewschamp.article.page.data.NewsArticleSearchRequest;
import com.enewschamp.domain.common.IPageHandler;
import com.enewschamp.domain.common.PageNavigationContext;
import com.enewschamp.publication.domain.service.GenreService;
import com.enewschamp.publication.domain.service.PublicationService;
import com.enewschamp.subscription.app.dto.StudentPreferencesDTO;
import com.enewschamp.subscription.domain.business.PreferenceBusiness;
import com.enewschamp.subscription.domain.business.StudentControlBusiness;
import com.enewschamp.subscription.domain.business.SubscriptionBusiness;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component(value = "PublicationPageHandler")
public class PublicationPageHandler implements IPageHandler {

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

	@Autowired
	SavedNewsArticleService savedNewsArticleService;

	@Autowired
	GenreService genreService;

	@Autowired
	PublicationService publicationService;
	@Autowired
	PreferenceBusiness preferenceBusiness;

	@Override
	public PageDTO handleAction(String actionName, PageRequestDTO pageRequest) {
		return null;
	}

	@Override
	public PageDTO loadPage(PageNavigationContext pageNavigationContext) {
		PageDTO pageDto = new PageDTO();
		String eMailId = pageNavigationContext.getPageRequest().getHeader().getEmailID();
		Long studentId = studentControlBusiness.getStudentId(eMailId);

		String action = pageNavigationContext.getActionName();
		String editionId = pageNavigationContext.getPageRequest().getHeader().getEditionID();
		LocalDate publicationDate = pageNavigationContext.getPageRequest().getHeader().getPublicationdate();
		int readingLevel = 0;
		StudentPreferencesDTO preferenceDto = preferenceBusiness.getPreferenceFromMaster(studentId);

		if (preferenceDto != null) {

			readingLevel = Integer.parseInt(preferenceDto.getReadingLevel());
		}
		if (PageAction.load.toString().equalsIgnoreCase(action) || PageAction.home.toString().equalsIgnoreCase(action)
				|| PageAction.back.toString().equalsIgnoreCase(action)
				|| PageAction.save.toString().equalsIgnoreCase(action)
				|| PageAction.Cancel.toString().equalsIgnoreCase(action)) {
			System.out.println("Inside load in Publication Handler");
			NewsArticleSearchRequest searchRequestData = new NewsArticleSearchRequest();

			// if publication date is null, load the latest publication... TO DO
			if (publicationDate == null) {

				publicationDate = publicationService.getLatestPublication(editionId, readingLevel);

			}
			// if page number is not passed, load the first page
			if (pageNavigationContext.getPageRequest().getHeader().getPageNo() == null) {
				pageNavigationContext.getPageRequest().getHeader().setPageNo(0);

			}
			// if page size is not passed, load from config.
			if (pageNavigationContext.getPageRequest().getHeader().getPageSize() == null) {
				pageNavigationContext.getPageRequest().getHeader().setPageSize(appConfig.getPageSize());

			}

			searchRequestData.setEditionId(editionId);
			searchRequestData.setPublicationDate(publicationDate);
			Page<NewsArticleSummaryDTO> pageResult = newsArticleService.findArticles(searchRequestData,
					pageNavigationContext.getPageRequest().getHeader());

			HeaderDTO header = pageNavigationContext.getPageRequest().getHeader();
			header.setIsLastPage(pageResult.isLast());
			header.setPageCount(pageResult.getTotalPages());
			header.setRecordCount(pageResult.getNumberOfElements());
			header.setPageNo(pageResult.getNumber() + 1);
			pageDto.setHeader(header);

			List<PublicationData> publicationData = null;
			PublicationPageData pageData = new PublicationPageData();

			// update the quizcompletionIndicator
			if (studentId != null) {
				publicationData = updateQuizIndic(pageResult, studentId);

			} else {
				publicationData = mapData(pageResult);
			}
			pageData.setNewsArticles(publicationData);

			pageDto.setData(pageData);

			// searchResult.setNewsArticlesSummary(pageResult.getContent());

		}
		if (PageAction.upswipe.toString().equalsIgnoreCase(action)) {
			NewsArticleSearchRequest searchRequestData = new NewsArticleSearchRequest();
			searchRequestData.setEditionId(editionId);

			// get the articles for next publication date
			// LocalDate newDate = publicationDate.plusDays(1);

			LocalDate newDate = publicationService.getNextAvailablePublicationDate(publicationDate, editionId,
					readingLevel);
			searchRequestData.setPublicationDate(publicationDate);
			Page<NewsArticleSummaryDTO> pageResult = newsArticleService.findArticles(searchRequestData,
					pageNavigationContext.getPageRequest().getHeader());

			HeaderDTO header = pageNavigationContext.getPageRequest().getHeader();
			header.setIsLastPage(pageResult.isLast());
			header.setPageCount(pageResult.getTotalPages());
			header.setRecordCount(pageResult.getNumberOfElements());
			header.setPageNo(pageResult.getNumber() + 1);
			header.setPublicationdate(newDate);
			pageDto.setHeader(header);

			List<PublicationData> publicationData = null;
			PublicationPageData pageData = new PublicationPageData();

			// update the quizcompletionIndicator
			if (studentId != null) {
				publicationData = updateQuizIndic(pageResult, studentId);

			} else {
				publicationData = mapData(pageResult);
			}
			pageData.setNewsArticles(publicationData);

			pageDto.setData(pageData);

		}
		if (PageAction.downswipe.toString().equalsIgnoreCase(action)) {
			NewsArticleSearchRequest searchRequestData = new NewsArticleSearchRequest();
			searchRequestData.setEditionId(editionId);

			// get the articles for previous publication date
			// LocalDate newDate = publicationDate.minusDays(1);
			LocalDate newDate = publicationService.getPreviousAvailablePublicationDate(publicationDate, editionId,
					readingLevel);
			searchRequestData.setPublicationDate(newDate);
			Page<NewsArticleSummaryDTO> pageResult = newsArticleService.findArticles(searchRequestData,
					pageNavigationContext.getPageRequest().getHeader());

			HeaderDTO header = pageNavigationContext.getPageRequest().getHeader();
			header.setIsLastPage(pageResult.isLast());
			header.setPageCount(pageResult.getTotalPages());
			header.setRecordCount(pageResult.getNumberOfElements());
			header.setPageNo(pageResult.getNumber() + 1);
			header.setPublicationdate(newDate);
			pageDto.setHeader(header);

			List<PublicationData> publicationData = null;
			PublicationPageData pageData = new PublicationPageData();

			// update the quizcompletionIndicator
			if (studentId != null) {
				publicationData = updateQuizIndic(pageResult, studentId);

			} else {
				publicationData = mapData(pageResult);
			}
			pageData.setNewsArticles(publicationData);

			pageDto.setData(pageData);
		}
		if (PageAction.next.toString().equalsIgnoreCase(action)
				|| PageAction.LeftSwipe.toString().equalsIgnoreCase(action)) {

			NewsArticleSearchRequest searchRequestData = new NewsArticleSearchRequest();
			searchRequestData.setEditionId(editionId);
			int pageNumber = pageNavigationContext.getPageRequest().getHeader().getPageNo();
			pageNumber = pageNumber + 1;
			pageNavigationContext.getPageRequest().getHeader().setPageNo(pageNumber);

			searchRequestData.setPublicationDate(publicationDate);
			Page<NewsArticleSummaryDTO> pageResult = newsArticleService.findArticles(searchRequestData,
					pageNavigationContext.getPageRequest().getHeader());

			HeaderDTO header = pageNavigationContext.getPageRequest().getHeader();
			header.setIsLastPage(pageResult.isLast());
			header.setPageCount(pageResult.getTotalPages());
			header.setRecordCount(pageResult.getNumberOfElements());
			header.setPageNo(pageResult.getNumber() + 1);
			pageDto.setHeader(header);

			List<PublicationData> publicationData = null;
			PublicationPageData pageData = new PublicationPageData();

			// update the quizcompletionIndicator
			if (studentId != null) {
				publicationData = updateQuizIndic(pageResult, studentId);

			} else {
				publicationData = mapData(pageResult);
			}
			pageData.setNewsArticles(publicationData);

			pageDto.setData(pageData);

		}
		if (PageAction.previous.toString().equalsIgnoreCase(action)
				|| PageAction.RightSwipe.toString().equalsIgnoreCase(action)) {

			NewsArticleSearchRequest searchRequestData = new NewsArticleSearchRequest();
			searchRequestData.setEditionId(editionId);
			int pageNumber = pageNavigationContext.getPageRequest().getHeader().getPageNo();
			pageNumber = pageNumber - 1;
			if (pageNumber < 0) {
				pageNumber = 0;
			}
			pageNavigationContext.getPageRequest().getHeader().setPageNo(pageNumber);

			searchRequestData.setPublicationDate(publicationDate);
			Page<NewsArticleSummaryDTO> pageResult = newsArticleService.findArticles(searchRequestData,
					pageNavigationContext.getPageRequest().getHeader());

			HeaderDTO header = pageNavigationContext.getPageRequest().getHeader();
			header.setIsLastPage(pageResult.isLast());
			header.setPageCount(pageResult.getTotalPages());
			header.setRecordCount(pageResult.getNumberOfElements());
			header.setPageNo(pageResult.getNumber() + 1);
			pageDto.setHeader(header);

			List<PublicationData> publicationData = null;
			PublicationPageData pageData = new PublicationPageData();

			// update the quizcompletionIndicator
			if (studentId != null) {
				publicationData = updateQuizIndic(pageResult, studentId);

			} else {
				publicationData = mapData(pageResult);
			}
			pageData.setNewsArticles(publicationData);

			pageDto.setData(pageData);
		}
		pageDto.setHeader(pageNavigationContext.getPageRequest().getHeader());

		return pageDto;
	}

	@Override
	public PageDTO saveAsMaster(String actionName, PageRequestDTO pageRequest) {
		return null;
	}

	@Override
	public PageDTO handleAppAction(String actionName, PageRequestDTO pageRequest, PageNavigatorDTO pageNavigatorDTO) {
		return null;
	}

	private List<PublicationData> mapData(Page<NewsArticleSummaryDTO> page) {

		List<PublicationData> publicationPageDataList = new ArrayList<PublicationData>();
		List<NewsArticleSummaryDTO> pageDataList = page.getContent();

		for (NewsArticleSummaryDTO article : pageDataList) {
			PublicationData pPageData = new PublicationData();
			pPageData.setNewsArticleId(article.getNewsArticleId());
			pPageData.setImagePathMobile(article.getImagePathMobile());
			pPageData.setQuizCompletedIndicator(false);
			pPageData.setGenreID(article.getGenreId());
			pPageData.setHeadline(article.getHeadLine());

			publicationPageDataList.add(pPageData);
		}
		return publicationPageDataList;
	}

	private ArticlePageData mapPageData(ArticlePageData pageData, PageRequestDTO pageRequest) {
		try {
			pageData = objectMapper.readValue(pageRequest.getData().toString(), ArticlePageData.class);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return pageData;
	}

	private List<PublicationData> updateQuizIndic(Page<NewsArticleSummaryDTO> page, Long studentId) {
		List<NewsArticleSummaryDTO> pageData = page.getContent();
		List<PublicationData> publicationPageDataList = new ArrayList<PublicationData>();
		for (NewsArticleSummaryDTO article : pageData) {
			// get quiz score
			StudentActivityDTO stdactivity = studentActivityBusiness.getActivity(studentId, article.getNewsArticleId());
			PublicationData publicationata = new PublicationData();
			publicationata.setNewsArticleId(article.getNewsArticleId());
			publicationata.setImagePathMobile(article.getImagePathMobile());
			publicationata.setGenreID(article.getGenreId());
			publicationata.setHeadline(article.getHeadLine());

			if (stdactivity != null) {
				Long quizScore = stdactivity.getQuizScore();
				if (quizScore > 0) {
					publicationata.setQuizCompletedIndicator(true);
				} else {
					publicationata.setQuizCompletedIndicator(false);

				}
			}
			publicationPageDataList.add(publicationata);
		}

		return publicationPageDataList;
	}

}
