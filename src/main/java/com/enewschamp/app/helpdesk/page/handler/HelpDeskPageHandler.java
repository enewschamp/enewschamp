package com.enewschamp.app.helpdesk.page.handler;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enewschamp.app.article.page.handler.NewsArticlePageHandler;
import com.enewschamp.app.common.ErrorCodeConstants;
import com.enewschamp.app.common.PageDTO;
import com.enewschamp.app.common.PageRequestDTO;
import com.enewschamp.app.common.PropertyConstants;
import com.enewschamp.app.fw.page.navigation.dto.PageNavigatorDTO;
import com.enewschamp.app.helpdesk.dto.HelpDeskDTO;
import com.enewschamp.app.helpdesk.entity.HelpDesk;
import com.enewschamp.app.helpdesk.page.data.HelpDeskInputPageData;
import com.enewschamp.app.helpdesk.service.HelpDeskService;
import com.enewschamp.app.holiday.service.HolidayService;
import com.enewschamp.app.workinghours.service.WorkingHoursService;
import com.enewschamp.common.domain.service.PropertiesService;
import com.enewschamp.domain.common.IPageHandler;
import com.enewschamp.domain.common.PageNavigationContext;
import com.enewschamp.domain.common.RecordInUseType;
import com.enewschamp.domain.service.LOVService;
import com.enewschamp.page.dto.ListOfValuesItem;
import com.enewschamp.problem.BusinessException;
import com.enewschamp.subscription.domain.business.StudentControlBusiness;
import com.enewschamp.subscription.domain.business.StudentDetailsBusiness;
import com.enewschamp.subscription.domain.business.SubscriptionBusiness;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component(value = "HelpDeskPageHandler")
public class HelpDeskPageHandler implements IPageHandler {

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	SubscriptionBusiness subscriptionBusiness;

	@Autowired
	StudentControlBusiness studentControlBusiness;

	@Autowired
	ObjectMapper objectMapper;

	@Autowired
	NewsArticlePageHandler newsArticlePageHandler;

	@Autowired
	private PropertiesService propertiesService;

	@Autowired
	private HolidayService holidayService;

	@Autowired
	private WorkingHoursService workingHourService;

	@Autowired
	private LOVService lovService;

	@Autowired
	StudentDetailsBusiness studentDetails;

	@Autowired
	SubscriptionBusiness SubscriptionBusiness;

	@Autowired
	HelpDeskService helpDeskService;

	@Override
	public PageDTO handleAction(PageRequestDTO pageRequest) {
		return null;
	}

	@Override
	public PageDTO loadPage(PageNavigationContext pageNavigationContext) {
		PageDTO pageDto = new PageDTO();
		pageDto.setHeader(pageNavigationContext.getPageRequest().getHeader());
		pageDto.setData(null);
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
					e.printStackTrace();
				}
			} catch (SecurityException e) {
				e.printStackTrace();
			}
		}
		PageDTO pageDTO = new PageDTO();
		pageDTO.setHeader(pageRequest.getHeader());
		return pageDTO;
	}

	public PageDTO handleSaveAction(PageRequestDTO pageRequest, PageNavigatorDTO pageNavigatorDTO) {
		PageDTO pageDto = new PageDTO();
		pageDto.setHeader(pageRequest.getHeader());
		String editionId = pageRequest.getHeader().getEditionId();
		String emailId = pageRequest.getHeader().getEmailId();
		Long studentId = studentControlBusiness.getStudentId(emailId);
		if (studentId == null || studentId == 0L) {
			throw new BusinessException(ErrorCodeConstants.STUDENT_DTLS_NOT_FOUND);
		}
		HelpDeskInputPageData helpDeskInputPageData = null;

		try {
			helpDeskInputPageData = objectMapper.readValue(pageRequest.getData().toString(),
					HelpDeskInputPageData.class);
			HelpDeskDTO helpDeskDTO = new HelpDeskDTO();
			helpDeskDTO.setCategoryId(helpDeskInputPageData.getCategory());
			helpDeskDTO.setDetails(helpDeskInputPageData.getDetails());
			helpDeskDTO.setEditionId(editionId);
			helpDeskDTO.setPhoneNumber(helpDeskInputPageData.getPhoneNumber());
			String preferredDate = "2000-01-01";
			String prefererdTime = "00:00";
			if (helpDeskInputPageData.getPreferredDate() != null) {
				preferredDate = helpDeskInputPageData.getPreferredDate().toString();
			}
			if (helpDeskInputPageData.getPreferredTime() != null
					&& (!"".equals(helpDeskInputPageData.getPreferredTime().trim()))) {
				prefererdTime = helpDeskInputPageData.getPreferredTime();
			}
			String preferredCallback = preferredDate + " " + prefererdTime;
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
			LocalDateTime callbackTime = LocalDateTime.parse(preferredCallback, formatter);
			helpDeskDTO.setCallBackTime(callbackTime);
			helpDeskDTO.setStudentId(studentId);
			helpDeskDTO.setRecordInUse(RecordInUseType.Y);
			helpDeskDTO.setOperatorId("" + studentId);
			helpDeskDTO.setCreateDateTime(LocalDateTime.now());
			HelpDesk helpdesk = modelMapper.map(helpDeskDTO, HelpDesk.class);
			helpdesk = helpDeskService.create(helpdesk);
		} catch (Exception e) {
			throw new BusinessException(ErrorCodeConstants.RUNTIME_EXCEPTION, e.getMessage());
		}
		return pageDto;
	}

	private List<LocalDate> getWorkingDays(String editionId) {
		int workingDays = Integer.valueOf(propertiesService.getValue(PropertyConstants.WORKING_DAYS));
		List<LocalDate> dates = new ArrayList<LocalDate>();
		LocalDate currentDate = LocalDate.now();
		for (int i = 1; i <= workingDays; i++) {
			if (dates.size() >= workingDays) {
				break;
			}
			if (!holidayService.isHoliday(currentDate, editionId)) {
				dates.add(currentDate);
				currentDate = currentDate.plusDays(1);
			} else {
				workingDays += 1;
				currentDate = currentDate.plusDays(1);
			}
		}
		return dates;
	}

	public List<ListOfValuesItem> getCategoryLov(String type) {
		List<ListOfValuesItem> lovList = lovService.getLOV(type);
		return lovList;
	}
}
