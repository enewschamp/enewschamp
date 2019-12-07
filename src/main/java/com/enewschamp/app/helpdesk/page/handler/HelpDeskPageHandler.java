package com.enewschamp.app.helpdesk.page.handler;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enewschamp.EnewschampApplicationProperties;
import com.enewschamp.app.article.page.handler.NewsArticlePageHandler;
import com.enewschamp.app.common.ErrorCodes;
import com.enewschamp.app.common.PageDTO;
import com.enewschamp.app.common.PageRequestDTO;
import com.enewschamp.app.fw.page.navigation.common.PageAction;
import com.enewschamp.app.fw.page.navigation.dto.PageNavigatorDTO;
import com.enewschamp.app.helpdesk.dto.HelpDeskDTO;
import com.enewschamp.app.helpdesk.entity.HelpDesk;
import com.enewschamp.app.helpdesk.page.data.HelpDeskInputPageData;
import com.enewschamp.app.helpdesk.page.data.HelpDeskPageData;
import com.enewschamp.app.helpdesk.service.HelpDeskService;
import com.enewschamp.app.holiday.service.HolidayService;
import com.enewschamp.app.workinghours.entity.WorkingHours;
import com.enewschamp.app.workinghours.service.WorkingHoursService;
import com.enewschamp.domain.common.IPageHandler;
import com.enewschamp.domain.common.PageNavigationContext;
import com.enewschamp.domain.common.RecordInUseType;
import com.enewschamp.page.dto.ListOfValuesItem;
import com.enewschamp.problem.BusinessException;
import com.enewschamp.subscription.app.dto.StudentDetailsDTO;
import com.enewschamp.subscription.app.dto.StudentSubscriptionDTO;
import com.enewschamp.subscription.domain.business.StudentControlBusiness;
import com.enewschamp.subscription.domain.business.StudentDetailsBusiness;
import com.enewschamp.subscription.domain.business.SubscriptionBusiness;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.enewschamp.domain.service.LOVService;

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
	private EnewschampApplicationProperties appConfig;

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
	public PageDTO handleAction(String actionName, PageRequestDTO pageRequest) {
		return null;
	}

	@Override
	public PageDTO loadPage(PageNavigationContext pageNavigationContext) {
		PageDTO pageDto = new PageDTO();
		pageDto.setHeader(pageNavigationContext.getPageRequest().getHeader());

		String action = pageNavigationContext.getActionName();
		String editionId = pageNavigationContext.getPageRequest().getHeader().getEditionId();
		String emailId = pageNavigationContext.getPageRequest().getHeader().getEmailId();

		Long studentId = studentControlBusiness.getStudentId(emailId);
		if (studentId == null || studentId == 0L) {
			throw new BusinessException(ErrorCodes.STUDENT_DTLS_NOT_FOUND);
		}

		if (PageAction.home.toString().equalsIgnoreCase(action)) {
			pageDto = newsArticlePageHandler.loadPage(pageNavigationContext);

		}
		if (PageAction.HelpDesk.toString().equalsIgnoreCase(action)) {
			List<LocalDate> workingDays = getWorkingDays(editionId);
			WorkingHours workingHours = workingHourService.getWorkingHours(editionId);
			String incompleteFormText = appConfig.getIncompleteFormText();
			String helpDeskText = appConfig.getHelpDeskText();
			String helpdeskEmailId = appConfig.getHelpDeskEmail();

			List<ListOfValuesItem> categoryLOV = getCategoryLov("HelpDeskCategory");
			StudentDetailsDTO studentDto = studentDetails.getStudentDetailsFromMaster(studentId);
			String mobileNo = "";
			if (studentDto != null) {
				mobileNo = studentDto.getMobileNumber();
			}
			HelpDeskPageData pageData = new HelpDeskPageData();
			pageData.setEmailId(helpdeskEmailId);
			pageData.setStudentMobile(mobileNo);
			pageData.setHelpDeskText(helpDeskText);
			pageData.setIncompleteFormText(incompleteFormText);
			pageData.setNext7WorkingDays(workingDays);
			pageData.setWorkinghHoursFrom(workingHours.getStartTime());
			pageData.setWorkingHoursTo(workingHours.getEndTime());
			pageData.setCategoryLOV(categoryLOV);

			pageDto.setData(pageData);

		}

		return pageDto;
	}

	@Override
	public PageDTO saveAsMaster(String actionName, PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();

		return pageDto;
	}

	@Override
	public PageDTO handleAppAction(String actionName, PageRequestDTO pageRequest, PageNavigatorDTO pageNavigatorDTO) {
		PageDTO pageDto = new PageDTO();
		pageDto.setHeader(pageRequest.getHeader());

		String action = actionName;
		String editionId = pageRequest.getHeader().getEditionId();
		String emailId = pageRequest.getHeader().getEmailId();
		Long studentId = studentControlBusiness.getStudentId(emailId);
		if (studentId == null || studentId == 0L) {
			throw new BusinessException(ErrorCodes.STUDENT_DTLS_NOT_FOUND);
		}
		if (PageAction.save.toString().equalsIgnoreCase(action)) {
			HelpDeskInputPageData helpDeskInputPageData = null;

			try {
				helpDeskInputPageData = objectMapper.readValue(pageRequest.getData().toString(),
						HelpDeskInputPageData.class);

			} catch (JsonParseException e) {
				throw new RuntimeException(e);
			} catch (JsonMappingException e) {
				throw new RuntimeException(e);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}

			HelpDeskDTO helpDeskDTO = new HelpDeskDTO();
			helpDeskDTO.setCategoryId(helpDeskInputPageData.getCategoryId());
			helpDeskDTO.setDetails(helpDeskInputPageData.getDetails());
			helpDeskDTO.setEditionId(editionId);

			StudentSubscriptionDTO subscriptionDto = SubscriptionBusiness.getStudentSubscriptionFromMaster(studentId,
					editionId);

			String subsType = subscriptionDto.getSubscriptionSelected();
			if (!"S".equals(subsType)) {
				helpDeskDTO.setCallbackRequest("Y");
			}
			if (helpDeskDTO.getCallbackRequest() != null) {
				if (helpDeskInputPageData.getPhoneNumber() == null) {
					throw new BusinessException(ErrorCodes.MOBILE_IS_MANDATORY);
				}
			}
			helpDeskDTO.setPhoneNumber(helpDeskInputPageData.getPhoneNumber());

			String preferredDate = helpDeskInputPageData.getPreferredDate().toString();
			String prefererdTime = helpDeskInputPageData.getPreferredTime();
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

		}
		return pageDto;
	}

	private List<LocalDate> getWorkingDays(String editionId) {
		int workingDays = appConfig.getWorkingDays();
		List<LocalDate> dates = new ArrayList<LocalDate>();
		LocalDate currentDate = LocalDate.now();
		// dates.add(currentDate);

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
