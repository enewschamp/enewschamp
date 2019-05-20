package com.enewschamp.subscription.domain.business;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import com.enewschamp.app.common.HeaderDTO;
import com.enewschamp.subscription.app.dto.StudentSubscriptionPageDTO;
import com.enewschamp.subscription.domain.entity.StudentSubscription;
import com.enewschamp.subscription.domain.entity.StudentSubscriptionWork;
import com.enewschamp.subscription.domain.service.StudentSubscriptionService;
import com.enewschamp.subscription.domain.service.StudentSubscriptionWorkService;

public class SubscriptionBusiness {

	@Autowired
	ModelMapper modelMapper;
	
	@Autowired
	StudentSubscriptionService studentSubscription;
	
	@Autowired
	StudentSubscriptionWorkService studentWorkSubscription;
	
	public void createStudentScription(StudentSubscriptionPageDTO subscriptionDto)
	{
		HeaderDTO header = subscriptionDto.getHeader();
		 
		StudentSubscription subscripionDto =  modelMapper.map(subscriptionDto.getData(),StudentSubscription.class);
		String subscriptionType = subscripionDto.getSubscriptionType();
		String operation = header.getOperation();
		if("P".equals(subscriptionType) && "PremiumSubs".contentEquals(operation))
		{
			StudentSubscriptionWork subsWork = modelMapper.map(subscripionDto,StudentSubscriptionWork.class );
			studentWorkSubscription.create(subsWork);
		}
		//studentSubscription.create(subscripionDto);
	}
}
