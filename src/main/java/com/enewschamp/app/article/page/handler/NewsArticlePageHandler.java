package com.enewschamp.app.article.page.handler;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import com.enewschamp.EnewschampApplicationProperties;
import com.enewschamp.app.article.page.dto.ArticlePageData;
import com.enewschamp.app.article.page.dto.NewsArticlePageData;
import com.enewschamp.app.article.page.dto.PublicationData;
import com.enewschamp.app.article.page.dto.PublicationPageData;
import com.enewschamp.app.common.HeaderDTO;
import com.enewschamp.app.common.PageDTO;
import com.enewschamp.app.common.PageRequestDTO;
import com.enewschamp.app.fw.page.navigation.common.PageAction;
import com.enewschamp.app.fw.page.navigation.dto.PageNavigatorDTO;
import com.enewschamp.app.savedarticle.dto.MonthsLovData;
import com.enewschamp.app.savedarticle.dto.SavedArticleData;
import com.enewschamp.app.savedarticle.dto.SavedArticlePageData;
import com.enewschamp.app.savedarticle.dto.SavedNewsArticleSearchRequest;
import com.enewschamp.app.savedarticle.dto.SavedNewsArticleSummaryDTO;
import com.enewschamp.app.savedarticle.service.SavedNewsArticleService;
import com.enewschamp.app.student.business.StudentActivityBusiness;
import com.enewschamp.app.student.dto.StudentActivityDTO;
import com.enewschamp.article.app.dto.NewsArticleSummaryDTO;
import com.enewschamp.article.domain.common.ArticleType;
import com.enewschamp.article.domain.service.NewsArticleService;
import com.enewschamp.article.page.data.NewsArticleSearchRequest;
import com.enewschamp.article.page.data.NewsArticleSearchResultData;
import com.enewschamp.domain.common.IPageHandler;
import com.enewschamp.domain.common.PageNavigationContext;
import com.enewschamp.publication.domain.service.GenreService;
import com.enewschamp.subscription.domain.business.StudentControlBusiness;
import com.enewschamp.subscription.domain.business.SubscriptionBusiness;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component(value = "NewsArticlePageHandler")
public class NewsArticlePageHandler implements IPageHandler {

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

	@Override
	public PageDTO handleAction(String actionName, PageRequestDTO pageRequest) {
		return null;
	}

