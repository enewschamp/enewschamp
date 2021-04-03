package com.enewschamp.subscription.domain.business;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enewschamp.app.common.PageRequestDTO;
import com.enewschamp.subscription.app.dto.StudentSubscriptionPeriodDTO;
import com.enewschamp.subscription.app.dto.StudentSubscriptionPeriodWorkDTO;
import com.enewschamp.subscription.domain.entity.StudentSubscription;
import com.enewschamp.subscription.domain.entity.StudentSubscriptionWork;
import com.enewschamp.subscription.domain.service.StudentControlService;
import com.enewschamp.subscription.domain.service.StudentSubscriptionService;
import com.enewschamp.subscription.domain.service.StudentSubscriptionWorkService;

@Service
public class SubscriptionPeriodBusiness {

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	StudentSubscriptionService studentSubscriptionService;

	@Autowired
	StudentSubscriptionWorkService studentSubscriptionWorkService;

	@Autowired
	StudentControlService studentControlService;

	public void saveAsMaster(StudentSubscriptionPeriodDTO studentSubscriptionPeriodDTO, PageRequestDTO pageRequest) {
		String editionId = pageRequest.getHeader().getEditionId();
		Long studentId = studentSubscriptionPeriodDTO.getStudentId();
		// get existing subscription details..
		StudentSubscription studentSubscription = studentSubscriptionService.get(studentId, editionId);
		studentSubscription.setAutoRenewal(studentSubscriptionPeriodDTO.getAutoRenew());
		studentSubscriptionService.create(studentSubscription);
	}

	public void saveAsWork(StudentSubscriptionPeriodWorkDTO studentSubscriptionPeriodWorkDTO,
			PageRequestDTO pageRequest) {
		String editionId = pageRequest.getHeader().getEditionId();
		Long studentId = studentSubscriptionPeriodWorkDTO.getStudentId();
		// get existing subscription details..

		StudentSubscription studentSubscription = studentSubscriptionService.get(studentId, editionId);
		StudentSubscriptionWork studentSubscriptionWork = modelMapper.map(studentSubscription,
				StudentSubscriptionWork.class);
		studentSubscriptionWork.setSubscriptionPeriod(studentSubscriptionPeriodWorkDTO.getSubscriptionPeriodSelected());
		studentSubscriptionWork.setAutoRenewal(studentSubscriptionPeriodWorkDTO.getAutoRenew());
		studentSubscriptionWorkService.create(studentSubscriptionWork);
	}

	public void workToMaster(Long studentId, String editionId) {
		StudentSubscriptionWork subsWork = studentSubscriptionWorkService.get(studentId, editionId);
		if (subsWork != null) {
			StudentSubscription studSubsDto = modelMapper.map(subsWork, StudentSubscription.class);
			studentSubscriptionService.create(studSubsDto);
		}
	}
}
