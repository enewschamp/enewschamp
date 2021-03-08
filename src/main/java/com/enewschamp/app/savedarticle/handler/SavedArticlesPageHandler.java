package com.enewschamp.app.savedarticle.handler;

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

import com.enewschamp.app.article.page.dto.ArticlePageData;
import com.enewschamp.app.common.CommonFilterData;
import com.enewschamp.app.common.CommonService;
import com.enewschamp.app.common.ErrorCodeConstants;
import com.enewschamp.app.common.HeaderDTO;
import com.enewschamp.app.common.PageDTO;
import com.enewschamp.app.common.PageRequestDTO;
import com.enewschamp.app.common.PaginationData;
import com.enewschamp.app.common.PropertyConstants;
import com.enewschamp.app.fw.page.navigation.dto.PageNavigatorDTO;
import com.enewschamp.app.savedarticle.dto.SavedArticleData;
import com.enewschamp.app.savedarticle.dto.SavedArticlePageData;
import com.enewschamp.app.savedarticle.dto.SavedNewsArticleSearchRequest;
import com.enewschamp.app.savedarticle.service.SavedNewsArticleService;
import com.enewschamp.app.student.business.StudentActivityBusiness;
import com.enewschamp.article.app.dto.NewsArticleSummaryDTO;
import com.enewschamp.common.domain.service.PropertiesBackendService;
import com.enewschamp.domain.common.IPageHandler;
import com.enewschamp.domain.common.PageNavigationContext;
import com.enewschamp.problem.BusinessException;
import com.enewschamp.publication.domain.service.GenreService;
import com.enewschamp.subscription.app.dto.StudentPreferencesDTO;
import com.enewschamp.subscription.domain.business.PreferenceBusiness;
import com.enewschamp.subscription.domain.business.StudentControlBusiness;
import com.enewschamp.subscription.domain.business.SubscriptionBusiness;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component(value = "SavedArticlesPageHandler")
public class SavedArticlesPageHandler implements IPageHandler {

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	PreferenceBusiness preferenceBusiness;

	@Autowired
	SubscriptionBusiness subscriptionBusiness;

	@Autowired
	StudentControlBusiness studentControlBusiness;

	@Autowired
	ObjectMapper objectMapper;

	@Autowired
	CommonService commonService;

	@Autowired
	private PropertiesBackendService propertiesService;

	@Autowired
	private StudentActivityBusiness studentActivityBusiness;

	@Autowired
	SavedNewsArticleService savedNewsArticleService;

