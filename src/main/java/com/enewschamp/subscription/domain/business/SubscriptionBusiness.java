package com.enewschamp.subscription.domain.business;

import java.util.Calendar;
import java.util.Date;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enewschamp.app.common.HeaderDTO;
import com.enewschamp.subscription.app.dto.StudentSubscriptionPageDTO;
import com.enewschamp.subscription.app.dto.StudentSubscriptionWorkDTO;
import com.enewschamp.subscription.domain.entity.StudentControlWork;
import com.enewschamp.subscription.domain.entity.StudentSubscription;
import com.enewschamp.subscription.domain.entity.StudentSubscriptionWork;
import com.enewschamp.subscription.domain.repository.StudentControlWorkRepository;
import com.enewschamp.subscription.domain.service.StudentControlWorkService;
import com.enewschamp.subscription.domain.service.StudentSubscriptionService;
import com.enewschamp.subscription.domain.service.StudentSubscriptionWorkService;

@Service
public class SubscriptionBusiness {

	@Autowired
	ModelMapper modelMapper;
	
	@Autowired
	StudentSubscriptionService studentSubscription;
	
	@Autowired
	StudentSubscriptionWorkService studentWorkSubscription;
	
	@Autowired
	StudentControlWorkRepository repository;
	
	@Autowired
	StudentControlWorkService StudentControlWorkService; 
	
	public StudentSubscriptionWorkDTO existInWork(StudentSubscriptionPageDTO subscriptionDto) {
		String eMaildId = subscriptionDto.getData().getEmailID();
		StudentSubscriptionWorkDTO dto = studentWorkSubscription.ifExist(eMaildId);
		return dto;
	}
	
	public void saveAsWork(StudentSubscriptionPageDTO subscriptionDto)
	{
		HeaderDTO header = subscriptionDto.getHeader(); 
		//StudentSubscription subscripionDto =  modelMapper.map(subscriptionDto.getData(),StudentSubscription.class);
		StudentSubscription subscripionDto = new StudentSubscription();
		String emailId = subscriptionDto.getData().getEmailID();
		String subsType = subscriptionDto.getData().getSubscriptionSelected();
		String editionId = subscriptionDto.getHeader().getEditionId();
		StudentControlWork existingEntity = repository.searchByEmail(emailId);
		subscripionDto.setEditionID(header.getEditionId());
		
		if(existingEntity==null )
		{
			//create Student control
			StudentControlWork StudentControlWork = new StudentControlWork();
			StudentControlWork.setEMail(emailId);
			
			StudentControlWork created = StudentControlWorkService.create(StudentControlWork);
			Long studentId = created.getStudentID();
			subscripionDto.setStudentID(studentId);
			subscripionDto.setEditionID(editionId);
			if("S".contentEquals(subsType)) {
			int evalDays = 10;
			//set the evaluation type as E
			subscripionDto.setSubscriptionType("E");
			Date startDate = new Date();
			Calendar c = Calendar.getInstance();
			c.setTime(startDate);
			c.add(Calendar.DATE, evalDays);
			
			Date endDate = c.getTime();
			subscripionDto.setStartDate(startDate);
			subscripionDto.setEndDate(endDate);
			}
			
		}
		else
		{
			StudentControlWork StudentControlWork =  repository.searchByEmail(emailId);
			subscripionDto.setStudentID(StudentControlWork.getStudentID());
			
			if("S".contentEquals(subsType))
			{
				subscripionDto.setSubscriptionType("S");
				Date startDate = new Date();
				subscripionDto.setStartDate(startDate);
				//subscripionDto.setEndDate(endDate);
					
			}
			if("E".contentEquals(subsType))
			{
				subscripionDto.setSubscriptionType("E");
				
			}
		}
		subscripionDto.setAutoRenewal("N");
		//create student subscription
		studentSubscription.create(subscripionDto);
		
		
	}
	
	public void workToMaster(Long studentId, String editionId)
	{
		StudentSubscriptionWork dto =  studentWorkSubscription.get(studentId, editionId);
		StudentSubscription masterDto = modelMapper.map(dto,StudentSubscription.class);
		studentSubscription.create(masterDto);
		
	}
	
	public void deleteFromWork(Long studentId, String editionId)
	{
		studentWorkSubscription.delete(studentId,editionId);
		
	}
	
	
}
