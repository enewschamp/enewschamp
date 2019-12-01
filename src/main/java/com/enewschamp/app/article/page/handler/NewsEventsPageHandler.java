package com.enewschamp.app.article.page.handler;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import com.enewschamp.EnewschampApplicationProperties;
import com.enewschamp.app.article.page.dto.NewsEventsFilterData;
import com.enewschamp.app.article.page.dto.NewsEventsPageData;
import com.enewschamp.app.article.page.dto.NewsEventsPublicationData;
import com.enewschamp.app.article.page.dto.NewsEventsSearchData;
import com.enewschamp.app.common.HeaderDTO;
import com.enewschamp.app.common.PageDTO;
import com.enewschamp.app.common.PageRequestDTO;
import com.enewschamp.app.fw.page.navigation.common.PageAction;
import com.enewschamp.app.fw.page.navigation.dto.PageNavigatorDTO;
import com.enewschamp.app.savedarticle.dto.MonthsLovData;
import com.enewschamp.article.app.dto.NewsArticleSummaryDTO;
import com.enewschamp.article.domain.common.ArticleType;
import com.enewschamp.article.domain.service.NewsArticleService;
import com.enewschamp.article.page.data.NewsArticleSearchRequest;
import com.enewschamp.domain.common.IPageHandler;
import com.enewschamp.domain.common.PageNavigationContext;
import com.enewschamp.subscription.domain.business.StudentControlBusiness;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component(value = "NewsEventsPageHandler")
public class NewsEventsPageHandler implements IPageHandler {

	@Autowired
	StudentControlBusiness studentControlBusiness;

	@Autowired
	ObjectMapper objectMapper;

	@Autowired
	private EnewschampApplicationProperties appConfig;

	@Autowired
	private NewsArticleService newsArticleService;

