package com.enewschamp.subscription.domain.business;

import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enewschamp.app.common.PageRequestDTO;
import com.enewschamp.subscription.app.dto.StudentSubscriptionDTO;
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
		System.out.println("Sving in master");

		String editionId = pageRequest.getHeader().getEditionId();
		Long studentId = studentSubscriptionPeriodDTO.getStudentID();
		// get existing subscription details..

		StudentSubscription studentSubscription = studentSubscriptionService.get(studentId, editionId);
		StudentSubscriptionDTO studentSubscriptionDTO = modelMapper.map(studentSubscription,
				StudentSubscriptionDTO.class);

		// calculate the start and end date based on subscription period
		int subPeriod = studentSubscriptionPeriodDTO.getSubscriptionPeriodSelected();

		Date startDate = new Date();
		Calendar c = Calendar.getInstance();
		c.setTime(startDate);
		c.add(Calendar.DATE, subPeriod);

		Date endDate = c.getTime();

		studentSubscription.setStartDate(startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
		studentSubscription.setEndDate(endDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
		studentSubscription.setAutoRenewal(studentSubscriptionPeriodDTO.getAutoRenew());
		studentSubscriptionService.create(studentSubscription);
	}

	public void saveAsWork(StudentSubscriptionPeriodWorkDTO studentSubscriptionPeriodWorkDTO,
			PageRequestDTO pageRequest) {
		System.out.println("Sving in master");

		String editionId = pageRequest.getHeader().getEditionId();
		Long studentId = studentSubscriptionPeriodWorkDTO.getStudentID();
		// get existing subscription details..

		StudentSubscription studentSubscription = studentSubscriptionService.get(studentId, editionId);
		StudentSubscriptionWork studentSubscriptionWork = modelMapper.map(studentSubscription,
				StudentSubscriptionWork.class);

		// calculate the start and end date based on subscription period
		int subPeriod = studentSubscriptionPeriodWorkDTO.getSubscriptionPeriodSelected();

		Date startDate = new Date();
		Calendar c = Calendar.getInstance();
		c.setTime(startDate);
		c.add(Calendar.DATE, subPeriod);

		Date endDate = c.getTime();

		studentSubscriptionWork.setStartDate(startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
		studentSubscriptionWork.setEndDate(endDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
		studentSubscriptionWork.setAutoRenewal(studentSubscriptionPeriodWorkDTO.getAutoRenew());
		studentSubscriptionWorkService.create(studentSubscriptionWork);
	}

	public void workToMaster(Long studentId, String editionId) {
		StudentSubscriptionWork subsWork = studentSubscriptionWorkService.get(studentId, editionId);
		StudentSubscription studSubsDto = modelMapper.map(subsWork, StudentSubscription.class);
		studentSubscriptionService.create(studSubsDto);
	}
}
