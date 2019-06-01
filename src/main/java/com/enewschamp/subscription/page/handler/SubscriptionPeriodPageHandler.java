package com.enewschamp.subscription.page.handler;

import java.io.IOException;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

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
import com.enewschamp.app.fw.page.navigation.service.PageNavigationService;
import com.enewschamp.app.school.entity.SchoolPricing;
import com.enewschamp.app.school.service.SchoolPricingService;
import com.enewschamp.domain.common.IPageHandler;
import com.enewschamp.domain.common.PageNavigationContext;
import com.enewschamp.problem.Fault;
import com.enewschamp.problem.HttpStatusAdapter;
import com.enewschamp.subscription.app.dto.StudentControlDTO;
import com.enewschamp.subscription.app.dto.StudentControlWorkDTO;
import com.enewschamp.subscription.app.dto.StudentSchoolDTO;
import com.enewschamp.subscription.app.dto.StudentSubscriptionDTO;
import com.enewschamp.subscription.app.dto.StudentSubscriptionWorkDTO;
import com.enewschamp.subscription.app.dto.SubscriptionPeriodPageData;
import com.enewschamp.subscription.common.SubscriptionType;
import com.enewschamp.subscription.domain.business.SchoolDetailsBusiness;
import com.enewschamp.subscription.domain.business.StudentControlBusiness;
import com.enewschamp.subscription.domain.business.SubscriptionBusiness;
import com.enewschamp.subscription.domain.business.SubscriptionPeriodBusiness;
import com.enewschamp.subscription.pricing.entity.IndividualPricing;
import com.enewschamp.subscription.pricing.service.IndividualPricingService;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component(value = "SubscriptionPeriodPageHandler")
public class SubscriptionPeriodPageHandler implements IPageHandler {

	@Autowired
	ObjectMapper objectMapper;

	@Autowired
	StudentControlBusiness studentControlBusiness;

	@Autowired
	SubscriptionBusiness subscriptionBusiness;

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	SubscriptionPeriodBusiness subscriptionPeriodBusiness;
	
	@Autowired
	SchoolPricingService schoolPricingService;
	
	@Autowired
	IndividualPricingService individualPricingService;
	
	@Autowired
	private EnewschampApplicationProperties appConfig;
	
	@Autowired
	PageNavigationService pageNavigationService;
	
	@Autowired
	SchoolDetailsBusiness schoolDetailsBusiness;
	
	
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
		String operation = pageNavigationContext.getPageRequest().getHeader().getOperation();
		String editionId = pageNavigationContext.getPageRequest().getHeader().getEditionID();
		Long studentId=0L;
		String pageName = pageNavigationContext.getPreviousPage();
		String subscriptionType = "";
		SubscriptionPeriodPageData subscriptionPeriodPageData = new SubscriptionPeriodPageData();

		StudentControlDTO studentControlDTO = studentControlBusiness.getStudentFromMaster(emailId);
		if (studentControlDTO != null) {
			studentId = studentControlDTO.getStudentID();
			subscriptionType=studentControlDTO.getSubscriptionType();
			emailId = studentControlDTO.getEmailID();

		} else {
			StudentControlWorkDTO studentControlWorkDTO = studentControlBusiness.getStudentFromWork(emailId);
			if(studentControlWorkDTO!=null) {
				studentId = studentControlWorkDTO.getStudentID();
				subscriptionType=studentControlWorkDTO.getSubscriptionType();
				emailId = studentControlWorkDTO.getEmailID();

			}
		}
		
		
		if (PageAction.next.toString().equalsIgnoreCase(action)) {
			

			//get data from master table, fee text and fee
			subscriptionPeriodPageData = mapPricingDetails(subscriptionPeriodPageData, subscriptionType, studentId, editionId);
			subscriptionPeriodPageData.setIncompeleteFormText(appConfig.getStudentDetailsPageText().get("incompeleteFormText"));
			pageDto.setData(subscriptionPeriodPageData);
			

		} else if (PageAction.previous.toString().equalsIgnoreCase(action)) {
			PageNavigatorDTO pageNavDto = pageNavigationService.getNavPage(action, operation, pageName);
			if(PageSaveTable.M.toString().equalsIgnoreCase(pageNavDto.getUpdationTable()))
			{
				//get data from master table, fee text and fee
				
				subscriptionPeriodPageData = mapPricingDetails(subscriptionPeriodPageData, subscriptionType, studentId, editionId);
				StudentSubscriptionDTO studentSubscriptionDTO = subscriptionBusiness.getStudentSubscriptionFromMaster(studentId, editionId);
				subscriptionPeriodPageData = modelMapper.map(studentSubscriptionDTO, SubscriptionPeriodPageData.class);
				subscriptionPeriodPageData.setIncompeleteFormText(appConfig.getStudentDetailsPageText().get("incompeleteFormText"));
				pageDto.setData(subscriptionPeriodPageData);
			}
			else if (PageSaveTable.W.toString().equalsIgnoreCase(pageNavDto.getUpdationTable()))
			{
				//get data from work table, fee text and fee
				subscriptionPeriodPageData = mapPricingDetails(subscriptionPeriodPageData, subscriptionType, studentId, editionId);
				StudentSubscriptionWorkDTO studentSubscriptionWorkDTO = subscriptionBusiness.getStudentSubscriptionFromWork(studentId, editionId);
				subscriptionPeriodPageData = modelMapper.map(studentSubscriptionWorkDTO, SubscriptionPeriodPageData.class);

				subscriptionPeriodPageData.setIncompeleteFormText(appConfig.getStudentDetailsPageText().get("incompeleteFormText"));
				pageDto.setData(subscriptionPeriodPageData);
			}
		}

