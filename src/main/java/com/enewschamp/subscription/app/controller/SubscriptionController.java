package com.enewschamp.subscription.app.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.enewschamp.app.common.RequestStatusType;
import com.enewschamp.app.common.uicontrols.dto.UIControlsDTO;
import com.enewschamp.app.common.uicontrols.service.UIControlsService;
import com.enewschamp.subscription.app.dto.StudentSubscriptionPageDTO;
import com.enewschamp.subscription.app.dto.StudentSubscriptionWorkDTO;
import com.enewschamp.subscription.domain.business.SubscriptionBusiness;
import com.enewschamp.subscription.domain.service.StudentControlService;

import lombok.extern.java.Log;

@Log
@RestController
@RequestMapping("/enewschamp-api/v1")
public class SubscriptionController {

	
	@Autowired
	StudentControlService studentControlService;
	
	@Autowired
	SubscriptionBusiness subscriptionBusiness;
	
	@Autowired
	UIControlsService uiControlService;
	
	@PostMapping(value = "/page/subscription/action/next")
	public ResponseEntity<StudentSubscriptionPageDTO> processNext(@RequestBody @Valid StudentSubscriptionPageDTO subscriptionDto) {
		String eMailId  = subscriptionDto.getData().getEmailId();
		String subscriptionType = subscriptionDto.getData().getSubscriptionSelected();
		String fromScreenName = subscriptionDto.getHeader().getPageName();
		String action = subscriptionDto.getHeader().getAction();
		if(!studentControlService.studentExist(eMailId) && "S".contentEquals(subscriptionType))
		{
			subscriptionBusiness.saveAsWork(subscriptionDto);
		}
		
		List<UIControlsDTO> uiControls = uiControlService.getByScreenAndAction(fromScreenName,action);
		subscriptionDto.setScreenProperties(uiControls);
		subscriptionDto.getHeader().setRequestStatus(RequestStatusType.S);
		return new ResponseEntity<StudentSubscriptionPageDTO>(subscriptionDto, HttpStatus.CREATED);	
	}
	
	
	@PostMapping(value = "/page/subscription/action/previous")
	public ResponseEntity<StudentSubscriptionPageDTO> processPrev(@RequestBody @Valid StudentSubscriptionPageDTO subscriptionDto) {
		String eMailId  = subscriptionDto.getData().getEmailId();
		String subscriptionType = subscriptionDto.getData().getSubscriptionSelected();
		String fromScreenName = subscriptionDto.getHeader().getPageName();
		if(!studentControlService.studentExist(eMailId) && "S".contentEquals(subscriptionType))
		{
			StudentSubscriptionWorkDTO workDto = subscriptionBusiness.existInWork(subscriptionDto);
			if(workDto!=null)
			{
				//delete from work table
				subscriptionBusiness.deleteFromWork(workDto.getStudentID(),workDto.getEditionID());
			}
		}	
		List<UIControlsDTO> uiControls = uiControlService.get(fromScreenName);
		subscriptionDto.setScreenProperties(uiControls);
		subscriptionDto.getHeader().setRequestStatus(RequestStatusType.S);
		return new ResponseEntity<StudentSubscriptionPageDTO>(subscriptionDto, HttpStatus.CREATED);	
	}
}
