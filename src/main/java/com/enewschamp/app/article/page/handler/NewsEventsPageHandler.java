package com.enewschamp.app.article.page.handler;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import com.enewschamp.app.article.page.dto.NewsEventsPageData;
import com.enewschamp.app.article.page.dto.NewsEventsPublicationData;
import com.enewschamp.app.common.BusinessRulesPlugin;
import com.enewschamp.app.common.CommonFilterData;
import com.enewschamp.app.common.CommonService;
import com.enewschamp.app.common.ErrorCodeConstants;
import com.enewschamp.app.common.HeaderDTO;
import com.enewschamp.app.common.PageDTO;
import com.enewschamp.app.common.PageRequestDTO;
import com.enewschamp.app.common.PaginationData;
import com.enewschamp.app.common.PropertyConstants;
import com.enewschamp.app.common.city.service.CityService;
import com.enewschamp.app.fw.page.navigation.dto.PageNavigatorDTO;
import com.enewschamp.app.student.business.StudentActivityBusiness;
import com.enewschamp.app.student.registration.entity.StudentRegistration;
import com.enewschamp.app.student.registration.service.StudentRegistrationService;
import com.enewschamp.article.app.dto.NewsArticleSummaryDTO;
import com.enewschamp.article.domain.common.ArticleType;
import com.enewschamp.article.domain.service.NewsArticleService;
import com.enewschamp.article.page.data.NewsArticleSearchRequest;
import com.enewschamp.common.domain.service.PropertiesBackendService;
import com.enewschamp.domain.common.AppConstants;
import com.enewschamp.domain.common.IPageHandler;
import com.enewschamp.domain.common.PageNavigationContext;
import com.enewschamp.problem.BusinessException;
import com.enewschamp.publication.domain.service.GenreService;
import com.enewschamp.subscription.app.dto.StudentPreferencesDTO;
import com.enewschamp.subscription.domain.business.PreferenceBusiness;
import com.enewschamp.subscription.domain.business.StudentControlBusiness;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component(value = "NewsEventsPageHandler")
public class NewsEventsPageHandler implements IPageHandler {

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	StudentControlBusiness studentControlBusiness;

	@Autowired
	ObjectMapper objectMapper;

	@Autowired
	private GenreService genreService;

	@Autowired
	private NewsArticleService newsArticleService;

	@Autowired
	CommonService commonService;

	@Autowired
	PreferenceBusiness preferenceBusiness;

	@Autowired
	BusinessRulesPlugin businessRulesPlugin;

	@Autowired
	private StudentActivityBusiness studentActivityBusiness;

	@Autowired
	private PropertiesBackendService propertiesService;

	@Autowired
	CityService cityService;

	@Autowired
	StudentRegistrationService regService;

