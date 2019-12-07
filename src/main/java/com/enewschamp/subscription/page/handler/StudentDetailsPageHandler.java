package com.enewschamp.subscription.page.handler;

import java.io.IOException;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enewschamp.EnewschampApplicationProperties;
import com.enewschamp.app.common.ErrorCodes;
import com.enewschamp.app.common.PageDTO;
import com.enewschamp.app.common.PageRequestDTO;
import com.enewschamp.app.fw.page.navigation.common.PageAction;
import com.enewschamp.app.fw.page.navigation.common.PageSaveTable;
import com.enewschamp.app.fw.page.navigation.dto.PageNavigatorDTO;
import com.enewschamp.app.fw.page.navigation.service.PageNavigationService;
import com.enewschamp.domain.common.IPageHandler;
import com.enewschamp.domain.common.PageNavigationContext;
import com.enewschamp.problem.BusinessException;
import com.enewschamp.subscription.app.dto.StudentControlDTO;
import com.enewschamp.subscription.app.dto.StudentControlWorkDTO;
import com.enewschamp.subscription.app.dto.StudentDetailsDTO;
import com.enewschamp.subscription.app.dto.StudentDetailsPageData;
import com.enewschamp.subscription.app.dto.StudentDetailsWorkDTO;
import com.enewschamp.subscription.domain.business.StudentControlBusiness;
import com.enewschamp.subscription.domain.business.StudentDetailsBusiness;
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
	ModelMapper modelMapper;

	@Autowired
	private EnewschampApplicationProperties appConfig;
	@Autowired
	PageNavigationService pageNavigationService;

	@Override
	public PageDTO handleAction(String actionName, PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();

		return pageDto;
	}

	@Override
	public PageDTO loadPage(PageNavigationContext pageNavigationContext) {
		PageDTO pageDTO = new PageDTO();

		Long studentId = 0L;
		String action = pageNavigationContext.getActionName();
		String emailId = pageNavigationContext.getPageRequest().getHeader().getEmailId();
		String operation = pageNavigationContext.getPageRequest().getHeader().getOperation();
		String pageName = pageNavigationContext.getPreviousPage();

		StudentControlDTO studentControlDTO = studentControlBusiness.getStudentFromMaster(emailId);
		if (studentControlDTO != null) {
			studentId = studentControlDTO.getStudentID();
		} else {
			StudentControlWorkDTO studentControlWorkDTO = studentControlBusiness.getStudentFromWork(emailId);
			if (studentControlWorkDTO != null)
				studentId = studentControlWorkDTO.getStudentID();
		}

		if (PageAction.next.toString().equalsIgnoreCase(action)) {
			StudentDetailsPageData studentDetailsPageData = new StudentDetailsPageData();
			studentDetailsPageData
					.setCorrectDetailsText(appConfig.getStudentDetailsPageText().get("correctDetailsText"));
			studentDetailsPageData.setDobReasonText(appConfig.getStudentDetailsPageText().get("dobReasonText"));
			studentDetailsPageData.setDobText(appConfig.getStudentDetailsPageText().get("dobText"));
			studentDetailsPageData
					.setIncompeleteFormText(appConfig.getStudentDetailsPageText().get("incompeleteFormText"));
			pageDTO.setData(studentDetailsPageData);
		} else if (PageAction.previous.toString().equalsIgnoreCase(action)) {
			PageNavigatorDTO pageNavDto = pageNavigationService.getNavPage(action, operation, pageName);
			if (PageSaveTable.M.toString().equalsIgnoreCase(pageNavDto.getUpdationTable())) {
				// get the data from master table and add static data
				StudentDetailsDTO studentDetailsDTO = studentDetailsBusiness.getStudentDetailsFromMaster(studentId);
				StudentDetailsPageData studentDetailsPageData = modelMapper.map(studentDetailsDTO,
						StudentDetailsPageData.class);
				studentDetailsPageData
						.setCorrectDetailsText(appConfig.getStudentDetailsPageText().get("correctDetailsText"));
				studentDetailsPageData.setDobReasonText(appConfig.getStudentDetailsPageText().get("dobReasonText"));
				studentDetailsPageData.setDobText(appConfig.getStudentDetailsPageText().get("dobText"));
				studentDetailsPageData
						.setIncompeleteFormText(appConfig.getStudentDetailsPageText().get("incompeleteFormText"));
				pageDTO.setData(studentDetailsPageData);
			} else if (PageSaveTable.W.toString().equalsIgnoreCase(pageNavDto.getUpdationTable())) {
				// get the data from work table and add static data
				StudentDetailsWorkDTO studentDetailsWorkDTO = studentDetailsBusiness
						.getStudentDetailsFromWork(studentId);
				StudentDetailsPageData studentDetailsPageData = modelMapper.map(studentDetailsWorkDTO,
						StudentDetailsPageData.class);
				studentDetailsPageData
						.setCorrectDetailsText(appConfig.getStudentDetailsPageText().get("correctDetailsText"));
				studentDetailsPageData.setDobReasonText(appConfig.getStudentDetailsPageText().get("dobReasonText"));
				studentDetailsPageData.setDobText(appConfig.getStudentDetailsPageText().get("dobText"));
				studentDetailsPageData
						.setIncompeleteFormText(appConfig.getStudentDetailsPageText().get("incompeleteFormText"));
				pageDTO.setData(studentDetailsPageData);

			}
		}
		// set the header as is...
		pageDTO.setHeader(pageNavigationContext.getPageRequest().getHeader());
		return pageDTO;
	}

	@Override
	public PageDTO saveAsMaster(String actionName, PageRequestDTO pageRequest) {
		PageDTO pageDTO = new PageDTO();
		Long studentId = 0L;

		String emailId = pageRequest.getHeader().getEmailId();
		StudentControlDTO studentControlDTO = studentControlBusiness.getStudentFromMaster(emailId);
		if (studentControlDTO == null) {
			/// not known..
		} else {
			studentId = studentControlDTO.getStudentID();
		}
		StudentDetailsDTO studentDetailsDTO = null;
		try {
			studentDetailsDTO = objectMapper.readValue(pageRequest.getData().toString(), StudentDetailsDTO.class);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		studentDetailsDTO.setStudentID(studentId);
		studentDetailsBusiness.saveAsMaster(studentDetailsDTO);
		pageDTO.setHeader(pageRequest.getHeader());
		return pageDTO;
	}

	@Override
	public PageDTO handleAppAction(String actionName, PageRequestDTO pageRequest, PageNavigatorDTO pageNavigatorDTO) {
		PageDTO pageDTO = new PageDTO();
		if (PageAction.next.toString().equals(actionName)
				|| PageAction.StudentDetailsNext.toString().equals(actionName)) {
			if (PageSaveTable.M.toString().equals(pageNavigatorDTO.getUpdationTable())) {
				Long studentId = 0L;
<<<<<<< Updated upstream
				String emailId = pageRequest.getHeader().getEmailID();
=======
				String emailId = pageRequest.getHeader().getEmailId();
>>>>>>> Stashed changes
				studentId = studentControlBusiness.getStudentId(emailId);
				StudentDetailsDTO studentDetailsDTO = mapPageToDTO(pageRequest);
				studentDetailsDTO.setStudentID(studentId);
				studentDetailsBusiness.saveAsMaster(studentDetailsDTO);
			} else if (PageSaveTable.W.toString().equals(pageNavigatorDTO.getUpdationTable())) {
				Long studentId = 0L;
<<<<<<< Updated upstream
				String emailId = pageRequest.getHeader().getEmailID();
=======
				String emailId = pageRequest.getHeader().getEmailId();
>>>>>>> Stashed changes
				StudentControlDTO studentControlDTO = studentControlBusiness.getStudentFromMaster(emailId);
				studentId = studentControlBusiness.getStudentId(emailId);
				StudentDetailsWorkDTO studentDetailsWorkDTO = null;
				studentDetailsWorkDTO = mapPageToWorkDTO(pageRequest);
				studentDetailsWorkDTO.setStudentID(studentId);
				studentDetailsBusiness.saveAsWork(studentDetailsWorkDTO);
			}
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
			throw new BusinessException(ErrorCodes.SREVER_ERROR);

		}
		return studentDetailsPageData;
	}

	public StudentDetailsDTO mapPageToDTO(PageRequestDTO pageRequest) {
		StudentDetailsDTO studentDetailsDTO = null;
		try {
			studentDetailsDTO = objectMapper.readValue(pageRequest.getData().toString(), StudentDetailsDTO.class);
		} catch (IOException e) {
			throw new BusinessException(ErrorCodes.SREVER_ERROR);
		}
		return studentDetailsDTO;
	}

	public StudentDetailsWorkDTO mapPageToWorkDTO(PageRequestDTO pageRequest) {
		StudentDetailsWorkDTO studentDetailsDTO = null;
		try {
			studentDetailsDTO = objectMapper.readValue(pageRequest.getData().toString(), StudentDetailsWorkDTO.class);
		} catch (IOException e) {
			throw new BusinessException(ErrorCodes.SREVER_ERROR);
		}
		return studentDetailsDTO;
	}
}
