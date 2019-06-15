package com.enewschamp.subscription.app.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.enewschamp.app.common.uicontrols.service.UIControlsService;
import com.enewschamp.subscription.app.dto.StudentPreferencePageDTO;
import com.enewschamp.subscription.domain.business.PreferenceBusiness;
import com.enewschamp.subscription.domain.business.SubscriptionBusiness;
import com.enewschamp.subscription.domain.service.StudentControlService;

import lombok.extern.java.Log;

@Log
@RestController
@RequestMapping("/enewschamp-api/v1")
public class PreferenceController {

	@Autowired
	UIControlsService uiControlService;

	@Autowired
	StudentControlService studentControlService;
	
	@Autowired
	PreferenceBusiness preferenceBusiness;
	
	@Autowired
	SubscriptionBusiness subscriptionBusiness;
	
	
	@PostMapping(value = "/page/preference/action/next/student/{studentId}")
	public ResponseEntity<StudentPreferencePageDTO> processNext(@PathVariable Long studentId, @RequestBody @Valid StudentPreferencePageDTO preferenceDto) {
	
		String operation = preferenceDto.getHeader().getOperation();
		String fromScreen = preferenceDto.getHeader().getPageName();
		String action= preferenceDto.getHeader().getAction();
		//String editionId = preferenceDto.getHeader().getEditionId();
		if(!studentControlService.studentExist(studentId) && "subscription".equals(operation) )
		{
			
			//save in master of subscription and student control
			//subscriptionBusiness.workToMaster(studentId, editionId);
			//preferenceBusiness.saveAsMaster(preferenceDto);
			//subscriptionBusiness.deleteFromWork(studentId,editionId);
			
		}
		//List<UIControlsDTO> uiControls = uiControlService.getByScreenAndAction(fromScreen,action);
		//preferenceDto.setScreenProperties(uiControls);
		//preferenceDto.getHeader().setRequestStatus(RequestStatusType.S);
		return new ResponseEntity<StudentPreferencePageDTO>(preferenceDto, HttpStatus.CREATED);	
	}
	
	@PostMapping(value = "/page/preference/action/prev/student/{studentId}")
	public ResponseEntity<StudentPreferencePageDTO> processPrev(@PathVariable Long studentId, @RequestBody @Valid StudentPreferencePageDTO preferenceDto) {
	
		String operation = preferenceDto.getHeader().getOperation();
		String fromScreen = preferenceDto.getHeader().getPageName();
		String action= preferenceDto.getHeader().getAction();
		
		//List<UIControlsDTO> uiControls = uiControlService.getByScreenAndAction(fromScreen,action);
		//preferenceDto.setScreenProperties(uiControls);
		//preferenceDto.getHeader().setRequestStatus(RequestStatusType.S);
		return new ResponseEntity<StudentPreferencePageDTO>(preferenceDto, HttpStatus.CREATED);	
	}
}