	@Override
	public PageDTO handleAction(PageRequestDTO pageRequest) {
		// TODO Auto-generated method stub
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

	public PageDTO loadNewsEventsPage(PageNavigationContext pageNavigationContext) {
		PageDTO pageDto = new PageDTO();
		pageDto.setHeader(pageNavigationContext.getPageRequest().getHeader());
		String emailId = pageNavigationContext.getPageRequest().getHeader().getEmailId();
		String module = pageNavigationContext.getPageRequest().getHeader().getModule();
		String isTestUser = "";
		StudentRegistration studReg = regService.getStudentReg(emailId);
		if (studReg != null) {
			isTestUser = studReg.getIsTestUser();
		}
		Long studentId = studentControlBusiness.getStudentId(emailId);
		String editionId = pageNavigationContext.getPageRequest().getHeader().getEditionId();
		NewsEventsPageData pageData = (NewsEventsPageData) commonService.mapPageData(NewsEventsPageData.class,
				pageNavigationContext.getPageRequest());
		int pageNo = 1;
		int pageSize = Integer.valueOf(propertiesService
				.getValue(pageNavigationContext.getPageRequest().getHeader().getModule(), PropertyConstants.PAGE_SIZE));
		if (pageData.getPagination() != null) {
			pageNo = pageData.getPagination().getPageNumber() > 0 ? pageData.getPagination().getPageNumber() : 1;
			pageSize = pageData.getPagination().getPageSize() > 0 ? pageData.getPagination().getPageSize()
					: Integer.valueOf(
							propertiesService.getValue(pageNavigationContext.getPageRequest().getHeader().getModule(),
									PropertyConstants.PAGE_SIZE));
		} else {
			PaginationData paginationData = new PaginationData();
			paginationData.setPageNumber(pageNo);
			paginationData.setPageSize(pageSize);
			pageData.setPagination(paginationData);
		}
		int readingLevel = 0;
		StudentPreferencesDTO preferenceDto = null;
		if ((studentId > 0) && (preferenceBusiness.getPreferenceFromMaster(studentId) != null)) {
			preferenceDto = preferenceBusiness.getPreferenceFromMaster(studentId);
			readingLevel = Integer.parseInt(preferenceDto.getReadingLevel());
		}
		NewsArticleSearchRequest searchRequestData = new NewsArticleSearchRequest();
		searchRequestData.setEditionId(editionId);
		searchRequestData.setIsTestUser(isTestUser);
		searchRequestData.setArticleType(ArticleType.NEWSEVENT);
		searchRequestData.setPublicationDateFrom(
				commonService.getLimitDate(module, PropertyConstants.VIEW_LIMIT_NEWS_EVENTS, emailId));
		if (readingLevel == 1) {
			searchRequestData.setReadingLevel1(AppConstants.YES);
		} else if (readingLevel == 2) {
			searchRequestData.setReadingLevel2(AppConstants.YES);
		} else if (readingLevel == 3) {
			searchRequestData.setReadingLevel3(AppConstants.YES);
		} else if (readingLevel == 4) {
			searchRequestData.setReadingLevel4(AppConstants.YES);
		}
		CommonFilterData filterPageData = pageData.getFilter();
		if (filterPageData != null) {
			searchRequestData.setCityId(filterPageData.getCity());
			searchRequestData.setHeadline(filterPageData.getHeadline());
			searchRequestData.setGenreId(filterPageData.getGenre());
			String yearMonth = filterPageData.getYearMonth();

			if (yearMonth != null && !"".equals(yearMonth)) {
				String monthStr = yearMonth.substring(4, 6);
				String year = yearMonth.substring(0, 4);
				SimpleDateFormat inputFormat = new SimpleDateFormat("MM");
				Calendar cal = Calendar.getInstance();
				try {
					cal.setTime(inputFormat.parse(monthStr));
				} catch (ParseException e) {
					e.printStackTrace();
				}
				SimpleDateFormat outputFormat = new SimpleDateFormat("MM");
				String day1 = "01";
				LocalDate startDate = LocalDate.of(Integer.parseInt(year),
						Integer.parseInt(outputFormat.format(cal.getTime())), Integer.parseInt(day1));
				LocalDate endDate = LocalDate.of(startDate.getYear(), startDate.getMonthValue(), 1).plusMonths(1)
						.minusDays(1);
				searchRequestData.setPublicationDateFrom(startDate);
				searchRequestData.setPublicationDateTo(endDate);
			}
		}
		Page<NewsArticleSummaryDTO> pageResult = newsArticleService.findArticles(searchRequestData, studentId, pageNo,
				pageSize, pageNavigationContext.getPageRequest().getHeader());
		if (pageResult == null || pageResult.isLast()) {
			pageData.getPagination().setIsLastPage("Y");
		} else {
			pageData.getPagination().setIsLastPage("N");
		}
		HeaderDTO header = pageNavigationContext.getPageRequest().getHeader();
		pageDto.setHeader(header);
		List<NewsEventsPublicationData> eventsList = mapStudentData(pageResult, studentId);
		if (eventsList == null || eventsList.size() == 0) {
			pageData.getPagination().setPageNumber(-1);
		}
		pageData.setNewsArticles(eventsList);
		pageData.setGenreLOV(genreService.getLOV());
		pageData.setCityLOV(cityService.getLOVForNewsEvents());
		pageData.setFilter(filterPageData);
		pageDto.setData(pageData);
		return pageDto;
	}

	private List<NewsEventsPublicationData> mapStudentData(Page<NewsArticleSummaryDTO> page, Long studentId) {
		List<NewsEventsPublicationData> publicationPageDataList = new ArrayList<NewsEventsPublicationData>();
		if (page != null && page.getContent() != null && page.getContent().size() > 0) {
			List<NewsArticleSummaryDTO> pageData = page.getContent();
			for (NewsArticleSummaryDTO article : pageData) {
				NewsEventsPublicationData publicationData = new NewsEventsPublicationData();
				publicationData = modelMapper.map(article, NewsEventsPublicationData.class);
				publicationPageDataList.add(publicationData);
			}
		}
		return publicationPageDataList;
	}

	@Override
	public PageDTO saveAsMaster(PageRequestDTO pageRequest) {
		return null;
	}

	@Override
	public PageDTO handleAppAction(PageRequestDTO pageRequest, PageNavigatorDTO pageNavigatorDTO) {
		PageDTO pageDTO = new PageDTO();
		pageDTO.setHeader(pageRequest.getHeader());
		return pageDTO;
	}

	private NewsEventsPageData mapPageData(NewsEventsPageData pageData, PageRequestDTO pageRequest) {
		try {
			pageData = objectMapper.readValue(pageRequest.getData().toString(), NewsEventsPageData.class);
		} catch (JsonParseException e) {
			throw new RuntimeException(e);
		} catch (JsonMappingException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return pageData;
	}

}
