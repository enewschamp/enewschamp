package com.enewschamp.subscription.page.handler;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enewschamp.app.common.ErrorCodeConstants;
import com.enewschamp.app.common.PageDTO;
import com.enewschamp.app.common.PageRequestDTO;
import com.enewschamp.app.fw.page.navigation.common.PageSaveTable;
import com.enewschamp.app.fw.page.navigation.dto.PageNavigatorDTO;
import com.enewschamp.app.student.registration.business.StudentRegistrationBusiness;
import com.enewschamp.app.student.registration.service.StudentRegistrationService;
import com.enewschamp.app.user.login.service.UserLoginBusiness;
import com.enewschamp.domain.common.IPageHandler;
import com.enewschamp.domain.common.PageNavigationContext;
import com.enewschamp.domain.common.RecordInUseType;
import com.enewschamp.problem.BusinessException;
import com.enewschamp.subscription.app.dto.StudentControlDTO;
import com.enewschamp.subscription.app.dto.StudentControlWorkDTO;
import com.enewschamp.subscription.app.dto.StudentSubscriptionPageData;
import com.enewschamp.subscription.domain.business.StudentControlBusiness;
import com.enewschamp.subscription.domain.business.SubscriptionBusiness;
import com.enewschamp.subscription.domain.entity.StudentControlWork;
import com.enewschamp.subscription.domain.service.StudentSubscriptionWorkService;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component(value = "SubscriptionPageHandler")
public class SubscriptionPageHandler implements IPageHandler {

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	SubscriptionBusiness subscriptionBusiness;

	@Autowired
	StudentControlBusiness studentControlBusiness;

	@Autowired
	StudentSubscriptionWorkService studentSubscriptionWorkService;

	@Autowired
	ObjectMapper objectMapper;

	@Autowired
	UserLoginBusiness userLoginBusiness;

	@Autowired
	StudentRegistrationService regService;

	@Autowired
	StudentRegistrationBusiness studentRegBusiness;

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

	public PageDTO loadSubscriptionPage(PageNavigationContext pageNavigationContext) {
		PageDTO pageDto = new PageDTO();
		StudentSubscriptionPageData subscripionPagedata = new StudentSubscriptionPageData();
		String emailId = pageNavigationContext.getPageRequest().getData().get("emailId").asText();
		if (emailId != null) {
			subscripionPagedata.setEmailId(emailId);
			pageNavigationContext.getPageRequest().getHeader().setEmailId(emailId);
		}
		pageDto.setData(subscripionPagedata);
		pageDto.setHeader(pageNavigationContext.getPageRequest().getHeader());
		return pageDto;
	}

	public PageDTO loadWorkDataPage(PageNavigationContext pageNavigationContext) {
		PageDTO pageDto = new PageDTO();
		pageDto.setHeader(pageNavigationContext.getPageRequest().getHeader());
		String emailId = pageNavigationContext.getPageRequest().getHeader().getEmailId();
		StudentControlWorkDTO studentControlWorkDTO = studentControlBusiness.getStudentFromWork(emailId);
		StudentSubscriptionPageData subscripionPagedata = new StudentSubscriptionPageData();
		if (studentControlWorkDTO != null) {
			subscripionPagedata.setEmailId(studentControlWorkDTO.getEmailId());
			subscripionPagedata.setSubscriptionSelected(studentControlWorkDTO.getSubscriptionTypeW());
		}
		pageDto.setData(subscripionPagedata);
		return pageDto;
	}

	public PageDTO loadExistingSubscriptionPage(PageNavigationContext pageNavigationContext) {
		PageDTO pageDto = new PageDTO();
		pageDto.setHeader(pageNavigationContext.getPageRequest().getHeader());
		String emailId = pageNavigationContext.getPageRequest().getHeader().getEmailId();
		StudentControlDTO studentControlDTO = studentControlBusiness.getStudentFromMaster(emailId);
		StudentSubscriptionPageData subscripionPagedata = new StudentSubscriptionPageData();
		if (studentControlDTO != null) {
			subscripionPagedata.setEmailId(studentControlDTO.getEmailId());
			subscripionPagedata.setSubscriptionSelected(studentControlDTO.getSubscriptionType());
		}
		pageDto.setData(subscripionPagedata);
		return pageDto;
	}

	@Override
	public PageDTO saveAsMaster(PageRequestDTO pageRequest) {
		PageDTO pageDTO = new PageDTO();
		String emailId = pageRequest.getHeader().getEmailId();
		String editionId = pageRequest.getHeader().getEditionId();
		Long studentId = studentControlBusiness.getStudentId(emailId);
		subscriptionBusiness.workToMaster(pageRequest.getHeader().getModule(), studentId, editionId);
		studentSubscriptionWorkService.delete(studentId);
		pageDTO.setHeader(pageRequest.getHeader());
		return pageDTO;
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

	public PageDTO handleNextAction(PageRequestDTO pageRequest, PageNavigatorDTO pageNavigatorDTO) {
		PageDTO pageDTO = new PageDTO();
		String operation = pageRequest.getHeader().getOperation();
		String editionId = pageRequest.getHeader().getEditionId();
		String emailId = pageRequest.getHeader().getEmailId();
		String saveIn = pageNavigatorDTO.getUpdationTable();
		String evalAvailed = "";
		StudentSubscriptionPageData subscripionPagedata = mapPagedata(pageRequest);
		studentRegBusiness.checkAndUpdateIfEvalPeriodExpired(emailId, editionId);
		Long studentId = 0L;
		if (PageSaveTable.W.toString().equals(saveIn)) {
			StudentControlWorkDTO studentControlWorkDTO = studentControlBusiness.getStudentFromWork(emailId);
			if (studentControlWorkDTO != null) {
				studentId = studentControlWorkDTO.getStudentId();
				if ("Y".equalsIgnoreCase(studentControlWorkDTO.getEvalAvailed())) {
					evalAvailed = studentControlWorkDTO.getEvalAvailed();
				}
			}
			studentControlWorkDTO.setSubscriptionTypeW(subscripionPagedata.getSubscriptionSelected());
			if ("Subscription".equalsIgnoreCase(operation)
					&& "P".equalsIgnoreCase(subscripionPagedata.getSubscriptionSelected())) {
				studentControlWorkDTO.setNextPageOperation("PremiumSubs");
			} else if ("Subscription".equalsIgnoreCase(operation)
					&& "S".equalsIgnoreCase(subscripionPagedata.getSubscriptionSelected())) {
				studentControlWorkDTO.setNextPageOperation("SchoolSubs");
			}
			studentControlWorkDTO.setOperatorId(emailId);
			studentControlWorkDTO.setRecordInUse(RecordInUseType.Y);
			StudentControlWork studentControlWork = studentControlBusiness.saveAsWork(studentControlWorkDTO);
			studentId = studentControlWork.getStudentId();
			subscriptionBusiness.saveAsWork(studentId, evalAvailed, pageRequest);
		}
		pageDTO.setHeader(pageRequest.getHeader());
		return pageDTO;
	}

	public StudentSubscriptionPageData mapPagedata(PageRequestDTO pageRequest) {
		StudentSubscriptionPageData studentSubscriptionPageData = null;
		try {
			studentSubscriptionPageData = objectMapper.readValue(pageRequest.getData().toString(),
					StudentSubscriptionPageData.class);
		} catch (IOException e) {
			throw new BusinessException(ErrorCodeConstants.SREVER_ERROR);
		}
		return studentSubscriptionPageData;
	}
}
