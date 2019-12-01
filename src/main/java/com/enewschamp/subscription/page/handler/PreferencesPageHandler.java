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
import com.enewschamp.domain.common.IPageHandler;
import com.enewschamp.domain.common.PageNavigationContext;
import com.enewschamp.problem.BusinessException;
import com.enewschamp.subscription.app.dto.StudentControlWorkDTO;
import com.enewschamp.subscription.app.dto.StudentPreferenceCommPageData;
import com.enewschamp.subscription.app.dto.StudentPreferencePageData;
import com.enewschamp.subscription.app.dto.StudentPreferencesDTO;
import com.enewschamp.subscription.app.dto.StudentPreferencesWorkDTO;
import com.enewschamp.subscription.app.dto.StudentSubscriptionPageData;
import com.enewschamp.subscription.domain.business.PreferenceBusiness;
import com.enewschamp.subscription.domain.business.StudentControlBusiness;
import com.enewschamp.subscription.domain.business.SubscriptionBusiness;
import com.enewschamp.subscription.domain.entity.StudentControlWork;
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
	SubscriptionBusiness subscriptionBusiness;

	@Autowired
	StudentSubscriptionWorkService studentSubscriptionWorkService;

	@Autowired
	ModelMapper modelMapper;
	@Autowired
	private EnewschampApplicationProperties appConfig;

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
		Long studentId = 0L;

		if (PageAction.SubscriptionNext.toString().equalsIgnoreCase(action)) {
			// load the static data..
			StudentPreferencePageData studentPreferencePageData = new StudentPreferencePageData();
			studentPreferencePageData.setIncompleteFormText(appConfig.getIncompleteFormText());
			StudentPreferenceCommPageData communications = new StudentPreferenceCommPageData();
			communications.setEmailForComms(emailId);
			communications.setNewsPDFoverEmail("N");
			communications.setNotificationsOverEmail("N");
			communications.setScoresOverEmail("N");
			studentPreferencePageData.setCommunications(communications);
			pageDto.setData(studentPreferencePageData);
		} else if (PageAction.previous.toString().equalsIgnoreCase(action)
				|| PageAction.Preferences.toString().equalsIgnoreCase(action)) {
			studentId = studentControlBusiness.getStudentId(emailId);
			StudentPreferencesDTO studentPreferencesDTO = preferenceBusiness.getPreferenceFromMaster(studentId);
			StudentPreferencePageData studentPreferencePageData = modelMapper.map(studentPreferencesDTO,
					StudentPreferencePageData.class);
			studentPreferencePageData.setIncompleteFormText(appConfig.getIncompleteFormText());
			pageDto.setData(studentPreferencePageData);
		}
		return pageDto;
	}

	@Override
	public PageDTO saveAsMaster(String actionName, PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		String emailId = pageRequest.getHeader().getEmailID();
		Long studentId = studentControlBusiness.getStudentId(emailId);

		preferenceBusiness.workToMaster(studentId);
		return pageDto;
	}

	@Override
	public PageDTO handleAppAction(String actionName, PageRequestDTO pageRequest, PageNavigatorDTO pageNavigatorDTO) {
		PageDTO pageDto = new PageDTO();
		Long studentId = 0L;
		String emailId = pageRequest.getHeader().getEmailID();
		String editionID = pageRequest.getHeader().getEditionID();
		if (PageAction.PreferencesSave.toString().equalsIgnoreCase(actionName)) {
			StudentPreferencePageData studentPreferencePageData = mapPagedata(pageRequest);
			StudentPreferencesDTO studPref = modelMapper.map(studentPreferencePageData, StudentPreferencesDTO.class);

			StudentControlWorkDTO studentControlWorkDTO = studentControlBusiness.getStudentFromWork(emailId);
			if (studentControlWorkDTO != null) {
				studentId = studentControlWorkDTO.getStudentID();
				studentControlWorkDTO.setPreferences("Y");
				studentControlBusiness.updateAsWork(studentControlWorkDTO);
				subscriptionBusiness.saveWorkToMaster(studentId, editionID);
				studentControlBusiness.workToMaster(studentId);
				studentSubscriptionWorkService.delete(studentId);
				studentControlBusiness.deleteFromWork(studentControlWorkDTO);
			} else {
				studentId = studentControlBusiness.getStudentId(emailId);
			}
			studPref.setStudentID(studentId);
			preferenceBusiness.saveAsMaster(studPref);
		} else if (PageAction.PreferencesBack.toString().equalsIgnoreCase(actionName)) {
			StudentControlWorkDTO studentControlWorkDTO = studentControlBusiness.getStudentFromWork(emailId);
			if (studentControlWorkDTO != null) {
				studentSubscriptionWorkService.delete(studentControlWorkDTO.getStudentID());
				studentControlBusiness.deleteFromWork(studentControlWorkDTO);
			}
		}
		pageDto.setHeader(pageRequest.getHeader());
		return pageDto;
	}

	public StudentPreferencePageData mapPagedata(PageRequestDTO pageRequest) {
		StudentPreferencePageData studentPreferencePageData = null;
		try {
			studentPreferencePageData = objectMapper.readValue(pageRequest.getData().toString(),
					StudentPreferencePageData.class);
		} catch (IOException e) {
			throw new BusinessException(ErrorCodes.SREVER_ERROR);

		}
		return studentPreferencePageData;
	}
}
