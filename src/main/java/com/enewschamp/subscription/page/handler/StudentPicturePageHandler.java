package com.enewschamp.subscription.page.handler;

import java.io.IOException;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.enewschamp.app.common.ErrorCodes;
import com.enewschamp.app.common.PageDTO;
import com.enewschamp.app.common.PageRequestDTO;
import com.enewschamp.app.fw.page.navigation.common.PageAction;
import com.enewschamp.app.fw.page.navigation.common.PageSaveTable;
import com.enewschamp.app.fw.page.navigation.dto.PageNavigatorDTO;
import com.enewschamp.domain.common.IPageHandler;
import com.enewschamp.domain.common.PageNavigationContext;
import com.enewschamp.problem.Fault;
import com.enewschamp.problem.HttpStatusAdapter;
import com.enewschamp.subscription.app.dto.StudentPictDetailsPageData;
import com.enewschamp.subscription.app.dto.StudentPicturePageData;
import com.enewschamp.subscription.domain.business.StudentControlBusiness;
import com.enewschamp.subscription.domain.entity.StudentDetails;
import com.enewschamp.subscription.domain.service.StudentDetailsService;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component(value = "StudentPicturePageHandler")
public class StudentPicturePageHandler implements IPageHandler {

	@Autowired
	ObjectMapper objectMapper;

	@Autowired
	StudentControlBusiness StudentControlBusiness;

	@Autowired
	StudentDetailsService studentDetailsService;

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
		String emailId = pageNavigationContext.getPageRequest().getHeader().getEmailID();
	
		Long studentId=0L;
		if(PageAction.back.toString().equalsIgnoreCase(action))
		{
			studentId = StudentControlBusiness.getStudentId(emailId);
			StudentDetails studentDetails = studentDetailsService.get(studentId);
			StudentPicturePageData studentPicturePageData = new StudentPicturePageData();
			StudentPictDetailsPageData studentPicDetailsPageData=new StudentPictDetailsPageData();
			studentPicDetailsPageData.setAvtarId(studentDetails.getAvtarID());
			studentPicDetailsPageData.setImage(studentDetails.getPhoto());
			studentPicturePageData.setPicture(studentPicDetailsPageData);
			pageDto.setData(studentPicturePageData);
		}
		return pageDto;
	}

	@Override
	public PageDTO saveAsMaster(String actionName, PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		/*String emailId = pageRequest.getHeader().getEmailID();
		StudentControlWorkDTO studentControlWorkDTO = StudentControlBusiness.getStudentFromWork(emailId);
		Long studentId = 0L;
		if (studentControlWorkDTO != null) {
			studentId = studentControlWorkDTO.getStudentID();
		}

		preferenceBusiness.workToMaster(studentId);*/
		return pageDto;
	}

	@Override
	public PageDTO handleAppAction(String actionName, PageRequestDTO pageRequest, PageNavigatorDTO pageNavigatorDTO) {

		PageDTO pageDto = new PageDTO();
		String saveIn = pageNavigatorDTO.getUpdationTable();
		String emailId = pageRequest.getHeader().getEmailID();
		Long studentId = 0L;
		StudentPicturePageData studentPicturePageData = new StudentPicturePageData();
		if (PageAction.save.toString().equalsIgnoreCase(actionName)) {
			
			if (PageSaveTable.M.toString().equals(saveIn)) {
				
				studentId  = StudentControlBusiness.getStudentId(emailId);
				
				StudentDetails studentDetails = studentDetailsService.get(studentId);
				studentPicturePageData = mapPagedata(pageRequest);
				studentDetails.setAvtarID(studentPicturePageData.getPicture().getAvtarId());
				studentDetails.setPhoto(studentPicturePageData.getPicture().getImage());
				studentDetailsService.create(studentDetails);
			} 
			else if (PageSaveTable.W.toString().equals(saveIn)) {
				
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
			throw new Fault(new HttpStatusAdapter(HttpStatus.INTERNAL_SERVER_ERROR), ErrorCodes.SREVER_ERROR,
					"Error in mapping Preference Page Data fields. Contact System administrator!");

		}
		return studentPicturePageData;
	}
}
