package com.enewschamp.app.notification.page.handler;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import com.enewschamp.app.article.page.dto.NotificationPageData;
import com.enewschamp.app.article.page.handler.NewsArticlePageHandler;
import com.enewschamp.app.common.CommonFilterData;
import com.enewschamp.app.common.CommonService;
import com.enewschamp.app.common.ErrorCodeConstants;
import com.enewschamp.app.common.HeaderDTO;
import com.enewschamp.app.common.PageDTO;
import com.enewschamp.app.common.PageRequestDTO;
import com.enewschamp.app.common.PaginationData;
import com.enewschamp.app.common.PropertyConstants;
import com.enewschamp.app.fw.page.navigation.dto.PageNavigatorDTO;
import com.enewschamp.app.notification.page.data.NotificationsPageData;
import com.enewschamp.app.notification.page.data.NotificationsSearchRequest;
import com.enewschamp.app.student.notification.ReadFlag;
import com.enewschamp.app.student.notification.StudentNotification;
import com.enewschamp.app.student.notification.StudentNotificationDTO;
import com.enewschamp.app.student.notification.StudentNotificationData;
import com.enewschamp.app.student.notification.service.StudentNotificationService;
import com.enewschamp.common.domain.service.PropertiesBackendService;
import com.enewschamp.domain.common.IPageHandler;
import com.enewschamp.domain.common.PageNavigationContext;
import com.enewschamp.domain.common.RecordInUseType;
import com.enewschamp.problem.BusinessException;
import com.enewschamp.subscription.domain.business.StudentControlBusiness;
import com.enewschamp.subscription.domain.business.SubscriptionBusiness;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component(value = "NotificationsPageHandler")
public class NotificationsPageHandler implements IPageHandler {

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
	NewsArticlePageHandler newsArticlePageHandler;

	@Autowired
	StudentNotificationService studentNotificationService;

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

	public PageDTO loadNotificationsPage(PageNavigationContext pageNavigationContext) {
		PageDTO pageDto = new PageDTO();
		pageDto.setHeader(pageNavigationContext.getPageRequest().getHeader());
		String emailId = pageNavigationContext.getPageRequest().getHeader().getEmailId();
		Long studentId = studentControlBusiness.getStudentId(emailId);
		String editionId = pageNavigationContext.getPageRequest().getHeader().getEditionId();
		String module = pageNavigationContext.getPageRequest().getHeader().getModule();
		NotificationsPageData pageData = new NotificationsPageData();
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
		NotificationsSearchRequest searchRequest = new NotificationsSearchRequest();
		searchRequest.setEditionId(editionId);
		searchRequest.setStudentId(studentId);
		String limitDate = commonService.getLimitDate(module, PropertyConstants.VIEW_LIMIT_NOTIFICATIONS, emailId)
				+ " 00:00";
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		searchRequest.setOperationDateTime(LocalDateTime.parse(limitDate, formatter));
		if (filterData != null) {
			searchRequest.setIsRead(filterData.getRead());
		}
		Page<StudentNotificationDTO> pageResult = studentNotificationService.getNotificationList(searchRequest, pageNo,
				pageSize);
		if (pageResult == null || pageResult.isLast()) {
			pageData.getPagination().setIsLastPage("Y");
		} else {
			pageData.getPagination().setIsLastPage("N");
		}
		HeaderDTO header = pageNavigationContext.getPageRequest().getHeader();
		pageDto.setHeader(header);
		List<StudentNotificationData> notificationsList = mapData(pageResult);
		pageData.setNotifications(notificationsList);
		if (notificationsList == null || notificationsList.size() == 0) {
			pageData.getPagination().setPageNumber(-1);
		}
		pageData.setFilter(filterData);
		pageDto.setData(pageData);
		return pageDto;
	}

	public PageDTO loadResponsePage(PageNavigationContext pageNavigationContext) {
		PageDTO pageDto = new PageDTO();
		pageDto.setHeader(pageNavigationContext.getPageRequest().getHeader());
		pageDto.setData(pageNavigationContext.getPreviousPageResponse().getData());
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
		pageDTO.setHeader(pageRequest.getHeader());
		return pageDTO;
	}

	public PageDTO handleReadAction(PageRequestDTO pageRequest, PageNavigatorDTO pageNavigatorDTO) {
		PageDTO pageDTO = new PageDTO();
		NotificationPageData notificationPageData = new NotificationPageData();
		notificationPageData = mapPageData(notificationPageData, pageRequest);
		Long studentNotificationId = notificationPageData.getStudentNotificationId();
		StudentNotification studentNotification = studentNotificationService.get(studentNotificationId);
		if (studentNotification != null) {
			studentNotification.setReadDateTime(LocalDateTime.now());
			studentNotification.setReadFlag(ReadFlag.Y);
			studentNotificationService.update(studentNotification);
		} else {
			throw new BusinessException(ErrorCodeConstants.STUDENT_NOTIFICATION_NOT_FOUND,
					String.valueOf(studentNotificationId));
		}
		pageDTO.setData(notificationPageData);
		pageDTO.setHeader(pageRequest.getHeader());
		return pageDTO;
	}

	public PageDTO handleDeleteAction(PageRequestDTO pageRequest, PageNavigatorDTO pageNavigatorDTO) {
		PageDTO pageDTO = new PageDTO();
		NotificationPageData notificationPageData = new NotificationPageData();
		notificationPageData = mapPageData(notificationPageData, pageRequest);
		Long studentNotificationId = notificationPageData.getStudentNotificationId();
		StudentNotification studentNotification = studentNotificationService.get(studentNotificationId);
		if (studentNotification != null) {
			studentNotification.setRecordInUse(RecordInUseType.N);
			studentNotificationService.update(studentNotification);
		} else {
			throw new BusinessException(ErrorCodeConstants.STUDENT_NOTIFICATION_NOT_FOUND,
					String.valueOf(studentNotificationId));
		}
		pageDTO.setData(notificationPageData);
		pageDTO.setHeader(pageRequest.getHeader());
		return pageDTO;
	}

	private NotificationsPageData mapPageData(NotificationsPageData pageData, PageRequestDTO pageRequest) {
		try {
			pageData = objectMapper.readValue(pageRequest.getData().toString(), NotificationsPageData.class);
		} catch (JsonParseException e) {
			throw new RuntimeException(e);
		} catch (JsonMappingException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return pageData;
	}

	private NotificationPageData mapPageData(NotificationPageData pageData, PageRequestDTO pageRequest) {
		try {
			pageData = objectMapper.readValue(pageRequest.getData().toString(), NotificationPageData.class);
		} catch (JsonParseException e) {
			throw new RuntimeException(e);
		} catch (JsonMappingException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return pageData;
	}

	public List<StudentNotificationData> mapData(Page<StudentNotificationDTO> page) {
		List<StudentNotificationData> opinionPageDataList = new ArrayList<StudentNotificationData>();
		if (page != null && page.getContent() != null && page.getContent().size() > 0) {
			List<StudentNotificationDTO> pageDataList = page.getContent();
			for (StudentNotificationDTO article : pageDataList) {
				StudentNotificationData publicationData = new StudentNotificationData();
				publicationData = modelMapper.map(article, StudentNotificationData.class);
				opinionPageDataList.add(publicationData);
			}
		}
		return opinionPageDataList;
	}
}