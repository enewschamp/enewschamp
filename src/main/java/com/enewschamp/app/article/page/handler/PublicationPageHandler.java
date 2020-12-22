package com.enewschamp.app.article.page.handler;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import com.enewschamp.app.article.page.dto.PublicationData;
import com.enewschamp.app.article.page.dto.PublicationPageData;
import com.enewschamp.app.common.CommonService;
import com.enewschamp.app.common.HeaderDTO;
import com.enewschamp.app.common.PageDTO;
import com.enewschamp.app.common.PageRequestDTO;
import com.enewschamp.app.common.PaginationData;
import com.enewschamp.app.common.PropertyConstants;
import com.enewschamp.app.fw.page.navigation.dto.PageNavigatorDTO;
import com.enewschamp.app.notification.page.data.NotificationsSearchRequest;
import com.enewschamp.app.savedarticle.service.SavedNewsArticleService;
import com.enewschamp.app.student.business.StudentActivityBusiness;
import com.enewschamp.app.student.notification.ReadFlag;
import com.enewschamp.app.student.notification.StudentNotificationDTO;
import com.enewschamp.app.student.notification.service.StudentNotificationService;
import com.enewschamp.app.student.registration.business.StudentRegistrationBusiness;
import com.enewschamp.article.app.dto.NewsArticleSummaryDTO;
import com.enewschamp.article.domain.common.ArticleType;
import com.enewschamp.article.domain.service.NewsArticleService;
import com.enewschamp.article.page.data.NewsArticleSearchRequest;
import com.enewschamp.common.domain.service.PropertiesService;
import com.enewschamp.domain.common.AppConstants;
import com.enewschamp.domain.common.IPageHandler;
import com.enewschamp.domain.common.PageNavigationContext;
import com.enewschamp.problem.BusinessException;
import com.enewschamp.publication.domain.service.GenreService;
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
	StudentRegistrationBusiness studentRegBusiness;

	@Autowired
	StudentControlBusiness studentControlBusiness;

	@Autowired
	ObjectMapper objectMapper;

	@Autowired
	private PropertiesService propertiesService;

	@Autowired
	private StudentActivityBusiness studentActivityBusiness;

	@Autowired
	private NewsArticleService newsArticleService;

	@Autowired
	SavedNewsArticleService savedNewsArticleService;

	@Autowired
	CommonService commonService;

	@Autowired
	GenreService genreService;

	@Autowired
	PreferenceBusiness preferenceBusiness;

	@Autowired
	StudentNotificationService studentNotificationService;

	@Override
	public PageDTO handleAction(PageRequestDTO pageRequest) {
		return null;
	}

	@Override
	public PageDTO loadPage(PageNavigationContext pageNavigationContext) {
		PageDTO pageDTO = new PageDTO();
		String emailId = pageNavigationContext.getPageRequest().getHeader().getEmailId();
		String editionId = pageNavigationContext.getPageRequest().getHeader().getEditionId();
		studentRegBusiness.checkAndUpdateIfEvalPeriodExpired(emailId, editionId);
		studentRegBusiness.checkAndUpdateIfSubscriptionExpired(emailId, editionId);
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
		} else {
		}
		return pageDTO;
	}

	public PageDTO loadPublicationPage(PageNavigationContext pageNavigationContext) {
		PageDTO pageDTO = new PageDTO();
		String emailId = pageNavigationContext.getPageRequest().getHeader().getEmailId();
		Long studentId = studentControlBusiness.getStudentId(emailId);
		String editionId = pageNavigationContext.getPageRequest().getHeader().getEditionId();
		LocalDate publicationDate = pageNavigationContext.getPageRequest().getHeader().getPublicationDate();
		int readingLevel = 0;
		StudentPreferencesDTO preferenceDto = null;
		if (studentId > 0) {
			preferenceDto = preferenceBusiness.getPreferenceFromMaster(studentId);
		}
		if (preferenceDto != null) {
			readingLevel = Integer.parseInt(preferenceDto.getReadingLevel());
		} else {
			readingLevel = 3;
		}
		NewsArticleSearchRequest searchRequestData = new NewsArticleSearchRequest();
		searchRequestData.setEditionId(editionId);
		searchRequestData.setArticleType(ArticleType.NEWSARTICLE);
		if (readingLevel == 1) {
			searchRequestData.setReadingLevel1(AppConstants.YES);
		} else if (readingLevel == 2) {
			searchRequestData.setReadingLevel2(AppConstants.YES);
		} else if (readingLevel == 3) {
			searchRequestData.setReadingLevel3(AppConstants.YES);
		} else if (readingLevel == 4) {
			searchRequestData.setReadingLevel4(AppConstants.YES);
		}
		searchRequestData
				.setPublicationDateLimit(commonService.getLimitDate(PropertyConstants.VIEW_PUBLICATION_LIMIT, emailId));
		if (publicationDate == null) {
			publicationDate = newsArticleService.getLatestPublication(editionId, readingLevel, ArticleType.NEWSARTICLE);
		}
		searchRequestData.setPublicationDate(publicationDate);
		PublicationPageData pageData = new PublicationPageData();
		pageData = mapPageData(pageData, pageNavigationContext.getPageRequest());
		int pageNo = 1;
		int pageSize = Integer.valueOf(propertiesService.getProperty(PropertyConstants.PAGE_SIZE));
		if (pageData.getPagination() != null) {
			pageNo = pageData.getPagination().getPageNumber() > 0 ? pageData.getPagination().getPageNumber() : 1;
			pageSize = pageData.getPagination().getPageSize() > 0 ? pageData.getPagination().getPageSize()
					: Integer.valueOf(propertiesService.getProperty(PropertyConstants.PAGE_SIZE));
		} else {
			PaginationData paginationData = new PaginationData();
			paginationData.setPageNumber(pageNo);
			paginationData.setPageSize(pageSize);
			pageData.setPagination(paginationData);
		}
		Page<NewsArticleSummaryDTO> pageResult = newsArticleService.findArticles(searchRequestData, studentId, pageNo,
				pageSize, pageNavigationContext.getPageRequest().getHeader());
		HeaderDTO header = pageNavigationContext.getPageRequest().getHeader();
		header.setPublicationDate(publicationDate);
		List<PublicationData> publicationData = commonService.mapStudentData(pageResult, studentId);
		if (publicationData.size() == 0) {
			pageData.getPagination().setPageNumber(-1);
		}
		pageData.setNewsArticles(publicationData);
		pageData.setNotificationsCount(getUnreadNotificationsCount(emailId, studentId, editionId));
		pageDTO.setData(pageData);
		pageDTO.setHeader(header);
		return pageDTO;
	}

	private Long getUnreadNotificationsCount(String emailId, Long studentId, String editionId) {
		NotificationsSearchRequest searchRequest = new NotificationsSearchRequest();
		searchRequest.setEditionId(editionId);
		searchRequest.setStudentId(studentId);
		String limitDate = commonService.getLimitDate(PropertyConstants.VIEW_NOTIFICATIONS_LIMIT, emailId) + " 00:00";
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		searchRequest.setOperationDateTime(LocalDateTime.parse(limitDate, formatter));
		searchRequest.setIsRead("N");
		Page<StudentNotificationDTO> pageResult = studentNotificationService.getNotificationList(searchRequest, 1,
				Integer.MAX_VALUE);
		if (pageResult != null && pageResult.getContent() != null) {
			return Long.valueOf(pageResult.getContent().size());
		}
		return 0L;
	}

	public PageDTO loadNextPublicationPage(PageNavigationContext pageNavigationContext) {
		PageDTO pageDto = new PageDTO();
		String emailId = pageNavigationContext.getPageRequest().getHeader().getEmailId();
		Long studentId = studentControlBusiness.getStudentId(emailId);
		String editionId = pageNavigationContext.getPageRequest().getHeader().getEditionId();
		LocalDate publicationDate = pageNavigationContext.getPageRequest().getHeader().getPublicationDate();
		int readingLevel = 0;
		StudentPreferencesDTO preferenceDto = null;
		if (studentId > 0) {
			preferenceDto = preferenceBusiness.getPreferenceFromMaster(studentId);
		}
		if (preferenceDto != null) {
			readingLevel = Integer.parseInt(preferenceDto.getReadingLevel());
		} else {
			readingLevel = 3;
		}
		NewsArticleSearchRequest searchRequestData = new NewsArticleSearchRequest();
		searchRequestData.setEditionId(editionId);
		searchRequestData.setArticleType(ArticleType.NEWSARTICLE);
		if (readingLevel == 1) {
			searchRequestData.setReadingLevel1(AppConstants.YES);
		} else if (readingLevel == 2) {
			searchRequestData.setReadingLevel2(AppConstants.YES);
		} else if (readingLevel == 3) {
			searchRequestData.setReadingLevel3(AppConstants.YES);
		} else if (readingLevel == 4) {
			searchRequestData.setReadingLevel4(AppConstants.YES);
		}

		searchRequestData.setEditionId(editionId);
		searchRequestData
				.setPublicationDateLimit(commonService.getLimitDate(PropertyConstants.VIEW_PUBLICATION_LIMIT, emailId));
		LocalDate newDate = newsArticleService.getNextAvailablePublicationDate(publicationDate, editionId, readingLevel,
				ArticleType.NEWSARTICLE);
		searchRequestData.setPublicationDate(newDate);
		PublicationPageData pageData = new PublicationPageData();
		pageData = mapPageData(pageData, pageNavigationContext.getPageRequest());
		int pageNo = 1;
		int pageSize = Integer.valueOf(propertiesService.getProperty(PropertyConstants.PAGE_SIZE));
		if (pageData.getPagination() != null) {
			pageNo = pageData.getPagination().getPageNumber() > 0 ? pageData.getPagination().getPageNumber() : 1;
			pageSize = pageData.getPagination().getPageSize() > 0 ? pageData.getPagination().getPageSize()
					: Integer.valueOf(propertiesService.getProperty(PropertyConstants.PAGE_SIZE));
		} else {
			PaginationData paginationData = new PaginationData();
			paginationData.setPageNumber(pageNo);
			paginationData.setPageSize(pageSize);
			pageData.setPagination(paginationData);
		}
		Page<NewsArticleSummaryDTO> pageResult = newsArticleService.findArticles(searchRequestData, studentId, pageNo,
				pageSize, pageNavigationContext.getPageRequest().getHeader());

		HeaderDTO header = pageNavigationContext.getPageRequest().getHeader();
		header.setPublicationDate(newDate);

		List<PublicationData> publicationData = commonService.mapStudentData(pageResult, studentId);
		if (publicationData.size() == 0) {
			pageData.getPagination().setPageNumber(-1);
		}
		pageData.setNewsArticles(publicationData);
		pageData.setNotificationsCount(getUnreadNotificationsCount(emailId, studentId, editionId));
		pageDto.setData(pageData);
		pageDto.setHeader(header);
		return pageDto;
	}

	public PageDTO loadPreviousPublicationPage(PageNavigationContext pageNavigationContext) {
		PageDTO pageDto = new PageDTO();
		String emailId = pageNavigationContext.getPageRequest().getHeader().getEmailId();
		Long studentId = studentControlBusiness.getStudentId(emailId);
		String editionId = pageNavigationContext.getPageRequest().getHeader().getEditionId();
		LocalDate publicationDate = pageNavigationContext.getPageRequest().getHeader().getPublicationDate();
		int readingLevel = 0;
		StudentPreferencesDTO preferenceDto = null;
		if (studentId > 0) {
			preferenceDto = preferenceBusiness.getPreferenceFromMaster(studentId);
		}
		if (preferenceDto != null) {
			readingLevel = Integer.parseInt(preferenceDto.getReadingLevel());
		} else {
			readingLevel = 3;
		}
		NewsArticleSearchRequest searchRequestData = new NewsArticleSearchRequest();
		searchRequestData.setEditionId(editionId);
		searchRequestData.setArticleType(ArticleType.NEWSARTICLE);
		searchRequestData
				.setPublicationDateFrom(commonService.getLimitDate(PropertyConstants.VIEW_PUBLICATION_LIMIT, emailId));
		if (readingLevel == 1) {
			searchRequestData.setReadingLevel1(AppConstants.YES);
		} else if (readingLevel == 2) {
			searchRequestData.setReadingLevel2(AppConstants.YES);
		} else if (readingLevel == 3) {
			searchRequestData.setReadingLevel3(AppConstants.YES);
		} else if (readingLevel == 4) {
			searchRequestData.setReadingLevel4(AppConstants.YES);
		}
		searchRequestData.setEditionId(editionId);
		LocalDate newDate = newsArticleService.getPreviousAvailablePublicationDate(publicationDate, editionId,
				readingLevel, ArticleType.NEWSARTICLE);
		searchRequestData.setPublicationDate(newDate);
		PublicationPageData pageData = new PublicationPageData();
		pageData = mapPageData(pageData, pageNavigationContext.getPageRequest());
		int pageNo = 1;
		int pageSize = Integer.valueOf(propertiesService.getProperty(PropertyConstants.PAGE_SIZE));
		if (pageData.getPagination() != null) {
			pageNo = pageData.getPagination().getPageNumber() > 0 ? pageData.getPagination().getPageNumber() : 1;
			pageSize = pageData.getPagination().getPageSize() > 0 ? pageData.getPagination().getPageSize()
					: Integer.valueOf(propertiesService.getProperty(PropertyConstants.PAGE_SIZE));
		} else {
			PaginationData paginationData = new PaginationData();
			paginationData.setPageNumber(pageNo);
			paginationData.setPageSize(pageSize);
			pageData.setPagination(paginationData);
		}
		Page<NewsArticleSummaryDTO> pageResult = newsArticleService.findArticles(searchRequestData, studentId, pageNo,
				pageSize, pageNavigationContext.getPageRequest().getHeader());

		HeaderDTO header = pageNavigationContext.getPageRequest().getHeader();
		header.setPublicationDate(newDate);

		List<PublicationData> publicationData = commonService.mapStudentData(pageResult, studentId);
		if (publicationData.size() == 0) {
			pageData.getPagination().setPageNumber(-1);
		}
		pageData.setNewsArticles(publicationData);
		pageData.setNotificationsCount(getUnreadNotificationsCount(emailId, studentId, editionId));
		pageDto.setData(pageData);
		pageDto.setHeader(header);
		return pageDto;
	}

	public PageDTO loadDownSwipe(PageNavigationContext pageNavigationContext) {
		PageDTO pageDto = new PageDTO();
		String emailId = pageNavigationContext.getPageRequest().getHeader().getEmailId();
		Long studentId = studentControlBusiness.getStudentId(emailId);
		String editionId = pageNavigationContext.getPageRequest().getHeader().getEditionId();
		LocalDate publicationDate = pageNavigationContext.getPageRequest().getHeader().getPublicationDate();
		int readingLevel = 0;
		StudentPreferencesDTO preferenceDto = null;
		if (studentId > 0) {
			preferenceDto = preferenceBusiness.getPreferenceFromMaster(studentId);
		}
		if (preferenceDto != null) {
			readingLevel = Integer.parseInt(preferenceDto.getReadingLevel());
		} else {
			readingLevel = 3;
		}
		NewsArticleSearchRequest searchRequestData = new NewsArticleSearchRequest();
		searchRequestData.setEditionId(editionId);
		searchRequestData.setArticleType(ArticleType.NEWSARTICLE);
		if (readingLevel == 1) {
			searchRequestData.setReadingLevel1(AppConstants.YES);
		} else if (readingLevel == 2) {
			searchRequestData.setReadingLevel2(AppConstants.YES);
		} else if (readingLevel == 3) {
			searchRequestData.setReadingLevel3(AppConstants.YES);
		} else if (readingLevel == 4) {
			searchRequestData.setReadingLevel4(AppConstants.YES);
		}

		searchRequestData.setEditionId(editionId);
		searchRequestData.setPublicationDate(publicationDate);
		PublicationPageData pageData = new PublicationPageData();
		pageData = mapPageData(pageData, pageNavigationContext.getPageRequest());
		int pageNo = 1;
		int pageSize = Integer.valueOf(propertiesService.getProperty(PropertyConstants.PAGE_SIZE));
		if (pageData.getPagination() != null) {
			pageNo = pageData.getPagination().getPageNumber() > 0 ? pageData.getPagination().getPageNumber() : 1;
			pageSize = pageData.getPagination().getPageSize() > 0 ? pageData.getPagination().getPageSize()
					: Integer.valueOf(propertiesService.getProperty(PropertyConstants.PAGE_SIZE));
		} else {
			PaginationData paginationData = new PaginationData();
			paginationData.setPageNumber(pageNo);
			paginationData.setPageSize(pageSize);
			pageData.setPagination(paginationData);
		}
		Page<NewsArticleSummaryDTO> pageResult = newsArticleService.findArticles(searchRequestData, studentId, pageNo,
				pageSize, pageNavigationContext.getPageRequest().getHeader());
		HeaderDTO header = pageNavigationContext.getPageRequest().getHeader();

		List<PublicationData> publicationData = commonService.mapStudentData(pageResult, studentId);
		if (publicationData.size() == 0) {
			pageData.getPagination().setPageNumber(-1);
		}
		pageData.setNewsArticles(publicationData);
		pageData.setNotificationsCount(getUnreadNotificationsCount(emailId, studentId, editionId));
		pageDto.setData(pageData);
		pageDto.setHeader(header);
		return pageDto;
	}

	public PageDTO loadUpSwipe(PageNavigationContext pageNavigationContext) {
		PageDTO pageDto = new PageDTO();
		String emailId = pageNavigationContext.getPageRequest().getHeader().getEmailId();
		Long studentId = studentControlBusiness.getStudentId(emailId);
		String editionId = pageNavigationContext.getPageRequest().getHeader().getEditionId();
		LocalDate publicationDate = pageNavigationContext.getPageRequest().getHeader().getPublicationDate();
		int readingLevel = 0;
		StudentPreferencesDTO preferenceDto = null;
		if (studentId > 0) {
			preferenceDto = preferenceBusiness.getPreferenceFromMaster(studentId);
		}
		if (preferenceDto != null) {
			readingLevel = Integer.parseInt(preferenceDto.getReadingLevel());
		} else {
			readingLevel = 3;
		}
		NewsArticleSearchRequest searchRequestData = new NewsArticleSearchRequest();
		searchRequestData.setEditionId(editionId);
		searchRequestData.setArticleType(ArticleType.NEWSARTICLE);
		if (readingLevel == 1) {
			searchRequestData.setReadingLevel1(AppConstants.YES);
		} else if (readingLevel == 2) {
			searchRequestData.setReadingLevel2(AppConstants.YES);
		} else if (readingLevel == 3) {
			searchRequestData.setReadingLevel3(AppConstants.YES);
		} else if (readingLevel == 4) {
			searchRequestData.setReadingLevel4(AppConstants.YES);
		}

		searchRequestData.setEditionId(editionId);
		searchRequestData.setPublicationDate(publicationDate);
		PublicationPageData pageData = new PublicationPageData();
		pageData = mapPageData(pageData, pageNavigationContext.getPageRequest());
		int pageNo = 1;
		int pageSize = Integer.valueOf(propertiesService.getProperty(PropertyConstants.PAGE_SIZE));
		if (pageData.getPagination() != null) {
			pageNo = pageData.getPagination().getPageNumber() > 0 ? pageData.getPagination().getPageNumber() : 1;
			pageSize = pageData.getPagination().getPageSize() > 0 ? pageData.getPagination().getPageSize()
					: Integer.valueOf(propertiesService.getProperty(PropertyConstants.PAGE_SIZE));
		} else {
			PaginationData paginationData = new PaginationData();
			paginationData.setPageNumber(pageNo);
			paginationData.setPageSize(pageSize);
			pageData.setPagination(paginationData);
		}
		Page<NewsArticleSummaryDTO> pageResult = newsArticleService.findArticles(searchRequestData, studentId, pageNo,
				pageSize, pageNavigationContext.getPageRequest().getHeader());
		HeaderDTO header = pageNavigationContext.getPageRequest().getHeader();

		List<PublicationData> publicationData = commonService.mapStudentData(pageResult, studentId);
		if (publicationData.size() == 0) {
			pageData.getPagination().setPageNumber(-1);
		}
		pageData.setNewsArticles(publicationData);
		pageData.setNotificationsCount(getUnreadNotificationsCount(emailId, studentId, editionId));
		pageDto.setData(pageData);
		pageDto.setHeader(header);
		return pageDto;
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

	private PublicationPageData mapPageData(PublicationPageData pageData, PageRequestDTO pageRequest) {
		try {
			pageData = objectMapper.readValue(pageRequest.getData().toString(), PublicationPageData.class);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return pageData;
	}
}
