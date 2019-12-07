package com.enewschamp.subscription.page.handler;

import java.io.IOException;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enewschamp.app.common.ErrorCodes;
import com.enewschamp.app.common.PageDTO;
import com.enewschamp.app.common.PageRequestDTO;
import com.enewschamp.app.fw.page.navigation.common.PageAction;
import com.enewschamp.app.fw.page.navigation.common.PageSaveTable;
import com.enewschamp.app.fw.page.navigation.dto.PageNavigatorDTO;
import com.enewschamp.domain.common.IPageHandler;
import com.enewschamp.domain.common.PageNavigationContext;
import com.enewschamp.problem.BusinessException;
import com.enewschamp.subscription.app.dto.StudentPictDetailsPageData;
import com.enewschamp.subscription.app.dto.StudentPicturePageData;
import com.enewschamp.subscription.domain.business.StudentControlBusiness;
import com.enewschamp.subscription.domain.entity.StudentDetails;
import com.enewschamp.subscription.domain.entity.StudentDetailsWork;
import com.enewschamp.subscription.domain.service.StudentDetailsService;
import com.enewschamp.subscription.domain.service.StudentDetailsWorkService;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component(value = "StudentPicturePageHandler")
public class StudentPicturePageHandler implements IPageHandler {

	@Autowired
	ObjectMapper objectMapper;

	@Autowired
	StudentControlBusiness studentControlBusiness;

	@Autowired
	StudentDetailsService studentDetailsService;

	@Autowired
	StudentDetailsWorkService studentDetailsWorkService;

	@Autowired
	ModelMapper modelMapper;

	@Override
	public PageDTO handleAction(String actionName, PageRequestDTO pageRequest) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PageDTO loadPage(PageNavigationContext pageNavigationContext) {
		PageDTO pageDto = new PageDTO();
		pageDto.setHeader(pageNavigationContext.getPageRequest().getHeader());
		String action = pageNavigationContext.getActionName();
<<<<<<< Updated upstream
		String emailId = pageNavigationContext.getPageRequest().getHeader().getEmailID();
=======
		String emailId = pageNavigationContext.getPageRequest().getHeader().getEmailId();
>>>>>>> Stashed changes

		Long studentId = 0L;
		if (PageAction.back.toString().equalsIgnoreCase(action)
				|| PageAction.PicImage.toString().equalsIgnoreCase(action)) {
			studentId = studentControlBusiness.getStudentId(emailId);
			StudentPicturePageData studentPicturePageData = new StudentPicturePageData();
			StudentPictDetailsPageData studentPicDetailsPageData = new StudentPictDetailsPageData();
			StudentDetails studentDetails = studentDetailsService.get(studentId);
			if (studentDetails != null) {
				studentPicDetailsPageData.setAvtarId(studentDetails.getAvtarID());
				studentPicDetailsPageData.setImage(studentDetails.getPhoto());
				studentPicturePageData.setPicture(studentPicDetailsPageData);
			} else {
				StudentDetailsWork studentDetailsWork = studentDetailsWorkService.get(studentId);
				if (studentDetailsWork != null) {
					studentPicDetailsPageData.setAvtarId(studentDetailsWork.getAvtarID());
					studentPicDetailsPageData.setImage(studentDetailsWork.getPhoto());
					studentPicturePageData.setPicture(studentPicDetailsPageData);
				} else {
					studentPicDetailsPageData.setAvtarId(null);
					studentPicDetailsPageData.setImage("");
					studentPicturePageData.setPicture(studentPicDetailsPageData);
				}
			}
			pageDto.setData(studentPicturePageData);
		}
		return pageDto;
	}

	@Override
	public PageDTO saveAsMaster(String actionName, PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		/*
		 * String emailId = pageRequest.getHeader().getEmailID(); StudentControlWorkDTO
		 * studentControlWorkDTO = StudentControlBusiness.getStudentFromWork(emailId);
		 * Long studentId = 0L; if (studentControlWorkDTO != null) { studentId =
		 * studentControlWorkDTO.getStudentID(); }
		 * 
		 * preferenceBusiness.workToMaster(studentId);
		 */
		return pageDto;
	}

	@Override
	public PageDTO handleAppAction(String actionName, PageRequestDTO pageRequest, PageNavigatorDTO pageNavigatorDTO) {

		PageDTO pageDto = new PageDTO();
		String saveIn = pageNavigatorDTO.getUpdationTable();
		String emailId = pageRequest.getHeader().getEmailId();
		Long studentId = 0L;
		StudentPicturePageData studentPicturePageData = new StudentPicturePageData();
		if (PageAction.save.toString().equalsIgnoreCase(actionName)) {

			if (PageSaveTable.M.toString().equals(saveIn)) {
				studentId = studentControlBusiness.getStudentId(emailId);
				StudentDetails studentDetails = studentDetailsService.get(studentId);
				studentPicturePageData = mapPagedata(pageRequest);
				studentDetails.setAvtarID(studentPicturePageData.getPicture().getAvtarId());
				studentDetails.setPhoto(studentPicturePageData.getPicture().getImage());
				studentDetailsService.create(studentDetails);
			} else if (PageSaveTable.W.toString().equals(saveIn)) {

			} // not sure if image is to be saved in work table..
		}

		pageDto.setHeader(pageRequest.getHeader());
		return pageDto;
	}

	public StudentPicturePageData mapPagedata(PageRequestDTO pageRequest) {
		StudentPicturePageData studentPicturePageData = null;
		try {
			studentPicturePageData = objectMapper.readValue(pageRequest.getData().toString(),
					StudentPicturePageData.class);
		} catch (IOException e) {
			throw new BusinessException(ErrorCodes.SREVER_ERROR);
		}
		return studentPicturePageData;
	}
}
