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
import com.enewschamp.subscription.app.dto.StudentSubscriptionDTO;
import com.enewschamp.subscription.app.dto.StudentSubscriptionPageData;
import com.enewschamp.subscription.app.dto.StudentSubscriptionWorkDTO;
import com.enewschamp.subscription.domain.business.StudentControlBusiness;
import com.enewschamp.subscription.domain.business.SubscriptionBusiness;
import com.enewschamp.subscription.domain.entity.StudentControl;
import com.enewschamp.subscription.domain.entity.StudentControlWork;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component(value = "SubscriptionPageHandler")
public class SubscriptionPageHandler implements IPageHandler {

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	SubscriptionBusiness subscriptionBusiness;

	@Autowired
	StudentControlBusiness StudentControlBusiness;

	@Autowired
	ObjectMapper objectMapper;

	@Autowired
	private EnewschampApplicationProperties appConfig;

	@Override
	public PageDTO handleAction(String actionName, PageRequestDTO pageRequest) {
		return null;
	}

	@Override
	public PageDTO loadPage(PageNavigationContext pageNavigationContext) {
		PageDTO pageDto = new PageDTO();
		String eMailId = pageNavigationContext.getPageRequest().getHeader().getEmailID();
		String action = pageNavigationContext.getActionName();
		String editionId =  pageNavigationContext.getPageRequest().getHeader().getEditionID();
		StudentControlDTO studentControlDTO = StudentControlBusiness.getStudentFromMaster(eMailId);
		StudentControlWorkDTO studentControlWorkDTO = null;
		Long studentId = 0L;
		
		if(PageAction.next.toString().equalsIgnoreCase(action))
		{
			// add static data from proerties
			StudentSubscriptionPageData subscripionPagedata = new StudentSubscriptionPageData();

			subscripionPagedata.setTerms(appConfig.getSubscriptionText().get("TermsOfUseText"));
			subscripionPagedata.setIncompeleteFormText(appConfig.getSubscriptionText().get("incompeleteFormText"));
			subscripionPagedata.setPrivacyPolicy(appConfig.getSubscriptionText().get("privacyPolicy"));
			subscripionPagedata.setWhatYouGetTextPremium(appConfig.getSubscriptionText().get("whatYouGetTextPremium"));
			subscripionPagedata.setWhatYouGetTextStandard(appConfig.getSubscriptionText().get("whatYouGetTextStandard"));
			pageDto.setData(subscripionPagedata);

		}
		else if(PageAction.previous.toString().equalsIgnoreCase(action))
		{
			// if the user is new then fetch from work table
			if (studentControlDTO == null) {
				studentControlWorkDTO = StudentControlBusiness.getStudentFromWork(eMailId);
				studentId = studentControlWorkDTO.getStudentID();
				StudentSubscriptionWorkDTO studentSubscriptionWorkDTO = subscriptionBusiness.getStudentSubscriptionFromWork(studentId, editionId);
				
				StudentSubscriptionPageData subscripionPagedata = modelMapper.map(studentSubscriptionWorkDTO, StudentSubscriptionPageData.class);
				subscripionPagedata.setEMailID(studentControlWorkDTO.getEmailID());
				// add static data from proerties
				subscripionPagedata.setTerms(appConfig.getSubscriptionText().get("TermsOfUseText"));
				subscripionPagedata.setIncompeleteFormText(appConfig.getSubscriptionText().get("incompeleteFormText"));
				subscripionPagedata.setPrivacyPolicy(appConfig.getSubscriptionText().get("privacyPolicy"));
				subscripionPagedata.setWhatYouGetTextPremium(appConfig.getSubscriptionText().get("whatYouGetTextPremium"));
				subscripionPagedata.setWhatYouGetTextStandard(appConfig.getSubscriptionText().get("whatYouGetTextStandard"));
				pageDto.setData(subscripionPagedata);


			} else {
				
				StudentSubscriptionDTO  studentSubscriptionDTO = subscriptionBusiness.getStudentSubscriptionFromMaster(studentId, editionId);
				
				StudentSubscriptionPageData subscripionPagedata = modelMapper.map(studentSubscriptionDTO, StudentSubscriptionPageData.class);
				subscripionPagedata.setEMailID(studentControlDTO.getEmailID());
				// add static data from proerties
				subscripionPagedata.setTerms(appConfig.getSubscriptionText().get("TermsOfUseText"));
				subscripionPagedata.setIncompeleteFormText(appConfig.getSubscriptionText().get("incompeleteFormText"));
				subscripionPagedata.setPrivacyPolicy(appConfig.getSubscriptionText().get("privacyPolicy"));
				subscripionPagedata.setWhatYouGetTextPremium(appConfig.getSubscriptionText().get("whatYouGetTextPremium"));
				subscripionPagedata.setWhatYouGetTextStandard(appConfig.getSubscriptionText().get("whatYouGetTextStandard"));
				pageDto.setData(subscripionPagedata);

			}
		}

		return pageDto;
	}