	@Override
	public PageDTO loadPage(PageNavigationContext pageNavigationContext) {
		PageDTO pageDto = new PageDTO();
		pageDto.setHeader(pageNavigationContext.getPageRequest().getHeader());
		String eMailId = pageNavigationContext.getPageRequest().getHeader().getEmailID();
		Long studentId = studentControlBusiness.getStudentId(eMailId);

		String action = pageNavigationContext.getActionName();
		String editionId = pageNavigationContext.getPageRequest().getHeader().getEditionID();
		LocalDate publicationDate = pageNavigationContext.getPageRequest().getHeader().getPublicationdate();
		if (PageAction.load.toString().equalsIgnoreCase(action)
				|| PageAction.home.toString().equalsIgnoreCase(action)) {
			NewsArticleSearchRequest searchRequestData = new NewsArticleSearchRequest();
			searchRequestData.setEditionId(editionId);
			searchRequestData.setPublicationDate(publicationDate);

			NewsArticleSearchResultData searchResult = new NewsArticleSearchResultData();
			// pageDto.setData(searchResult);

			// Load News and Events..
			List<ArticleType> articleTypeList = new ArrayList<ArticleType>();
			articleTypeList.add(ArticleType.Article);

			searchRequestData.setArticleTypeList(articleTypeList);

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

		} else if (PageAction.clickArticleImage.toString().equalsIgnoreCase(action)) {
			ArticlePageData articlePageData = new ArticlePageData();
			articlePageData = mapPageData(articlePageData, pageNavigationContext.getPageRequest());

			NewsArticleSearchRequest searchRequestData = new NewsArticleSearchRequest();
			searchRequestData.setArticleId(articlePageData.getNewsArticleId());
			// Load News and Events..
			List<ArticleType> articleTypeList = new ArrayList<ArticleType>();
			articleTypeList.add(ArticleType.Article);

			searchRequestData.setArticleTypeList(articleTypeList);
			Page<NewsArticleSummaryDTO> pageResult = newsArticleService.findArticles(searchRequestData,
					pageNavigationContext.getPageRequest().getHeader());

			HeaderDTO header = pageNavigationContext.getPageRequest().getHeader();
			header.setIsLastPage(pageResult.isLast());
			header.setPageCount(pageResult.getTotalPages());
			header.setRecordCount(pageResult.getNumberOfElements());
			header.setPageNo(pageResult.getNumber() + 1);
			pageDto.setHeader(header);
			NewsArticlePageData newsArticlePageData = new NewsArticlePageData();
			if (!pageResult.getContent().isEmpty()) {
				NewsArticleSummaryDTO newsArticleSummary = pageResult.getContent().get(0);
				newsArticlePageData.setNewsArticleId(newsArticleSummary.getNewsArticleId());
				newsArticlePageData.setAuthorId(newsArticleSummary.getAuthorId());
				newsArticlePageData.setHeadline(newsArticleSummary.getHeadLine());
				newsArticlePageData.setGenreId(newsArticleSummary.getGenreId());
				newsArticlePageData.setImage(newsArticleSummary.getImagePathMobile());
				if (studentId != null && studentId != 0) {
					StudentActivityDTO stdactivity = studentActivityBusiness.getActivity(studentId,
							newsArticleSummary.getNewsArticleId());
					newsArticlePageData.setOpitionText(stdactivity.getOpinion());
					newsArticlePageData.setReactionType(stdactivity.getLikeLevel());
					newsArticlePageData.setSaved(stdactivity.getSaved());

				} else {
					newsArticlePageData.setOpitionText("");
					newsArticlePageData.setReactionType("");
					newsArticlePageData.setSaved("false");
				}

			}
			pageDto.setData(newsArticlePageData);

		} else if (PageAction.savedarticles.toString().equalsIgnoreCase(action)
				|| PageAction.back.toString().equalsIgnoreCase(action)
				|| PageAction.FilterSavedArticles.toString().equalsIgnoreCase(action)
				|| PageAction.ClearFilterSavedArticles.toString().equalsIgnoreCase(action)) {
			ArticlePageData articlePageData = new ArticlePageData();
			articlePageData = mapPageData(articlePageData, pageNavigationContext.getPageRequest());

			studentId = studentControlBusiness.getStudentId(eMailId);
			// List<StudentActivityDTO> savedArticles =
			// studentActivityBusiness.getSavedArticles(studentId);
			SavedNewsArticleSearchRequest searchRequestData = new SavedNewsArticleSearchRequest();
			searchRequestData.setEditionId(editionId);
			searchRequestData.setGenreId(articlePageData.getGenreId());
			searchRequestData.setHeadline(articlePageData.getHeadlineKeyWord());
			searchRequestData.setStudentId(studentId);
			searchRequestData.setPublishMonth(articlePageData.getPublishMonth());

			Page<SavedNewsArticleSummaryDTO> pageResult = savedNewsArticleService.findSavedArticles(searchRequestData,
					pageNavigationContext.getPageRequest().getHeader());
			HeaderDTO header = pageNavigationContext.getPageRequest().getHeader();
			header.setIsLastPage(pageResult.isLast());
			header.setPageCount(pageResult.getTotalPages());
			header.setRecordCount(pageResult.getNumberOfElements());
			header.setPageNo(pageResult.getNumber() + 1);
			pageDto.setHeader(header);

			List<SavedNewsArticleSummaryDTO> savedArticleSummaryList = pageResult.getContent();
			SavedArticlePageData savedPageData = new SavedArticlePageData();
			List<SavedArticleData> savedArticleList = new ArrayList<SavedArticleData>();
			List<StudentActivityDTO> savedArticlesList = studentActivityBusiness.getSavedArticles(studentId);

			for (SavedNewsArticleSummaryDTO dto : savedArticleSummaryList) {
				Long savedArticleId = 0L;
				if (savedArticlesList != null && !savedArticlesList.isEmpty()) {
					for (StudentActivityDTO studentSavedArticles : savedArticlesList) {
						savedArticleId = studentSavedArticles.getNewsArticleId();
					}
					if (dto.getNewsArticleId() == savedArticleId) {
						SavedArticleData savedData = new SavedArticleData();
						savedData.setGenreId(dto.getGenreId());

						savedData.setHeadline(dto.getHeadLine());
						savedData.setPublicationDate(dto.getPublicationDate());
						savedData.setNewsArticleId(dto.getNewsArticleId());

						savedArticleList.add(savedData);
					}
				}

			}
			savedPageData.setSavedNewsArticles(savedArticleList);
			savedPageData.setGenreLOV(genreService.getLOV());
			savedPageData.setMonthsLOV(getMonthsLov());

			// savedPageData.setMonthsLOV(MonthType.getLOV());
			// SavedArticlePageData pageData = new SavedArticlePageData();
			// pageData.setSavedNewsArticles(savedNewsArticles);
			pageDto.setData(savedPageData);
		}
		if (PageAction.next.toString().equalsIgnoreCase(action)
				|| PageAction.upswipe.toString().equalsIgnoreCase(action)
				|| PageAction.LeftSwipe.toString().equalsIgnoreCase(action)) {

			NewsArticleSearchRequest searchRequestData = new NewsArticleSearchRequest();
			ArticlePageData articlePageData = new ArticlePageData();
			articlePageData = mapPageData(articlePageData, pageNavigationContext.getPageRequest());
			searchRequestData.setEditionId(editionId);
			searchRequestData.setPublicationDate(publicationDate);
			int pageNumber = pageNavigationContext.getPageRequest().getHeader().getPageNo();
			pageNumber = pageNumber + 1;
			pageNavigationContext.getPageRequest().getHeader().setPageNo(pageNumber);

			NewsArticleSearchResultData searchResult = new NewsArticleSearchResultData();
			// pageDto.setData(searchResult);
			searchRequestData.setEditionId(editionId);
			searchRequestData.setGenreId(articlePageData.getGenreId());
			searchRequestData.setHeadline(articlePageData.getHeadlineKeyWord());
			// searchRequestData.setStudentId(studentId);
			// searchRequestData.setPublishMonth(articlePageData.getPublishMonth());
			// Load News and Events..
			List<ArticleType> articleTypeList = new ArrayList<ArticleType>();
			articleTypeList.add(ArticleType.Article);

			searchRequestData.setArticleTypeList(articleTypeList);
			Page<NewsArticleSummaryDTO> pageResult = newsArticleService.findArticles(searchRequestData,
					pageNavigationContext.getPageRequest().getHeader());
			System.out.println("Data in pageResult " + pageResult.getContent());

			HeaderDTO header = pageNavigationContext.getPageRequest().getHeader();
			header.setIsLastPage(pageResult.isLast());
			header.setPageCount(pageResult.getTotalPages());
			header.setRecordCount(pageResult.getNumberOfElements());
			header.setPageNo(pageNumber);
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
		if (PageAction.previous.toString().equalsIgnoreCase(action)
				|| PageAction.RightSwipe.toString().equalsIgnoreCase(action)) {

			NewsArticleSearchRequest searchRequestData = new NewsArticleSearchRequest();
			searchRequestData.setEditionId(editionId);
			searchRequestData.setPublicationDate(publicationDate);
			int pageNumber = pageNavigationContext.getPageRequest().getHeader().getPageNo();
			pageNumber = pageNumber - 1;
			if (pageNumber < 0) {
				pageNumber = 0;
			}
			pageNavigationContext.getPageRequest().getHeader().setPageNo(pageNumber);

			NewsArticleSearchResultData searchResult = new NewsArticleSearchResultData();
			// pageDto.setData(searchResult);
			// Load News and Events..
			List<ArticleType> articleTypeList = new ArrayList<ArticleType>();
			articleTypeList.add(ArticleType.Article);

			searchRequestData.setArticleTypeList(articleTypeList);
			Page<NewsArticleSummaryDTO> pageResult = newsArticleService.findArticles(searchRequestData,
					pageNavigationContext.getPageRequest().getHeader());

			HeaderDTO header = pageNavigationContext.getPageRequest().getHeader();
			header.setIsLastPage(pageResult.isLast());
			header.setPageCount(pageResult.getTotalPages());
			header.setRecordCount(pageResult.getNumberOfElements());
			header.setPageNo(pageNumber);
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
		return pageDto;
	}

	private List<MonthsLovData> getMonthsLov() {
		List<MonthsLovData> monthsLovList = new ArrayList<MonthsLovData>();
		LocalDate currdate = LocalDate.now();
		// LocalDate startDate = currdate.minusMonths(appConfig.getMonthLov());
		for (int i = 0; i < appConfig.getMonthLov(); i++) {
			String key = currdate.toString();
			String value = currdate.format(DateTimeFormatter.ofPattern(appConfig.getMonthLovFormat()));
			MonthsLovData monthlov = new MonthsLovData();
			monthlov.setKey(key);
			monthlov.setValue(value);
			monthsLovList.add(monthlov);
			currdate = currdate.minusMonths(1);
		}

		return monthsLovList;
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

		} else if (PageAction.opinion.toString().equals(actionName)) {
			ArticlePageData articlePageData = new ArticlePageData();
			articlePageData = mapPageData(articlePageData, pageRequest);
			String opinion = articlePageData.getOpinion();
			Long newsArticleId = articlePageData.getNewsArticleId();

			studentActivityBusiness.saveOpinion(studentId, newsArticleId, opinion);
		} else if (PageAction.savearticle.toString().equals(actionName)) {
			ArticlePageData articlePageData = new ArticlePageData();
			articlePageData = mapPageData(articlePageData, pageRequest);
			String saveFlag = articlePageData.getSaveFlag();
			Long newsArticleId = articlePageData.getNewsArticleId();

			studentActivityBusiness.saveArticle(studentId, newsArticleId, saveFlag);

		}
		pageDTO.setHeader(pageRequest.getHeader());
		return pageDTO;
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

	private List<PublicationData> mapData(Page<NewsArticleSummaryDTO> page) {

		List<PublicationData> publicationPageDataList = new ArrayList<PublicationData>();
		List<NewsArticleSummaryDTO> pageDataList = page.getContent();

		for (NewsArticleSummaryDTO article : pageDataList) {
			PublicationData pPageData = new PublicationData();
			pPageData.setNewsArticleId(article.getNewsArticleId());
			pPageData.setImagePathMobile(article.getImagePathMobile());
			pPageData.setQuizCompletedIndicator(false);
			publicationPageDataList.add(pPageData);
		}
		return publicationPageDataList;
	}
}
