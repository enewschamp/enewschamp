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
import com.enewschamp.app.fw.page.navigation.service.PageNavigationService;
import com.enewschamp.domain.common.IPageHandler;
import com.enewschamp.domain.common.PageNavigationContext;
import com.enewschamp.domain.common.RecordInUseType;
import com.enewschamp.problem.BusinessException;
import com.enewschamp.publication.domain.service.AvatarService;
import com.enewschamp.subscription.app.dto.StudentControlDTO;
import com.enewschamp.subscription.app.dto.StudentControlWorkDTO;
import com.enewschamp.subscription.app.dto.StudentDetailsDTO;
import com.enewschamp.subscription.app.dto.StudentDetailsPageData;
import com.enewschamp.subscription.app.dto.StudentDetailsWorkDTO;
import com.enewschamp.subscription.domain.business.StudentControlBusiness;
import com.enewschamp.subscription.domain.business.StudentDetailsBusiness;
import com.enewschamp.subscription.domain.service.StudentDetailsWorkService;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component(value = "StudentDetailsPageHandler")
public class StudentDetailsPageHandler implements IPageHandler {

	@Autowired
	StudentDetailsBusiness studentDetailsBusiness;

	@Autowired
	ObjectMapper objectMapper;

	@Autowired
	StudentControlBusiness studentControlBusiness;

	@Autowired
	StudentDetailsWorkService studentDetailsWorkService;

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	AvatarService avatarService;

	@Autowired
	PageNavigationService pageNavigationService;