	@Override
	public PageDTO saveAsMaster(String actionName, PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		String emailId = pageRequest.getHeader().getEmailID();
		String eidtionId = pageRequest.getHeader().getEditionID();

		StudentControlWorkDTO studentControlWorkDTO = StudentControlBusiness.getStudentFromWork(emailId);
		Long studentId = 0L;
		if (studentControlWorkDTO != null) {
			studentId = studentControlWorkDTO.getStudentID();
		}

		subscriptionBusiness.saveWorkToMaster(studentId, eidtionId);
		StudentControlBusiness.workToMaster(studentId);

		pageDto.setHeader(pageRequest.getHeader());
		return pageDto;
	}

	@Override
	public PageDTO handleAppAction(String actionName, PageRequestDTO pageRequest, PageNavigatorDTO pageNavigatorDTO) {
		PageDTO pageDTO = new PageDTO();

		if (PageAction.next.toString().equals(actionName)) {
			String saveIn = pageNavigatorDTO.getUpdationTable();

			if (PageSaveTable.W.toString().equals(saveIn)) {
				Long studentId = 0L;

				StudentSubscriptionPageData subscripionPagedata = null;
			
				subscripionPagedata = mapPagedata(pageRequest);
				
				String emailId = subscripionPagedata.getEMailID();
				StudentControlWorkDTO studentControlWorkDTO = StudentControlBusiness.getStudentFromWork(emailId);
				if (studentControlWorkDTO == null) {
					// create student control in work
					StudentControlWorkDTO studentControlWorkDto = new StudentControlWorkDTO();
					studentControlWorkDto.setEmailID(subscripionPagedata.getEMailID());
					studentControlWorkDto.setSubscriptionType(subscripionPagedata.getSubscriptionSelected());
					StudentControlWork studentControlWorkEntity = StudentControlBusiness
							.saveAsWork(studentControlWorkDto);
					studentId = studentControlWorkEntity.getStudentID();
				}
				else 
				{
					//throw an error...that student already exists in registration table.
				}

				subscriptionBusiness.saveAsWork(studentId, pageRequest);
			}
			if (PageSaveTable.M.toString().equals(saveIn)) {

				Long studentId = 0L;
				StudentSubscriptionPageData subscripionPagedata = null;
				
				subscripionPagedata = mapPagedata(pageRequest);
				
				String emailId = subscripionPagedata.getEMailID();
				StudentControlDTO studentControlDTO = StudentControlBusiness.getStudentFromMaster(emailId);
				if (studentControlDTO == null) {
					// create student control in work
					StudentControlDTO studentControlDto = new StudentControlDTO();
					studentControlDto.setEmailID(subscripionPagedata.getEMailID());
					studentControlDto.setSubscriptionType(subscripionPagedata.getSubscriptionSelected());
					StudentControl studentControlEntity = StudentControlBusiness
							.saveAsMaster(studentControlDto);
					studentId = studentControlEntity.getStudentID();
					
					subscriptionBusiness.saveAsMaster(studentId,pageRequest);

				}
				else 
				{
					//throw an error...that student already exists in registration table.
				}


			}
		} 
		else if(PageAction.previous.toString().equals(actionName))
		{
				// TO Do may want to delete the data from work table
		}
		pageDTO.setHeader(pageRequest.getHeader());
		return pageDTO;
	}
	public StudentSubscriptionPageData mapPagedata(PageRequestDTO pageRequest)
	{
		StudentSubscriptionPageData studentSubscriptionPageData=null;
		try {
			studentSubscriptionPageData = objectMapper.readValue(pageRequest.getData().toString(),StudentSubscriptionPageData.class);
		}catch(IOException e)
		{
			throw new Fault(new HttpStatusAdapter(HttpStatus.INTERNAL_SERVER_ERROR), ErrorCodes.SREVER_ERROR, "Error in mapping Subscription Page Data fields. Contact System administrator!");

		}
		return studentSubscriptionPageData;
	}
}
