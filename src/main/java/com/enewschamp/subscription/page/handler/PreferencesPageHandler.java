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
import com.enewschamp.domain.common.IPageHandler;
import com.enewschamp.domain.common.PageNavigationContext;
import com.enewschamp.domain.common.RecordInUseType;
import com.enewschamp.problem.BusinessException;
import com.enewschamp.subscription.app.dto.StudentControlDTO;
import com.enewschamp.subscription.app.dto.StudentControlWorkDTO;
import com.enewschamp.subscription.app.dto.StudentPreferencesCommPageData;
import com.enewschamp.subscription.app.dto.StudentPreferencesDTO;
import com.enewschamp.subscription.app.dto.StudentPreferencesPageData;
import com.enewschamp.subscription.app.dto.StudentPreferencesWorkDTO;
import com.enewschamp.subscription.domain.business.PreferenceBusiness;
import com.enewschamp.subscription.domain.business.StudentControlBusiness;
import com.enewschamp.subscription.domain.business.SubscriptionBusiness;
import com.enewschamp.subscription.domain.service.StudentPreferencesWorkService;
import com.enewschamp.subscription.domain.service.StudentSubscriptionWorkService;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component(value = "PreferencesPageHandler")
public class PreferencesPageHandler implements IPageHandler {

	@Autowired
	ObjectMapper objectMapper;

	@Autowired
	StudentControlBusiness studentControlBusiness;

	@Autowired
	PreferenceBusiness preferenceBusiness;

	@Autowired
	StudentPreferencesWorkService studentPreferencesWorkService;

	@Autowired
	SubscriptionBusiness subscriptionBusiness;

	@Autowired
	StudentSubscriptionWorkService studentSubscriptionWorkService;

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	StudentRegistrationBusiness studentRegBusiness;

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

	public PageDTO loadPreferencesPage(PageNavigationContext pageNavigationContext) {
		PageDTO pageDto = new PageDTO();
		String emailId = pageNavigationContext.getPageRequest().getHeader().getEmailId();
		StudentPreferencesPageData studentPreferencePageData = new StudentPreferencesPageData();
		StudentPreferencesCommPageData communications = new StudentPreferencesCommPageData();
		communications.setCommsEmailId(emailId);
		communications.setAlertsNotifications("N");
		communications.setDailyPublication("N");
		communications.setScoresProgressReports("N");
		studentPreferencePageData.setCommsOverEmail(communications);
		pageDto.setData(studentPreferencePageData);
		pageDto.setHeader(pageNavigationContext.getPageRequest().getHeader());
		return pageDto;
	}

	public PageDTO loadExistingPreferencesPage(PageNavigationContext pageNavigationContext) {
		PageDTO pageDto = new PageDTO();
		String emailId = pageNavigationContext.getPageRequest().getHeader().getEmailId();
		Long studentId = studentControlBusiness.getStudentId(emailId);
		StudentPreferencesPageData studentPreferencePageData = new StudentPreferencesPageData();
		StudentPreferencesDTO studentPreferencesDTO = preferenceBusiness.getPreferenceFromMaster(studentId);
		if (studentPreferencesDTO != null) {
			studentPreferencePageData = modelMapper.map(studentPreferencesDTO, StudentPreferencesPageData.class);
		}
		pageDto.setData(studentPreferencePageData);
		pageDto.setHeader(pageNavigationContext.getPageRequest().getHeader());
		return pageDto;
	}

	public PageDTO loadWorkDataPage(PageNavigationContext pageNavigationContext) {
		PageDTO pageDto = new PageDTO();
		String emailId = pageNavigationContext.getPageRequest().getHeader().getEmailId();
		Long studentId = studentControlBusiness.getStudentId(emailId);
		StudentPreferencesPageData studentPreferencePageData = new StudentPreferencesPageData();
		StudentPreferencesWorkDTO studentPreferencesWorkDTO = preferenceBusiness.getPreferenceFromWork(studentId);
		if (studentPreferencesWorkDTO != null) {
			studentPreferencePageData = modelMapper.map(studentPreferencesWorkDTO, StudentPreferencesPageData.class);
		} else {
			StudentPreferencesDTO studentPreferenceskDTO = preferenceBusiness.getPreferenceFromMaster(studentId);
			if (studentPreferenceskDTO != null) {
				studentPreferencePageData = modelMapper.map(studentPreferenceskDTO, StudentPreferencesPageData.class);
			}
		}
		pageDto.setData(studentPreferencePageData);
		pageDto.setHeader(pageNavigationContext.getPageRequest().getHeader());
		return pageDto;
	}

	@Override
	public PageDTO saveAsMaster(PageRequestDTO pageRequest) {
		PageDTO pageDTO = new PageDTO();
		String emailId = pageRequest.getHeader().getEmailId();
		Long studentId = studentControlBusiness.getStudentId(emailId);
		preferenceBusiness.workToMaster(studentId);
		studentPreferencesWorkService.delete(studentId);
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
				return (PageDTO) m.invoke(this, pageRequest, pageNavigatorDTO);
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
		return pageDTO;
	}

