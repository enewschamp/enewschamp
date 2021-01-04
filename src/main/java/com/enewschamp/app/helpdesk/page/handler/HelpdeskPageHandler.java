package com.enewschamp.app.helpdesk.page.handler;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enewschamp.app.article.page.handler.NewsArticlePageHandler;
import com.enewschamp.app.common.ErrorCodeConstants;
import com.enewschamp.app.common.PageDTO;
import com.enewschamp.app.common.PageRequestDTO;
import com.enewschamp.app.fw.page.navigation.dto.PageNavigatorDTO;
import com.enewschamp.app.helpdesk.dto.HelpdeskDTO;
import com.enewschamp.app.helpdesk.entity.Helpdesk;
import com.enewschamp.app.helpdesk.page.data.HelpdeskInputPageData;
import com.enewschamp.app.helpdesk.service.HelpdeskService;
import com.enewschamp.domain.common.IPageHandler;
import com.enewschamp.domain.common.PageNavigationContext;
import com.enewschamp.domain.common.RecordInUseType;
import com.enewschamp.problem.BusinessException;
import com.enewschamp.subscription.domain.business.StudentControlBusiness;
import com.enewschamp.subscription.domain.business.StudentDetailsBusiness;
import com.enewschamp.subscription.domain.business.SubscriptionBusiness;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component(value = "HelpdeskPageHandler")
public class HelpdeskPageHandler implements IPageHandler {

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
	StudentDetailsBusiness studentDetails;

	@Autowired
	SubscriptionBusiness SubscriptionBusiness;

	@Autowired
	HelpdeskService helpdeskService;

	@Override
	public PageDTO handleAction(PageRequestDTO pageRequest) {
		return null;
	}

	@Override
	public PageDTO loadPage(PageNavigationContext pageNavigationContext) {
		PageDTO pageDto = new PageDTO();
		String emailId = pageNavigationContext.getPageRequest().getHeader().getEmailId();
		Long studentId = studentControlBusiness.getStudentId(emailId);
		HelpdeskInputPageData helpDeskInputPageData = new HelpdeskInputPageData();
		Helpdesk helpDesk = helpdeskService.getByStudentId(studentId);
		if (helpDesk != null) {
			helpDeskInputPageData.setCategory(helpDesk.getCategoryId());
			helpDeskInputPageData.setDetails(helpDesk.getDetails());
			helpDeskInputPageData.setPhoneNumber(helpDesk.getPhoneNumber());
			helpDeskInputPageData.setPreferredTime("" + helpDesk.getCallbackDateTime());
		}
		pageDto.setHeader(pageNavigationContext.getPageRequest().getHeader());
		pageDto.setData(helpDeskInputPageData);
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
		HelpdeskInputPageData helpdeskInputPageData = null;
		try {
			helpdeskInputPageData = objectMapper.readValue(pageRequest.getData().toString(),
					HelpdeskInputPageData.class);
			HelpdeskDTO helpdeskDTO = new HelpdeskDTO();
			helpdeskDTO.setCategoryId(helpdeskInputPageData.getCategory());
			helpdeskDTO.setDetails(helpdeskInputPageData.getDetails());
			helpdeskDTO.setEditionId(editionId);
			helpdeskDTO.setPhoneNumber(helpdeskInputPageData.getPhoneNumber());
			String preferredDate = "2000-01-01";
			String prefererdTime = "00:00";
			if (helpdeskInputPageData.getPreferredDate() != null) {
				preferredDate = helpdeskInputPageData.getPreferredDate().toString();
			}
			if (helpdeskInputPageData.getPreferredTime() != null
					&& (!"".equals(helpdeskInputPageData.getPreferredTime().trim()))) {
				prefererdTime = helpdeskInputPageData.getPreferredTime();
			}
			String preferredCallback = preferredDate + " " + prefererdTime;
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
			LocalDateTime callbackDateTime = LocalDateTime.parse(preferredCallback, formatter);
			helpdeskDTO.setCallbackDateTime(callbackDateTime);
			helpdeskDTO.setStudentId(studentId);
			helpdeskDTO.setRecordInUse(RecordInUseType.Y);
			helpdeskDTO.setOperatorId("" + studentId);
			helpdeskDTO.setCreateDateTime(LocalDateTime.now());
			Helpdesk helpdesk = modelMapper.map(helpdeskDTO, Helpdesk.class);
			Helpdesk helpDeskExisting = helpdeskService.getByStudentId(studentId);
			if (helpDeskExisting != null) {
				helpdesk.setHelpdeskId(helpDeskExisting.getHelpdeskId());
			}
			helpdesk = helpdeskService.create(helpdesk);
		} catch (Exception e) {
			throw new BusinessException(ErrorCodeConstants.RUNTIME_EXCEPTION, e.getMessage());
		}
		return pageDto;
	}
}