		return pageDto;
	}

	@Override
	public PageDTO saveAsMaster(String actionName, PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		String emailId = pageRequest.getHeader().getEmailID();
		StudentControlWorkDTO studentControlWorkDTO = studentControlBusiness.getStudentFromWork(emailId);
		Long studentId = 0L;
		if (studentControlWorkDTO != null) {
			studentId = studentControlWorkDTO.getStudentID();
		}
		String eidtionId = pageRequest.getHeader().getEditionID();

		// get subscription dto from master
		StudentSubscriptionDTO studentSubscriotionDTO = subscriptionBusiness.getStudentSubscriptionFromMaster(studentId,
				eidtionId);
		SubscriptionPeriodPageData subscriptionPeriodPageData = null;
		//map the request data
		subscriptionPeriodPageData = mapPagedata(pageRequest);
		
		int subscriptionPeriod = subscriptionPeriodPageData.getSubscriptionPeriodSelected();
		// TODO Use Fee amount .. logic not known ...
		Long feeAmount = subscriptionPeriodPageData.getFeeAmount();

		String autoRenew = subscriptionPeriodPageData.getAutoRenew();

		// calculate the start and end date..
		Date startDate = new Date();
		Calendar c = Calendar.getInstance();
		c.setTime(startDate);
		c.add(Calendar.DATE, subscriptionPeriod);

		Date endDate = c.getTime();

		// set the calculated dates in the subscription object..

		studentSubscriotionDTO.setStartDate(startDate);
		studentSubscriotionDTO.setEndDate(endDate);
		studentSubscriotionDTO.setAutoRenewal(autoRenew);

		subscriptionBusiness.saveAsMaster(studentSubscriotionDTO);

		pageDto.setHeader(pageRequest.getHeader());
		return pageDto;
	}

	@Override
	public PageDTO handleAppAction(String actionName, PageRequestDTO pageRequest, PageNavigatorDTO pageNavigatorDTO) {

		PageDTO pageDTO = new PageDTO();

		String saveIn = pageNavigatorDTO.getUpdationTable();
		if (PageAction.next.toString().equals(actionName)) {
			if (PageSaveTable.W.toString().equals(saveIn)) {
				Long studentId = 0L;
				String emailId = pageRequest.getHeader().getEmailID();
				StudentControlWorkDTO studentControlWorkDTO = studentControlBusiness.getStudentFromWork(emailId);
				if (studentControlWorkDTO != null) {
					String eidtionId = pageRequest.getHeader().getEditionID();

					studentId = studentControlWorkDTO.getStudentID();
					// get subscription dto from master
					StudentSubscriptionWorkDTO studentSubscpritionWorkDTO = subscriptionBusiness
							.getStudentSubscriptionFromWork(studentId, eidtionId);
					SubscriptionPeriodPageData subscriptionPeriodPageData = null;

					//map the request data
					subscriptionPeriodPageData = mapPagedata(pageRequest);
					
					int subscriptionPeriod = subscriptionPeriodPageData.getSubscriptionPeriodSelected();
					// TODO Use Fee amount .. logic not known ...
					Long feeAmount = subscriptionPeriodPageData.getFeeAmount();

					String autoRenew = subscriptionPeriodPageData.getAutoRenew();

					// calculate the start and end date..
					Date startDate = new Date();
					Calendar c = Calendar.getInstance();
					c.setTime(startDate);
					c.add(Calendar.DATE, subscriptionPeriod);

					Date endDate = c.getTime();

					// set the calculated dates in the subscription object..

					studentSubscpritionWorkDTO
							.setStartDate(startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
					studentSubscpritionWorkDTO
							.setEndDate(endDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
					studentSubscpritionWorkDTO.setAutoRenewal(autoRenew);
					subscriptionBusiness.updateSubscriptionPeriodInWork(studentSubscpritionWorkDTO);
				}

			}
			if (PageSaveTable.M.toString().equals(saveIn)) {
				String emailId = pageRequest.getHeader().getEmailID();
				StudentControlWorkDTO studentControlWorkDTO = studentControlBusiness.getStudentFromWork(emailId);
				Long studentId = 0L;
				if (studentControlWorkDTO != null) {
					studentId = studentControlWorkDTO.getStudentID();
				}
				String eidtionId = pageRequest.getHeader().getEditionID();

				// get subscription dto from master
				StudentSubscriptionDTO studentSubscriotionDTO = subscriptionBusiness
						.getStudentSubscriptionFromMaster(studentId, eidtionId);
				SubscriptionPeriodPageData subscriptionPeriodPageData = null;
				try {
					subscriptionPeriodPageData = objectMapper.readValue(pageRequest.getData().toString(),
							SubscriptionPeriodPageData.class);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				int subscriptionPeriod = subscriptionPeriodPageData.getSubscriptionPeriodSelected();
				// TODO Use Fee amount .. logic not known ...
				Long feeAmount = subscriptionPeriodPageData.getFeeAmount();

				String autoRenew = subscriptionPeriodPageData.getAutoRenew();

				// calculate the start and end date..
				Date startDate = new Date();
				Calendar c = Calendar.getInstance();
				c.setTime(startDate);
				c.add(Calendar.DATE, subscriptionPeriod);

				Date endDate = c.getTime();

				// set the calculated dates in the subscription object..

				studentSubscriotionDTO.setStartDate(startDate);
				studentSubscriotionDTO.setEndDate(endDate);
				studentSubscriotionDTO.setAutoRenewal(autoRenew);

				subscriptionBusiness.saveAsMaster(studentSubscriotionDTO);
			}
		} else if (PageAction.previous.toString().equals(actionName)) {
			// TO DO may want to delete work data
		}
		pageDTO.setHeader(pageRequest.getHeader());
		return pageDTO;
	}

	public SubscriptionPeriodPageData mapPagedata(PageRequestDTO pageRequest)
	{
		SubscriptionPeriodPageData subscriptionPeriodPageData=null;
		try {
			subscriptionPeriodPageData = objectMapper.readValue(pageRequest.getData().toString(),SubscriptionPeriodPageData.class);
		}catch(IOException e)
		{
			throw new Fault(new HttpStatusAdapter(HttpStatus.INTERNAL_SERVER_ERROR), ErrorCodes.SREVER_ERROR, "Error in mapping Subscription Period Page Data fields. Contact System administrator!");

		}
		return subscriptionPeriodPageData;
	}
	
	private SubscriptionPeriodPageData mapPricingDetails(SubscriptionPeriodPageData subscriptionPeriodPageData, String subscriptionType, Long studentId, String editionId) 
	{
		if(SubscriptionType.P.toString().equalsIgnoreCase(subscriptionType))
		{
			// fetch individual pricing
			IndividualPricing individualPricing = individualPricingService.getPricingForIndividual(editionId);
			subscriptionPeriodPageData = modelMapper.map(individualPricing, SubscriptionPeriodPageData.class);
		}
		else if(SubscriptionType.S.toString().equalsIgnoreCase(subscriptionType))
		{
			//fetch the student school
			StudentSchoolDTO studentSchoolDTO = schoolDetailsBusiness.getStudentFromMaster(studentId);
			Long schoolId = studentSchoolDTO.getSchoolId();
			// fetch school pricing
			
			SchoolPricing schoolPricing = schoolPricingService.getPricingForInstitution(schoolId, editionId);
			subscriptionPeriodPageData = modelMapper.map(schoolPricing, SubscriptionPeriodPageData.class);

		}
		HashMap<String,String> feeMap = new HashMap<String,String>();
		feeMap.put("1Month",subscriptionPeriodPageData.getFeeCurrency()+" "+subscriptionPeriodPageData.getFeeMonthly().toString());
		feeMap.put("3Months",subscriptionPeriodPageData.getFeeCurrency()+" "+subscriptionPeriodPageData.getFeeQuarterly().toString());
		feeMap.put("6Months",subscriptionPeriodPageData.getFeeCurrency()+" "+subscriptionPeriodPageData.getFeeHalfYearly().toString());
		feeMap.put("12Months",subscriptionPeriodPageData.getFeeCurrency()+" "+subscriptionPeriodPageData.getFeeYearly().toString());
		subscriptionPeriodPageData.setFeeText(feeMap);
		
		List<String> feeList = new ArrayList<String>();
		feeList.add(subscriptionPeriodPageData.getFeeMonthly().toString());
		feeList.add(subscriptionPeriodPageData.getFeeQuarterly().toString());
		feeList.add(subscriptionPeriodPageData.getFeeHalfYearly().toString());
		feeList.add(subscriptionPeriodPageData.getFeeYearly().toString());
		subscriptionPeriodPageData.setFee(feeList);
		
		return subscriptionPeriodPageData;
	}
	 
}
