package com.enewschamp.opinions.page.handler;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import com.enewschamp.app.article.page.dto.ArticlePageData;
import com.enewschamp.app.common.CommonFilterData;
import com.enewschamp.app.common.CommonService;
import com.enewschamp.app.common.HeaderDTO;
import com.enewschamp.app.common.PageDTO;
import com.enewschamp.app.common.PageRequestDTO;
import com.enewschamp.app.common.PaginationData;
import com.enewschamp.app.common.PropertyConstants;
import com.enewschamp.app.fw.page.navigation.dto.PageNavigatorDTO;
import com.enewschamp.app.savedarticle.service.SavedNewsArticleService;
import com.enewschamp.article.app.dto.NewsArticleSummaryDTO;
import com.enewschamp.common.domain.service.PropertiesBackendService;
import com.enewschamp.domain.common.IPageHandler;
import com.enewschamp.domain.common.PageNavigationContext;
import com.enewschamp.opinions.page.dto.OpinionsData;
import com.enewschamp.opinions.page.dto.OpinionsPageData;
import com.enewschamp.opinions.page.dto.OpinionsSearchRequest;
import com.enewschamp.problem.BusinessException;
import com.enewschamp.publication.domain.service.GenreService;
import com.enewschamp.subscription.domain.business.StudentControlBusiness;
import com.enewschamp.subscription.domain.business.SubscriptionBusiness;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
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
	CommonService commonService;

	@Autowired
	SavedNewsArticleService savedNewsArticleService;

	@Autowired
	GenreService genreService;

	@Autowired
	private PropertiesBackendService propertiesService;

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
			} catch (NoSuchMethodException e1) {
				e1.printStackTrace();
			} catch (SecurityException e1) {
				e1.printStackTrace();
			}
			try {
				return (PageDTO) m.invoke(this, pageNavigationContext);
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				if (e.getCause() instanceof BusinessException) {
					throw ((BusinessException) e.getCause());
				} else {
					e.printStackTrace();
				}
			} catch (SecurityException e) {
				e.printStackTrace();
			}
		}
		PageDTO pageDTO = new PageDTO();
		return pageDTO;
	}

	public PageDTO loadOpinionsPage(PageNavigationContext pageNavigationContext) {
		PageDTO pageDto = new PageDTO();
		pageDto.setHeader(pageNavigationContext.getPageRequest().getHeader());
		String emailId = pageNavigationContext.getPageRequest().getHeader().getEmailId();
		Long studentId = studentControlBusiness.getStudentId(emailId);
		String editionId = pageNavigationContext.getPageRequest().getHeader().getEditionId();
		String module = pageNavigationContext.getPageRequest().getHeader().getModule();
		OpinionsPageData pageData = new OpinionsPageData();
		pageData = mapPageData(pageData, pageNavigationContext.getPageRequest());
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
		CommonFilterData filterData = pageData.getFilter();
		studentId = studentControlBusiness.getStudentId(emailId);
		OpinionsSearchRequest searchRequestData = new OpinionsSearchRequest();
		searchRequestData.setEditionId(editionId);
		searchRequestData.setStudentId(studentId);
		searchRequestData.setPublicationDateFrom(
				commonService.getLimitDate(module, PropertyConstants.VIEW_OPINIONS_LIMIT, emailId));
		if (filterData != null) {
			searchRequestData.setGenre(filterData.getGenre());
			searchRequestData.setHeadline(filterData.getHeadline());
			String yearMonth = filterData.getYearMonth();
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

		Page<NewsArticleSummaryDTO> pageResult = savedNewsArticleService.findArticlesWithOpinions(searchRequestData,
				pageNo, pageSize);
		if (pageResult == null || pageResult.isLast()) {
			pageData.getPagination().setIsLastPage("Y");
		} else {
			pageData.getPagination().setIsLastPage("N");
		}
		HeaderDTO header = pageNavigationContext.getPageRequest().getHeader();
		pageDto.setHeader(header);
		List<OpinionsData> opinionsArticleList = mapData(pageResult);
		pageData.setOpinionsNewsArticles(opinionsArticleList);
		if (opinionsArticleList == null || opinionsArticleList.size() == 0) {
			pageData.getPagination().setPageNumber(-1);
		}
		pageData.setFilter(filterData);
		pageData.setGenreLOV(genreService.getLOV());
		pageData.setOpinionsNewsArticles(opinionsArticleList);
		pageDto.setData(pageData);
		return pageDto;
	}

	@Override
	public PageDTO saveAsMaster(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		return pageDto;
	}

	@Override
	public PageDTO handleAppAction(PageRequestDTO pageRequest, PageNavigatorDTO pageNavigatorDTO) {
		PageDTO pageDTO = new PageDTO();
		pageDTO.setHeader(pageRequest.getHeader());
		return pageDTO;
	}

	public List<OpinionsData> mapData(Page<NewsArticleSummaryDTO> page) {
		List<OpinionsData> opinionPageDataList = new ArrayList<OpinionsData>();
		if (page != null && page.getContent() != null && page.getContent().size() > 0) {
			List<NewsArticleSummaryDTO> pageDataList = page.getContent();
			for (NewsArticleSummaryDTO article : pageDataList) {
				OpinionsData publicationData = new OpinionsData();
				publicationData = modelMapper.map(article, OpinionsData.class);
				opinionPageDataList.add(publicationData);
			}
		}
		return opinionPageDataList;
	}

	private ArticlePageData mapPageData(ArticlePageData pageData, PageRequestDTO pageRequest) {
		try {
			pageData = objectMapper.readValue(pageRequest.getData().toString(), ArticlePageData.class);
		} catch (JsonParseException e) {
			throw new RuntimeException(e);
		} catch (JsonMappingException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return pageData;
	}

	private OpinionsPageData mapPageData(OpinionsPageData pageData, PageRequestDTO pageRequest) {
		try {
			pageData = objectMapper.readValue(pageRequest.getData().toString(), OpinionsPageData.class);
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
