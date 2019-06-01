package com.enewschamp.subscription.page.handler;

import java.io.IOException;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
import com.enewschamp.problem.Fault;
import com.enewschamp.problem.HttpStatusAdapter;
import com.enewschamp.subscription.app.dto.StudentControlDTO;
import com.enewschamp.subscription.app.dto.StudentControlWorkDTO;
import com.enewschamp.subscription.app.dto.StudentPreferencePageData;
import com.enewschamp.subscription.app.dto.StudentPreferencesDTO;
import com.enewschamp.subscription.app.dto.StudentPreferencesWorkDTO;
import com.enewschamp.subscription.domain.business.PreferenceBusiness;
import com.enewschamp.subscription.domain.business.StudentControlBusiness;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component(value = "PreferencePageHandler")
public class PreferencePageHandler implements IPageHandler {

	@Autowired
	ObjectMapper objectMapper;

	@Autowired
	StudentControlBusiness StudentControlBusiness;

	@Autowired
	PreferenceBusiness preferenceBusiness;

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
		Long studentId=0L;
		
		
		if(PageAction.next.toString().equalsIgnoreCase(action))
		{
			//load the static data..
			StudentPreferencePageData studentPreferencePageData = new StudentPreferencePageData();
			studentPreferencePageData.setIncompleteFormText(appConfig.getIncompleteFormText());

		}
		else if(PageAction.previous.toString().equalsIgnoreCase(action))
		{
			
			studentId = StudentControlBusiness.getStudentId(emailId);
			
			//load the preferences saved data from master and static data..
			/*if (studentControlDTO == null) {
			
				StudentPreferencesWorkDTO studentPreferencesWorkDTO= preferenceBusiness.getPreferenceFromWork(studentId);
				StudentPreferencePageData studentPreferencePageData = modelMapper.map(studentPreferencesWorkDTO, StudentPreferencePageData.class);
				studentPreferencePageData.setIncompleteFormText(appConfig.getIncompleteFormText());
				pageDto.setData(studentPreferencePageData);
			}
			else
			{*/
				StudentPreferencesDTO studentPreferencesDTO= preferenceBusiness.getPreferenceFromMaster(studentId);
				StudentPreferencePageData studentPreferencePageData = modelMapper.map(studentPreferencesDTO, StudentPreferencePageData.class);
				studentPreferencePageData.setIncompleteFormText(appConfig.getIncompleteFormText());
				pageDto.setData(studentPreferencePageData);
			//}
		}
		return pageDto;
	}

	@Override
	public PageDTO saveAsMaster(String actionName, PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		String emailId = pageRequest.getHeader().getEmailID();
		Long studentId = StudentControlBusiness.getStudentId(emailId);
		
		preferenceBusiness.workToMaster(studentId);
		return pageDto;
	}

	@Override
	public PageDTO handleAppAction(String actionName, PageRequestDTO pageRequest, PageNavigatorDTO pageNavigatorDTO) {

		PageDTO pageDto = new PageDTO();
		String saveIn = pageNavigatorDTO.getUpdationTable();
		if (PageAction.next.toString().equalsIgnoreCase(actionName)) {
			
			if (PageSaveTable.M.toString().equals(saveIn)) {

				StudentPreferencePageData studentPreferencePageData = mapPagedata(pageRequest);

				// get student ID from student control..
				String emailId = pageRequest.getHeader().getEmailID();
				//StudentControlWorkDTO StudentControlWorkDTO = StudentControlBusiness.getStudentFromWork(emailId);

				Long studentId = StudentControlBusiness.getStudentId(emailId);

				StudentPreferencesDTO studPref = modelMapper.map(studentPreferencePageData,
						StudentPreferencesDTO.class);
				studPref.setStudentID(studentId);

				// studentPreferencePageDTO.set
				preferenceBusiness.saveAsMaster(studPref);
			} 
			else if (PageSaveTable.W.toString().equals(saveIn)) {
				
				StudentPreferencePageData studentPreferencePageData = mapPagedata(pageRequest);

				// get student ID from student control..
				String emailId = pageRequest.getHeader().getEmailID();
				//StudentControlWorkDTO StudentControlWorkDTO = StudentControlBusiness.getStudentFromWork(emailId);

				Long studentId = StudentControlBusiness.getStudentId(emailId);

				StudentPreferencesWorkDTO studPref = modelMapper.map(studentPreferencePageData,
						StudentPreferencesWorkDTO.class);
				studPref.setStudentID(studentId);

				// studentPreferencePageDTO.set
				preferenceBusiness.saveAsWork(studPref);
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
			throw new Fault(new HttpStatusAdapter(HttpStatus.INTERNAL_SERVER_ERROR), ErrorCodes.SREVER_ERROR,
					"Error in mapping Preference Page Data fields. Contact System administrator!");

		}
		return studentPreferencePageData;
	}
}
