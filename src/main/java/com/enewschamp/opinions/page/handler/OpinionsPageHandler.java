package com.enewschamp.opinions.page.handler;

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
import com.enewschamp.app.article.page.dto.PublicationData;
import com.enewschamp.app.common.ErrorCodes;
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
import com.enewschamp.article.app.dto.NewsArticleSummaryDTO;
import com.enewschamp.article.domain.service.NewsArticleService;
import com.enewschamp.domain.common.IPageHandler;
import com.enewschamp.domain.common.MonthType;
import com.enewschamp.domain.common.PageNavigationContext;
import com.enewschamp.problem.BusinessException;
import com.enewschamp.publication.domain.service.GenreService;
import com.enewschamp.subscription.domain.business.StudentControlBusiness;
import com.enewschamp.subscription.domain.business.SubscriptionBusiness;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component(value = "OpinionsPageHandler")
public class OpinionsPageHandler implements IPageHandler {

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
		System.out.println("OpinionsPageHandler  loadPage()");
		PageDTO pageDto = new PageDTO();
		// if page number is not passed, load the first page
		if (pageNavigationContext.getPageRequest().getHeader().getPageNo() == null) {
			pageNavigationContext.getPageRequest().getHeader().setPageNo(0);

		}
		int pageNo = pageNavigationContext.getPageRequest().getHeader().getPageNo();
		pageDto.setHeader(pageNavigationContext.getPageRequest().getHeader());
		String eMailId = pageNavigationContext.getPageRequest().getHeader().getEmailId();
		Long studentId = studentControlBusiness.getStudentId(eMailId);
		if (studentId == null || studentId == 0L) {
			throw new BusinessException(ErrorCodes.STUDENT_DTLS_NOT_FOUND);

		}
		String action = pageNavigationContext.getActionName();
		String editionId = pageNavigationContext.getPageRequest().getHeader().getEditionId();
		LocalDate publicationDate = pageNavigationContext.getPageRequest().getHeader().getPublicationDate();

		if (PageAction.upswipe.toString().equalsIgnoreCase(action)
				|| PageAction.next.toString().equalsIgnoreCase(action)) {
			pageNo++;
			pageNavigationContext.getPageRequest().getHeader().setPageNo(pageNo);
		} else if (PageAction.previous.toString().equalsIgnoreCase(action)
				|| PageAction.RightSwipe.toString().equalsIgnoreCase(action)) {
			if (pageNo > 0) {
				pageNo--;
				pageNavigationContext.getPageRequest().getHeader().setPageNo(pageNo);
			}
		}
		// if(PageAction.home.toString().equalsIgnoreCase(action) )
		// {
		ArticlePageData articlePageData = new ArticlePageData();
		articlePageData = mapPageData(articlePageData, pageNavigationContext.getPageRequest());

		studentId = studentControlBusiness.getStudentId(eMailId);
		// List<StudentActivityDTO> savedArticles =
		// studentActivityBusiness.getSavedArticles(studentId);
		SavedNewsArticleSearchRequest searchRequestData = new SavedNewsArticleSearchRequest();
		searchRequestData.setEditionId(editionId);
		searchRequestData.setGenreId(articlePageData.getGenreId());
		searchRequestData.setHeadline(articlePageData.getHeadlineKeyWord());
		searchRequestData.setPublishMonth(articlePageData.getPublishMonth());
		searchRequestData.setPublicationDate(publicationDate);

		searchRequestData.setStudentId(studentId);

		Page<SavedNewsArticleSummaryDTO> pageResult = savedNewsArticleService.findSavedArticles(searchRequestData,
				pageNavigationContext.getPageRequest().getHeader());
		HeaderDTO header = pageNavigationContext.getPageRequest().getHeader();
		header.setIsLastPage(pageResult.isLast());
		header.setPageCount(pageResult.getTotalPages());
		header.setRecordCount(pageResult.getNumberOfElements());
		header.setPageNo(pageNo);
		pageDto.setHeader(header);

		List<SavedNewsArticleSummaryDTO> savedArticleSummaryList = pageResult.getContent();
		SavedArticlePageData savedPageData = new SavedArticlePageData();
		List<SavedArticleData> savedArticleList = new ArrayList<SavedArticleData>();

		for (SavedNewsArticleSummaryDTO dto : savedArticleSummaryList) {
			if (dto.getOpinionText() != null) {
				SavedArticleData savedData = new SavedArticleData();
				savedData.setGenreId(dto.getGenreId());

				savedData.setHeadline(dto.getHeadLine());
				savedData.setPublicationDate(dto.getPublicationDate());
				savedData.setNewsArticleId(dto.getNewsArticleId());
				savedData.setOpinionText(dto.getOpinionText());

				savedArticleList.add(savedData);
			}

		}
		savedPageData.setGenreLOV(genreService.getLOV());

		// savedPageData.setMonthsLOV(MonthType.getLOV());
		savedPageData.setMonthsLOV(getMonthsLov());

		savedPageData.setSavedNewsArticles(savedArticleList);

		// SavedArticlePageData pageData = new SavedArticlePageData();
		// pageData.setSavedNewsArticles(savedNewsArticles);
		pageDto.setData(savedPageData);
		// }

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
		PageDTO pageDto = new PageDTO();

		return pageDto;
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

	private ArticlePageData mapPageData(ArticlePageData pageData, PageRequestDTO pageRequest) {
		try {
			pageData = objectMapper.readValue(pageRequest.getData().toString(), ArticlePageData.class);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return pageData;
	}
}