	@Autowired
	GenreService genreService;

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
					throw new BusinessException(ErrorCodeConstants.RUNTIME_EXCEPTION, ExceptionUtils.getStackTrace(e));
					// e.printStackTrace();
				}
			} catch (SecurityException e) {
				e.printStackTrace();
			}
		}
		PageDTO pageDTO = new PageDTO();
		pageDTO.setHeader(pageNavigationContext.getPageRequest().getHeader());
		return pageDTO;
	}

	public PageDTO loadSavedArticlesPage(PageNavigationContext pageNavigationContext) {
		PageDTO pageDto = new PageDTO();
		pageDto.setHeader(pageNavigationContext.getPageRequest().getHeader());
		String emailId = pageNavigationContext.getPageRequest().getHeader().getEmailId();
		String module = pageNavigationContext.getPageRequest().getHeader().getModule();
		Long studentId = studentControlBusiness.getStudentId(emailId);
		String editionId = pageNavigationContext.getPageRequest().getHeader().getEditionId();
		SavedArticlePageData pageData = new SavedArticlePageData();
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

		SavedNewsArticleSearchRequest searchRequestData = new SavedNewsArticleSearchRequest();
		searchRequestData.setEditionId(editionId);
		searchRequestData.setStudentId(studentId);
		searchRequestData.setPublicationDateFrom(
				commonService.getLimitDate(module, PropertyConstants.VIEW_LIMIT_SAVED_ARTICLES, emailId));
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
		Page<NewsArticleSummaryDTO> pageResult = savedNewsArticleService.findSavedArticles(searchRequestData, pageNo,
				pageSize);
		if (pageResult == null || pageResult.isLast()) {
			pageData.getPagination().setIsLastPage("Y");
		} else {
			pageData.getPagination().setIsLastPage("N");
		}
		HeaderDTO header = pageNavigationContext.getPageRequest().getHeader();
		pageDto.setHeader(header);
		List<SavedArticleData> savedArticleList = mapData(pageResult);
		pageData.setSavedNewsArticles(savedArticleList);
		if (savedArticleList == null || savedArticleList.size() == 0) {
			pageData.getPagination().setPageNumber(-1);
		}
		pageData.setGenreLOV(genreService.getLOV());
		pageData.setFilter(filterData);
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
		String methodName = pageNavigatorDTO.getSubmissionMethod();
		if (methodName != null && !"".equals(methodName)) {
			Class[] params = new Class[2];
			params[0] = PageRequestDTO.class;
			params[1] = PageNavigatorDTO.class;
			Method m = null;
			try {
				m = this.getClass().getDeclaredMethod(methodName, params);
			} catch (NoSuchMethodException e1) {
				e1.printStackTrace();
			} catch (SecurityException e1) {
				e1.printStackTrace();
			}
			try {
				return (PageDTO) m.invoke(this, pageRequest, pageNavigatorDTO);
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				if (e.getCause() instanceof BusinessException) {
					throw ((BusinessException) e.getCause());
				} else {
					throw new BusinessException(ErrorCodeConstants.RUNTIME_EXCEPTION, ExceptionUtils.getStackTrace(e));
					// e.printStackTrace();
				}
			} catch (SecurityException e) {
				e.printStackTrace();
			}
		}
		PageDTO pageDTO = new PageDTO();
		return pageDTO;
	}

	public PageDTO handleLikeAction(PageRequestDTO pageRequest, PageNavigatorDTO pageNavigatorDTO) {
		PageDTO pageDTO = new PageDTO();
		String emailId = pageRequest.getHeader().getEmailId();
		String editionId = pageRequest.getHeader().getEditionId();
		Long studentId = studentControlBusiness.getStudentId(emailId);
		ArticlePageData articlePageData = new ArticlePageData();
		articlePageData = mapPageData(articlePageData, pageRequest);
		String likeFlag = articlePageData.getLikeFlag();
		Long newsArticleId = articlePageData.getNewsArticleId();
		StudentPreferencesDTO studPref = preferenceBusiness.getPreferenceFromMaster(studentId);
		int readingLevel = 3;
		if (studPref != null && studPref.getReadingLevel() != null) {
			readingLevel = Integer.parseInt(studPref.getReadingLevel());
		}
		studentActivityBusiness.likeArticle(studentId, emailId, newsArticleId, likeFlag, readingLevel, editionId);
		pageDTO.setHeader(pageRequest.getHeader());
		return pageDTO;
	}

	public PageDTO handleSaveOpinionAction(PageRequestDTO pageRequest, PageNavigatorDTO pageNavigatorDTO) {
		PageDTO pageDTO = new PageDTO();
		String emailId = pageRequest.getHeader().getEmailId();
		String editionId = pageRequest.getHeader().getEditionId();
		Long studentId = studentControlBusiness.getStudentId(emailId);
		ArticlePageData articlePageData = new ArticlePageData();
		articlePageData = mapPageData(articlePageData, pageRequest);
		String opinion = articlePageData.getOpinionText();
		Long newsArticleId = articlePageData.getNewsArticleId();
		StudentPreferencesDTO studPref = preferenceBusiness.getPreferenceFromMaster(studentId);
		int readingLevel = 3;
		if (studPref != null && studPref.getReadingLevel() != null) {
			readingLevel = Integer.parseInt(studPref.getReadingLevel());
		}
		studentActivityBusiness.saveOpinion(studentId, emailId, newsArticleId, opinion, readingLevel, editionId);
		pageDTO.setHeader(pageRequest.getHeader());
		return pageDTO;
	}

	public PageDTO handleSaveArticleAction(PageRequestDTO pageRequest, PageNavigatorDTO pageNavigatorDTO) {
		PageDTO pageDTO = new PageDTO();
		String emailId = pageRequest.getHeader().getEmailId();
		String editionId = pageRequest.getHeader().getEditionId();
		Long studentId = studentControlBusiness.getStudentId(emailId);
		ArticlePageData saveData = new ArticlePageData();
		saveData = mapPageData(saveData, pageRequest);
		String saveFlag = saveData.getSaveFlag();
		Long newsArticleId = saveData.getNewsArticleId();
		StudentPreferencesDTO studPref = preferenceBusiness.getPreferenceFromMaster(studentId);
		int readingLevel = 3;
		if (studPref != null && studPref.getReadingLevel() != null) {
			readingLevel = Integer.parseInt(studPref.getReadingLevel());
		}
		studentActivityBusiness.saveArticle(studentId, emailId, newsArticleId, saveFlag, readingLevel, editionId);
		pageDTO.setHeader(pageRequest.getHeader());
		return pageDTO;
	}

	public List<SavedArticleData> mapData(Page<NewsArticleSummaryDTO> page) {
		List<SavedArticleData> opinionPageDataList = new ArrayList<SavedArticleData>();
		if (page != null && page.getContent() != null && page.getContent().size() > 0) {
			List<NewsArticleSummaryDTO> pageDataList = page.getContent();
			for (NewsArticleSummaryDTO article : pageDataList) {
				SavedArticleData publicationData = new SavedArticleData();
				publicationData = modelMapper.map(article, SavedArticleData.class);
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

	private SavedArticlePageData mapPageData(SavedArticlePageData pageData, PageRequestDTO pageRequest) {
		try {
			pageData = objectMapper.readValue(pageRequest.getData().toString(), SavedArticlePageData.class);
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