	@Override
	public PageDTO handleAction(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();

		return pageDto;
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

	public PageDTO loadStudentDetailsPage(PageNavigationContext pageNavigationContext) {
		PageDTO pageDTO = new PageDTO();
		String emailId = pageNavigationContext.getPageRequest().getHeader().getEmailId();
		Long studentId = studentControlBusiness.getStudentId(emailId);
		StudentDetailsPageData studentDetailsPageData = new StudentDetailsPageData();
		if (studentId == 0L) {
			throw new BusinessException(ErrorCodeConstants.STUDENT_DTLS_NOT_FOUND);
		}
		StudentDetailsDTO studentDetailsDTO = studentDetailsBusiness.getStudentDetailsFromMaster(studentId);
		if (studentDetailsDTO != null) {
			studentDetailsPageData = modelMapper.map(studentDetailsDTO, StudentDetailsPageData.class);
			studentDetailsPageData.setNameM(studentDetailsDTO.getName());
			studentDetailsPageData.setSurnameM(studentDetailsDTO.getSurname());
			studentDetailsPageData.setOtherNamesM(studentDetailsDTO.getOtherNames());
			studentDetailsPageData.setGenderM(studentDetailsDTO.getGender());
			studentDetailsPageData.setDoBM(studentDetailsDTO.getDoB());
			studentDetailsPageData.setMobileNumberM(studentDetailsDTO.getMobileNumber());
			studentDetailsPageData.setApprovalRequiredM(studentDetailsDTO.getApprovalRequired());
		}
		pageDTO.setData(studentDetailsPageData);
		pageDTO.setHeader(pageNavigationContext.getPageRequest().getHeader());
		return pageDTO;
	}

	public PageDTO loadWorkDataPage(PageNavigationContext pageNavigationContext) {
		PageDTO pageDTO = new PageDTO();
		String emailId = pageNavigationContext.getPageRequest().getHeader().getEmailId();
		Long studentId = studentControlBusiness.getStudentId(emailId);
		StudentDetailsPageData studentDetailsPageData = new StudentDetailsPageData();
		StudentDetailsWorkDTO studentDetailsWorkDTO = studentDetailsBusiness.getStudentDetailsFromWork(studentId);
		if (studentDetailsWorkDTO != null) {
			studentDetailsPageData = modelMapper.map(studentDetailsWorkDTO, StudentDetailsPageData.class);
		} else {
			StudentDetailsDTO studentDetailsDTO = studentDetailsBusiness.getStudentDetailsFromMaster(studentId);
			if (studentDetailsDTO != null) {
				studentDetailsPageData = modelMapper.map(studentDetailsDTO, StudentDetailsPageData.class);
			}
		}
		StudentDetailsDTO studentDetailsDTO = studentDetailsBusiness.getStudentDetailsFromMaster(studentId);
		if (studentDetailsDTO != null) {
			studentDetailsPageData.setNameM(studentDetailsDTO.getName());
			studentDetailsPageData.setSurnameM(studentDetailsDTO.getSurname());
			studentDetailsPageData.setOtherNamesM(studentDetailsDTO.getOtherNames());
			studentDetailsPageData.setGenderM(studentDetailsDTO.getGender());
			studentDetailsPageData.setDoBM(studentDetailsDTO.getDoB());
			studentDetailsPageData.setMobileNumberM(studentDetailsDTO.getMobileNumber());
			studentDetailsPageData.setApprovalRequiredM(studentDetailsDTO.getApprovalRequired());
		}
		pageDTO.setData(studentDetailsPageData);
		pageDTO.setHeader(pageNavigationContext.getPageRequest().getHeader());
		return pageDTO;
	}

	public PageDTO loadExistingStudentDetailsPage(PageNavigationContext pageNavigationContext) {
		PageDTO pageDTO = new PageDTO();
		String emailId = pageNavigationContext.getPageRequest().getHeader().getEmailId();
		Long studentId = studentControlBusiness.getStudentId(emailId);
		StudentDetailsPageData studentDetailsPageData = new StudentDetailsPageData();
		StudentDetailsDTO studentDetailsDTO = studentDetailsBusiness.getStudentDetailsFromMaster(studentId);
		if (studentDetailsDTO != null) {
			studentDetailsPageData = modelMapper.map(studentDetailsDTO, StudentDetailsPageData.class);
			studentDetailsPageData.setNameM(studentDetailsDTO.getName());
			studentDetailsPageData.setSurnameM(studentDetailsDTO.getSurname());
			studentDetailsPageData.setOtherNamesM(studentDetailsDTO.getOtherNames());
			studentDetailsPageData.setGenderM(studentDetailsDTO.getGender());
			studentDetailsPageData.setDoBM(studentDetailsDTO.getDoB());
			studentDetailsPageData.setMobileNumberM(studentDetailsDTO.getMobileNumber());
			studentDetailsPageData.setApprovalRequiredM(studentDetailsDTO.getApprovalRequired());
		}
		pageDTO.setData(studentDetailsPageData);
		pageDTO.setHeader(pageNavigationContext.getPageRequest().getHeader());
		return pageDTO;
	}

	@Override
	public PageDTO saveAsMaster(PageRequestDTO pageRequest) {
		PageDTO pageDTO = new PageDTO();
		String emailId = pageRequest.getHeader().getEmailId();
		Long studentId = studentControlBusiness.getStudentId(emailId);
		studentDetailsBusiness.workToMaster(studentId);
		studentDetailsWorkService.delete(studentId);
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

	public PageDTO handleStudentDetailsNextAction(PageRequestDTO pageRequest, PageNavigatorDTO pageNavigatorDTO) {
		PageDTO pageDTO = new PageDTO();
		if (PageSaveTable.M.toString().equals(pageNavigatorDTO.getUpdationTable())) {
			Long studentId = 0L;
			String emailId = pageRequest.getHeader().getEmailId();
			studentId = studentControlBusiness.getStudentId(emailId);
			StudentDetailsDTO studentDetailsDTO = mapPageToDTO(pageRequest);
			studentDetailsDTO.setStudentId(studentId);
			studentDetailsDTO.setOperatorId("" + studentId);
			studentDetailsDTO.setRecordInUse(RecordInUseType.Y);
			studentDetailsBusiness.saveAsMaster(studentDetailsDTO);
			StudentControlDTO studentControlDTO = studentControlBusiness.getStudentFromMaster(emailId);
			studentControlDTO.setStudentDetails("Y");
			studentControlBusiness.saveAsMaster(studentControlDTO);
		} else if (PageSaveTable.W.toString().equals(pageNavigatorDTO.getUpdationTable())) {
			String emailId = pageRequest.getHeader().getEmailId();
			Long studentId = studentControlBusiness.getStudentId(emailId);
			StudentDetailsWorkDTO studentDetailsWorkDTO = null;
			studentDetailsWorkDTO = mapPageToWorkDTO(pageRequest);
			studentDetailsWorkDTO.setStudentId(studentId);
			studentDetailsWorkDTO.setOperatorId("" + studentId);
			studentDetailsWorkDTO.setRecordInUse(RecordInUseType.Y);
			studentDetailsBusiness.saveAsWork(studentDetailsWorkDTO);
			StudentControlWorkDTO studentControlWorkDTO = studentControlBusiness.getStudentFromWork(emailId);
			studentControlWorkDTO.setStudentDetailsW("Y");
			studentControlBusiness.saveAsWork(studentControlWorkDTO);
		}
		pageDTO.setHeader(pageRequest.getHeader());
		return pageDTO;
	}

	public PageDTO handlePreviousWorkDataPage(PageRequestDTO pageRequest, PageNavigatorDTO pageNavigatorDTO) {
		PageDTO pageDTO = new PageDTO();
		Long studentId = 0L;
		String emailId = pageRequest.getHeader().getEmailId();
		String saveIn = pageNavigatorDTO.getUpdationTable();
		StudentDetailsPageData studentDetailsPageData = mapPagedata(pageRequest);
		if (PageSaveTable.W.toString().equals(saveIn)) {
			StudentControlWorkDTO studentControlWorkDTO = studentControlBusiness.getStudentFromWork(emailId);
			studentId = studentControlWorkDTO.getStudentId();
			StudentDetailsWorkDTO studentDetailsWorkDTO = studentDetailsBusiness.getStudentDetailsFromWork(studentId);
			if (studentDetailsWorkDTO == null) {
				studentDetailsWorkDTO = new StudentDetailsWorkDTO();
			}
			studentDetailsWorkDTO = modelMapper.map(studentDetailsPageData, StudentDetailsWorkDTO.class);
			studentDetailsWorkDTO.setStudentId(studentId);
			studentDetailsWorkDTO.setOperatorId("" + studentId);
			studentDetailsWorkDTO.setRecordInUse(RecordInUseType.Y);
			studentDetailsBusiness.saveAsWork(studentDetailsWorkDTO);
		}
		pageDTO.setHeader(pageRequest.getHeader());
		return pageDTO;
	}

	public StudentDetailsPageData mapPagedata(PageRequestDTO pageRequest) {
		StudentDetailsPageData studentDetailsPageData = null;
		try {
			studentDetailsPageData = objectMapper.readValue(pageRequest.getData().toString(),
					StudentDetailsPageData.class);
		} catch (IOException e) {
			throw new BusinessException(ErrorCodeConstants.SREVER_ERROR);

		}
		return studentDetailsPageData;
	}

	public StudentDetailsDTO mapPageToDTO(PageRequestDTO pageRequest) {
		StudentDetailsDTO studentDetailsDTO = null;
		try {
			studentDetailsDTO = objectMapper.readValue(pageRequest.getData().toString(), StudentDetailsDTO.class);
		} catch (IOException e) {
			throw new BusinessException(ErrorCodeConstants.SREVER_ERROR);
		}
		return studentDetailsDTO;
	}

	public StudentDetailsWorkDTO mapPageToWorkDTO(PageRequestDTO pageRequest) {
		StudentDetailsWorkDTO studentDetailsDTO = null;
		try {
			studentDetailsDTO = objectMapper.readValue(pageRequest.getData().toString(), StudentDetailsWorkDTO.class);
		} catch (IOException e) {
			throw new BusinessException(ErrorCodeConstants.SREVER_ERROR);
		}
		return studentDetailsDTO;
	}

}