	public PageDTO handlePreferencesSaveAction(PageRequestDTO pageRequest, PageNavigatorDTO pageNavigatorDTO) {
		PageDTO pageDto = new PageDTO();
		Long studentId = 0L;
		String emailId = pageRequest.getHeader().getEmailId();
		String saveIn = pageNavigatorDTO.getUpdationTable();
		StudentPreferencesPageData studentPreferencePageData = mapPagedata(pageRequest);
		if (PageSaveTable.W.toString().equals(saveIn)) {
			StudentControlWorkDTO studentControlWorkDTO = studentControlBusiness.getStudentFromWork(emailId);
			studentId = studentControlWorkDTO.getStudentId();
			studentControlWorkDTO.setPreferencesW("Y");
			StudentPreferencesWorkDTO studentPreferencesWorkDTO = modelMapper.map(studentPreferencePageData,
					StudentPreferencesWorkDTO.class);
			studentPreferencesWorkDTO.setStudentId(studentId);
			studentPreferencesWorkDTO.setOperatorId("" + studentId);
			studentPreferencesWorkDTO.setRecordInUse(RecordInUseType.Y);
			preferenceBusiness.saveAsWork(studentPreferencesWorkDTO);
			studentControlBusiness.updateAsWork(studentControlWorkDTO);
		} else {
			StudentControlDTO studentControlDTO = studentControlBusiness.getStudentFromMaster(emailId);
			studentId = studentControlDTO.getStudentId();
			studentControlDTO.setPreferences("Y");
			StudentPreferencesDTO studentPreferencesDTO = modelMapper.map(studentPreferencePageData,
					StudentPreferencesDTO.class);
			studentPreferencesDTO.setStudentId(studentId);
			studentPreferencesDTO.setOperatorId("" + studentId);
			studentPreferencesDTO.setRecordInUse(RecordInUseType.Y);
			preferenceBusiness.saveAsMaster(studentPreferencesDTO);
			studentControlDTO.setOperatorId("" + studentId);
			studentControlDTO.setRecordInUse(RecordInUseType.Y);
			studentControlBusiness.saveAsMaster(studentControlDTO);
			StudentControlWorkDTO studentControlWorkDTO = studentControlBusiness.getStudentFromWork(emailId);
			if (studentControlWorkDTO != null) {
				studentControlWorkDTO.setPreferences("Y");
				studentControlWorkDTO.setOperatorId("" + studentId);
				studentControlWorkDTO.setRecordInUse(RecordInUseType.Y);
				studentControlBusiness.updateAsWork(studentControlWorkDTO);
			}
		}
		if (studentId == 0L) {
			throw new BusinessException(ErrorCodeConstants.STUDENT_DTLS_NOT_FOUND);
		}
		pageDto.setHeader(pageRequest.getHeader());
		return pageDto;
	}

	public PageDTO handlePreviousWorkDataPage(PageRequestDTO pageRequest, PageNavigatorDTO pageNavigatorDTO) {
		PageDTO pageDto = new PageDTO();
		Long studentId = 0L;
		String emailId = pageRequest.getHeader().getEmailId();
		String saveIn = pageNavigatorDTO.getUpdationTable();
		StudentPreferencesPageData studentPreferencePageData = mapPagedata(pageRequest);
		if (PageSaveTable.W.toString().equals(saveIn)) {
			StudentControlWorkDTO studentControlWorkDTO = studentControlBusiness.getStudentFromWork(emailId);
			studentId = studentControlWorkDTO.getStudentId();
			StudentPreferencesWorkDTO studentPreferencesWorkDTO = preferenceBusiness.getPreferenceFromWork(studentId);
			if (studentPreferencesWorkDTO == null) {
				studentPreferencesWorkDTO = new StudentPreferencesWorkDTO();
			}
			studentPreferencesWorkDTO = modelMapper.map(studentPreferencePageData, StudentPreferencesWorkDTO.class);
			studentPreferencesWorkDTO.setStudentId(studentId);
			studentPreferencesWorkDTO.setOperatorId("" + studentId);
			studentPreferencesWorkDTO.setRecordInUse(RecordInUseType.Y);
			preferenceBusiness.saveAsWork(studentPreferencesWorkDTO);
		}
		pageDto.setHeader(pageRequest.getHeader());
		return pageDto;
	}

	public StudentPreferencesPageData mapPagedata(PageRequestDTO pageRequest) {
		StudentPreferencesPageData studentPreferencePageData = null;
		try {
			studentPreferencePageData = objectMapper.readValue(pageRequest.getData().toString(),
					StudentPreferencesPageData.class);
		} catch (IOException e) {
			throw new BusinessException(ErrorCodeConstants.SREVER_ERROR);

		}
		return studentPreferencePageData;
	}
}