	@Override
	public PageDTO handleAction(String actionName, PageRequestDTO pageRequest) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PageDTO loadPage(PageNavigationContext pageNavigationContext) {
		PageDTO pageDto = new PageDTO();
		pageDto.setHeader(pageNavigationContext.getPageRequest().getHeader());

		String action = pageNavigationContext.getActionName();
		String editionId = pageNavigationContext.getPageRequest().getHeader().getEditionID();
		LocalDate publicationDate = pageNavigationContext.getPageRequest().getHeader().getPublicationdate();
		if (pageNavigationContext.getPageRequest().getHeader().getPageNo() == null) {
			pageNavigationContext.getPageRequest().getHeader().setPageNo(0);
		}
		int pageNumber = pageNavigationContext.getPageRequest().getHeader().getPageNo();

		if (PageAction.NewsEvents.toString().equalsIgnoreCase(action)
				|| PageAction.back.toString().equalsIgnoreCase(action)
				|| PageAction.ClearFilterNewsEvents.toString().equalsIgnoreCase(action)) {
			NewsArticleSearchRequest searchRequestData = new NewsArticleSearchRequest();
			searchRequestData.setEditionId(editionId);
			searchRequestData.setPublicationDate(publicationDate);

			// Load News and Events..
			List<ArticleType> articleTypeList = new ArrayList<ArticleType>();
			articleTypeList.add(ArticleType.NEWSEVENT);

			searchRequestData.setArticleTypeList(articleTypeList);

			Page<NewsArticleSummaryDTO> pageResult = newsArticleService.findArticles(searchRequestData,
					pageNavigationContext.getPageRequest().getHeader());

			HeaderDTO header = pageNavigationContext.getPageRequest().getHeader();
			header.setIsLastPage(pageResult.isLast());
			header.setPageCount(pageResult.getTotalPages());
			header.setRecordCount(pageResult.getNumberOfElements());
			header.setPageNo(pageNumber);
			pageDto.setHeader(header);

			NewsEventsPageData newsEventsPageData = new NewsEventsPageData();
			List<NewsEventsPublicationData> eventsList = mapArticleData(pageResult);
			newsEventsPageData.setNewsArticles(eventsList);

			pageDto.setData(newsEventsPageData);

		} else if (PageAction.FilterNewsEvents.toString().equalsIgnoreCase(action)) {
			NewsEventsSearchData newsEventsSearchPageData = new NewsEventsSearchData();
			newsEventsSearchPageData = mapPageData(newsEventsSearchPageData, pageNavigationContext.getPageRequest());

			NewsArticleSearchRequest searchRequestData = new NewsArticleSearchRequest();
			searchRequestData.setEditionId(editionId);
			searchRequestData.setCity(newsEventsSearchPageData.getCity());
			// Load News and Events..
			List<ArticleType> articleTypeList = new ArrayList<ArticleType>();
			articleTypeList.add(ArticleType.NEWSEVENT);

			searchRequestData.setHeadline(newsEventsSearchPageData.getHeadline());
			String monthYear = newsEventsSearchPageData.getMonth();

			// format is MMM-yyyy
			if (monthYear != null && !"".equals(monthYear)) {
				String monthStr = monthYear.substring(0, 3);
				String year = monthYear.substring(4, 7);
				// int month = Month.valueOf(monthStr.toUpperCase()).monthStr();
				SimpleDateFormat inputFormat = new SimpleDateFormat("MMMM");
				Calendar cal = Calendar.getInstance();
				try {
					cal.setTime(inputFormat.parse(monthStr));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				SimpleDateFormat outputFormat = new SimpleDateFormat("MM"); // 01-12
				String day1 = "01";
				// form date..
				LocalDate startDate = LocalDate.of(Integer.parseInt(year),
						Integer.parseInt(outputFormat.format(cal.getTime())), Integer.parseInt(day1));
				LocalDate endDate = LocalDate.of(startDate.getYear(), startDate.getMonthValue(), 1).plusMonths(12)
						.minusDays(1);
				searchRequestData.setPublicationDateFrom(startDate);
				searchRequestData.setPublicationDateFrom(endDate);
			}
			Page<NewsArticleSummaryDTO> pageResult = newsArticleService.findArticles(searchRequestData,
					pageNavigationContext.getPageRequest().getHeader());
			HeaderDTO header = pageNavigationContext.getPageRequest().getHeader();
			header.setIsLastPage(pageResult.isLast());
			header.setPageCount(pageResult.getTotalPages());
			header.setRecordCount(pageResult.getNumberOfElements());
			header.setPageNo(pageResult.getNumber() + 1);
			pageDto.setHeader(header);
			NewsEventsPageData newsEventsPageData = new NewsEventsPageData();
			NewsEventsFilterData filterData = new NewsEventsFilterData();
			filterData.setCityID(newsEventsSearchPageData.getCity());
			filterData.setHeadline(newsEventsSearchPageData.getHeadline());
			filterData.setMonth(newsEventsSearchPageData.getMonth());
			newsEventsPageData.setMonthsLov(getMonthsLov());

			newsEventsPageData.setFilter(filterData);
			List<NewsEventsPublicationData> eventsList = mapArticleData(pageResult);
			newsEventsPageData.setNewsArticles(eventsList);

			pageDto.setData(newsEventsPageData);

		}
		if (PageAction.next.toString().equalsIgnoreCase(action)
				|| PageAction.upswipe.toString().equalsIgnoreCase(action)) {

			NewsArticleSearchRequest searchRequestData = new NewsArticleSearchRequest();
			searchRequestData.setEditionId(editionId);
			searchRequestData.setPublicationDate(publicationDate);

			// Load News and Events..
			List<ArticleType> articleTypeList = new ArrayList<ArticleType>();
			articleTypeList.add(ArticleType.NEWSEVENT);

			searchRequestData.setArticleTypeList(articleTypeList);

			Page<NewsArticleSummaryDTO> pageResult = newsArticleService.findArticles(searchRequestData,
					pageNavigationContext.getPageRequest().getHeader());

			HeaderDTO header = pageNavigationContext.getPageRequest().getHeader();
			header.setIsLastPage(pageResult.isLast());
			header.setPageCount(pageResult.getTotalPages());
			header.setRecordCount(pageResult.getNumberOfElements());
			header.setPageNo(pageNumber + 1);
			pageDto.setHeader(header);

			NewsEventsPageData newsEventsPageData = new NewsEventsPageData();
			List<NewsEventsPublicationData> eventsList = mapArticleData(pageResult);
			newsEventsPageData.setNewsArticles(eventsList);

			pageDto.setData(newsEventsPageData);

		}
		if (PageAction.RightSwipe.toString().equalsIgnoreCase(action)
				|| PageAction.previous.toString().equalsIgnoreCase(action)) {
			if (pageNumber > 1) {
				pageNumber = pageNumber - 1;
			}
			NewsArticleSearchRequest searchRequestData = new NewsArticleSearchRequest();
			searchRequestData.setEditionId(editionId);
			searchRequestData.setPublicationDate(publicationDate);

			// Load News and Events..
			List<ArticleType> articleTypeList = new ArrayList<ArticleType>();
			articleTypeList.add(ArticleType.NEWSEVENT);

			searchRequestData.setArticleTypeList(articleTypeList);

			Page<NewsArticleSummaryDTO> pageResult = newsArticleService.findArticles(searchRequestData,
					pageNavigationContext.getPageRequest().getHeader());

			HeaderDTO header = pageNavigationContext.getPageRequest().getHeader();
			header.setIsLastPage(pageResult.isLast());
			header.setPageCount(pageResult.getTotalPages());
			header.setRecordCount(pageResult.getNumberOfElements());
			header.setPageNo(pageNumber);
			pageDto.setHeader(header);

			NewsEventsPageData newsEventsPageData = new NewsEventsPageData();
			List<NewsEventsPublicationData> eventsList = mapArticleData(pageResult);
			newsEventsPageData.setNewsArticles(eventsList);

			pageDto.setData(newsEventsPageData);
		}
		return pageDto;
	}

	private List<NewsEventsPublicationData> mapArticleData(Page<NewsArticleSummaryDTO> page) {

		List<NewsEventsPublicationData> newsEventsPublicationData = new ArrayList<NewsEventsPublicationData>();
		List<NewsArticleSummaryDTO> pageDataList = page.getContent();

		for (NewsArticleSummaryDTO article : pageDataList) {
			NewsEventsPublicationData pPageData = new NewsEventsPublicationData();
			pPageData.setNewsArticleID(article.getNewsArticleId());
			pPageData.setImage(article.getImagePathMobile());
			pPageData.setCity(article.getCity());
			pPageData.setPublishDate(article.getPublicationDate());

			newsEventsPublicationData.add(pPageData);
		}
		return newsEventsPublicationData;

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

	private NewsEventsSearchData mapPageData(NewsEventsSearchData pageData, PageRequestDTO pageRequest) {
		try {
			pageData = objectMapper.readValue(pageRequest.getData().toString(), NewsEventsSearchData.class);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return pageData;
	}

	@Override
	public PageDTO saveAsMaster(String actionName, PageRequestDTO pageRequest) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PageDTO handleAppAction(String actionName, PageRequestDTO pageRequest, PageNavigatorDTO pageNavigatorDTO) {
		// TODO Auto-generated method stub
		return null;
	}

}
