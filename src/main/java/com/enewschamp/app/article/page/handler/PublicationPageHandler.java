package com.enewschamp.app.article.page.handler;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import com.enewschamp.app.article.page.dto.PublicationData;
import com.enewschamp.app.article.page.dto.PublicationPageData;
import com.enewschamp.app.common.CommonService;
import com.enewschamp.app.common.ErrorCodeConstants;
import com.enewschamp.app.common.HeaderDTO;
import com.enewschamp.app.common.PageDTO;
import com.enewschamp.app.common.PageRequestDTO;
import com.enewschamp.app.common.PaginationData;
import com.enewschamp.app.common.PropertyConstants;
import com.enewschamp.app.fw.page.navigation.dto.PageNavigatorDTO;
import com.enewschamp.app.notification.page.data.NotificationsSearchRequest;
import com.enewschamp.app.savedarticle.service.SavedNewsArticleService;
import com.enewschamp.app.student.notification.StudentNotificationDTO;
import com.enewschamp.app.student.notification.service.StudentNotificationService;
import com.enewschamp.app.student.registration.business.StudentRegistrationBusiness;
import com.enewschamp.app.student.registration.entity.StudentRegistration;
import com.enewschamp.app.student.registration.service.StudentRegistrationService;
import com.enewschamp.app.user.login.entity.UserType;
import com.enewschamp.app.user.login.service.UserLoginBusiness;
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
	StudentRegistrationService regService;

	@Autowired
	ObjectMapper objectMapper;

	@Autowired
	private PropertiesBackendService propertiesService;

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

	@Autowired
	UserLoginBusiness userLoginBusiness;

	@Override
	public PageDTO handleAction(PageRequestDTO pageRequest) {
		return null;
	}

	@Override
	public PageDTO loadPage(PageNavigationContext pageNavigationContext) {
		PageDTO pageDTO = new PageDTO();
		String emailId = pageNavigationContext.getPageRequest().getHeader().getEmailId();
		String editionId = pageNavigationContext.getPageRequest().getHeader().getEditionId();
		Long studentId = pageNavigationContext.getPageRequest().getHeader().getStudentId();
		studentRegBusiness.checkAndUpdateIfEvalPeriodExpired(studentId, editionId);
		studentRegBusiness.checkAndUpdateIfSubscriptionExpired(studentId, editionId);
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
		} else {
			pageDTO.setHeader(pageNavigationContext.getPageRequest().getHeader());
		}
		return pageDTO;
	}

	public PageDTO loadPublicationPage(PageNavigationContext pageNavigationContext) {
		PageDTO pageDTO = new PageDTO();
		String emailId = pageNavigationContext.getPageRequest().getHeader().getEmailId();
		String module = pageNavigationContext.getPageRequest().getHeader().getModule();
		String tokenId = pageNavigationContext.getPageRequest().getHeader().getLoginCredentials();
		String deviceId = pageNavigationContext.getPageRequest().getHeader().getDeviceId();
		Long studentId = pageNavigationContext.getPageRequest().getHeader().getStudentId();
		String isTestUser = "";
		StudentRegistration studReg = regService.getStudentReg(emailId);
		if (studReg != null) {
			isTestUser = studReg.getIsTestUser();
		}
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
		searchRequestData.setIsTestUser(isTestUser);
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
		if (publicationDate == null) {
			publicationDate = newsArticleService.getLatestPublication(editionId, isTestUser, readingLevel,
					ArticleType.NEWSARTICLE);
		}
		searchRequestData.setPublicationDate(publicationDate);
		PublicationPageData pageData = (PublicationPageData) commonService.mapPageData(PublicationPageData.class,
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
		Page<NewsArticleSummaryDTO> pageResult = newsArticleService.findArticles(searchRequestData, studentId, pageNo,
				pageSize, pageNavigationContext.getPageRequest().getHeader());
		if (pageResult == null || pageResult.isLast()) {
			pageData.getPagination().setIsLastPage("Y");
		} else {
			pageData.getPagination().setIsLastPage("N");
		}
		HeaderDTO header = pageNavigationContext.getPageRequest().getHeader();
		header.setPublicationDate(publicationDate);
		List<PublicationData> publicationData = commonService.mapStudentData(pageResult, studentId);
		if (publicationData.size() == 0) {
			pageData.getPagination().setPageNumber(-1);
		}
		pageData.setNewsArticles(publicationData);
		pageData.setNotificationsCount(
				getUnreadNotificationsCount(module, emailId, studentId, editionId, deviceId, tokenId));
		pageDTO.setData(pageData);
		pageDTO.setHeader(header);
		return pageDTO;
	}

	private Long getUnreadNotificationsCount(String module, String emailId, Long studentId, String editionId,
			String deviceId, String tokenId) {
		if (userLoginBusiness.getLoginDetails(deviceId, tokenId, emailId, UserType.S) != null) {
			NotificationsSearchRequest searchRequest = new NotificationsSearchRequest();
			searchRequest.setEditionId(editionId);
			searchRequest.setStudentId(studentId);
			String limitDate = commonService.getLimitDate(module, PropertyConstants.VIEW_LIMIT_NOTIFICATIONS, studentId)
					+ " 00:00";
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
			searchRequest.setOperationDateTime(LocalDateTime.parse(limitDate, formatter));
			searchRequest.setIsRead("N");
			searchRequest.setCountOnly("Y");
			Page<StudentNotificationDTO> pageResult = studentNotificationService.getNotificationList(searchRequest, 1,
					Integer.MAX_VALUE);
			if (pageResult != null && pageResult.getContent() != null) {
				return Long.valueOf(pageResult.getContent().size());
			}
		}
		return 0L;
	}

	public PageDTO loadNextPublicationPage(PageNavigationContext pageNavigationContext) {
		PageDTO pageDto = new PageDTO();
		String emailId = pageNavigationContext.getPageRequest().getHeader().getEmailId();
		String module = pageNavigationContext.getPageRequest().getHeader().getModule();
		String tokenId = pageNavigationContext.getPageRequest().getHeader().getLoginCredentials();
		String deviceId = pageNavigationContext.getPageRequest().getHeader().getDeviceId();
		Long studentId = pageNavigationContext.getPageRequest().getHeader().getStudentId();
		String isTestUser = "";
		StudentRegistration studReg = regService.getStudentReg(emailId);
		if (studReg != null) {
			isTestUser = studReg.getIsTestUser();
		}
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
		searchRequestData.setIsTestUser(isTestUser);
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
		LocalDate newDate = newsArticleService.getNextAvailablePublicationDate(publicationDate, isTestUser, editionId,
				readingLevel, ArticleType.NEWSARTICLE);
		searchRequestData.setPublicationDate(newDate);
		PublicationPageData pageData = (PublicationPageData) commonService.mapPageData(PublicationPageData.class,
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
		Page<NewsArticleSummaryDTO> pageResult = newsArticleService.findArticles(searchRequestData, studentId, pageNo,
				pageSize, pageNavigationContext.getPageRequest().getHeader());
		if (pageResult == null || pageResult.isLast()) {
			pageData.getPagination().setIsLastPage("Y");
		} else {
			pageData.getPagination().setIsLastPage("N");
		}
		HeaderDTO header = pageNavigationContext.getPageRequest().getHeader();
		header.setPublicationDate(newDate);

		List<PublicationData> publicationData = commonService.mapStudentData(pageResult, studentId);
		if (publicationData.size() == 0) {
			pageData.getPagination().setPageNumber(-1);
		}
		pageData.setNewsArticles(publicationData);
		pageData.setNotificationsCount(
				getUnreadNotificationsCount(module, emailId, studentId, editionId, deviceId, tokenId));
		pageDto.setData(pageData);
		pageDto.setHeader(header);
		return pageDto;
	}

	public PageDTO loadPreviousPublicationPage(PageNavigationContext pageNavigationContext) {
		PageDTO pageDto = new PageDTO();
		String emailId = pageNavigationContext.getPageRequest().getHeader().getEmailId();
		String module = pageNavigationContext.getPageRequest().getHeader().getModule();
		String tokenId = pageNavigationContext.getPageRequest().getHeader().getLoginCredentials();
		String deviceId = pageNavigationContext.getPageRequest().getHeader().getDeviceId();
		Long studentId = pageNavigationContext.getPageRequest().getHeader().getStudentId();
		String isTestUser = "";
		StudentRegistration studReg = regService.getStudentReg(emailId);
		if (studReg != null) {
			isTestUser = studReg.getIsTestUser();
		}
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
		searchRequestData.setIsTestUser(isTestUser);
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
		LocalDate newDate = newsArticleService.getPreviousAvailablePublicationDate(publicationDate, isTestUser,
				editionId, readingLevel, ArticleType.NEWSARTICLE);
		searchRequestData.setPublicationDate(newDate);
		PublicationPageData pageData = (PublicationPageData) commonService.mapPageData(PublicationPageData.class,
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
		Page<NewsArticleSummaryDTO> pageResult = newsArticleService.findArticles(searchRequestData, studentId, pageNo,
				pageSize, pageNavigationContext.getPageRequest().getHeader());
		if (pageResult == null || pageResult.isLast()) {
			pageData.getPagination().setIsLastPage("Y");
		} else {
			pageData.getPagination().setIsLastPage("N");
		}
		HeaderDTO header = pageNavigationContext.getPageRequest().getHeader();
		header.setPublicationDate(newDate);

		List<PublicationData> publicationData = commonService.mapStudentData(pageResult, studentId);
		if (publicationData.size() == 0) {
			pageData.getPagination().setPageNumber(-1);
		}
		pageData.setNewsArticles(publicationData);
		pageData.setNotificationsCount(
				getUnreadNotificationsCount(module, emailId, studentId, editionId, deviceId, tokenId));
		pageDto.setData(pageData);
		pageDto.setHeader(header);
		return pageDto;
	}

	public PageDTO loadDownSwipe(PageNavigationContext pageNavigationContext) {
		PageDTO pageDto = new PageDTO();
		String emailId = pageNavigationContext.getPageRequest().getHeader().getEmailId();
		String module = pageNavigationContext.getPageRequest().getHeader().getModule();
		String tokenId = pageNavigationContext.getPageRequest().getHeader().getLoginCredentials();
		String deviceId = pageNavigationContext.getPageRequest().getHeader().getDeviceId();
		Long studentId = pageNavigationContext.getPageRequest().getHeader().getStudentId();
		String isTestUser = "";
		StudentRegistration studReg = regService.getStudentReg(emailId);
		if (studReg != null) {
			isTestUser = studReg.getIsTestUser();
		}
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
		searchRequestData.setIsTestUser(isTestUser);
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
		searchRequestData.setPublicationDate(publicationDate);
		PublicationPageData pageData = (PublicationPageData) commonService.mapPageData(PublicationPageData.class,
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
		Page<NewsArticleSummaryDTO> pageResult = newsArticleService.findArticles(searchRequestData, studentId, pageNo,
				pageSize, pageNavigationContext.getPageRequest().getHeader());
		if (pageResult == null || pageResult.isLast()) {
			pageData.getPagination().setIsLastPage("Y");
		} else {
			pageData.getPagination().setIsLastPage("N");
		}
		HeaderDTO header = pageNavigationContext.getPageRequest().getHeader();
		List<PublicationData> publicationData = commonService.mapStudentData(pageResult, studentId);
		if (publicationData.size() == 0) {
			pageData.getPagination().setPageNumber(-1);
		}
		pageData.setNewsArticles(publicationData);
		pageData.setNotificationsCount(
				getUnreadNotificationsCount(module, emailId, studentId, editionId, deviceId, tokenId));
		pageDto.setData(pageData);
		pageDto.setHeader(header);
		return pageDto;
	}

	public PageDTO loadUpSwipe(PageNavigationContext pageNavigationContext) {
		PageDTO pageDto = new PageDTO();
		String emailId = pageNavigationContext.getPageRequest().getHeader().getEmailId();
		String module = pageNavigationContext.getPageRequest().getHeader().getModule();
		String tokenId = pageNavigationContext.getPageRequest().getHeader().getLoginCredentials();
		String deviceId = pageNavigationContext.getPageRequest().getHeader().getDeviceId();
		Long studentId = pageNavigationContext.getPageRequest().getHeader().getStudentId();
		String isTestUser = "";
		StudentRegistration studReg = regService.getStudentReg(emailId);
		if (studReg != null) {
			isTestUser = studReg.getIsTestUser();
		}
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
		searchRequestData.setIsTestUser(isTestUser);
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
		searchRequestData.setPublicationDate(publicationDate);
		PublicationPageData pageData = (PublicationPageData) commonService.mapPageData(PublicationPageData.class,
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
		Page<NewsArticleSummaryDTO> pageResult = newsArticleService.findArticles(searchRequestData, studentId, pageNo,
				pageSize, pageNavigationContext.getPageRequest().getHeader());
		if (pageResult == null || pageResult.isLast()) {
			pageData.getPagination().setIsLastPage("Y");
		} else {
			pageData.getPagination().setIsLastPage("N");
		}
		HeaderDTO header = pageNavigationContext.getPageRequest().getHeader();
		List<PublicationData> publicationData = commonService.mapStudentData(pageResult, studentId);
		if (publicationData.size() == 0) {
			pageData.getPagination().setPageNumber(-1);
		}
		pageData.setNewsArticles(publicationData);
		pageData.setNotificationsCount(
				getUnreadNotificationsCount(module, emailId, studentId, editionId, deviceId, tokenId));
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
}
