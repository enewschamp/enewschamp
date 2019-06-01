package com.enewschamp.subscription.domain.business;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enewschamp.domain.common.RecordInUseType;
import com.enewschamp.subscription.app.dto.StudentPreferencesDTO;
import com.enewschamp.subscription.app.dto.StudentPreferencesWorkDTO;
import com.enewschamp.subscription.domain.entity.StudentPreference;
import com.enewschamp.subscription.domain.entity.StudentPreferenceWork;
import com.enewschamp.subscription.domain.service.StudentPreferenceService;
import com.enewschamp.subscription.domain.service.StudentPreferenceWorkService;

@Service
public class PaymentBusiness {
	
	@Autowired
	ModelMapper modelMapper; 
	
	@Autowired
	StudentPreferenceService  studentPreferenceService;
	
	@Autowired
	StudentPreferenceWorkService  studentPreferenceWorkService;
	
	public void saveAsMaster(StudentPreferencesDTO studentPreferencePageDTO)
	{
		System.out.println("Sving in master");
		StudentPreference studentPref = modelMapper.map(studentPreferencePageDTO,StudentPreference.class);
		studentPref.setRecordInUse(RecordInUseType.Y);
		
		// to be corrected
		studentPref.setOperatorId("SYSTEM");
		
		studentPreferenceService.create(studentPref);
	}
	
	public void saveAsWork(StudentPreferencesWorkDTO studentPreferenceWorkDTO)
	{
		StudentPreferenceWork studentPref = modelMapper.map(studentPreferenceWorkDTO,StudentPreferenceWork.class);
		studentPreferenceWorkService.create(studentPref);
	}
	
	public void workToMaster(Long studentId)
	{
		StudentPreferenceWork workEntity = studentPreferenceWorkService.get(studentId);
		StudentPreference masterEntity = modelMapper.map(workEntity,StudentPreference.class);
		masterEntity.setOperatorId("SYSTEM");
		masterEntity.setRecordInUse(RecordInUseType.Y);
		studentPreferenceService.create(masterEntity);
	}
}
