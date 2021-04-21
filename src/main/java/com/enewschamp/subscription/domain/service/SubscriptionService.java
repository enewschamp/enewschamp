package com.enewschamp.subscription.domain.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enewschamp.app.common.HeaderDTO;
import com.enewschamp.subscription.app.dto.StudentSubscriptionDTO;
import com.enewschamp.subscription.app.dto.StudentSubscriptionPageDTO;

@Service
public class SubscriptionService {
	@Autowired
	ModelMapper modelMapper;

	public void createStudentSubscription(StudentSubscriptionPageDTO subscriptionDto) {
		HeaderDTO header = subscriptionDto.getHeader();
		StudentSubscriptionDTO subscripionDto = modelMapper.map(subscriptionDto.getData(),
				StudentSubscriptionDTO.class);
	}
}
