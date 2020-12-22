package com.enewschamp.subscription.page.handler;

import java.io.IOException;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enewschamp.app.common.CommonService;
import com.enewschamp.app.common.ErrorCodeConstants;
import com.enewschamp.app.common.PageDTO;
import com.enewschamp.app.common.PageRequestDTO;
import com.enewschamp.app.fw.page.navigation.dto.PageNavigatorDTO;
import com.enewschamp.app.student.registration.entity.StudentRegistration;
import com.enewschamp.app.student.registration.service.StudentRegistrationService;
import com.enewschamp.common.domain.service.PropertiesService;
import com.enewschamp.domain.common.IPageHandler;
import com.enewschamp.domain.common.PageNavigationContext;
import com.enewschamp.problem.BusinessException;
import com.enewschamp.publication.domain.service.AvatarService;
import com.enewschamp.subscription.app.dto.MyPicturePageData;
import com.enewschamp.subscription.app.dto.StudentControlDTO;
import com.enewschamp.subscription.domain.business.StudentControlBusiness;
import com.enewschamp.subscription.domain.entity.StudentControl;
import com.enewschamp.subscription.domain.entity.StudentDetails;
import com.enewschamp.subscription.domain.service.StudentControlService;
import com.enewschamp.subscription.domain.service.StudentDetailsService;
import com.enewschamp.subscription.domain.service.StudentDetailsWorkService;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component(value = "MyPicturePageHandler")
public class MyPicturePageHandler implements IPageHandler {

	@Autowired
	ObjectMapper objectMapper;

	@Autowired
	private PropertiesService propertiesService;

	@Autowired
	StudentControlBusiness studentControlBusiness;

	@Autowired
	StudentControlService studentControlService;

	@Autowired
	StudentDetailsService studentDetailsService;

	@Autowired
	StudentDetailsWorkService studentDetailsWorkService;

	@Autowired
	StudentRegistrationService studentRegistrationService;

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	AvatarService avatarService;

	@Autowired
	CommonService commonService;

	@Override
	public PageDTO handleAction(PageRequestDTO pageRequest) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PageDTO loadPage(PageNavigationContext pageNavigationContext) {
		PageDTO pageDto = new PageDTO();
		pageDto.setHeader(pageNavigationContext.getPageRequest().getHeader());
		String emailId = pageNavigationContext.getPageRequest().getHeader().getEmailId();
		MyPicturePageData myPicturePageData = new MyPicturePageData();
		myPicturePageData.setAvatarLOV(avatarService.getAvatarLOV());
		StudentRegistration studentRegistration = studentRegistrationService.getStudentReg(emailId);
		if (studentRegistration != null) {
			myPicturePageData.setAvatarName(studentRegistration.getAvatarName());
			myPicturePageData.setPhotoName(studentRegistration.getPhotoName());
		} else {
			myPicturePageData.setAvatarName("");
			myPicturePageData.setPhotoName("");
		}
		pageDto.setData(myPicturePageData);
		return pageDto;
	}

	@Override
	public PageDTO saveAsMaster(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		return pageDto;
	}

	@Override
	public PageDTO handleAppAction(PageRequestDTO pageRequest, PageNavigatorDTO pageNavigatorDTO) {
		PageDTO pageDto = new PageDTO();
		String emailId = pageRequest.getHeader().getEmailId();
		MyPicturePageData myPicturePageData = new MyPicturePageData();
		StudentRegistration studentRegistration = studentRegistrationService.getStudentReg(emailId);
		myPicturePageData = mapPagedata(pageRequest);
		if ("Y".equals(myPicturePageData.getDeleteImage())) {
			studentRegistration.setAvatarName(null);
			studentRegistration.setPhotoName(null);
			studentRegistrationService.update(studentRegistration);
		} else {
			if (myPicturePageData.getAvatarName() != null && !"".equals(myPicturePageData.getAvatarName())) {
				studentRegistration.setAvatarName(myPicturePageData.getAvatarName());
				studentRegistration.setPhotoName(null);
			} else if (myPicturePageData.getPhotoBase64() != null && !"".equals(myPicturePageData.getPhotoBase64())) {
				String newImageName = studentRegistration.getStudentId() + "_" + System.currentTimeMillis();
				String imageType = "jpg";
				if (myPicturePageData.getImageTypeExt() != null && !"".equals(myPicturePageData.getImageTypeExt())) {
					imageType = myPicturePageData.getImageTypeExt();
				}
				boolean saveFlag = commonService.saveImages("student", imageType, myPicturePageData.getPhotoBase64(),
						newImageName, studentRegistration.getPhotoName());
				if (saveFlag) {
					studentRegistration.setAvatarName(null);
					studentRegistration.setPhotoName(newImageName + "." + imageType);
				}
			}
			studentRegistrationService.update(studentRegistration);
		}
		pageDto.setHeader(pageRequest.getHeader());
		return pageDto;
	}

	public MyPicturePageData mapPagedata(PageRequestDTO pageRequest) {
		MyPicturePageData myPicturePageData = null;
		try {
			myPicturePageData = objectMapper.readValue(pageRequest.getData().toString(), MyPicturePageData.class);
		} catch (IOException e) {
			throw new BusinessException(ErrorCodeConstants.SREVER_ERROR);
		}
		return myPicturePageData;
	}
}
